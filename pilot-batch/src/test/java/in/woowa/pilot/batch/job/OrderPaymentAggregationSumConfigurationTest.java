package in.woowa.pilot.batch.job;

import in.woowa.pilot.batch.common.BatchIntegrationTest;
import in.woowa.pilot.core.order.OrderPaymentAggregationRepository;
import in.woowa.pilot.core.order.OrderPaymentAggregationSum;
import in.woowa.pilot.core.order.OrderPaymentAggregationSumRepository;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static in.woowa.pilot.fixture.order.OrderPaymentAggregationFixture.createWithoutId;
import static in.woowa.pilot.fixture.order.OrderPaymentAggregationFixture.getPaymentAggregation;
import static in.woowa.pilot.fixture.order.OrderPaymentAggregationFixture.getPaymentAggregations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderPaymentAggregationSumConfigurationTest extends BatchIntegrationTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderPaymentAggregationRepository orderPaymentAggregationRepository;

    @Autowired
    OrderPaymentAggregationSumRepository aggregationSumRepository;

    @DisplayName("최종 금액을 집계할 데이터가 없는 경우 배치는 수행되나, 값은 저장되지 않는다.")
    @Test
    void createEmpty() throws Exception {
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(OrderPaymentAggregationSumConfiguration.JOB_NAME)
                .launchJob(jobParameters);

        // then
        List<OrderPaymentAggregationSum> result = aggregationSumRepository.findAll();

        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(0)
        );
    }

    @DisplayName("정상적으로 결제수단별 지급금의 총합을 구할 수 있다.")
    @Test
    void createAggregationSum() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        orderPaymentAggregationRepository.saveAll(Arrays.asList(
                createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(30_000)),
                createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(20_000)),
                createWithoutId(owner, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(20_000))
        ));
        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher(OrderPaymentAggregationSumConfiguration.JOB_NAME)
                .launchJob(jobParameters);

        List<OrderPaymentAggregationSum> result = aggregationSumRepository.findAll();
        OrderPaymentAggregationSum card = getPaymentAggregation(result, PaymentType.CARD, PaymentOption.EMPTY);
        OrderPaymentAggregationSum coupon = getPaymentAggregation(result, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON);
        // then
        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(2),

                () -> assertThat(card.getPaymentType()).isEqualTo(PaymentType.CARD),
                () -> assertThat(card.getPaymentOption()).isEqualTo(PaymentOption.EMPTY),
                () -> assertThatBigDecimal(card.getTotalAmount()).isEqualTo(BigDecimal.valueOf(50_000)),

                () -> assertThat(coupon.getPaymentType()).isEqualTo(PaymentType.COUPON),
                () -> assertThat(coupon.getPaymentOption()).isEqualTo(PaymentOption.BAEMIN_COUPON),
                () -> assertThatBigDecimal(coupon.getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000))
        );
    }

    @DisplayName("다양한 업주의 다양한 집계를 하나로 합칠 수 있다.")
    @Test
    void createWithOwners() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderPaymentAggregationRepository.saveAll(Arrays.asList(
                createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                createWithoutId(owner, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(10_000)),

                createWithoutId(owner1, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(20_000)),
                createWithoutId(owner1, PaymentType.POINT, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(20_000)),
                createWithoutId(owner1, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(20_000)),

                createWithoutId(owner2, PaymentType.MOBILE, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(30_000)),
                createWithoutId(owner2, PaymentType.COUPON, PaymentOption.OWNER_COUPON, LocalDate.now(), BigDecimal.valueOf(-30_000)),
                createWithoutId(owner2, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(30_000))
        ));

        // when
        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregationSumConfiguration.JOB_NAME)
                .launchJob(jobParameters);

        // then
        List<OrderPaymentAggregationSum> result = aggregationSumRepository.findAll();

        OrderPaymentAggregationSum card = getPaymentAggregation(result, PaymentType.CARD, PaymentOption.EMPTY);
        OrderPaymentAggregationSum baeminCoupon = getPaymentAggregation(result, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON);
        OrderPaymentAggregationSum ownerCoupon = getPaymentAggregation(result, PaymentType.COUPON, PaymentOption.OWNER_COUPON);
        OrderPaymentAggregationSum mobile = getPaymentAggregation(result, PaymentType.MOBILE, PaymentOption.EMPTY);
        OrderPaymentAggregationSum point = getPaymentAggregation(result, PaymentType.POINT, PaymentOption.EMPTY);

        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(5),

                () -> assertThatBigDecimal(card.getTotalAmount()).isEqualTo(BigDecimal.valueOf(40_000)),
                () -> assertThatBigDecimal(baeminCoupon.getTotalAmount()).isEqualTo(BigDecimal.valueOf(60_000)),
                () -> assertThatBigDecimal(ownerCoupon.getTotalAmount()).isEqualTo(BigDecimal.valueOf(-30_000)),
                () -> assertThatBigDecimal(mobile.getTotalAmount()).isEqualTo(BigDecimal.valueOf(30_000)),
                () -> assertThatBigDecimal(point.getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000))
        );
    }

    @DisplayName("다른 날짜로 실행되는 경우 기존 값은 영향을 받지 않는다.")
    @Test
    void createTwiceSum() throws Exception {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderPaymentAggregationRepository.saveAll(Arrays.asList(
                createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now().minusDays(1), BigDecimal.valueOf(10_000)),
                createWithoutId(owner, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now().minusDays(1), BigDecimal.valueOf(10_000)),
                createWithoutId(owner, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDate.now().minusDays(1), BigDecimal.valueOf(10_000)),

                createWithoutId(owner1, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                createWithoutId(owner1, PaymentType.CARD, PaymentOption.EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                createWithoutId(owner1, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(10_000))
        ));

        JobParameters jobParameters = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().minusDays(1).toString())
                .toJobParameters();
        jobLauncher(OrderPaymentAggregationSumConfiguration.JOB_NAME).launchJob(jobParameters);

        // when

        JobParameters jobParametersToday = uniqueParameterBuilder()
                .addString("criteriaDate", LocalDate.now().toString())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher(OrderPaymentAggregationSumConfiguration.JOB_NAME)
                .launchJob(jobParametersToday);

        // then
        List<OrderPaymentAggregationSum> result = aggregationSumRepository.findAll();

        List<OrderPaymentAggregationSum> card = getPaymentAggregations(result, PaymentType.CARD, PaymentOption.EMPTY);
        List<OrderPaymentAggregationSum> baeminCoupon = getPaymentAggregations(result, PaymentType.COUPON, PaymentOption.BAEMIN_COUPON);

        assertAll(
                () -> assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED),
                () -> assertThat(result).hasSize(4),

                () -> assertThatBigDecimal(card.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThatBigDecimal(baeminCoupon.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(card.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThatBigDecimal(baeminCoupon.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000))
        );
    }
}