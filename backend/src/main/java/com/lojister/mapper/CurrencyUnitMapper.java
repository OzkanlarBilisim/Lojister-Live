package com.lojister.mapper;

import com.lojister.model.dto.dynamic.CurrencyUnitDto;
import com.lojister.model.entity.adminpanel.CurrencyUnit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyUnitMapper extends BaseMapper<CurrencyUnitDto, CurrencyUnit>{

}
