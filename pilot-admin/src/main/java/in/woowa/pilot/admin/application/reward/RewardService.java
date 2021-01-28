package in.woowa.pilot.admin.application.reward;

import in.woowa.pilot.admin.application.reward.dto.request.*;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponseDto;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponsesDto;
import in.woowa.pilot.admin.common.exception.ResourceInvalidException;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.repository.order.OrderCustomRepository;
import in.woowa.pilot.admin.repository.reward.RewardCustomRepository;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RewardService {
    private final OrderCustomRepository orderCustomRepository;
    private final RewardRepository rewardRepository;
    private final RewardCustomRepository rewardCustomRepository;
    private final OwnerRepository ownerRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public RewardResponseDto create(RewardCreateDto requestDto) {
        Owner owner = findOwner(requestDto);

        return new RewardResponseDto(rewardRepository.save(requestDto.toReward(owner)));
    }

    @Transactional
    public void createByOrders(RewardOrdersCreateDto request) {
        List<Order> orders = orderRepository.findAllById(request.getOrderIds());

        List<Reward> rewards = orders.stream()
                .map(request::toReward)
                .collect(Collectors.toList());

        rewardRepository.saveAll(rewards);
    }

    @Transactional
    public void createByPeriod(RewardPeriodDto request) {
        List<Order> orders = orderCustomRepository.fetchByPeriod(request.getStartAt(), request.getEndAt());

        if (orders.size() == 0) {
            throw new ResourceInvalidException("orders", "orders", "orders", "해당 기간에 발생한 주문이 없습니다.");
        }

        List<Reward> rewards = orders.stream()
                .map(request::toReward)
                .collect(Collectors.toList());

        rewardRepository.saveAll(rewards);
    }

    @Transactional(readOnly = true)
    public RewardResponseDto fetchById(Long id) {
        Reward reward = rewardCustomRepository.fetchById(id)
                .orElseThrow(() -> new ResourceNotFoundException("reward", "id", id));

        return new RewardResponseDto(reward);
    }

    @Transactional(readOnly = true)
    public RewardResponsesDto fetchPagedByCondition(RewardSearchDto condition, Pageable pageable) {
        Page<Reward> rewards = rewardCustomRepository.fetchPagedByCondition(condition, pageable);

        return new RewardResponsesDto(rewards);
    }

    @Transactional(readOnly = true)
    public RewardResponsesDto findAll(Pageable pageable) {
        Page<Reward> rewards = rewardCustomRepository.fetchAll(pageable);

        return new RewardResponsesDto(rewards);
    }

    @Transactional(readOnly = true)
    public RewardResponsesDto fetchBySettleId(Long settleId, Pageable pageable) {
        return new RewardResponsesDto(rewardRepository.findAllBySettleId(settleId, pageable));
    }

    @Transactional
    public void update(RewardUpdateDto requestDto) {
        Reward reward = findReward(requestDto.getId());

        reward.update(requestDto.getAmount(), requestDto.getRewardType().name(), requestDto.getDescription());
    }

    @Transactional
    public void delete(Long id) {
        Reward reward = findReward(id);

        reward.delete();
    }

    private Reward findReward(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("reward", "id", id));
    }

    private Owner findOwner(RewardCreateDto requestDto) {
        return ownerRepository.findById(requestDto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("owner", "id", requestDto.getOwnerId()));
    }
}
