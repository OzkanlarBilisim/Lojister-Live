package com.lojister.model.entity.client;

import com.lojister.controller.client.UpdateClientAccountSettingRequest;
import com.lojister.model.entity.AccountSetting;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class ClientAccountSetting extends AccountSetting {
    Boolean createAdvertisementStartingAddressAutoFill=Boolean.FALSE;

    public ClientAccountSetting update(UpdateClientAccountSettingRequest updateClientAccountSettingRequest){
        this.createAdvertisementStartingAddressAutoFill=updateClientAccountSettingRequest.getCreateAdvertisementStartingAddressAutoFill();
        return this;
    }
}
