package in.woowa.pilot.admin.application.order.dto.request;

import in.woowa.pilot.core.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderSearchDto {
    private final Long ownerId;
    private final String orderNo;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final OrderStatus orderStatus;

    @Builder
    public OrderSearchDto(
            Long ownerId,
            String orderNo,
            LocalDateTime startAt,
            LocalDateTime endAt,
            OrderStatus orderStatus
    ) {
        if ((startAt != null && endAt != null) && startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전일 수 없습니다.");
        }

        this.ownerId = ownerId;
        this.orderNo = orderNo;
        this.startAt = startAt;
        this.endAt = endAt;
        this.orderStatus = orderStatus;
    }
}
