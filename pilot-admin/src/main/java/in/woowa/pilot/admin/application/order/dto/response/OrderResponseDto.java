package in.woowa.pilot.admin.application.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.woowa.pilot.admin.application.owner.dto.response.OwnerResponseDto;
import in.woowa.pilot.core.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String orderNo;
    private OrderDetailResponsesDto orderDetails;
    private OrderSnapshotResponsesDto orderSnapShots;
    private OwnerResponseDto owner;
    private BigDecimal amount;
    private String orderStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
    private LocalDateTime orderDateTime;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.orderNo = order.getBusinessNoValue();
        this.orderDetails = new OrderDetailResponsesDto(order.getOrderDetails());
        this.orderSnapShots = new OrderSnapshotResponsesDto(order.getOrderSnapShots());
        this.owner = new OwnerResponseDto(order.getOwner());
        this.amount = order.getAmount();
        this.orderStatus = order.getOrderStatus().name();
        this.orderDateTime = order.getOrderDateTime();
    }
}
