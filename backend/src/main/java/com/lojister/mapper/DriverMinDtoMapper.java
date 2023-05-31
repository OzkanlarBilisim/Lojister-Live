package com.lojister.mapper;

import com.lojister.model.dto.driver.DriverMinDto;
import com.lojister.model.entity.driver.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMinDtoMapper extends BaseMapper<DriverMinDto, Driver>{
}
