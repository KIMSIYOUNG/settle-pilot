package in.woowa.pilot.admin.application.order.dto.response;

import in.woowa.pilot.core.order.OrderDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderDetailResponsesDto {
    private List<OrderDetailResponseDto> orderDetails;

    public OrderDetailResponsesDto(OrderDetails orderDetails) {
        this.orderDetails = orderDetails.getOrderDetails().stream()
                .map(OrderDetailResponseDto::new)
                .collect(Collectors.toList());
    }
}
