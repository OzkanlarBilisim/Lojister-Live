package com.lojister.controller.driver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDriverNotificationSettingRequest {
    private Boolean statusChangeAdvertisementEmailSending=Boolean.FALSE;
    private Boolean statusChangeAdvertisementMobileSending=Boolean.FALSE;
    private Boolean newAdvertisementMailSending=Boolean.FALSE;
    private Boolean newAdvertisementMobileSending=Boolean.FALSE;
}
