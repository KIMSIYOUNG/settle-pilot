package in.woowa.pilot.admin.presentation.member;


import in.woowa.pilot.admin.application.member.MemberService;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponseDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberResponsesDto;
import in.woowa.pilot.admin.presentation.member.dto.request.MemberRoleRequestDto;
import in.woowa.pilot.admin.presentation.member.dto.request.MemberSearchRequestDto;
import in.woowa.pilot.admin.security.CurrentUser;
import in.woowa.pilot.admin.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/api/members")
    public ResponseEntity<MemberResponseDto> getCurrentMember(@CurrentUser UserPrincipal userPrincipal) {
        MemberResponseDto response = memberService.findById(userPrincipal.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/members/all")
    public ResponseEntity<MemberResponsesDto> fetchAll(@PageableDefault Pageable pageable, MemberSearchRequestDto condition) {
        MemberResponsesDto response = memberService.fetchMembersWithAuthorities(condition.toServiceCondition(), pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/members/role")
    public ResponseEntity<Void> changeMemberRole(@RequestBody @Valid MemberRoleRequestDto request) {
        memberService.changeMemberRole(request.toServiceDto());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/members/{target}")
    public ResponseEntity<Void> deleteByAdminMember(@PathVariable @NotNull Long target) {
        memberService.deleteById(target);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/members")
    public ResponseEntity<Void> deleteCurrentMember(@CurrentUser UserPrincipal userPrincipal) {
        memberService.deleteById(userPrincipal.getId());

        return ResponseEntity.noContent().build();
    }
}
