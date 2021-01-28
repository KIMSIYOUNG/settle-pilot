package in.woowa.pilot.fixture.reward;

import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
import in.woowa.pilot.fixture.owner.OwnerFixture;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class RewardFixture {
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(10000);
    public static final RewardType SYSTEM_ERROR = RewardType.SYSTEM_ERROR;
    public static final String SYSTEM_ERROR_MESSAGE = "시스템 오류로 인한 보상금액";
    public static final String RESOURCE = "/api/rewards";

    public static Reward createWithoutId() {
        return Reward.testBuilder()
                .amount(AMOUNT)
                .rewardType(SYSTEM_ERROR)
                .description(SYSTEM_ERROR_MESSAGE)
                .rewardDateTime(LocalDateTime.now())
                .owner(OwnerFixture.createWithoutId())
                .build();
    }

    public static Reward createWithId(long id, Owner owner) {
        return Reward.testBuilder()
                .id(id)
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .amount(BigDecimal.valueOf(20000L))
                .rewardType(RewardType.DELIVERY_ACCIDENT)
                .description("배달 도중 오토바이 사고로 인한 보상금액")
                .owner(owner)
                .rewardDateTime(LocalDateTime.now())
                .build();
    }

    public static Reward createWithId(long id, Owner owner, LocalDateTime dateTime) {
        return Reward.testBuilder()
                .id(id)
                .businessNo(new BusinessNo(LocalDate.now(), UUID.randomUUID().toString()))
                .amount(BigDecimal.valueOf(20000L))
                .rewardType(RewardType.DELIVERY_ACCIDENT)
                .description("배달 도중 오토바이 사고로 인한 보상금액")
                .owner(owner)
                .rewardDateTime(dateTime)
                .build();
    }


    public static Reward createWithoutId(Owner owner, BigDecimal amount, LocalDateTime rewardDateTime) {
        return Reward.testBuilder()
                .amount(amount)
                .rewardType(SYSTEM_ERROR)
                .description(SYSTEM_ERROR_MESSAGE)
                .owner(owner)
                .rewardDateTime(rewardDateTime)
                .build();
    }
}
