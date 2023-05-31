package com.lojister.mapper;

import com.lojister.model.dto.dynamic.TrailerFloorTypeDto;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrailerFloorTypeMapper extends BaseMapper<TrailerFloorTypeDto, TrailerFloorType>{
}
