package in.woowa.pilot.admin.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.admin.application.member.dto.request.MemberSearchDto;
import in.woowa.pilot.admin.application.member.dto.response.MemberAuthorityResponseDto;
import in.woowa.pilot.core.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static in.woowa.pilot.core.authority.QAuthority.authority;
import static in.woowa.pilot.core.member.QMember.member;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    public Page<MemberAuthorityResponseDto> fetchMemberWithAuthorities(MemberSearchDto condition, Pageable pageable) {
        List<MemberAuthorityResponseDto> content = queryFactory.select(Projections.fields(
                MemberAuthorityResponseDto.class,
                authority.id.as("authorityId"),
                authority.target.as("target"),
                member.id.as("memberId"),
                member.name.as("name"),
                member.email.as("email"),
                member.provider.as("provider"),
                member.role.as("role")
        ))
                .from(authority)
                .rightJoin(authority.member, member)
                .where(
                        isActiveMember(),
                        isSameMemberId(condition.getId()),
                        containName(condition.getName()),
                        containEmail(condition.getEmail())
                )
                .orderBy(member.updatedAt.desc(), member.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> memberCountQuery(condition));
    }

    private long memberCountQuery(MemberSearchDto condition) {
        return queryFactory.select(member)
                .from(member)
                .where(
                        isActiveMember(),
                        isSameMemberId(condition.getId()),
                        containName(condition.getName()),
                        containEmail(condition.getEmail())
                )
                .fetchCount();
    }

    private BooleanExpression containEmail(String email) {
        return StringUtils.hasText(email) ? member.email.contains(email) : null;
    }

    private BooleanExpression containName(String name) {
        return StringUtils.hasText(name) ? member.name.contains(name) : null;
    }

    private BooleanExpression isSameMemberId(Long id) {
        return id == null ? null : member.id.eq(id);
    }

    private BooleanExpression isActiveMember() {
        return member.status.eq(Status.ACTIVE);
    }
}
