package com.lojister.mapper;

import com.lojister.model.dto.VehicleDto;
import com.lojister.model.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CompanyMapper.class,DriverMapper.class,VehicleTypeMapper.class,TrailerTypeMapper.class,TrailerFloorTypeMapper.class,TrailerFeatureMapper.class})
public interface VehicleMapper extends BaseMapper<VehicleDto, Vehicle>{
}
