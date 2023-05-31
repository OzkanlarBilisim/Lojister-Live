package com.lojister.core.validator;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.CompanyValidatorDto;
import com.lojister.core.exception.ValidatorException;
import com.lojister.core.exception.validator.CompanyAlreadyExists;
import com.lojister.model.entity.Company;
import com.lojister.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DuplicateCompanyValidator implements CompanyValidator<CompanyValidatorDto> {

    private final CompanyRepository companyRepository;


    @Override
    public void validate(CompanyValidatorDto companyValidatorDto) throws ValidatorException {

        Optional<Company> company = companyRepository.findByTaxAdministrationAndTaxNumber(companyValidatorDto.getTaxAdministration(), companyValidatorDto.getTaxNumber());

        if (company.isPresent()) {
            throw new CompanyAlreadyExists(Translator.toLocale("lojister.account.CompanyAlreadyExists"));
        }

    }
}
