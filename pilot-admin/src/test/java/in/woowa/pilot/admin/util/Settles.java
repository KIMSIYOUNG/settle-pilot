package in.woowa.pilot.admin.util;

import in.woowa.pilot.admin.application.settle.dto.request.SettleInCaseCreateDto;
import in.woowa.pilot.admin.application.settle.dto.request.SettleUpdateDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleAmountResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponseDto;
import in.woowa.pilot.admin.application.settle.dto.response.SettleResponsesDto;
import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.admin.presentation.settle.dto.request.SettleCompleteRequestDto;
import in.woowa.pilot.admin.presentation.settle.dto.request.SettleInCaseCreateRequestDto;
import in.woowa.pilot.admin.presentation.settle.dto.request.SettleRegularCreateRequestDto;
import in.woowa.pilot.admin.presentation.settle.dto.request.SettleUpdateRequestDto;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.*;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.settle.SettleFixture;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Settles {
    public static final SettleStatus UPDATED_SETTLE_STATUS = SettleStatus.COMPLETED;
    public static final String RESOURCE = "/api/settles";

    public static SettleInCaseCreateDto createRequestDto(Long ownerId) {
        return createRequestDto(ownerId, SettleType.DAILY, LocalDate.now());
    }

    public static SettleInCaseCreateRequestDto webCreateRequestDto(Long ownerId) {
        return new SettleInCaseCreateRequestDto(ownerId, SettleType.DAILY, LocalDate.now());
    }

    public static SettleInCaseCreateDto createRequestDto(Long ownerId, SettleType type, LocalDate criteriaDate) {
        return new SettleInCaseCreateDto(
                ownerId,
                type,
                criteriaDate
        );
    }

    public static SettleInCaseCreateRequestDto webCreateRequestDto(Long ownerId, SettleType type, LocalDate criteriaDate) {
        return new SettleInCaseCreateRequestDto(
                ownerId,
                type,
                criteriaDate
        );
    }

    public static SettleUpdateDto updateRequestDto(Long id) {
        return new SettleUpdateDto(id, UPDATED_SETTLE_STATUS);
    }

    public static SettleUpdateRequestDto webUpdateRequestDto(Long id) {
        return new SettleUpdateRequestDto(id, UPDATED_SETTLE_STATUS);
    }

    public static SettleResponseDto createResponseDto(Owner owner) {
        return new SettleResponseDto(SettleFixture.createWithId(owner, 1L));
    }

    public static SettleResponsesDto createPagedResponsesDto(Owner owner) {
        List<Settle> settles = createSettles(owner);
        Page<Settle> pagedSettle = new CustomPageImpl<>(settles, BaseFixture.DEFAULT_PAGEABLE, settles.size());

        return new SettleResponsesDto(pagedSettle);
    }

    public static SettleAmountResponseDto createAmountResponseDto(Owner owner) {
        return new SettleAmountResponseDto(snapshots(owner), LocalDateTime.now().minusDays(3), LocalDateTime.now());
    }

    private static List<SettleSnapshot> snapshots(Owner owner) {
        return IntStream.range(1, 3)
                .mapToObj(i -> SettleSnapshot.builder()
                        .type(SettleSnapshotType.BATCH)
                        .settles(createSettles(owner))
                        .startAt(LocalDateTime.now().minusDays(i))
                        .endAt(LocalDateTime.now())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private static List<Settle> createSettles(Owner owner) {
        return IntStream.range(1, 3)
                .mapToObj(i -> SettleFixture.createWithId(owner, i))
                .collect(Collectors.toList());
    }

    public static SettleCompleteRequestDto webSettleCompleteCondition() {
        return SettleCompleteRequestDto.testBuilder()
                .ownerId(1L)
                .settleIds(Arrays.asList(1L, 2L, 3L))
                .startAt(LocalDateTime.now().minusDays(3))
                .endAt(LocalDateTime.now())
                .build();
    }

    public static SettleRegularCreateRequestDto createRegularRequestDto() {
        return SettleRegularCreateRequestDto.testBuilder()
                .settleType(SettleType.DAILY)
                .criteriaDate(LocalDate.of(2020, 12, 1))
                .build();
    }
}
