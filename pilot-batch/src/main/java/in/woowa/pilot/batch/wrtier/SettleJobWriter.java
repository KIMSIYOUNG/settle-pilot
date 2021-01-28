package in.woowa.pilot.batch.wrtier;

import in.woowa.pilot.batch.dto.SettleJobDto;
import in.woowa.pilot.batch.parameter.SettleParameter;
import in.woowa.pilot.batch.repository.OrderBatchRepository;
import in.woowa.pilot.batch.repository.RewardBatchRepository;
import in.woowa.pilot.core.settle.Settle;
import in.woowa.pilot.core.settle.SettleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SettleJobWriter implements ItemWriter<SettleJobDto> {
    private final SettleParameter jobParameter;
    private final SettleRepository settleRepository;
    private final OrderBatchRepository orderBatchRepository;
    private final RewardBatchRepository rewardBatchRepository;

    @Override
    public void write(List<? extends SettleJobDto> items) throws Exception {
        for (SettleJobDto item : items) {
            Settle savedSettle = settleRepository.save(item.toSettle(jobParameter.getSettleType(), jobParameter.getCriteriaDate()));
            rewardBatchRepository.updateBatchSettle(item.getRewards(), savedSettle);
            orderBatchRepository.updateBatchSettle(item.getOrders(), savedSettle);
        }
    }
}
