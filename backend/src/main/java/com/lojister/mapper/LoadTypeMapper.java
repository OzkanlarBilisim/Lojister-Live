package com.lojister.mapper;

import com.lojister.model.dto.dynamic.LoadTypeDto;
import com.lojister.model.entity.adminpanel.LoadType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoadTypeMapper extends BaseMapper<LoadTypeDto, LoadType>{
}
