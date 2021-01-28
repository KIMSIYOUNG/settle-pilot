package in.woowa.pilot.fixture.settle;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

public class SettleFixture {

    public static Settle createWithId(Owner owner, long id) {
        return Settle.testBuilder()
                .id(id)
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .settleType(SettleType.DAILY)
                .transactionStartAt(BaseFixture.YESTERDAY_MIDNIGHT)
                .transactionEndAt(LocalDateTime.now())
                .settleScheduleDate(LocalDateTime.now())
                .owner(owner)
                .orders(Arrays.asList(OrderFixture.createWithoutId(owner, 1)))
                .rewards(Arrays.asList(RewardFixture.createWithId(8, owner)))
                .build();
    }

    public static Settle createWithoutId(Owner owner) {
        return Settle.testBuilder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .settleType(SettleType.DAILY)
                .transactionStartAt(BaseFixture.YESTERDAY_MIDNIGHT)
                .transactionEndAt(LocalDateTime.now())
                .settleScheduleDate(LocalDateTime.now())
                .owner(owner)
                .orders(Arrays.asList(OrderFixture.createWithoutId(owner, 1)))
                .rewards(Arrays.asList(RewardFixture.createWithId(8, owner)))
                .build();
    }

    public static Settle createWithoutId(Owner owner, LocalDate dateTime, SettleType type) {
        return Settle.testBuilder()
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .settleType(type)
                .transactionStartAt(type.getStartCriteriaAt(dateTime))
                .transactionEndAt(type.getEndCriteriaAt(dateTime))
                .settleScheduleDate(LocalDateTime.now())
                .owner(owner)
                .orders(Arrays.asList(OrderFixture.createWithoutId(owner, 1, LocalDateTime.of(dateTime, LocalTime.MIDNIGHT))))
                .rewards(Arrays.asList(RewardFixture.createWithId(8, owner, LocalDateTime.of(dateTime, LocalTime.MIDNIGHT))))
                .build();
    }
}
