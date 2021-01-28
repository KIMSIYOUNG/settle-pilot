package in.woowa.pilot.admin.presentation.order.dto.request;

import in.woowa.pilot.core.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class OrderStatusUpdateRequestDto {

    @NotNull(message = "수정하고자 하는 주문의 ID는 필수사항입니다.")
    private Long id;

    @NotNull(message = "변경할 주문상태는 필수항목입니다.")
    private OrderStatus status;

    public OrderStatusUpdateRequestDto(Long id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }

    public in.woowa.pilot.admin.application.order.dto.request.OrderStatusUpdateDto toServiceDto() {
        return in.woowa.pilot.admin.application.order.dto.request.OrderStatusUpdateDto.builder()
                .id(id)
                .status(status)
                .build();
    }
}
