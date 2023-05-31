package com.lojister.business.abstracts;

import com.lojister.model.dto.request.CommissionRateDto;
import com.lojister.model.dto.CompanyDashboardRatingCountDto;
import com.lojister.model.dto.CompanyDto;
import com.lojister.model.entity.Company;

import java.util.List;

public interface CompanyService {

    CompanyDto update(CompanyDto dto);

    CompanyDto getById(Long id);

    List<CompanyDto> getAll();

    CompanyDto getMyCompany();

    CompanyDto updateCommissionRate(CommissionRateDto commissionRateDto);

    List<CompanyDto> findByCommercialTitleContainsOrderByIdDesc(String commercialTitle);

    Company findDataById(Long id);

    Company getDataMyCompany();

    Company saveRepo(Company company);

    CompanyDashboardRatingCountDto getDashboardRatingCountInformation();

}
