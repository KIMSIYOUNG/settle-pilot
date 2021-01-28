package in.woowa.pilot.admin.application.order.dto.request;

import in.woowa.pilot.core.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderStatusUpdateDto {
    private final Long id;
    private final OrderStatus status;

    @Builder
    public OrderStatusUpdateDto(Long id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }
}
