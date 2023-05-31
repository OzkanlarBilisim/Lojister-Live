package com.lojister.controller.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientNotificationSettingRequest {
    private Boolean statusChangeAdvertisementEmailSending=Boolean.FALSE;
    private Boolean statusChangeAdvertisementMobileSending=Boolean.FALSE;
    private Boolean newAdvertisementBidEmailSending=Boolean.FALSE;
    private Boolean newAdvertisementBidMobileSending=Boolean.FALSE;
}
