package in.woowa.pilot.admin.application.settle.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.woowa.pilot.core.settle.SettleSnapshot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class SettleAmountResponseDto {
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime endAt;

    public SettleAmountResponseDto(List<SettleSnapshot> snapshots, LocalDateTime startAt, LocalDateTime endAt) {
        this.amount = snapshots.stream()
                .map(SettleSnapshot::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.startAt = startAt;
        this.endAt = endAt;
    }
}
