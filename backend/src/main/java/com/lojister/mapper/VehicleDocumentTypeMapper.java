package com.lojister.mapper;

import com.lojister.model.dto.dynamic.VehicleDocumentTypeDto;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleDocumentTypeMapper extends BaseMapper<VehicleDocumentTypeDto, VehicleDocumentType>{
}
