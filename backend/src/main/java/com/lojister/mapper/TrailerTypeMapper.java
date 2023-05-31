package com.lojister.mapper;

import com.lojister.model.dto.dynamic.TrailerTypeDto;
import com.lojister.model.entity.adminpanel.TrailerType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrailerTypeMapper extends BaseMapper<TrailerTypeDto, TrailerType>{
}
