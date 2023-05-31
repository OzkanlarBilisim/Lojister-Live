package com.lojister.mapper;

import com.lojister.model.dto.DriverDocumentFileDto;
import com.lojister.model.entity.driver.DriverDocumentFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {DriverMapper.class,DriverDocumentTypeMapper.class})
public interface DriverDocumentFileMapper extends BaseMapper<DriverDocumentFileDto, DriverDocumentFile>{

}
