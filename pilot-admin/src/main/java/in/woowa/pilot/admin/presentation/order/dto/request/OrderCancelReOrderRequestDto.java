package in.woowa.pilot.admin.presentation.order.dto.request;

import in.woowa.pilot.admin.application.order.dto.request.OrderCancelReOrderDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderDetailDto;
import in.woowa.pilot.core.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderCancelReOrderRequestDto {

    @NotNull(message = "주문번호는 필수항목입니다.")
    private String orderNo;

    @Valid
    private List<OrderDetailRequestDto> orderDetails;

    @NotNull(message = "주문 상태는 필수사항입니다.")
    private OrderStatus orderStatus;

    @NotNull(message = "주문시각은 필수사항입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime orderDateTime;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public OrderCancelReOrderRequestDto(
            String orderNo,
            List<OrderDetailRequestDto> orderDetails,
            OrderStatus orderStatus,
            LocalDateTime orderDateTime
    ) {
        this.orderNo = orderNo;
        this.orderDetails = orderDetails;
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
    }

    public OrderCancelReOrderDto toServiceDto() {
        return OrderCancelReOrderDto.builder()
                .orderNo(orderNo)
                .orderDetails(createOrderDetails())
                .orderStatus(orderStatus)
                .orderDateTime(orderDateTime)
                .build();
    }

    public List<OrderDetailDto> createOrderDetails() {
        return orderDetails.stream()
                .map(od -> new OrderDetailDto(
                        od.getPaymentType(),
                        od.getPaymentOption(),
                        od.getAmount())
                )
                .collect(Collectors.toList());
    }
}
