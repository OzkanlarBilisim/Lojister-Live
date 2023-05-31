package com.lojister.model.entity.client;

import com.lojister.controller.client.UpdateClientNotificationSettingRequest;
import com.lojister.model.entity.NotificationSetting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class ClientNotificationSetting extends NotificationSetting {
    private Boolean newAdvertisementBidEmailSending=Boolean.FALSE;
    private Boolean newAdvertisementBidMobileSending=Boolean.FALSE;

    public ClientNotificationSetting update(UpdateClientNotificationSettingRequest updateClientNotificationSettingRequest){
      super.statusChangeAdvertisementEmailSending=updateClientNotificationSettingRequest.getStatusChangeAdvertisementEmailSending();
      super.statusChangeAdvertisementMobileSending=updateClientNotificationSettingRequest.getStatusChangeAdvertisementMobileSending();
      this.newAdvertisementBidEmailSending=updateClientNotificationSettingRequest.getNewAdvertisementBidEmailSending();
      this.newAdvertisementBidMobileSending=updateClientNotificationSettingRequest.getNewAdvertisementBidMobileSending();
      return this;
    }
}
