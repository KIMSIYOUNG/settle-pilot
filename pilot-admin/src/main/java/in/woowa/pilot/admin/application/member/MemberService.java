package in.woowa.pilot.admin.application.member;

import in.woowa.pilot.admin.application.member.dto.request.MemberChangeRoleDto;
import in.woowa.pilot.admin.application.member.dto.request.MemberCreateDto;
import in.woowa.pilot.admin.application.member.dto.request.MemberSearchDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberAuthorityResponseDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponsesDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.repository.member.MemberCustomRepository;
import in.woowa.pilot.core.authority.Authority;
import in.woowa.pilot.core.authority.AuthorityRepository;
import in.woowa.pilot.core.common.BaseEntity;
import in.woowa.pilot.core.member.AuthProvider;
import in.woowa.pilot.core.member.Member;
import in.woowa.pilot.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberCustomRepository memberCustomRepository;
    private final AuthorityRepository authorityRepository;

    @Transactional
    public MemberResponseDto create(MemberCreateDto createDto) {
        Member member = createDto.toMember();

        return new MemberResponseDto(saveMember(member));
    }

    public MemberResponseDto findById(Long id) {
        return new MemberResponseDto(findMember(id));
    }

    public MemberResponsesDto fetchMembersWithAuthorities(MemberSearchDto condition, Pageable pageable) {
        Page<MemberAuthorityResponseDto> response = memberCustomRepository.fetchMemberWithAuthorities(condition, pageable);

        return new MemberResponsesDto(response);
    }

    @Transactional
    public MemberResponseDto findOrElseCreate(String email, AuthProvider provider, MemberCreateDto createDto) {
        Member member = memberRepository.findByEmailAndProvider(email, provider)
                .orElseGet(() -> saveMember(createDto.toMember()));

        if (member.isDeleted()) {
            member.activate();
        }

        return new MemberResponseDto(member);
    }

    @Transactional
    public void changeMemberRole(MemberChangeRoleDto changeRoleDto) {
        Member member = findMember(changeRoleDto.getMemberId());
        member.changeRole(changeRoleDto.getRole());
    }

    @Transactional
    public void deleteById(Long id) {
        Member findMember = findMember(id);
        findMember.delete();

        Optional<Authority> authority = authorityRepository.findByMember(findMember);
        authority.ifPresent(BaseEntity::delete);
    }

    private Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
    }
}
