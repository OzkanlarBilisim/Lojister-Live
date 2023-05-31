package com.lojister.mapper;

import com.lojister.model.dto.NotificationDto;
import com.lojister.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends BaseMapper<NotificationDto,Notification> {

}
