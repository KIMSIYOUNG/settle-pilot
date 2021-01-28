package in.woowa.pilot.admin.presentation.order.dto.request;

import in.woowa.pilot.admin.application.order.dto.request.OrderDetailDto;
import in.woowa.pilot.core.order.PaymentOption;
import in.woowa.pilot.core.order.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class OrderDetailRequestDto {

    @NotNull(message = "주문타입은 필수항목입니다.")
    private PaymentType paymentType;

    private PaymentOption paymentOption;

    @NotNull(message = "주문금액은 필수항목입니다.")
    private BigDecimal amount;

    public OrderDetailRequestDto(PaymentType paymentType, PaymentOption paymentOption, BigDecimal amount) {
        this.paymentType = paymentType;
        this.paymentOption = paymentOption;
        this.amount = amount;
    }

    public OrderDetailDto toServiceDto() {
        return OrderDetailDto.builder()
                .paymentType(paymentType)
                .paymentOption(paymentOption)
                .amount(amount)
                .build();
    }
}
