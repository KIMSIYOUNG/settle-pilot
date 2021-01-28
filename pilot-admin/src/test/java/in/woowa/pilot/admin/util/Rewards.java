package in.woowa.pilot.admin.util;

import in.woowa.pilot.admin.application.reward.dto.request.RewardCreateDto;
import in.woowa.pilot.admin.application.reward.dto.request.RewardUpdateDto;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponseDto;
import in.woowa.pilot.admin.application.reward.dto.response.RewardResponsesDto;
import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.admin.presentation.reward.dto.request.RewardCreateRequestDto;
import in.woowa.pilot.admin.presentation.reward.dto.request.RewardOrdersCreateRequestDto;
import in.woowa.pilot.admin.presentation.reward.dto.request.RewardPeriodCreateRequestDto;
import in.woowa.pilot.admin.presentation.reward.dto.request.RewardUpdateRequestDto;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.reward.Reward;
import in.woowa.pilot.core.reward.RewardType;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.reward.RewardFixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Rewards {
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(10000);
    public static final BigDecimal UPDATED_AMOUNT = BigDecimal.valueOf(10000);
    public static final RewardType SYSTEM_ERROR = RewardType.SYSTEM_ERROR;
    public static final RewardType UPDATED_TYPE = RewardType.DELIVERY_ACCIDENT;
    public static final String SYSTEM_ERROR_MESSAGE = "시스템 오류로 인한 보상금액";
    public static final String UPDATED_MESSAGE = "오토바이 사고가 났네요ㅠㅠ!";

    public static final String RESOURCE = "/api/rewards";

    public static RewardCreateDto createRequestDto(Long ownerId, LocalDateTime rewardDateTime) {
        return new RewardCreateDto(
                AMOUNT,
                SYSTEM_ERROR,
                SYSTEM_ERROR_MESSAGE,
                ownerId,
                rewardDateTime
        );
    }

    public static RewardCreateRequestDto webCreateRequestDto(Long ownerId, LocalDateTime rewardDateTime) {
        return new RewardCreateRequestDto(
                AMOUNT,
                SYSTEM_ERROR,
                SYSTEM_ERROR_MESSAGE,
                ownerId,
                rewardDateTime
        );
    }

    public static RewardCreateDto createRequestDto(Long ownerId) {
        return createRequestDto(ownerId, BaseFixture.YESTERDAY_MIDNIGHT);
    }

    public static RewardCreateRequestDto webCreateRequestDto(Long ownerId) {
        return webCreateRequestDto(ownerId, BaseFixture.YESTERDAY_MIDNIGHT);
    }

    public static RewardUpdateDto updateRequestDto(Long id) {
        return new RewardUpdateDto(
                id,
                UPDATED_AMOUNT,
                UPDATED_TYPE,
                UPDATED_MESSAGE
        );
    }

    public static RewardUpdateRequestDto webUpdateRequestDto(Long id) {
        return RewardUpdateRequestDto.testBuilder()
                .id(id)
                .amount(UPDATED_AMOUNT)
                .rewardType(UPDATED_TYPE)
                .description(UPDATED_MESSAGE)
                .build();
    }

    public static List<RewardCreateDto> createRequestDtos(Long ownerId, int count) {
        return IntStream.range(0, count)
                .mapToObj((i) -> createRequestDto(ownerId))
                .collect(Collectors.toList());
    }

    public static RewardResponseDto createResponseDto(Owner owner) {
        return new RewardResponseDto(RewardFixture.createWithId(2L, owner));
    }

    public static RewardResponsesDto createPagedResponseDto(Owner owner) {
        List<Reward> rewards = IntStream.range(2, 4)
                .mapToObj(i -> RewardFixture.createWithId(i, owner))
                .collect(Collectors.toList());
        CustomPageImpl<Reward> pagedRewards = new CustomPageImpl<>(rewards, BaseFixture.DEFAULT_PAGEABLE, rewards.size());

        return new RewardResponsesDto(pagedRewards);
    }

    public static RewardPeriodCreateRequestDto webRewardPeriodCreateRequest() {
        return RewardPeriodCreateRequestDto.testBuilder()
                .rewardType(RewardType.ABUSING)
                .description("어뷰징 처벌 보정금액")
                .rewardDateTime(LocalDateTime.now().plusDays(3))
                .startAt(LocalDateTime.now().minusDays(3))
                .endAt(LocalDateTime.now())
                .build();
    }

    public static RewardOrdersCreateRequestDto webRewardOrdersCreateRequest() {
        return RewardOrdersCreateRequestDto.testBuilder()
                .orderIds(Arrays.asList(1L, 2L, 3L, 4L, 6L))
                .rewardType(RewardType.SYSTEM_ERROR)
                .rewardDateTime(LocalDateTime.now().plusDays(3))
                .description("시스템 장애로 인한 주문금액 보상")
                .build();
    }
}
