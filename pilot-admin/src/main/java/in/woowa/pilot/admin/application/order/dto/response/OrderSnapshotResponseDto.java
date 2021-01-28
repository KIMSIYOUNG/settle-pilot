package in.woowa.pilot.admin.application.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderSnapshotResponseDto {
    private Long orderId;
    private String orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime statusAt;

    public OrderSnapshotResponseDto(Long orderId, String orderStatus, LocalDateTime statusAt) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.statusAt = statusAt;
    }
}
