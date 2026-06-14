package sk.krizan.fitness_app_be.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    @NotNull
    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @NotNull
    @CreatedBy
    @Column(nullable = false)
    private String createdBy;

    @NotNull
    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModifiedAt;

    @NotNull
    @LastModifiedBy
    @Column(nullable = false)
    private String lastModifiedBy;

    @Version
    @NotNull
    @Column(nullable = false)
    private Long version = 0L;

}
