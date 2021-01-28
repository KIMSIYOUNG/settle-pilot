package in.woowa.pilot.batch.dto;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
public class SettleJobDto {
    private final Owner owner;
    private final List<Reward> rewards;
    private final List<Order> orders;

    public SettleJobDto(Owner owner, List<Reward> rewards, List<Order> orders) {
        this.owner = owner;
        this.rewards = rewards;
        this.orders = orders;
    }

    public Settle toSettle(SettleType type, LocalDate criteriaDate) {
        return Settle.builder()
                .businessNo(new BusinessNo(criteriaDate, UUID.randomUUID().toString()))
                .orders(orders)
                .rewards(rewards)
                .owner(owner)
                .settleType(type)
                .settleScheduleDate(LocalDateTime.of(criteriaDate, LocalTime.MIDNIGHT).plusDays(4))
                .transactionStartAt(type.getStartCriteriaAt(criteriaDate))
                .transactionEndAt(type.getEndCriteriaAt(criteriaDate))
                .build();
    }
}
