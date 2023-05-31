package com.lojister.model.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractTimestampNoId {

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private LocalDateTime deletedDateTime;

    @PrePersist
    protected void onCreate() {
        updatedDateTime = createdDateTime =  LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDateTime = LocalDateTime.now();
    }

    @PreRemove
    protected void onDelete(){
        deletedDateTime = LocalDateTime.now();
    }

}

