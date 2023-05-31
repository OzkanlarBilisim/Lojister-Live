package com.lojister.mapper;

import com.lojister.model.dto.client.ClientAccountSettingDto;
import com.lojister.model.entity.client.ClientAccountSetting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientAccountSettingDtoMapper extends BaseMapper<ClientAccountSettingDto, ClientAccountSetting>{
}
