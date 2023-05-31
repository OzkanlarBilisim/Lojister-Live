package com.lojister.mapper;

import com.lojister.model.dto.dynamic.VehicleTypeDto;
import com.lojister.model.entity.adminpanel.VehicleType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleTypeMapper extends BaseMapper<VehicleTypeDto, VehicleType>{

}
