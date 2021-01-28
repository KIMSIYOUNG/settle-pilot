package in.woowa.pilot.admin.application;

import in.woowa.pilot.admin.application.owner.OwnerService;
import in.woowa.pilot.admin.application.settle.SettleService;
import in.woowa.pilot.admin.application.settle.dto.request.*;
import in.woowa.pilot.admin.application.settle.dto.response.SettleAmountResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.util.Settles;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardRepository;
import in.woowa.pilot.core.settle.*;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SettleServiceTest extends IntegrationTest {
    private static final Long NOT_EXIST_ID = 10000L;

    @Autowired
    private SettleService settleService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private SettleSnapshotRepository settleSnapshotRepository;

    @DisplayName("존재하지 않는 Owner의 지급금을 생성할 때 예외가 발생한다.")
    @Test
    void createWithNotExistOwnerId() {
        // given
        SettleInCaseCreateDto requestDto = Settles.createRequestDto(NOT_EXIST_ID);
        // when, then
        assertThatThrownBy(() -> settleService.createInCaseSettle(requestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("owner not found with id : '%s'", NOT_EXIST_ID));
    }

    @DisplayName("일단위로 지급금을 생성할 수 있다. 생성시 주문/보상금액에는 지급금 번호가 세팅된다.")
    @Test
    void create() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());

        Order savedOrder1 = orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));
        Order savedOrder2 = orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        Reward savedReward = rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50_000), LocalDateTime.now()));
        // when
        SettleInCaseCreateDto createDto = new SettleInCaseCreateDto(
                owner1.getId(),
                SettleType.DAILY,
                LocalDate.now()
        );
        SettleResponseDto response = settleService.createInCaseSettle(createDto);
        List<SettleSnapshot> snapshots = settleSnapshotRepository.findByPeriod(createDto.getStartDate(), createDto.getEndDate());
        Order findOrder1 = orderRepository.findById(savedOrder1.getId()).get();
        Order findOrder2 = orderRepository.findById(savedOrder2.getId()).get();
        Reward reward = rewardRepository.findById(savedReward.getId()).get();
        // then
        assertAll(
                () -> assertThatBigDecimal(snapshots.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(90_000)),
                () -> assertThat(snapshots.get(0).getType()).isEqualTo(SettleSnapshotType.IN_CASE),
                () -> assertThatBigDecimal(response.getAmount()).isEqualTo(BigDecimal.valueOf(90_000)),
                () -> assertThat(response.getId()).isEqualTo(findOrder1.getSettle().getId()),
                () -> assertThat(response.getId()).isEqualTo(findOrder2.getSettle().getId()),
                () -> assertThat(response.getId()).isEqualTo(reward.getSettle().getId())
        );
    }

    @DisplayName("주단위로 지급금을 생성할 수 있다.")
    @Test
    void createWeek() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.of(LocalDate.of(2020, 12, 27), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.of(LocalDate.of(2020, 12, 31), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2021, 1, 4), LocalTime.MIDNIGHT)));
        // when
        SettleResponseDto response = settleService.createInCaseSettle(new SettleInCaseCreateDto(
                owner1.getId(),
                SettleType.WEEK,
                LocalDate.of(2020, 12, 29)
        ));
        // then
        assertThatBigDecimal(response.getAmount()).isEqualTo(BigDecimal.valueOf(90_000));
    }

    @DisplayName("월단위로 지급금을 생성할 수 있다.")
    @Test
    void createMonth() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.of(LocalDate.of(2020, 12, 10), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.of(LocalDate.of(2020, 12, 23), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.of(LocalDate.of(2020, 12, 29), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.MIDNIGHT)));
        // when
        SettleInCaseCreateDto createDto = new SettleInCaseCreateDto(
                owner1.getId(),
                SettleType.MONTH,
                LocalDate.of(2020, 12, 1)
        );
        SettleResponseDto response = settleService.createInCaseSettle(createDto);
        // then
        assertThatBigDecimal(response.getAmount()).isEqualTo(BigDecimal.valueOf(100_000));
    }

    @DisplayName("일반적인 지급금 생성로직 1. 일단위 정산")
    @Test
    void createRegularSettles() {
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));
        Owner owner3 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));

        orderRepository.save(OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner3, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner3, 2, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner3, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        // when
        SettleRegularCreateDto createDto = SettleRegularCreateDto.builder()
                .settleType(SettleType.DAILY)
                .criteriaDate(LocalDate.of(2020, 12, 1))
                .build();
        settleService.createRegularSettle(createDto);
        List<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles().getContent();
        List<SettleSnapshot> snapshots = settleSnapshotRepository.findByPeriod(createDto.getStartDateTime(), createDto.getEndDateTime());
        // then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(snapshots).hasSize(1),
                () -> assertThatBigDecimal(response.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(90_000)),
                () -> assertThatBigDecimal(response.get(1).getAmount()).isEqualTo(BigDecimal.valueOf(90_000)),
                () -> assertThatBigDecimal(snapshots.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(180_000)),
                () -> assertThat(response).noneMatch((res) -> res.getOwnerId().equals(owner2.getId())),
                () -> assertThat(snapshots.get(0).getType()).isEqualTo(SettleSnapshotType.BATCH)
        );
    }

    @DisplayName("일반적인 지급금 생성로직 1. 주단위 정산")
    @Test
    void createRegularSettleByWeek() {
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.WEEK));
        Owner owner3 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.DAILY));

        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner3, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner3, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner3, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        // when
        SettleRegularCreateDto createDto = SettleRegularCreateDto.builder()
                .settleType(SettleType.WEEK)
                .criteriaDate(LocalDate.of(2020, 12, 1))
                .build();
        settleService.createRegularSettle(createDto);

        List<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles().getContent();
        List<SettleSnapshot> snapshots = settleSnapshotRepository.findByPeriod(createDto.getStartDateTime(), createDto.getEndDateTime());
        // then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(snapshots).hasSize(1),
                () -> assertThatBigDecimal(response.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(70_000)),
                () -> assertThatBigDecimal(response.get(1).getAmount()).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThatBigDecimal(snapshots.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(90_000)),
                () -> assertThat(response).noneMatch((res) -> res.getOwnerId().equals(owner3.getId())),
                () -> assertThat(snapshots.get(0).getType()).isEqualTo(SettleSnapshotType.BATCH)
        );
    }

    @DisplayName("일반적인 지급금 생성로직 1. 월단위 정산")
    @Test
    void createRegularSettleByMonth() {
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId(SettleType.MONTH));

        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.of(LocalDate.of(2020, 12, 1), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.of(LocalDate.of(2020, 12, 31), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.of(LocalDate.of(2020, 11, 30), LocalTime.MIDNIGHT)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2020, 12, 15), LocalTime.MIDNIGHT)));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.of(LocalDate.of(2020, 11, 30), LocalTime.MIDNIGHT)));
        // when
        SettleRegularCreateDto createDto = SettleRegularCreateDto.builder()
                .settleType(SettleType.MONTH)
                .criteriaDate(LocalDate.of(2020, 12, 1))
                .build();
        settleService.createRegularSettle(createDto);

        List<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles().getContent();
        List<SettleSnapshot> snapshots = settleSnapshotRepository.findByPeriod(createDto.getStartDateTime(), createDto.getEndDateTime());
        // then
        assertAll(
                () -> assertThat(response).hasSize(1),
                () -> assertThat(snapshots).hasSize(1),
                () -> assertThatBigDecimal(response.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(70_000)),
                () -> assertThatBigDecimal(snapshots.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(70_000)),
                () -> assertThat(response).noneMatch((res) -> res.getOwnerId().equals(owner2.getId())),
                () -> assertThat(snapshots.get(0).getType()).isEqualTo(SettleSnapshotType.BATCH)
        );
    }

    @DisplayName("삭제한 업주의 정산금액은 조회되지 않는다.")
    @Test
    void fetchPagedAllWithOwner() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        // when
        settleService.createInCaseSettle(new SettleInCaseCreateDto(
                owner1.getId(),
                SettleType.WEEK,
                LocalDate.now()
        ));
        ownerService.delete(owner1.getId());
        Page<SettleResponseDto> settles = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        // then
        assertThat(settles.getTotalElements()).isEqualTo(0);
    }

    @DisplayName("모든 정산 내역을 조회한다.")
    @Test
    void fetchPagedAll() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        for (int i = 1; i <= 5; i++) {
            LocalDateTime orderDateTime = LocalDateTime.now().minusDays(i);
            orderRepository.save(OrderFixture.createWithoutId(owner1, 1, orderDateTime));
            settleService.createInCaseSettle(new SettleInCaseCreateDto(
                    owner1.getId(),
                    SettleType.DAILY,
                    orderDateTime.toLocalDate()
            ));
        }
        // when
        Page<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        SettleResponseDto content = response.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(5),
                () -> assertThat(content.getSettleStatus()).isEqualTo(SettleStatus.CREATED.name()),
                () -> assertThat(content.getOwnerId()).isEqualTo(owner1.getId()),
                () -> assertThatBigDecimal(calculateTotalAmount(response)).isEqualTo(BigDecimal.valueOf(50_000))
        );
    }

    @DisplayName("검색 조건에 따른 지급금 조회")
    @Test
    void fetchPagedByCondition() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        for (int i = 1; i <= 5; i++) {
            LocalDateTime orderDateTime = LocalDateTime.of(LocalDate.now().minusDays(i), LocalTime.MIDNIGHT);
            orderRepository.save(OrderFixture.createWithoutId(owner1, 1, orderDateTime));
            settleService.createInCaseSettle(new SettleInCaseCreateDto(
                    owner1.getId(),
                    SettleType.DAILY,
                    orderDateTime.toLocalDate()
            ));
        }
        // when
        SettleSearchDto condition = SettleSearchDto.builder()
                .ownerId(owner1.getId())
                .startAt(LocalDateTime.now().minusDays(3))
                .endAt(LocalDateTime.now())
                .settleStatus(SettleStatus.CREATED)
                .build();
        Page<SettleResponseDto> response = settleService.fetchPagedByCondition(condition, BaseFixture.DEFAULT_PAGEABLE).getSettles();
        // then
        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(2),
                () -> assertThatBigDecimal(calculateTotalAmount(response)).isEqualTo(BigDecimal.valueOf(20_000)),
                () -> assertThat(response.getContent().get(0).getOwnerId()).isEqualTo(owner1.getId()),
                () -> assertThat(response.getContent().get(1).getSettleStatus()).isEqualTo(SettleStatus.CREATED.name())
        );
    }

    /**
     * 업주 1 - 주문 40,000원 | 보상금액 50,000원 -> 정산 완료
     * 업주 2 - 주문 40,000원 -> 정산 대기중
     * 총 금액 : 130,000원 / 완료 금액 : 90,000원 / 남은 금액 40,000원
     */
    @DisplayName("전체 정산 금액을 가져올 수 있다.")
    @Test
    void fetchTotalMoney() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 3, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.now()));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.now()));

        SettleInCaseCreateDto createDto = Settles.createRequestDto(owner1.getId());
        SettleResponseDto owner1Settle = settleService.createInCaseSettle(createDto);
        SettleResponseDto owner2Settle = settleService.createInCaseSettle(Settles.createRequestDto(owner2.getId()));
        settleService.updateSettleStatus(Settles.updateRequestDto(owner1Settle.getId()));
        // when
        SettleAmountResponseDto amountDto = settleService.fetchAmountByCondition(
                SettleSnapshotSearchDto.builder()
                        .startAt(createDto.getStartDate())
                        .endAt(createDto.getEndDate())
                        .build()
        );
        // then
        assertThatBigDecimal(amountDto.getAmount()).isEqualTo(BigDecimal.valueOf(130_000));
    }

    /**
     * 업주 1(조회 대상) - 주문 40,000원 | 보상금액 50,000원
     * 업주 2 - 주문 40,000원
     * 총 금액 : 90,000원 / 완료 금액 : 90,000원 / 남은 금액 0원
     */
    @DisplayName("업주 단위로 정산 금액 가져올 수 있다.")
    @Test
    void fetchMoneyByOwnerId() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());

        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        rewardRepository.save(RewardFixture.createWithoutId(owner1, BigDecimal.valueOf(50000), LocalDateTime.now()));

        orderRepository.save(OrderFixture.createWithoutId(owner2, 3, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 1, LocalDateTime.now()));

        SettleInCaseCreateDto createDto = Settles.createRequestDto(
                owner1.getId(),
                SettleType.DAILY,
                LocalDate.now()
        );
        SettleResponseDto owner1Settle = settleService.createInCaseSettle(createDto);
        settleService.updateSettleStatus(Settles.updateRequestDto(owner1Settle.getId()));
        // when
        SettleAmountResponseDto amountDto = settleService.fetchAmountByCondition(
                SettleSnapshotSearchDto.builder()
                        .startAt(createDto.getStartDate())
                        .endAt(createDto.getEndDate())
                        .build()
        );
        // then
        assertThatBigDecimal(amountDto.getAmount()).isEqualTo(BigDecimal.valueOf(90_000));
    }

    @DisplayName("지급금을 처리완료한다.")
    @Test
    void updateSettleStatus() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));

        SettleResponseDto todaySettle = settleService.createInCaseSettle(new SettleInCaseCreateDto(
                owner1.getId(),
                SettleType.DAILY,
                LocalDate.now()
        ));
        // when
        settleService.updateSettleStatus(Settles.updateRequestDto(todaySettle.getId()));
        Page<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        SettleResponseDto changedSettle = response.getContent().get(0);
        // then
        assertThat(changedSettle.getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name());
    }

    @DisplayName("지급금을 벌크성으로 완료처리 할 수 있다.")
    @Test
    void updateBulkSettle() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now().minusDays(1)));
        orderRepository.save(OrderFixture.createWithoutId(owner2, 3, LocalDateTime.now()));

        settleService.createInCaseSettle(new SettleInCaseCreateDto(owner1.getId(), SettleType.DAILY, LocalDate.now()));
        settleService.createInCaseSettle(new SettleInCaseCreateDto(owner1.getId(), SettleType.DAILY, LocalDate.now().minusDays(1)));
        settleService.createInCaseSettle(new SettleInCaseCreateDto(owner2.getId(), SettleType.DAILY, LocalDate.now()));

        // when
        settleService.bulkCompleteSettleStatus(new SettleCompleteConditionDto(owner1.getId(), null, null, null));
        // then
        Page<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        List<SettleResponseDto> content = response.getContent();

        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(3),
                () -> assertThat(content.get(0).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name()),
                () -> assertThat(content.get(1).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name()),
                () -> assertThat(content.get(2).getSettleStatus()).isEqualTo(SettleStatus.CREATED.name())
        );
    }

    @DisplayName("지급금을 벌크성으로 완료처리 할 수 있다. - 지급금 번호들로")
    @Test
    void updateBulkSettleIds() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now().minusDays(1)));

        SettleResponseDto settle1 = settleService.createInCaseSettle(new SettleInCaseCreateDto(owner1.getId(), SettleType.DAILY, LocalDate.now()));
        SettleResponseDto settle2 = settleService.createInCaseSettle(new SettleInCaseCreateDto(owner1.getId(), SettleType.DAILY, LocalDate.now().minusDays(1)));
        // when
        settleService.bulkCompleteSettleStatus(new SettleCompleteConditionDto(null, Arrays.asList(settle1.getId(), settle2.getId()), null, null));
        // then
        Page<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        List<SettleResponseDto> content = response.getContent();

        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(2),
                () -> assertThat(content.get(0).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name()),
                () -> assertThat(content.get(1).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name())
        );
    }

    @DisplayName("지급금을 벌크성으로 완료처리 할 수 있다. - 특정일자를 기준으로")
    @Test
    void updateBulkByStartAndEnd() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now()));
        orderRepository.save(OrderFixture.createWithoutId(owner1, 3, LocalDateTime.now().minusDays(1)));

        settleService.createInCaseSettle(new SettleInCaseCreateDto(owner1.getId(), SettleType.DAILY, LocalDate.now()));
        settleService.createInCaseSettle(new SettleInCaseCreateDto(owner1.getId(), SettleType.DAILY, LocalDate.now().minusDays(1)));
        // when
        settleService.bulkCompleteSettleStatus(new SettleCompleteConditionDto(
                null,
                null,
                DateTimeUtils.dailyStartDateTime(LocalDate.now().minusDays(1)),
                DateTimeUtils.dailyEndDateTime(LocalDate.now()))
        );
        // then
        Page<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        List<SettleResponseDto> content = response.getContent();

        assertAll(
                () -> assertThat(response.getTotalElements()).isEqualTo(2),
                () -> assertThat(content.get(0).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name()),
                () -> assertThat(content.get(1).getSettleStatus()).isEqualTo(SettleStatus.COMPLETED.name())
        );
    }

    @DisplayName("지급 내역을 삭제한다.")
    @Test
    void deleteById() {
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner1, 1, LocalDateTime.now()));

        SettleResponseDto todaySettle = settleService.createInCaseSettle(new SettleInCaseCreateDto(
                owner1.getId(),
                SettleType.DAILY,
                LocalDate.now()
        ));
        // when
        settleService.deleteById(todaySettle.getId());
        Page<SettleResponseDto> response = settleService.fetchPagedAll(BaseFixture.DEFAULT_PAGEABLE).getSettles();
        // then
        assertThat(response.getTotalElements()).isEqualTo(0);
    }

    private BigDecimal calculateTotalAmount(Page<SettleResponseDto> response) {
        return response.getContent().stream()
                .map(SettleResponseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}