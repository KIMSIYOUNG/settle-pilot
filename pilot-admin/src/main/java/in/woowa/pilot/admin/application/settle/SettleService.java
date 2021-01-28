package in.woowa.pilot.admin.application.settle;

import in.woowa.pilot.admin.application.order.dto.request.OrderSearchDto;
import in.woowa.pilot.admin.application.reward.dto.request.RewardSearchDto;
import in.woowa.pilot.admin.application.settle.dto.request.*;
import in.woowa.pilot.admin.application.settle.dto.response.SettleAmountResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponsesDto;
import in.woowa.pilot.admin.common.ErrorCode;
import in.woowa.pilot.admin.common.exception.BusinessException;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.repository.order.OrderCustomRepository;
import in.woowa.pilot.admin.repository.owner.OwnerCustomRepository;
import in.woowa.pilot.admin.repository.reward.RewardCustomRepository;
import in.woowa.pilot.admin.repository.settle.SettleCustomRepository;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SettleService {
    private final SettleCustomRepository settleCustomRepository;
    private final OrderCustomRepository orderCustomRepository;
    private final RewardCustomRepository rewardCustomRepository;
    private final OwnerCustomRepository ownerCustomRepository;

    private final SettleRepository settleRepository;
    private final OwnerRepository ownerRepository;
    private final SettleSnapshotRepository settleSnapshotRepository;

    /*
    지급금 배치 실패에 대한 대비 API
    특정 업주에게 특정 요일에 해당하는 지급금을 생성해주는 것
     */
    @Transactional
    public SettleResponseDto createInCaseSettle(SettleInCaseCreateDto requestDto) {
        Owner owner = ownerRepository.findById(requestDto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("owner", "id", requestDto.getOwnerId()));
        List<Order> orders = orderCustomRepository.fetchUnSettledByPeriod(getOrderCondition(requestDto, owner));
        List<Reward> rewards = rewardCustomRepository.fetchUnSettledByPeriod(getRewardCondition(requestDto, owner));

        Settle savedSettle = settleRepository.save(requestDto.toSettle(owner, orders, rewards));
        orderCustomRepository.updateBatchSettle(orders, savedSettle);
        rewardCustomRepository.updateBatchSettle(rewards, savedSettle);

        settleSnapshotRepository.save(SettleSnapshot.builder()
                .settles(Arrays.asList(savedSettle))
                .startAt(requestDto.getStartDate())
                .endAt(requestDto.getEndDate())
                .type(SettleSnapshotType.IN_CASE)
                .build());

        return new SettleResponseDto(savedSettle);
    }

    /*
    일반적인 지급금 생성 로직
     */
    @Transactional
    public void createRegularSettle(SettleRegularCreateDto requestDto) {
        Map<Long, Owner> idToOwner = ownerCustomRepository.fetchIdToOwnerBySettleType(requestDto.getSettleType());
        Map<Long, List<Order>> ownerIdToOrders = orderCustomRepository.fetchIdToOrdersBy(requestDto.getSettleType(), requestDto.getCriteriaDate());
        Map<Long, List<Reward>> ownerIdToRewards = rewardCustomRepository.fetchIdToRewardsBy(requestDto.getSettleType(), requestDto.getCriteriaDate());

        List<Settle> savedSettles = settleRepository.saveAll(convertToSettle(requestDto, ownerIdToOrders, ownerIdToRewards, idToOwner));
        for (Settle settle : savedSettles) {
            orderBatchUpdate(ownerIdToOrders, settle);
            rewardBatchUpdate(ownerIdToRewards, settle);
        }

        settleSnapshotRepository.save(SettleSnapshot.builder()
                .settles(savedSettles)
                .startAt(requestDto.getStartDateTime())
                .endAt(requestDto.getEndDateTime())
                .type(SettleSnapshotType.BATCH)
                .build());
    }

    public SettleResponsesDto fetchPagedByCondition(SettleSearchDto condition, Pageable pageable) {
        Page<Settle> settles = settleCustomRepository.fetchPagedByCondition(condition, pageable);

        return new SettleResponsesDto(settles);
    }

    /*
    특정 Snapshot만 보여주는 형태로 변경
     */
    public SettleAmountResponseDto fetchAmountByCondition(SettleSnapshotSearchDto requestDto) {
        List<SettleSnapshot> snapshots = settleSnapshotRepository.findByPeriod(requestDto.getStartAt(), requestDto.getEndAt());

        return new SettleAmountResponseDto(snapshots, requestDto.getStartAt(), requestDto.getEndAt());
    }

    public SettleResponsesDto fetchPagedAll(Pageable pageable) {
        Page<Settle> settles = settleCustomRepository.fetchAll(pageable);

        return new SettleResponsesDto(settles);
    }

    @Transactional
    public void bulkCompleteSettleStatus(SettleCompleteConditionDto condition) {
        if (condition.notContainsCondition()) {
            throw new BusinessException(ErrorCode.INVALID_VALIDATE, "지급완료할 대상/범위를 정해주세요.");
        }

        settleCustomRepository.updateBulkComplete(condition);
    }

    @Transactional
    public void updateSettleStatus(SettleUpdateDto updateRequest) {
        SettleStatus updateStatus = updateRequest.getSettleStatus();
        Settle settle = findSettle(updateRequest.getId());

        settle.changeStatus(updateStatus);
    }

    @Transactional
    public void deleteById(Long id) {
        Settle settle = findSettle(id);

        settle.delete();
    }

    private Settle findSettle(Long id) {
        return settleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("settle", "id", id));
    }

    private RewardSearchDto getRewardCondition(SettleInCaseCreateDto requestDto, Owner owner) {
        SettleType type = requestDto.getSettleType();

        return RewardSearchDto.builder()
                .ownerId(owner.getId())
                .startAt(type.getStartCriteriaAt(requestDto.getCriteriaDate()))
                .endAt(type.getEndCriteriaAt(requestDto.getCriteriaDate()))
                .build();
    }

    private OrderSearchDto getOrderCondition(SettleInCaseCreateDto requestDto, Owner owner) {
        SettleType type = requestDto.getSettleType();

        return OrderSearchDto.builder()
                .ownerId(owner.getId())
                .startAt(type.getStartCriteriaAt(requestDto.getCriteriaDate()))
                .endAt(type.getEndCriteriaAt(requestDto.getCriteriaDate()))
                .build();
    }

    private void rewardBatchUpdate(Map<Long, List<Reward>> rewards, Settle settle) {
        rewards.entrySet().stream()
                .filter(entry -> settle.getOwner().getId().equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(value -> rewardCustomRepository.updateBatchSettle(value, settle));
    }

    private void orderBatchUpdate(Map<Long, List<Order>> orders, Settle settle) {
        orders.entrySet().stream()
                .filter(listEntry -> settle.getOwner().getId().equals(listEntry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(ownerListEntry1Value -> orderCustomRepository.updateBatchSettle(ownerListEntry1Value, settle));
    }

    private List<Settle> convertToSettle(
            SettleRegularCreateDto requestDto,
            Map<Long, List<Order>> orders,
            Map<Long, List<Reward>> rewards,
            Map<Long, Owner> idToOwner
    ) {
        return orders.entrySet().stream()
                .map(entry -> requestDto.toSettle(
                        entry.getValue() == null ? Collections.emptyList() : entry.getValue(),
                        idToOwner.get(entry.getKey()),
                        rewards.getOrDefault(entry.getKey(), Collections.emptyList())))
                .collect(Collectors.toList());
    }
}
