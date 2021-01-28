package in.woowa.pilot.admin.application.order.dto.response;

import in.woowa.pilot.admin.common.CustomPageImpl;
import in.woowa.pilot.core.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderResponsesDto {
    private CustomPageImpl<OrderResponseDto> orders;

    public OrderResponsesDto(Page<Order> orders) {
        List<OrderResponseDto> content = orders.getContent().stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());

        this.orders = new CustomPageImpl<>(
                content,
                orders.getPageable(),
                orders.getTotalElements()
        );
    }
}
