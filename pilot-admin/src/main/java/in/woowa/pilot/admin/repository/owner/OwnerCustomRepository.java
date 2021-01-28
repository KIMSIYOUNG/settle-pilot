package in.woowa.pilot.admin.repository.owner;

import in.woowa.pilot.admin.application.owner.dto.request.OwnerSearchDto;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.settle.SettleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface OwnerCustomRepository {

    Page<Owner> fetchPagedByCondition(OwnerSearchDto webOwnerCondition, Pageable pageable);

    Map<Long, Owner> fetchIdToOwnerBySettleType(SettleType settleType);
}
