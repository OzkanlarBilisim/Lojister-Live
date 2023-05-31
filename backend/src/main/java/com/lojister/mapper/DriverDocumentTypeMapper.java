package com.lojister.mapper;

import com.lojister.model.dto.dynamic.DriverDocumentTypeDto;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverDocumentTypeMapper extends BaseMapper<DriverDocumentTypeDto, DriverDocumentType>{
}
