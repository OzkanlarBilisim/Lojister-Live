package com.lojister.mapper;

import com.lojister.model.dto.dynamic.PackagingTypeDto;
import com.lojister.model.entity.adminpanel.PackagingType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PackagingTypeMapper extends BaseMapper<PackagingTypeDto, PackagingType>{
}
