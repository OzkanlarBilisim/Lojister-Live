package com.lojister.model.entity;

import com.lojister.model.dto.SaveInsuranceCompanyDto;
import com.lojister.model.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@DiscriminatorValue("InsuranceCompany")
public class InsuranceCompany extends User {

    protected InsuranceCompany() {
        this.setRole(Role.ROLE_INSURANCE_COMPANY);
    }

    public static InsuranceCompany create(SaveInsuranceCompanyDto saveInsuranceCompanyDto){
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        insuranceCompany.setFirstName(saveInsuranceCompanyDto.getFirstName());
        insuranceCompany.setLastName(saveInsuranceCompanyDto.getLastName());
        insuranceCompany.setPhone(saveInsuranceCompanyDto.getPhone());
        insuranceCompany.setEmail(saveInsuranceCompanyDto.getEmail());
        insuranceCompany.setPassword(saveInsuranceCompanyDto.getPassword());
        insuranceCompany.setMailConfirmed(true);
        insuranceCompany.setPhoneConfirmed(true);
        return insuranceCompany;
    }

}
