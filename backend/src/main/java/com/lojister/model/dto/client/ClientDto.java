package com.lojister.model.dto.client;

import com.lojister.model.dto.CompanyDto;
import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.enums.ClientTitle;
import com.lojister.model.enums.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto extends BaseDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private ClientTitle clientTitle;
    private ClientType clientType;
    private CompanyDto company;

}
