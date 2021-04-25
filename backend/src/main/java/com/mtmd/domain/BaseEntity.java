package com.mtmd.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Usually having this infrastructure-code within the domain is considered a code-smell, but
 * the benefits of the ORM outweigh the costs.
 */
@MappedSuperclass
public class BaseEntity {

    @Version
    private long version;
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp updated;
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp created;

    public LocalDateTime getUpdated() {
        return updated.toLocalDateTime();
    }

    public LocalDateTime getCreated() {
        return created.toLocalDateTime();
    }

    @PrePersist
    @PreUpdate
    void onCreateOrUpdate() {
        if (created == null) {
            this.created = Timestamp.valueOf(LocalDateTime.now());
        }
        this.updated = Timestamp.valueOf(LocalDateTime.now());
    }
}
