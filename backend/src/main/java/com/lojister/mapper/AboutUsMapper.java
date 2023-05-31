package com.lojister.mapper;

import com.lojister.model.dto.dynamic.AboutUsDto;
import com.lojister.model.entity.adminpanel.AboutUs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AboutUsMapper extends BaseMapper<AboutUsDto, AboutUs> {
}
