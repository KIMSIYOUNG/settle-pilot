package in.woowa.pilot.admin.application.authority;

import in.woowa.pilot.admin.application.authority.dto.AuthorityDto;
import in.woowa.pilot.admin.application.authority.dto.AuthorityResponseDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.authority.AuthorityStatus;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public AuthorityResponseDto create(AuthorityDto createDto) {
        Member member = memberRepository.findById(createDto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", createDto.getMemberId()));

        return new AuthorityResponseDto(authorityRepository.save(createDto.toAuthority(member)));
    }

    @Transactional
    public void approve(Long id) {
        Authority authority = getAuthority(id);
        Member member = authority.getMember();
        member.changeRole(authority.getTarget());
        authority.changeStatus(AuthorityStatus.APPROVE);
    }

    @Transactional
    public void reject(Long id) {
        Authority authority = getAuthority(id);
        authority.changeStatus(AuthorityStatus.REJECT);
    }

    private Authority getAuthority(Long id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("authority", "id", id));
    }
}
