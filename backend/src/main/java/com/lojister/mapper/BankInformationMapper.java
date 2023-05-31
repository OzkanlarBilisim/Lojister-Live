package com.lojister.mapper;

import com.lojister.model.dto.BankInformationDto;
import com.lojister.model.entity.BankInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankInformationMapper extends BaseMapper<BankInformationDto, BankInformation>{
}
