package com.lojister.model.dto.client;

import com.lojister.model.dto.AccountSettingDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientAccountSettingDto extends AccountSettingDto {
    Boolean createAdvertisementStartingAddressAutoFill;
}
