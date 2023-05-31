package com.lojister.model.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class CreatedDateAndIdBaseEntity extends BaseEntity {

    private LocalDateTime createdDateTime;

    @PrePersist
    protected void onCreate() {
         createdDateTime =  LocalDateTime.now();
    }

}
