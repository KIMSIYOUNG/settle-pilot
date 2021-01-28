package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.common.BatchIntegrationTest;
import in.woowa.pilot.core.order.OrderPaymentAggregation;
import in.woowa.pilot.core.order.OrderPaymentAggregationRepository;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderPaymentAggregatorConfigurationTest extends BatchIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderPaymentAggregationRepository orderPaymentAggregationRepository;

    @DisplayName("주문/결제가 없는 경우 결과는 없다.")
    @Test
    void createOrderAggregationWithoutOrdersAndRewards() throws Exception {
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().minusDays(1).toString())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParameters);

        List<OrderPaymentAggregation> results = orderPaymentAggregationRepository.findAll();
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(results).hasSize(0)
        );
    }

    @DisplayName("결제수단별로 집계할 수 있다.")
    @Test
    void createOrderAggregation() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.save(OrderFixture.createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY));
        orderRepository.save(OrderFixture.createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY));
        orderRepository.save(OrderFixture.createWithoutId(owner, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON));
        orderRepository.save(OrderFixture.createWithoutId(owner, PaymentType.COUPON, PaymentOption.OWNER_COUPON));

        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().minusDays(1).toString())
                .toJobParameters();
        // when
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParameters);

        List<OrderPaymentAggregation> results = orderPaymentAggregationRepository.findAll();
        OrderPaymentAggregation card = getAggregation(results, PaymentType.CARD, PaymentOption.EMPTY);
        OrderPaymentAggregation baeminCoupon = getAggregation(results, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON);
        OrderPaymentAggregation ownerCoupon = getAggregation(results, PaymentType.COUPON, PaymentOption.OWNER_COUPON);
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(results).hasSize(3),

                () -> assertThat(card.getCriteriaDate()).isEqualTo(LocalDate.now().minusDays(1)),
                () -> assertThat(baeminCoupon.getCriteriaDate()).isEqualTo(LocalDate.now().minusDays(1)),
                () -> assertThat(ownerCoupon.getCriteriaDate()).isEqualTo(LocalDate.now().minusDays(1)),

                () -> assertThatBigDecimal(card.getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThatBigDecimal(baeminCoupon.getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(ownerCoupon.getTotalAmount()).isEqualTo(BigDecimal.valueOf(-10_000))
        );
    }

    @DisplayName("업주별로 결제수단이 집계된다.")
    @Test
    void createWithOwners() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, PaymentType.CARD, PaymentOption.EMPTY, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner1, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner2, PaymentType.POINT, PaymentOption.EMPTY, LocalDateTime.now())
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParameters);
        Collection<StepExecution> steps = jobExecution.getStepExecutions();
        // then
        List<OrderPaymentAggregation> results = orderPaymentAggregationRepository.findAll();
        OrderPaymentAggregation card = getAggregation(results, PaymentType.CARD, PaymentOption.EMPTY);
        OrderPaymentAggregation baeminCoupon = getAggregation(results, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON);
        OrderPaymentAggregation point = getAggregation(results, PaymentType.POINT, PaymentOption.EMPTY);

        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(results).hasSize(3),
                () -> assertThat(steps).hasSize(1),

                () -> assertThat(card.getOwner().getId()).isEqualTo(owner1.getId()),
                () -> assertThat(baeminCoupon.getOwner().getId()).isEqualTo(owner1.getId()),
                () -> assertThat(point.getOwner().getId()).isEqualTo(owner2.getId()),

                () -> assertThat(card.getCriteriaDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(baeminCoupon.getCriteriaDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(point.getCriteriaDate()).isEqualTo(LocalDate.now()),

                () -> assertThatBigDecimal(card.getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(baeminCoupon.getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(point.getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000))
        );
    }

    @DisplayName("어제날로 생성된 주문집계는 오늘 생성된 집계에 영향을 받지 않는다.")
    @Test
    void createTwice() throws Exception {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, PaymentType.CARD, PaymentOption.EMPTY, LocalDateTime.now().minusDays(1)),
                OrderFixture.createWithoutId(owner1, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDateTime.now().minusDays(1)),
                OrderFixture.createWithoutId(owner2, PaymentType.POINT, PaymentOption.EMPTY, LocalDateTime.now().minusDays(1))
        ));

        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().minusDays(1).toString())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParameters);

        // when

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner1, PaymentType.CARD, PaymentOption.EMPTY, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner1, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDateTime.now()),
                OrderFixture.createWithoutId(owner2, PaymentType.POINT, PaymentOption.EMPTY, LocalDateTime.now())
        ));

        JobParameters jobParametersToday = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        JobExecution jobExecutionToday = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParametersToday);

        // then
        List<OrderPaymentAggregation> results = orderPaymentAggregationRepository.findAll();
        List<OrderPaymentAggregation> card = getAggregations(results, PaymentType.CARD, PaymentOption.EMPTY);
        List<OrderPaymentAggregation> baeminCoupon = getAggregations(results, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON);
        List<OrderPaymentAggregation> point = getAggregations(results, PaymentType.POINT, PaymentOption.EMPTY);

        assertAll(
                () -> assertThat(jobExecutionToday.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(results).hasSize(6),

                () -> assertThat(card.get(0).getCriteriaDate()).isEqualTo(LocalDate.now().minusDays(1)),
                () -> assertThat(baeminCoupon.get(0).getCriteriaDate()).isEqualTo(LocalDate.now().minusDays(1)),
                () -> assertThat(point.get(0).getCriteriaDate()).isEqualTo(LocalDate.now().minusDays(1)),
                () -> assertThat(card.get(1).getCriteriaDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(baeminCoupon.get(1).getCriteriaDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(point.get(1).getCriteriaDate()).isEqualTo(LocalDate.now()),

                () -> assertThatBigDecimal(card.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(baeminCoupon.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(point.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(card.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(baeminCoupon.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(point.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000))
        );
    }

    @DisplayName("업주/주문이 모두 없는 경우 주문이 없는 경우 배치는 완료되고, 결과는 없다.")
    @Test
    void createOrderAggregationWithoutAnyOrder() throws Exception {
        // given
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        // when
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParameters);
        Collection<StepExecution> steps = jobExecution.getStepExecutions();
        List<OrderPaymentAggregation> results = orderPaymentAggregationRepository.findAll();
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(steps).hasSize(1),
                () -> assertThat(results).hasSize(0)
        );
    }

    @DisplayName("업주는 존재하지만 주문이 없는 경우도 결과는 생성되지 않는다.")
    @Test
    void createWithoutOwner() throws Exception {
        // given
        ownerRepository.save(OwnerFixture.createWithoutId());
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(OrderPaymentAggregatorConfiguration.JOB_NAME).launchJob(jobParameters);
        Collection<StepExecution> steps = jobExecution.getStepExecutions();
        // then
        List<OrderPaymentAggregation> results = orderPaymentAggregationRepository.findAll();

        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(steps).hasSize(1),
                () -> assertThat(results).hasSize(0)
        );
    }

    public static OrderPaymentAggregation getAggregation(List<OrderPaymentAggregation> results, PaymentType type, PaymentOption option) {
        return results.stream()
                .filter(op -> op.getPaymentType() == type)
                .filter(op -> op.getPaymentOption() == option)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<OrderPaymentAggregation> getAggregations(List<OrderPaymentAggregation> results, PaymentType type, PaymentOption option) {
        return results.stream()
                .filter(op -> op.getPaymentType() == type)
                .filter(op -> op.getPaymentOption() == option)
                .collect(Collectors.toList());
    }
}