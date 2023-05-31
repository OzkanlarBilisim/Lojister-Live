package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import lombok.Data;


@Data
public class BankInformationDto extends BaseDto {

    private String accountName;

    private String bankName;

    private String branch;

    private String accountNumber;

    private String iban;

}
