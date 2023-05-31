package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.entity.Notification;
import lombok.Data;

@Data
public class NotificationDto extends BaseDto {

    private String message;

    private Long advertisementBidId;

    private Notification.NotificationType notificationType;

    private Notification.AdvertisementType type;


}
