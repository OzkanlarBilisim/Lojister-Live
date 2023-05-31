package com.lojister.mapper;

import com.lojister.model.dto.client.ClientNotificationSettingDto;
import com.lojister.model.entity.client.ClientNotificationSetting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientNotificationSettingDtoMapper extends BaseMapper<ClientNotificationSettingDto,ClientNotificationSetting>{

}
