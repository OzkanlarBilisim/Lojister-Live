package com.lojister.mapper;

import com.lojister.model.dto.VehicleDocumentFileDto;
import com.lojister.model.entity.VehicleDocumentFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VehicleDocumentTypeMapper.class, VehicleMapper.class})
public interface VehicleDocumentFileMapper extends BaseMapper<VehicleDocumentFileDto,VehicleDocumentFile>{
}
