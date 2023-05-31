package com.lojister.mapper;

import com.lojister.model.dto.dynamic.CargoTypeDto;
import com.lojister.model.entity.adminpanel.CargoType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CargoTypeMapper extends BaseMapper<CargoTypeDto, CargoType> {
}
