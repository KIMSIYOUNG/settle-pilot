package in.woowa.pilot.admin.application.settle.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.woowa.pilot.core.order.BusinessNo;
import in.woowa.pilot.core.settle.Settle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SettleResponseDto {
    private Long id;
    private BusinessNo businessNo;
    private BigDecimal amount;
    private Long ownerId;
    private String settleStatus;
    private String settleType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime transactionStartAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime transactionEndAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime settleScheduleDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime settleCompleteDate;

    public SettleResponseDto(Settle settle) {
        this.id = settle.getId();
        this.businessNo = settle.getBusinessNo();
        this.amount = settle.getAmount();
        this.ownerId = settle.getOwner().getId();
        this.settleType = settle.getSettleType().name();
        this.settleStatus = settle.getSettleStatus().name();
        this.transactionStartAt = settle.getTransactionStartAt();
        this.transactionEndAt = settle.getTransactionEndAt();
        this.settleScheduleDate = settle.getSettleScheduleDate();
        this.settleCompleteDate = settle.getSettleCompleteDate();
    }
}
