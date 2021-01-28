package in.woowa.pilot.admin.util;

import in.woowa.pilot.admin.application.member.dto.request.MemberChangeRoleDto;
import in.woowa.pilot.admin.application.member.dto.request.MemberCreateDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberAuthorityResponseDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponsesDto;
import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.admin.presentation.member.dto.request.MemberRoleRequestDto;
import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.Role;
import in.woowa.pilot.fixture.BaseFixture;
import in.woowa.pilot.fixture.member.MemberFixture;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Members {
    public static final String TEST_NAME = "TEST_NAME";
    public static final String TEST_EMAIL = "siyoung@woowahan.com";
    public static final AuthProvider TEST_PROVIDER = AuthProvider.GOOGLE;
    public static final Role CHANGED_ROLE = Role.ADMIN;

    public static MemberCreateDto createRequestDto() {
        return new MemberCreateDto(TEST_NAME, TEST_EMAIL, TEST_PROVIDER);
    }

    public static MemberChangeRoleDto appChangeRoleDto(Long memberId) {
        return new MemberChangeRoleDto(memberId, CHANGED_ROLE);
    }

    public static MemberRoleRequestDto webChangeRoleDto(Long memberId) {
        return new MemberRoleRequestDto(memberId, CHANGED_ROLE);
    }

    public static MemberResponseDto createMemberResponse() {
        return new MemberResponseDto(MemberFixture.createWithId());
    }

    public static MemberResponseDto createAdminWithId() {
        Member member = MemberFixture.createWithId();
        member.changeRole(Role.ADMIN);

        return new MemberResponseDto(member);
    }

    public static MemberResponsesDto appMemberpagedResponsesDto() {
        return new MemberResponsesDto(new CustomPageImpl<>(
                LongStream.range(1, 4)
                        .mapToObj(Members::appMemberAuthoritiesResponseDto)
                        .collect(Collectors.toList()),
                BaseFixture.DEFAULT_PAGEABLE, 4
        ));
    }

    private static MemberAuthorityResponseDto appMemberAuthoritiesResponseDto(Long id) {
        return MemberAuthorityResponseDto.builder()
                .authorityId(id)
                .memberId(id)
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .provider(AuthProvider.GOOGLE)
                .role(Role.NORMAL)
                .target(Role.ADMIN)
                .build();
    }
}
