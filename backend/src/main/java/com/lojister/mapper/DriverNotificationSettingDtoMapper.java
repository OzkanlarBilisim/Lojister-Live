package com.lojister.mapper;

import com.lojister.model.dto.driver.DriverNotificationSettingDto;
import com.lojister.model.entity.driver.DriverNotificationSetting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverNotificationSettingDtoMapper extends  BaseMapper<DriverNotificationSettingDto, DriverNotificationSetting>{
}
