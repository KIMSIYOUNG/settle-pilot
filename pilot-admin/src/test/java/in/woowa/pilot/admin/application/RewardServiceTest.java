package in.woowa.pilot.admin.application;

import in.woowa.pilot.admin.application.owner.OwnerService;
import in.woowa.pilot.admin.application.reward.RewardService;
import in.woowa.pilot.admin.application.reward.dto.request.RewardCreateDto;
import in.woowa.pilot.admin.application.reward.dto.request.RewardOrdersCreateDto;
import in.woowa.pilot.admin.application.reward.dto.request.RewardPeriodDto;
import in.woowa.pilot.admin.application.reward.dto.request.RewardSearchDto;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponseDto;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponsesDto;
import in.woowa.pilot.admin.application.settle.SettleService;
import in.woowa.pilot.admin.application.settle.dto.request.SettleInCaseCreateDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.util.Rewards;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import in.woowa.pilot.core.reward.RewardType;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.order.OrderFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static in.woowa.pilot.fixture.TestUtils.assertThatBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class RewardServiceTest extends IntegrationTest {
    private static final Long NOT_EXIST_ID = 1000L;

    @Autowired
    RewardService rewardService;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OwnerService ownerService;

    @Autowired
    SettleService settleService;

    @DisplayName("업주정보 없이 보상금액을 생성할 수 없다.")
    @Test
    void createWithoutOwner() {
        // given
        RewardCreateDto requestDto = Rewards.createRequestDto(NOT_EXIST_ID);
        // when, then
        assertThatThrownBy(() -> rewardService.create(requestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("owner not found with id : '%s'", NOT_EXIST_ID));
    }

    @DisplayName("정상적으로 보상금액을 생성할 수 있다.")
    @Test
    void create() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardCreateDto expected = Rewards.createRequestDto(owner.getId());
        // when
        RewardResponseDto actual = rewardService.create(expected);
        // then
        assertAll(
                () -> assertThat(actual.getRewardNo()).isNotNull(),
                () -> assertThat(actual.getRewardType()).isEqualTo(expected.getRewardType()),
                () -> assertThat(actual).isEqualToIgnoringGivenFields(expected, "id", "owner", "order", "amount", "rewardNo", "rewardType"),
                () -> assertThat(actual.getOwner().getId()).isEqualTo(expected.getOwnerId()),
                () -> assertThatBigDecimal(actual.getAmount()).isEqualTo(expected.getAmount())
        );
    }

    @DisplayName("주문을 기반으로 지급금을 생성할 수 있다.")
    @Test
    void createByOrders() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        List<Long> orderIds = Arrays.asList(
                orderRepository.save(OrderFixture.createWithoutId(owner, 1)).getId(),
                orderRepository.save(OrderFixture.createWithoutId(owner, 1)).getId(),
                orderRepository.save(OrderFixture.createWithoutId(owner, 1)).getId(),
                orderRepository.save(OrderFixture.createWithoutId(owner, 1)).getId()
        );
        // when
        RewardOrdersCreateDto request = RewardOrdersCreateDto.builder()
                .orderIds(orderIds)
                .rewardType(RewardType.ETC)
                .description("기타")
                .rewardDateTime(LocalDateTime.now())
                .build();
        rewardService.createByOrders(request);
        List<RewardResponseDto> content = rewardService.findAll(BaseFixture.DEFAULT_PAGEABLE).getRewards().getContent();
        // then
        assertAll(
                () -> assertThat(content).hasSize(orderIds.size()),
                () -> assertThat(content.get(0).getOrder().getId()).isEqualTo(orderIds.get(0)),
                () -> assertThat(content.get(1).getOrder().getId()).isEqualTo(orderIds.get(1)),
                () -> assertThat(content.get(2).getOrder().getId()).isEqualTo(orderIds.get(2)),
                () -> assertThat(content.get(3).getOrder().getId()).isEqualTo(orderIds.get(3))
        );
    }

    @DisplayName("특정 기간동안 발생한 주문데이터를 통해 보상금액을 생성할 수 있다.")
    @Test
    void createByPeriod() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        orderRepository.save(OrderFixture.createWithoutId(owner, 3, LocalDateTime.now().minusDays(1)));
        orderRepository.save(OrderFixture.createWithoutId(owner, 3, LocalDateTime.now().minusDays(2)));
        Order order1 = orderRepository.save(OrderFixture.createWithoutId(owner, 3, LocalDateTime.now().minusDays(3)));
        Order order2 = orderRepository.save(OrderFixture.createWithoutId(owner, 3, LocalDateTime.now().minusDays(4)));
        // when
        RewardPeriodDto request = RewardPeriodDto.builder()
                .rewardType(RewardType.SYSTEM_ERROR)
                .rewardDateTime(LocalDateTime.now())
                .description("시스템 오류")
                .startAt(LocalDateTime.now().minusDays(5))
                .endAt(LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.MIDNIGHT))
                .build();
        rewardService.createByPeriod(request);
        List<RewardResponseDto> content = rewardService.findAll(BaseFixture.DEFAULT_PAGEABLE).getRewards().getContent();
        // then
        assertAll(
                () -> assertThat(content).hasSize(2),
                () -> assertThat(content.get(0).getOrder().getId()).isEqualTo(order1.getId()),
                () -> assertThat(content.get(1).getOrder().getId()).isEqualTo(order2.getId())
        );
    }

    @DisplayName("저장된 보상금액을 페이지기반으로 찾아올 수 있다.")
    @Test
    void findWithPage() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto expected = rewardService.create(Rewards.createRequestDto(owner.getId()));
        rewardService.create(Rewards.createRequestDto(owner.getId()));
        rewardService.create(Rewards.createRequestDto(owner.getId()));
        // when
        Page<RewardResponseDto> rewards = rewardService.findAll(PageRequest.of(0, 1)).getRewards();
        RewardResponseDto actual = rewards.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(rewards.getTotalElements()).isEqualTo(3),
                () -> assertThat(rewards.getTotalPages()).isEqualTo(3),
                () -> assertThat(actual).isEqualToIgnoringGivenFields(expected, "amount", "owner"),
                () -> assertThatBigDecimal(actual.getAmount()).isEqualTo(expected.getAmount()),
                () -> assertThat(actual.getOwner().getId()).isEqualToComparingFieldByField(expected.getOwner().getId())
        );
    }

    @DisplayName("저장된 보상금액을 ID로 찾아올 수 있다.")
    @Test
    void findById() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto expected = rewardService.create(Rewards.createRequestDto(owner.getId()));
        // when
        RewardResponseDto actual = rewardService.fetchById(expected.getId());
        // then
        assertAll(
                () -> assertThat(actual).isEqualToIgnoringGivenFields(expected, "amount", "owner"),
                () -> assertThatBigDecimal(actual.getAmount()).isEqualTo(expected.getAmount()),
                () -> assertThat(actual.getOwner().getId()).isEqualTo(expected.getOwner().getId())
        );
    }

    @DisplayName("이름으로 업주를 조회하여 보상금액을 가져올 수 있다.")
    @Test
    void fetchByCondition() {
        // given
        Owner nameOwner = ownerRepository.save(OwnerFixture.createWithoutId("AAA", "aaa@gmail.com"));
        Owner emailOwner = ownerRepository.save(OwnerFixture.createWithoutId("BBB", "bbb@gmail.com"));
        List<RewardCreateDto> nameRequest = Rewards.createRequestDtos(nameOwner.getId(), 2);
        List<RewardCreateDto> emailRequest = Rewards.createRequestDtos(emailOwner.getId(), 3);
        saveAll(nameRequest, emailRequest);
        // when
        Page<RewardResponseDto> rewardsByName = rewardService.fetchPagedByCondition(
                RewardSearchDto.builder().ownerName(nameOwner.getName()).build(),
                PageRequest.of(0, 1)
        ).getRewards();
        RewardCreateDto expectedByName = nameRequest.get(0);
        RewardResponseDto actualByName = rewardsByName.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(rewardsByName.getTotalElements()).isEqualTo(2),
                () -> assertThat(rewardsByName.getPageable().getPageNumber()).isEqualTo(0),
                () -> assertThat(rewardsByName.getContent()).hasSize(1),
                () -> assertThat(actualByName.getRewardNo()).isNotNull(),
                () -> assertThat(actualByName.getRewardType()).isEqualTo(expectedByName.getRewardType()),
                () -> assertThat(actualByName.getOwner().getId()).isEqualTo(expectedByName.getOwnerId()),
                () -> assertThatBigDecimal(actualByName.getAmount()).isEqualTo(expectedByName.getAmount()),
                () -> assertThat(actualByName)
                        .isEqualToIgnoringGivenFields(expectedByName, "id", "owner", "order", "amount", "rewardNo", "rewardType")
        );
    }

    @DisplayName("이메일로 업주를 조회하여 보상금액을 가져올 수 있다.")
    @Test
    void fetchByEmail() {
        // given
        Owner nameOwner = ownerRepository.save(OwnerFixture.createWithoutId("AAA", "aaa@gmail.com"));
        Owner emailOwner = ownerRepository.save(OwnerFixture.createWithoutId("BBB", "bbb@gmail.com"));
        List<RewardCreateDto> nameRequest = Rewards.createRequestDtos(nameOwner.getId(), 2);
        List<RewardCreateDto> emailRequest = Rewards.createRequestDtos(emailOwner.getId(), 3);
        saveAll(nameRequest, emailRequest);
        // when
        Page<RewardResponseDto> rewardsByEmail = fetchByOwnerCondition(null, emailOwner.getEmail());
        RewardCreateDto expectedByEmail = emailRequest.get(0);
        RewardResponseDto actualByEmail = rewardsByEmail.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(rewardsByEmail.getContent()).hasSize(3),
                () -> assertThat(actualByEmail.getOwner().getId()).isEqualTo(expectedByEmail.getOwnerId()),
                () -> assertThatBigDecimal(actualByEmail.getAmount()).isEqualTo(expectedByEmail.getAmount()),
                () -> assertThat(actualByEmail.getRewardNo()).isNotNull(),
                () -> assertThat(actualByEmail.getRewardType()).isEqualTo(expectedByEmail.getRewardType()),
                () -> assertThat(actualByEmail)
                        .isEqualToIgnoringGivenFields(expectedByEmail, "id", "owner", "order", "amount", "rewardNo", "rewardType")
        );
    }

    @DisplayName("업주의 이름과 이메일로 업주를 조회하여 보상금액을 가져올 수 있다.")
    @Test
    void fetchByEmailAndName() {
        // given
        Owner owner1 = ownerRepository.save(OwnerFixture.createWithoutId("AAA", "aaa@gmail.com"));
        Owner owner2 = ownerRepository.save(OwnerFixture.createWithoutId("AAA", "bbb@gmail.com"));
        Owner owner3 = ownerRepository.save(OwnerFixture.createWithoutId("BBB", "ccc@gmail.com"));
        List<RewardCreateDto> owner1Request = Rewards.createRequestDtos(owner1.getId(), 2);
        List<RewardCreateDto> owner2Request = Rewards.createRequestDtos(owner2.getId(), 2);
        List<RewardCreateDto> owner3Request = Rewards.createRequestDtos(owner3.getId(), 3);
        saveAll(owner1Request, owner2Request, owner3Request);
        // when
        Page<RewardResponseDto> rewards = fetchByOwnerCondition(owner1.getName(), owner1.getEmail());

        RewardCreateDto expectedFirstOwner = owner1Request.get(0);
        RewardResponseDto actualFirstOwner = rewards.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(rewards.getContent()).hasSize(2),
                () -> assertThat(actualFirstOwner.getOwner().getId()).isEqualTo(expectedFirstOwner.getOwnerId()),
                () -> assertThat(actualFirstOwner.getRewardType()).isEqualTo(expectedFirstOwner.getRewardType()),
                () -> assertThat(actualFirstOwner.getRewardNo()).isNotNull(),
                () -> assertThatBigDecimal(actualFirstOwner.getAmount()).isEqualTo(expectedFirstOwner.getAmount()),
                () -> assertThat(actualFirstOwner).isEqualToIgnoringGivenFields(expectedFirstOwner, "id", "owner", "order", "amount", "rewardNo", "rewardType")
        );
    }

    @DisplayName("특정 일시를 기준으로 보상금액을 조회할 수 있다.")
    @Test
    void findByDateTime() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto expected = rewardService.create(Rewards.createRequestDto(owner.getId(), BaseFixture.TODAY_MIDNIGHT));
        rewardService.create(Rewards.createRequestDto(owner.getId(), BaseFixture.YESTERDAY_MIDNIGHT));
        rewardService.create(Rewards.createRequestDto(owner.getId(), BaseFixture.YESTERDAY_MIDNIGHT));
        // when
        RewardSearchDto condition = RewardSearchDto.builder()
                .startAt(BaseFixture.TODAY_MIDNIGHT)
                .endAt(BaseFixture.TODAY_MIDNIGHT.plusMinutes(20))
                .build();
        Page<RewardResponseDto> rewards = rewardService.fetchPagedByCondition(condition, BaseFixture.DEFAULT_PAGEABLE).getRewards();
        RewardResponseDto actual = rewards.getContent().get(0);
        // then
        assertAll(
                () -> assertThat(rewards.getTotalElements()).isEqualTo(1),
                () -> assertThat(actual.getRewardType()).isEqualTo(expected.getRewardType()),
                () -> assertThatBigDecimal(actual.getAmount()).isEqualTo(expected.getAmount()),
                () -> assertThat(actual.getRewardDateTime()).isEqualTo(expected.getRewardDateTime())
        );
    }

    @DisplayName("검색시 삭제된 업주는 제외된다.")
    @Test
    void fetchAll() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto expected = rewardService.create(Rewards.createRequestDto(owner.getId()));
        // when
        ownerService.delete(owner.getId());
        RewardResponsesDto response = rewardService.findAll(BaseFixture.DEFAULT_PAGEABLE);
        // then
        assertThat(response.getRewards().getTotalElements()).isEqualTo(0);
    }

    @DisplayName("정산된 보상금액을 조회한다.")
    @Test
    void fetchSettleId() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        rewardService.create(Rewards.createRequestDto(owner.getId(), LocalDateTime.now()));
        rewardService.create(Rewards.createRequestDto(owner.getId(), LocalDateTime.now()));
        rewardService.create(Rewards.createRequestDto(owner.getId(), LocalDateTime.now()));
        // when
        SettleInCaseCreateDto settleRequest = new SettleInCaseCreateDto(owner.getId(), SettleType.WEEK, LocalDate.now());
        SettleResponseDto settle = settleService.createInCaseSettle(settleRequest);
        Page<RewardResponseDto> rewards = rewardService.fetchBySettleId(settle.getId(), PageRequest.of(0, 2)).getRewards();
        // then
        assertAll(
                () -> assertThat(rewards.getTotalElements()).isEqualTo(3),
                () -> assertThat(rewards.getTotalPages()).isEqualTo(2),
                () -> assertThat(rewards.getContent()).hasSize(2)
        );
    }

    @DisplayName("보상금액을 수정할 수 있다.")
    @Test
    void update() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto expected = rewardService.create(Rewards.createRequestDto(owner.getId()));
        // when
        rewardService.update(Rewards.updateRequestDto(expected.getId()));
        RewardResponseDto response = rewardService.fetchById(expected.getId());
        // then
        assertAll(
                () -> assertThat(response.getRewardType()).isEqualTo(Rewards.UPDATED_TYPE),
                () -> assertThatBigDecimal(response.getAmount()).isEqualTo(Rewards.UPDATED_AMOUNT),
                () -> assertThat(response.getDescription()).isEqualTo(Rewards.UPDATED_MESSAGE)
        );
    }

    @DisplayName("이미 지급금으로 사용된 보상금액은 삭제할 수 없다.")
    @Test
    void rewardWithSettle() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto expected = rewardService.create(RewardCreateDto.builder()
                .ownerId(owner.getId())
                .rewardDateTime(LocalDateTime.now())
                .description("TEST")
                .amount(BigDecimal.ZERO)
                .rewardType(RewardType.ETC)
                .build());

        settleService.createInCaseSettle(SettleInCaseCreateDto.builder()
                .ownerId(owner.getId())
                .settleType(SettleType.DAILY)
                .criteriaDate(LocalDate.now())
                .build());

        // when , then
        assertThatThrownBy(() -> rewardService.delete(expected.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지급금으로 생성된 보상금액은 삭제할 수 없습니다.");
    }

    @DisplayName("보상금액을 삭제할 수 있다.")
    @Test
    void delete() {
        // given
        Owner owner = ownerRepository.save(OwnerFixture.createWithoutId());
        RewardResponseDto response = rewardService.create(Rewards.createRequestDto(owner.getId()));
        // when
        rewardService.delete(response.getId());
        // then
        assertThatThrownBy(() -> rewardService.fetchById(response.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("reward not found with id : '%s'", response.getId()));
    }

    private void saveAll(List<RewardCreateDto>... rewards) {
        Arrays.stream(rewards)
                .flatMap(Collection::stream)
                .forEach(rewardCreateRequest -> rewardService.create(rewardCreateRequest));
    }

    private Page<RewardResponseDto> fetchByOwnerCondition(String ownerName, String ownerEmail) {
        return rewardService.fetchPagedByCondition(
                RewardSearchDto.builder()
                        .ownerName(ownerName)
                        .ownerEmail(ownerEmail)
                        .build(),
                BaseFixture.DEFAULT_PAGEABLE
        )
                .getRewards();
    }
}