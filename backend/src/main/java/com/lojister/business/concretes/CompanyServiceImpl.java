package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.request.CommissionRateDto;
import com.lojister.model.dto.CompanyDashboardRatingCountDto;
import com.lojister.model.dto.CompanyDto;
import com.lojister.model.enums.Role;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.CompanyMapper;
import com.lojister.model.entity.BankInformation;
import com.lojister.model.entity.Company;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.driver.Driver;
import com.lojister.repository.company.CompanyRepository;
import com.lojister.business.abstracts.BankInformationService;
import com.lojister.business.abstracts.CompanyService;
import com.lojister.core.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final BankInformationService bankInformationService;
    private final SecurityContextUtil securityContextUtil;


    @Override
    public CompanyDto update(CompanyDto companyDto) {

        Company company = getDataMyCompany();
        if (Optional.ofNullable(companyDto.getBankInformation()).isPresent()) {
            BankInformation bankInformation = company.getBankInformation();

            bankInformation.setAccountNumber(companyDto.getBankInformation().getAccountNumber());
            bankInformation.setBankName(companyDto.getBankInformation().getBankName());
            bankInformation.setBranch(companyDto.getBankInformation().getBranch());
            bankInformation.setIban(companyDto.getBankInformation().getIban());
            bankInformation = bankInformationService.saveRepo(bankInformation);

            company.setBankInformation(bankInformation);
        }


        company.setCommercialTitle(companyDto.getCommercialTitle());
        company.setTaxAdministration(companyDto.getTaxAdministration());
        company.setTaxNumber(companyDto.getTaxNumber());
        company.setMail(companyDto.getMail());
        company.setPhone(companyDto.getPhone());
        company.setFinancialStaffFirstname(companyDto.getFinancialStaffFirstname());
        company.setFinancialStaffLastname(companyDto.getFinancialStaffLastname());
        company.setFinancialStaffPhone(companyDto.getFinancialStaffPhone());
        company.setAddress(companyDto.getAddress());

        return companyMapper.entityToDto(companyRepository.save(company));

    }


    @Override
    public Company getDataMyCompany() {

        Role role = securityContextUtil.getCurrentUserRole();

        if (role == Role.ROLE_DRIVER) {

            Driver driver = securityContextUtil.getCurrentBossDriver();

            return driver.getCompany();

        } else if (role == Role.ROLE_DRIVER_EMPLOYEE) {

            Driver driver = securityContextUtil.getCurrentDriver();

            return driver.getCompany();
        } else if (role == Role.ROLE_CLIENT) {

            Client client = securityContextUtil.getCurrentClient();

            return client.getCompany();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.company.EntityNotFoundException"));
        }

    }


    @Override
    public CompanyDto getMyCompany() {

        Company company = getDataMyCompany();

        return companyMapper.entityToDto(company);

    }

    @Override
    @OnlyAdmin
    public CompanyDto updateCommissionRate(CommissionRateDto commissionRateDto) {

        Company company = findDataById(commissionRateDto.getCompanyId());

        company.setCommissionRate(commissionRateDto.getCommissionRate());

        return companyMapper.entityToDto(companyRepository.save(company));

    }

    @Override
    public List<CompanyDto> findByCommercialTitleContainsOrderByIdDesc(String commercialTitle) {

        return companyMapper.entityListToDtoList(companyRepository.findByCommercialTitleContainsOrderByIdDesc(commercialTitle));

    }

    @Override
    public CompanyDto getById(Long id) {

        return companyMapper.entityToDto(findDataById(id));
    }


    @Override
    public List<CompanyDto> getAll() {

        if (securityContextUtil.getCurrentUserRole() == Role.ROLE_ADMIN) {
            return companyMapper.entityListToDtoList(companyRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        } else {
            return Collections.singletonList(getMyCompany());
        }
    }


    @Override
    public CompanyDashboardRatingCountDto getDashboardRatingCountInformation() {

        Driver currentDriver = securityContextUtil.getCurrentBossDriver();
        Company company = currentDriver.getCompany();

        CompanyDashboardRatingCountDto companyDashboardRatingCountDto = new CompanyDashboardRatingCountDto();
        companyDashboardRatingCountDto.setTotalRatingCount(company.getNumberOfRating());
        companyDashboardRatingCountDto.setAverageRating(company.getRating());

        return companyDashboardRatingCountDto;
    }

    @Override
    public Company findDataById(Long id) {

        Optional<Company> company = companyRepository.findById(id);

        if (company.isPresent()) {
            return company.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.company.EntityNotFoundException"));
        }
    }


    @Override
    public Company saveRepo(Company company) {
        return companyRepository.save(company);
    }


}
