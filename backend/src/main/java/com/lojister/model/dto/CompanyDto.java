package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto extends BaseDto {

    private String commercialTitle;

    private String taxNumber;

    private String taxAdministration;

    private String phone;

    private String mail;

    private String financialStaffFirstname;

    private String financialStaffLastname;

    private String financialStaffPhone;

    private Double rating;

    private Long numberOfRating;

    @Embedded
    private Address address;

    private BankInformationDto bankInformation;

    private Double commissionRate;

    private LocalDateTime createdDateTime;
}
