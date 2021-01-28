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
import java.util.List;
import java.util.UUID;

@Getter
public class SettleRegularCreateDto {
    private final SettleType settleType;
    private final LocalDate criteriaDate;

    @Builder
    public SettleRegularCreateDto(SettleType settleType, LocalDate criteriaDate) {
        this.settleType = settleType;
        this.criteriaDate = criteriaDate;
    }

    public Settle toSettle(List<Order> orders, Owner owner, List<Reward> rewards) {
        return Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .owner(owner)
                .transactionEndAt(settleType.getEndCriteriaAt(criteriaDate))
                .transactionStartAt(settleType.getStartCriteriaAt(criteriaDate))
                .settleType(settleType)
                .settleScheduleDate(LocalDateTime.now().plusDays(4))
                .businessNo(new BusinessNo(LocalDate.now().plusDays(4), UUID.randomUUID().toString()))
                .build();
    }

    public LocalDateTime getStartDateTime() {
        return settleType.getStartCriteriaAt(criteriaDate);
    }

    public LocalDateTime getEndDateTime() {
        return settleType.getEndCriteriaAt(criteriaDate);
    }
}
