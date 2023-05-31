package com.lojister.model.entity.driver;

import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.controller.driver.UpdateDriverNotificationSettingRequest;
import com.lojister.model.entity.NotificationSetting;
import com.lojister.model.entity.client.ClientNotificationSetting;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class DriverNotificationSetting extends NotificationSetting {
    private Boolean newAdvertisementMailSending=Boolean.FALSE;
    private Boolean newAdvertisementMobileSending=Boolean.FALSE;

    public DriverNotificationSetting update(UpdateDriverNotificationSettingRequest updateDriverNotificationSettingRequest){
        super.statusChangeAdvertisementEmailSending=updateDriverNotificationSettingRequest.getStatusChangeAdvertisementEmailSending();
        super.statusChangeAdvertisementMobileSending=updateDriverNotificationSettingRequest.getStatusChangeAdvertisementMobileSending();
        this.newAdvertisementMailSending=updateDriverNotificationSettingRequest.getNewAdvertisementMailSending();
        this.newAdvertisementMobileSending=updateDriverNotificationSettingRequest.getNewAdvertisementMobileSending();
        return this;
    }
}
