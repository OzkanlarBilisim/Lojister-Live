package com.lojister.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyValidatorDto {

    private String taxAdministration;

    private String taxNumber;

    public CompanyValidatorDto(String taxAdministration, String taxNumber) {
        this.taxAdministration = taxAdministration;
        this.taxNumber = taxNumber;
    }

}
