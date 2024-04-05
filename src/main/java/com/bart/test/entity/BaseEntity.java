package com.bart.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class BaseEntity {

    @CreationTimestamp
    @Column
    private Date createdAt;

    @UpdateTimestamp
    @Column
    private Date updatedAt;

    @CreatedBy
    @Column
    private String createdBy;

    @LastModifiedBy
    @Column
    private String updatedBy;

    @Column(columnDefinition = "tinyint(1) not null default '0'")
    private boolean deleted;
}