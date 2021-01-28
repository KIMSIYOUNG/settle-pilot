package in.woowa.pilot.core.settle;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SettleSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private SettleSnapshotType type;

    @Builder
    public SettleSnapshot(
            List<Settle> settles,
            LocalDateTime startAt,
            LocalDateTime endAt,
            SettleSnapshotType type
    ) {
        this.amount = settles.stream()
                .map(Settle::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.startAt = startAt;
        this.endAt = endAt;
        this.type = type;
    }
}
