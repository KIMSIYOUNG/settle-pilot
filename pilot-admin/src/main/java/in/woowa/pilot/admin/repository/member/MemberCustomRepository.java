package in.woowa.pilot.admin.repository.member;

import in.woowa.pilot.admin.application.member.dto.request.MemberSearchDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberAuthorityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {

    Page<MemberAuthorityResponseDto> fetchMemberWithAuthorities(MemberSearchDto condition, Pageable pageable);
}
