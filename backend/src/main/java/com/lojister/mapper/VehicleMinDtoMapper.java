package com.lojister.mapper;

import com.lojister.model.dto.VehicleDto;
import com.lojister.model.dto.VehicleMinDto;
import com.lojister.model.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMinDtoMapper extends BaseMapper<VehicleMinDto, Vehicle>{
}
