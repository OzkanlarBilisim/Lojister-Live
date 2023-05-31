package com.lojister.mapper;

import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.entity.driver.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CompanyMapper.class,DriverBossMapper.class})
public interface DriverMapper extends BaseMapper<DriverDto,Driver> {


}
