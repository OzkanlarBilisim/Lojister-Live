package com.lojister.mapper;

import com.lojister.model.dto.CompanyDto;
import com.lojister.model.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = BankInformationMapper.class)
public interface CompanyMapper extends BaseMapper<CompanyDto, Company>{

}
