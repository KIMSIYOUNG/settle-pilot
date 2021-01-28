package in.woowa.pilot.admin.util;

import in.woowa.pilot.admin.application.owner.dto.request.OwnerCreateDto;
import in.woowa.pilot.admin.application.owner.dto.request.OwnerUpdateDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerPagedResponsesDto;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.admin.presentation.owner.dto.request.AccountDto;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerCreateRequestDto;
import in.woowa.pilot.admin.presentation.owner.dto.request.OwnerUpdateRequestDto;
import in.woowa.pilot.core.account.AccountNumber;
import in.woowa.pilot.core.account.AccountType;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.owner.OwnerFixture;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Owners {
    public static final String TEST_NAME = "OWNER";
    public static final String TEST_EMAIL = "owner@gmail.com";
    public static final String CHANGED_NAME = "CHANGED_NAME";
    public static final String CHANGED_EMAIL = "CHANGE@gmail.com";
    public static final SettleType DEFAULT_SETTLE_TYPE = SettleType.DAILY;

    public static final String RESOURCE = "/api/owners";

    public static OwnerCreateDto createRequestDto() {
        return createRequestDto(TEST_NAME, UUID.randomUUID() + TEST_EMAIL);
    }

    public static OwnerCreateRequestDto webCreateRequestDto() {
        return webCreateRequestDto(TEST_NAME, UUID.randomUUID() + TEST_EMAIL);
    }

    public static OwnerCreateRequestDto webCreateRequestDto(String name, String email) {
        return new OwnerCreateRequestDto(name, email, DEFAULT_SETTLE_TYPE, getRandomAccountNo());
    }

    public static OwnerCreateDto createRequestDto(String name, String email) {
        return new OwnerCreateDto(name, email, DEFAULT_SETTLE_TYPE, getRandomAccountNo());
    }

    public static OwnerUpdateDto createUpdateRequest(Long id) {
        return new OwnerUpdateDto(id, CHANGED_NAME, CHANGED_EMAIL, SettleType.DAILY);
    }

    public static OwnerUpdateRequestDto webCreateUpdateRequest(Long id) {
        return new OwnerUpdateRequestDto(id, CHANGED_NAME, CHANGED_EMAIL, SettleType.DAILY);
    }

    public static OwnerResponseDto createResponseDto() {
        return new OwnerResponseDto(OwnerFixture.createWithId());
    }

    public static OwnerPagedResponsesDto createPagedResponse() {
        List<Owner> owners = IntStream.range(0, 5)
                .mapToObj(i -> OwnerFixture.createWithId(i, "OWNER" + i, "OWNER@woowahan.com"))
                .collect(Collectors.toList());

        Page<Owner> pagedOwners = new CustomPageImpl<>(owners, BaseFixture.DEFAULT_PAGEABLE, 5);

        return new OwnerPagedResponsesDto(pagedOwners);
    }

    private static AccountDto getRandomAccountNo() {
        return new AccountDto(AccountType.BANK, new AccountNumber(UUID.randomUUID().toString()));
    }
}
