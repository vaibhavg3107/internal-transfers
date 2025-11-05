package com.example.internaltransfers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    @JsonIgnore
    private Long id;

    @Version
    private Long version;

    @CreationTimestamp
    @JsonIgnore
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    private OffsetDateTime updatedAt;
}
