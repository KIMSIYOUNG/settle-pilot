package in.woowa.pilot.admin.presentation.order.dto.request;

import in.woowa.pilot.admin.application.order.dto.request.OrderCreateDto;
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
public class OrderCreateRequestDto {

    @Valid
    private List<OrderDetailRequestDto> orderDetails;

    @NotNull(message = "업주번호는 필수사항입니다.")
    private Long ownerId;

    @NotNull(message = "주문상태는 필수사항입니다.")
    private OrderStatus orderStatus;

    @NotNull(message = "주문시각은 필수사항입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime orderDateTime;

    @Builder(builderClassName = "testBuilder", builderMethodName = "testBuilder")
    public OrderCreateRequestDto(
            List<OrderDetailRequestDto> orderDetails,
            Long ownerId,
            OrderStatus orderStatus,
            LocalDateTime orderDateTime
    ) {
        this.orderDetails = orderDetails;
        this.ownerId = ownerId;
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
    }

    public OrderCreateDto toServiceDto() {
        return OrderCreateDto.builder()
                .orderDateTime(orderDateTime)
                .orderDetails(createOrderDetails())
                .orderStatus(orderStatus)
                .ownerId(ownerId)
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
