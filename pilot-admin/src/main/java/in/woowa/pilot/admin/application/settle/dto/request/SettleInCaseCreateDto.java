package in.woowa.pilot.admin.application.settle.dto.request;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
public class SettleInCaseCreateDto {
    private final Long ownerId;
    private final SettleType settleType;
    private final LocalDate criteriaDate;

    @Builder
    public SettleInCaseCreateDto(Long ownerId, SettleType settleType, LocalDate criteriaDate) {
        this.ownerId = ownerId;
        this.settleType = settleType;
        this.criteriaDate = criteriaDate;
    }

    public Settle toSettle(Owner owner, List<Order> orders, List<Reward> rewards) {
        return Settle.builder()
                .owner(owner)
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .transactionStartAt(settleType.getStartCriteriaAt(criteriaDate))
                .transactionEndAt(settleType.getEndCriteriaAt(criteriaDate))
                .orders(orders)
                .rewards(rewards)
                .settleType(settleType)
                .settleScheduleDate(LocalDateTime.of(LocalDate.now().plusDays(4), LocalTime.MIDNIGHT))
                .build();
    }

    public LocalDateTime getStartDate() {
        return settleType.getStartCriteriaAt(criteriaDate);
    }

    public LocalDateTime getEndDate() {
        return settleType.getEndCriteriaAt(criteriaDate);
    }
}
