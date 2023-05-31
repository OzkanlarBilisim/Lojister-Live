package com.lojister.model.entity;

import com.lojister.model.entity.base.CreatedDateAndIdBaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
public class Notification extends CreatedDateAndIdBaseEntity {

    public enum NotificationType{
        NEW,
        SEEN,
        READ
    }

     public enum AdvertisementType{
        DRIVER,
        CLIENT
    }

    private String message;

    //Teklif Id
    private Long advertisementBidId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private AdvertisementType type;

}
