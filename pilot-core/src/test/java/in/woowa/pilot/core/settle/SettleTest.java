package in.woowa.pilot.core.settle;

import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SettleTest {

    @DisplayName("비즈니스 번호가 없는 경우 빈 문자열을 반환한다.")
    @Test
    void businessNoValue() {
        // given
        List<Order> orders = Arrays.asList(
                OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3),
                OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3)
        );
        List<Reward> rewards = Arrays.asList(
                RewardFixture.createWithoutId(),
                RewardFixture.createWithoutId()
        );
        // when
        Settle settle = Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .owner(OwnerFixture.createWithoutId())
                .transactionStartAt(LocalDateTime.now())
                .transactionEndAt(LocalDateTime.now())
                .build();
        // when & then
        assertThat(settle.getBusinessNoValue()).isEqualTo("");
    }

    @DisplayName("지급금은 생성시에 orders + rewards로 금액이 결정된다.")
    @Test
    void create() {
        // given
        List<Order> orders = Arrays.asList(
                OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3),
                OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3)
        );
        BigDecimal orderAmount = orders.stream()
                .map(Order::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Reward> rewards = Arrays.asList(
                RewardFixture.createWithoutId(),
                RewardFixture.createWithoutId()
        );
        BigDecimal rewardAmount = rewards.stream()
                .map(Reward::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // when
        Settle settle = Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .owner(OwnerFixture.createWithoutId())
                .transactionStartAt(LocalDateTime.now())
                .transactionEndAt(LocalDateTime.now())
                .build();
        // then
        assertAll(
                () -> assertThat(settle.getSettleStatus()).isEqualTo(SettleStatus.CREATED),
                () -> assertThatBigDecimal(settle.getAmount()).isEqualTo(orderAmount.add(rewardAmount))
        );
    }

    @DisplayName("지급금의 거래일 시작일자는 종료일자보다 이전일 수 없다.")
    @Test
    void createWithNullTransactionDate() {
        List<Order> orders = Arrays.asList(OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3));
        List<Reward> rewards = Arrays.asList(RewardFixture.createWithoutId());

        assertThatThrownBy(() -> Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .transactionStartAt(LocalDateTime.now().plusSeconds(1))
                .transactionEndAt(LocalDateTime.now())
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거래시작일은 종료일보다 이전일 수 없습니다.");
    }

    @DisplayName("상태는 null로 변경할 수 없다.")
    @Test
    void updateWithNull() {
        // given
        List<Order> orders = Arrays.asList(OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3));
        List<Reward> rewards = Arrays.asList(RewardFixture.createWithoutId());

        Settle settle = Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .transactionStartAt(LocalDateTime.now().minusDays(2))
                .transactionEndAt(LocalDateTime.now())
                .build();
        // when , then

        assertThatThrownBy(() -> settle.changeStatus(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지급상태는 null이어서는 안됩니다.");
    }

    @DisplayName("이미 지급완료된 지급금은 변경할 수 없다.")
    @Test
    void updateWithComplete() {
        // given
        List<Order> orders = Arrays.asList(OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3));
        List<Reward> rewards = Arrays.asList(RewardFixture.createWithoutId());

        Settle settle = Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .transactionStartAt(LocalDateTime.now().minusDays(2))
                .transactionEndAt(LocalDateTime.now())
                .build();
        settle.changeStatus(SettleStatus.COMPLETED);
        // when , then

        assertThatThrownBy(() -> settle.changeStatus(SettleStatus.CREATED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 지급금입니다.");
    }

    @DisplayName("지급금의 상태를 변경할 수 있다.")
    @Test
    void changeStatus() {
        // given
        List<Order> orders = Arrays.asList(OrderFixture.createWithoutId(OwnerFixture.createWithoutId(), 3));
        List<Reward> rewards = Arrays.asList(RewardFixture.createWithoutId());

        Settle settle = Settle.builder()
                .orders(orders)
                .rewards(rewards)
                .transactionStartAt(LocalDateTime.now().minusDays(2))
                .transactionEndAt(LocalDateTime.now())
                .build();
        // when
        SettleStatus expected = SettleStatus.COMPLETED;
        settle.changeStatus(expected);
        // then
        assertThat(settle.getSettleStatus()).isEqualTo(expected);
    }
}
