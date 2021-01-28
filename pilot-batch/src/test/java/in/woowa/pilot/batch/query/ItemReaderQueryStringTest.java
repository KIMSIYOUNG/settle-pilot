package in.woowa.pilot.batch.query;

import in.woowa.pilot.core.order.OrderPaymentAggregationRepository;
import in.woowa.pilot.core.order.OrderPaymentAggregationSumRepository;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.settle.SettleRepository;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.IntegrationTest;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.order.OrderPaymentAggregationFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import jpql.dto.OrderAggregationByOwnerDto;
import jpql.dto.OrderAggregationSumDto;
import jpql.dto.SettleSumDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static in.woowa.pilot.core.order.PaymentOption.BAEMIN_COUPON;
import static in.woowa.pilot.core.order.PaymentOption.EMPTY;
import static in.woowa.pilot.core.order.PaymentType.CARD;
import static in.woowa.pilot.core.order.PaymentType.COUPON;
import static in.woowa.pilot.core.order.PaymentType.MOBILE;
import static in.woowa.pilot.core.order.PaymentType.POINT;
import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ItemReaderQueryStringTest extends IntegrationTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderPaymentAggregationRepository orderPaymentAggregationRepository;

    @Autowired
    OrderPaymentAggregationSumRepository sumRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    SettleRepository settleRepository;

    @DisplayName("업주별 주문을 결제수단별로 집계할 수 있다.")
    @Test
    void findOrderAggregationByOwnerAndPayment() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());

        SettleType type = SettleType.DAILY;
        LocalDateTime criteriaDate = LocalDateTime.now();

        orderRepository.saveAll(Arrays.asList(
                OrderFixture.createWithoutId(owner, CARD, EMPTY, criteriaDate),
                OrderFixture.createWithoutId(owner, POINT, EMPTY, criteriaDate),
                OrderFixture.createWithoutId(owner, MOBILE, EMPTY, criteriaDate),
                OrderFixture.createWithoutId(owner, COUPON, BAEMIN_COUPON, criteriaDate),

                OrderFixture.createWithoutId(owner1, CARD, EMPTY, criteriaDate),
                OrderFixture.createWithoutId(owner1, CARD, EMPTY, criteriaDate),
                OrderFixture.createWithoutId(owner1, COUPON, BAEMIN_COUPON, criteriaDate)
        ));
        // when
        List<OrderAggregationByOwnerDto> aggregationSum = entityManager.createQuery(
                ItemReaderQueryString.findOrderAggregationByOwnerAndPayment(),
                OrderAggregationByOwnerDto.class
        )
                .setParameter("start", type.getStartCriteriaAt(LocalDate.now()))
                .setParameter("end", type.getEndCriteriaAt(LocalDate.now()))
                .getResultList();
        // then
        assertAll(
                () -> assertThat(aggregationSum).hasSize(6),
                () -> assertThatBigDecimal(aggregationSum.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(aggregationSum.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(aggregationSum.get(2).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(aggregationSum.get(3).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),

                () -> assertThatBigDecimal(aggregationSum.get(4).getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThatBigDecimal(aggregationSum.get(5).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),

                () -> assertThat(aggregationSum.stream()
                        .filter(ag -> Objects.equals(ag.getOwner().getId(), owner.getId()))
                        .count()).isEqualTo(4),
                () -> assertThat(aggregationSum.stream()
                        .filter(ag -> Objects.equals(ag.getOwner().getId(), owner1.getId()))
                        .count()).isEqualTo(2)
        );
    }

    @DisplayName("전체 주문을 집계할 수 있다.")
    @Test
    void findOrderAggregationSumByPayment() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderPaymentAggregationRepository.saveAll(Arrays.asList(
                OrderPaymentAggregationFixture.createWithoutId(owner, CARD, EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                OrderPaymentAggregationFixture.createWithoutId(owner, POINT, EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                OrderPaymentAggregationFixture.createWithoutId(owner, MOBILE, EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                OrderPaymentAggregationFixture.createWithoutId(owner, COUPON, BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(10_000)),

                OrderPaymentAggregationFixture.createWithoutId(owner1, CARD, EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                OrderPaymentAggregationFixture.createWithoutId(owner1, CARD, EMPTY, LocalDate.now(), BigDecimal.valueOf(10_000)),
                OrderPaymentAggregationFixture.createWithoutId(owner1, COUPON, BAEMIN_COUPON, LocalDate.now(), BigDecimal.valueOf(10_000))
        ));
        // when
        List<OrderAggregationSumDto> aggregationSum = entityManager.createQuery(
                ItemReaderQueryString.findOrderAggregationSumByPayment(),
                OrderAggregationSumDto.class
        )
                .setParameter("criteriaDate", LocalDate.now())
                .getResultList();
        // then
        assertAll(
                () -> assertThat(aggregationSum).hasSize(4),
                () -> assertThatBigDecimal(aggregationSum.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(30_000)),
                () -> assertThatBigDecimal(aggregationSum.get(1).getTotalAmount()).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThatBigDecimal(aggregationSum.get(2).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000)),
                () -> assertThatBigDecimal(aggregationSum.get(3).getTotalAmount()).isEqualTo(BigDecimal.valueOf(10_000))
        );
    }

    @DisplayName("지급금 유형에 따른 업주를 조회할 수 있다.")
    @Test
    void findActiveOwnerBySettleType() {
        // given
        ownerRepository.saveAll(Arrays.asList(
                OwnerFixture.createWithoutId(SettleType.DAILY),
                OwnerFixture.createWithoutId(SettleType.DAILY),
                OwnerFixture.createWithoutId(SettleType.DAILY),
                OwnerFixture.createWithoutId(SettleType.WEEK),
                OwnerFixture.createWithoutId(SettleType.MONTH),
                OwnerFixture.createWithoutId(SettleType.MONTH)
        ));
        // when
        List<Owner> owners = entityManager.createQuery(
                ItemReaderQueryString.findActiveOwnerBySettleType(),
                Owner.class
        )
                .setParameter("settleType", SettleType.DAILY)
                .getResultList();
        // then
        assertThat(owners).hasSize(3);
    }

    @DisplayName("지급금을 지급금 유형 및 날짜를 기준으로 조회할 수 있다.")
    @Test
    void findSettleByTypeAndDateTime() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());

        settleRepository.save(SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.DAILY));
        settleRepository.save(SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.DAILY));
        settleRepository.save(SettleFixture.createWithoutId(owner, LocalDate.now(), SettleType.WEEK));
        settleRepository.save(SettleFixture.createWithoutId(owner, LocalDate.now().minusDays(1), SettleType.DAILY));
        // when
        SettleType type = SettleType.DAILY;
        SettleSumDto settleSumDto = entityManager.createQuery(
                ItemReaderQueryString.findSettleByTypeAndDateTime(),
                SettleSumDto.class
        )
                .setParameter("settleType", type)
                .setParameter("start", type.getStartCriteriaAt(LocalDate.now()))
                .setParameter("end", type.getEndCriteriaAt(LocalDate.now()))
                .getSingleResult();
        // then
        assertThatBigDecimal(settleSumDto.getTotalAmount()).isEqualTo(BigDecimal.valueOf(60_000));
    }

    @DisplayName("JPQL에서 값이 없는 경우 0원을 반환한다.")
    @Test
    void findSettleByTypeAndDateTimeWithoutValues() {
        // when
        SettleType type = SettleType.DAILY;
        SettleSumDto settleSumDto = entityManager.createQuery(
                ItemReaderQueryString.findSettleByTypeAndDateTime(),
                SettleSumDto.class
        )
                .setParameter("settleType", type)
                .setParameter("start", type.getStartCriteriaAt(LocalDate.now()))
                .setParameter("end", type.getEndCriteriaAt(LocalDate.now()))
                .getSingleResult();
        // then
        assertThatBigDecimal(settleSumDto.getTotalAmount()).isEqualTo(BigDecimal.valueOf(0));
    }
}