package in.woowa.pilot.admin.presentation.order;

import in.woowa.pilot.admin.application.order.OrderService;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponseDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponsesDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCancelReOrderRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderCreateRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderSearchRequestDto;
import in.woowa.pilot.admin.presentation.order.dto.request.OrderStatusUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Validated
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<Void> create(@RequestBody @Valid OrderCreateRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.create(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/orders/%s", responseDto.getId()))).build();
    }

    @PostMapping("/api/orders/re-order")
    public ResponseEntity<Void> reOrder(@RequestBody @Valid OrderCancelReOrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.cancelAndReOrder(requestDto.toServiceDto());

        return ResponseEntity.created(URI.create(String.format("/api/orders/%s", responseDto.getId()))).build();
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderResponseDto> fetchById(@PathVariable @NotNull Long id) {
        OrderResponseDto response = orderService.fetchById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrderResponsesDto> fetchByCondition(
            OrderSearchRequestDto orderSearchRequestDto,
            @PageableDefault Pageable pageable
    ) {

        OrderResponsesDto response = orderService.fetchPagedByCondition(orderSearchRequestDto.toAppOrderCondition(), pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/orders/settles/{settleId}")
    public ResponseEntity<OrderResponsesDto> fetchBySettleId(@PathVariable @NotNull Long settleId, Pageable pageable) {
        OrderResponsesDto response = orderService.fetchBySettleId(settleId, pageable);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/orders")
    public ResponseEntity<Void> updateOrderStatus(@RequestBody @Valid OrderStatusUpdateRequestDto requestDto) {
        orderService.updateOrderStatus(requestDto.toServiceDto());

        return ResponseEntity.noContent().location(URI.create(String.format("/api/orders/%s", requestDto.getId()))).build();
    }

    @DeleteMapping("/api/orders/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        orderService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
