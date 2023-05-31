package com.lojister.model.entity.base;

import lombok.*;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AbstractTimestampEntity extends BaseEntity {

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

    private Boolean deleted;

}
