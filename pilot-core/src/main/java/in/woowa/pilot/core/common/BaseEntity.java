package in.woowa.pilot.core.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    protected Status status = Status.ACTIVE;

    protected LocalDateTime deletedAt;

    public void delete() {
        this.status = Status.DELETED;
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return status == Status.DELETED;
    }

    protected void changeStatus(Status status) {
        this.status = status;
    }
}
