package com.lojister.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingDto {
    Boolean statusChangeAdvertisementEmailSending=Boolean.FALSE;
    Boolean statusChangeAdvertisementMobileSending=Boolean.FALSE;
}
