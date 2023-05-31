package com.lojister.mapper;

import com.lojister.model.dto.driver.DriverBossDto;
import com.lojister.model.entity.driver.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CompanyMapper.class } )
public interface DriverBossMapper extends BaseMapper<DriverBossDto, Driver>{
}
