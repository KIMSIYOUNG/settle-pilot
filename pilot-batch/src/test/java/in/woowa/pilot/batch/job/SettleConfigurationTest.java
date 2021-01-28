package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.common.BatchIntegrationTest;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SettleConfigurationTest extends BatchIntegrationTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RewardRepository rewardRepository;

    @Autowired
    SettleRepository settleRepository;

    @DisplayName("정상적으로 지급금을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.now())
        ));

        rewardRepository.saveAll(Arrays.asList(
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.now()),
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.now()),
                RewardFixture.createWithoutId(owner2, BigDecimal.valueOf(20_000), LocalDateTime.now())
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.DAILY.name())
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        // then
        JobExecution execution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(jobParameters);
        List<Settle> result = settleRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Reward> rewards = rewardRepository.findAll();

        assertAll(
                () -> assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(2),
                () -> assertThatBigDecimal(result.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(80_000)),
                () -> assertThatBigDecimal(result.get(1).getAmount()).isEqualTo(BigDecimal.valueOf(60_000)),
                () -> assertThat(orders.stream()
                        .noneMatch(order -> order.getSettle() == null)).isTrue(),
                () -> assertThat(rewards.stream()
                        .noneMatch(reward -> reward.getSettle() == null)).isTrue()
        );
    }

    @DisplayName("일자에 포함된 주문/보정금액만 지급금을 생성한다.")
    @Test
    void createWithInCondition() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.now().minusDays(1)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.now().minusDays(1))
        ));

        rewardRepository.saveAll(Arrays.asList(
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.now()),
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.now().minusDays(1)),
                RewardFixture.createWithoutId(owner2, BigDecimal.valueOf(20_000), LocalDateTime.now().minusDays(2))
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.DAILY.name())
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        // then
        JobExecution execution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(jobParameters);
        List<Settle> result = settleRepository.findAll();

        assertAll(
                () -> assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getOwner().getId()).isEqualTo(owner1.getId()),
                () -> assertThat(result.get(1).getOwner().getId()).isEqualTo(owner2.getId()),
                () -> assertThatBigDecimal(result.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(60_000))
        );
    }

    @DisplayName("주 단위로 지급금을 생성한다.")
    @Test
    void createWithWeek() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2021, 1, 10), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(2021, 1, 12, 13, 23, 53)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(2021, 1, 16, 23, 59, 59)),

                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(LocalDate.of(2021, 1, 17), LocalTime.MIDNIGHT))
        ));

        rewardRepository.saveAll(Arrays.asList(
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.of(LocalDate.of(2021, 1, 10), LocalTime.MIDNIGHT)),
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.of(2021, 1, 12, 13, 23, 53)),
                RewardFixture.createWithoutId(owner2, BigDecimal.valueOf(20_000), LocalDateTime.of(2021, 1, 16, 23, 59, 59))
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.WEEK.name())
                .addString("criteriaDate", LocalDate.of(2021, 1, 10).toString())
                .toJobParameters();
        // then
        JobExecution execution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(jobParameters);
        List<Settle> result = settleRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        List<Reward> rewards = rewardRepository.findAll();

        assertAll(
                () -> assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getTransactionStartAt()).isEqualTo(LocalDateTime.of(2021, 1, 10, 0, 0, 0, 0)),
                () -> assertThat(result.get(0).getTransactionEndAt()).isEqualTo(LocalDateTime.of(2021, 1, 17, 0, 0, 0, 0)),
                () -> assertThat(result.get(1).getTransactionEndAt()).isEqualTo(LocalDateTime.of(2021, 1, 17, 0, 0, 0, 0)),
                () -> assertThat(result.get(1).getTransactionEndAt()).isEqualTo(LocalDateTime.of(2021, 1, 17, 0, 0, 0, 0)),
                () -> assertThatBigDecimal(result.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(80_000)),
                () -> assertThatBigDecimal(result.get(1).getAmount()).isEqualTo(BigDecimal.valueOf(40_000)),
                () -> assertThat(orders.stream().filter(o -> o.getSettle() == null).count()).isEqualTo(1),
                () -> assertThat(rewards.stream().noneMatch(r -> r.getSettle() == null)).isTrue()
        );
    }

    @DisplayName("월단위로 지급금을 생성할 수 있다.")
    @Test
    void createWithInConditionAndMonth() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(2020, 12, 31, 23, 59, 59)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(2020, 11, 30, 23, 59, 59)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(2021, 1, 1, 0, 0, 0))
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.MONTH.name())
                .addString("criteriaDate", LocalDate.of(2020, 12, 15).toString())
                .toJobParameters();
        // then
        JobExecution execution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(jobParameters);
        List<Settle> result = settleRepository.findAll();

        assertAll(
                () -> assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(2),
                () -> assertThatBigDecimal(result.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(40_000)),
                () -> assertThatBigDecimal(result.get(1).getAmount()).isEqualTo(BigDecimal.valueOf(0))
        );
    }

    @DisplayName("chunk단위 이상의 지급금을 생성하는 경우도 정상적으로 생성된다.")
    @Test
    void createOverChunk() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(2020, 12, 31, 23, 59, 59)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(2020, 12, 31, 23, 59, 59)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(2020, 12, 31, 23, 59, 59))
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.MONTH.name())
                .addString("criteriaDate", LocalDate.of(2020, 12, 15).toString())
                .toJobParameters();
        // then
        JobExecution execution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(jobParameters);
        List<Settle> result = settleRepository.findAll();

        assertAll(
                () -> assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(1),
                () -> assertThatBigDecimal(result.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(120_000))
        );
    }

    @DisplayName("이미 지급된 주문/보정금액을 제외하고 지급금이 생성된다.")
    @Test
    void createWithoutSettledOrderAndReward() throws Exception {
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(2020, 12, 13, 23, 59, 59)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(2020, 12, 19, 23, 59, 59)),
                OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(2020, 12, 31, 23, 59, 59))
        ));

        rewardRepository.saveAll(Arrays.asList(
                RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(20_000), LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)),
                RewardFixture.createWithoutId(owner2, BigDecimal.valueOf(20_000), LocalDateTime.of(2020, 12, 15, 13, 23, 53))
        ));

        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.DAILY.name())
                .addString("criteriaDate", LocalDate.of(2020, 12, 1).toString())
                .toJobParameters();
        JobExecution execution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(jobParameters);
        // when
        JobParameters monthParameters = uniqueParameterBuilder()
                .addString("settleType", SettleType.MONTH.name())
                .addString("criteriaDate", LocalDate.of(2020, 12, 1).toString())
                .toJobParameters();

        // then
        JobExecution montExecution = jobLauncher(SettleConfiguration.JOB_NAME).launchJob(monthParameters);
        List<Settle> result = settleRepository.findAll();

        assertAll(
                () -> assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(montExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(2),
                () -> assertThatBigDecimal(result.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(80_000)),
                () -> assertThatBigDecimal(result.get(1).getAmount()).isEqualTo(BigDecimal.valueOf(80_000))
        );
    }
}
