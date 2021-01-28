package in.woowa.pilot.admin.repository.owner;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import in.woowa.pilot.admin.application.owner.dto.request.OwnerSearchDto;
import in.woowa.pilot.core.common.Status;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static in.woowa.pilot.core.account.QAccount.account;
import static in.woowa.pilot.core.owner.QOwner.owner;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class OwnerCustomRepositoryImpl implements OwnerCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Owner> fetchPagedByCondition(OwnerSearchDto condition, Pageable pageable) {
        List<Owner> content = queryFactory.select(owner)
                .from(owner)
                .where(
                        isActiveOwner(),
                        isSameId(condition),
                        containName(condition),
                        containEmail(condition),
                        isSameSettleType(condition.getSettleType())
                )
                .orderBy(owner.updatedAt.desc(), owner.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> countOrder(condition));
    }

    @Override
    public Map<Long, Owner> fetchIdToOwnerBySettleType(SettleType settleType) {
        return queryFactory.select(owner)
                .from(owner)
                .innerJoin(owner.account, account).fetchJoin()
                .where(isActiveOwner(), isSameSettleType(settleType))
                .transform(groupBy(owner.id).as(owner));
    }

    private Long countOrder(OwnerSearchDto condition) {
        return queryFactory.select(owner)
                .from(owner)
                .where(
                        isActiveOwner(),
                        isSameId(condition),
                        containName(condition),
                        containEmail(condition)
                ).fetchCount();
    }


    private BooleanExpression isSameId(OwnerSearchDto condition) {
        return condition.getOwnerId() == null ? null : owner.id.eq(condition.getOwnerId());
    }

    private BooleanExpression containName(OwnerSearchDto condition) {
        return StringUtils.hasText(condition.getName()) ? owner.name.contains(condition.getName()) : null;
    }

    private BooleanExpression containEmail(OwnerSearchDto condition) {
        return StringUtils.hasText(condition.getEmail()) ? owner.email.contains(condition.getEmail()) : null;
    }

    private BooleanExpression isActiveOwner() {
        return owner.status.eq(Status.ACTIVE);
    }

    private BooleanExpression isSameSettleType(SettleType settleType) {
        return settleType == null ? null : owner.settleType.eq(settleType);
    }
}
