package in.woowa.pilot.admin.application.order;

import in.woowa.pilot.admin.application.order.dto.request.OrderCancelReOrderDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderCreateDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderSearchDto;
import in.woowa.pilot.admin.application.order.dto.request.OrderStatusUpdateDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponseDto;
import in.woowa.pilot.admin.application.order.dto.response.OrderResponsesDto;
import in.woowa.pilot.admin.common.exception.ResourceNotFoundException;
import in.woowa.pilot.admin.repository.order.OrderCustomRepository;
import in.woowa.pilot.core.order.Order;
import in.woowa.pilot.core.order.OrderRepository;
import in.woowa.pilot.core.owner.Owner;
import in.woowa.pilot.core.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderCustomRepository orderCustomRepository;
    private final OrderRepository orderRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    public OrderResponseDto create(OrderCreateDto request) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("owner", "id", request.getOwnerId()));

        Order order = request.toOrder(owner);

        return new OrderResponseDto(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDto cancelAndReOrder(OrderCancelReOrderDto request) {
        Order existOrder = orderCustomRepository.findByOrderNo(request.getOrderNo())
                .orElseThrow(() -> new ResourceNotFoundException("order", "orderNo", request.getOrderNo()));

        existOrder.cancel();

        Order reOrder = request.toReOrder(existOrder.getOwner(), existOrder.getBusinessNo());
        return new OrderResponseDto(orderRepository.save(reOrder));
    }

    public OrderResponseDto fetchById(Long orderId) {
        Order order = orderCustomRepository.fetchById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order", "id", orderId));

        return new OrderResponseDto(order);
    }

    public OrderResponsesDto fetchPagedByCondition(OrderSearchDto condition, Pageable pageable) {
        Page<Order> orders = orderCustomRepository.fetchPagedByCondition(condition, pageable);

        return new OrderResponsesDto(orders);
    }

    public OrderResponsesDto fetchBySettleId(Long settleId, Pageable pageable) {
        Page<Order> orders = orderCustomRepository.findAllBySettleId(settleId, pageable);

        return new OrderResponsesDto(orders);
    }

    @Transactional
    public void updateOrderStatus(OrderStatusUpdateDto request) {
        Order order = orderRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("order", "id", request.getId()));

        order.updateStatus(request.getStatus());
    }

    @Transactional
    public void delete(Long orderId) {
        Order order = orderCustomRepository.fetchAggregateById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order", "id", orderId));

        order.delete();
    }
}
