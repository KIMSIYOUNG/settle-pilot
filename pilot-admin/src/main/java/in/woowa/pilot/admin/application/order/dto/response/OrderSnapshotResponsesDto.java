package in.woowa.pilot.admin.application.order.dto.response;

import in.woowa.pilot.core.order.OrderSnapShot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderSnapshotResponsesDto {
    private List<OrderSnapshotResponseDto> snapshots;

    public OrderSnapshotResponsesDto(List<OrderSnapShot> snapshots) {
        this.snapshots = snapshots.stream()
                .map((snapShot) -> new OrderSnapshotResponseDto(
                        snapShot.getOrder().getId(),
                        snapShot.getOrderStatus().name(),
                        snapShot.getStatusAt())
                )
                .collect(Collectors.toList());
    }
}
