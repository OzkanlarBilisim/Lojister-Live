package com.lojister.mapper;

import com.lojister.model.dto.dynamic.TrailerFeatureDto;
import com.lojister.model.entity.adminpanel.TrailerFeature;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrailerFeatureMapper extends BaseMapper<TrailerFeatureDto, TrailerFeature>{
}
