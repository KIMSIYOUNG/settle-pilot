package in.woowa.pilot.admin.presentation.order.dto.request;

import in.woowa.pilot.admin.application.order.dto.request.OrderSearchDto;
import in.woowa.pilot.core.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class OrderSearchRequestDto {

    private Long ownerId;

    private String orderNo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endAt;

    private OrderStatus orderStatus;

    public OrderSearchDto toAppOrderCondition() {
        return OrderSearchDto.builder()
                .ownerId(ownerId)
                .orderStatus(orderStatus)
                .startAt(startAt)
                .endAt(endAt)
                .orderNo(orderNo)
                .build();
    }
}
