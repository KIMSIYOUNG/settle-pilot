package in.woowa.pilot.admin.repository.settle;

import in.woowa.pilot.admin.application.settle.dto.request.SettleCompleteConditionDto;
import in.woowa.pilot.admin.application.settle.dto.request.SettleSearchDto;
import in.woowa.pilot.core.settle.Settle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SettleCustomRepository {

    Page<Settle> fetchPagedByCondition(SettleSearchDto webSettleCondition, Pageable pageable);

    Page<Settle> fetchAll(Pageable pageable);

    List<Settle> fetchByCondition(SettleSearchDto condition);

    long updateBulkComplete(SettleCompleteConditionDto condition);
}
