package com.lojister.business.concretes;

import com.lojister.model.dto.SaveInsuranceCompanyDto;
import com.lojister.model.entity.InsuranceCompany;
import com.lojister.repository.company.InsuranceCompanyRepository;
import com.lojister.business.abstracts.InsuranceCompanyService;
import com.lojister.core.validator.DuplicateEmailValidator;
import com.lojister.core.validator.DuplicatePhoneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService {

    private final InsuranceCompanyRepository insuranceCompanyRepository;
    private final DuplicatePhoneValidator duplicatePhoneValidator;
    private final DuplicateEmailValidator duplicateEmailValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void save(SaveInsuranceCompanyDto saveInsuranceCompanyDto) {

        duplicatePhoneValidator.validate(saveInsuranceCompanyDto.getPhone());
        duplicateEmailValidator.validate(saveInsuranceCompanyDto.getEmail());
        saveInsuranceCompanyDto.setPassword(bCryptPasswordEncoder.encode(saveInsuranceCompanyDto.getPassword()));

        insuranceCompanyRepository.save(InsuranceCompany.create(saveInsuranceCompanyDto));
    }
}
