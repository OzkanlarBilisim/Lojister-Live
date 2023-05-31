package com.lojister.model.dto.client;

import com.lojister.model.dto.NotificationSettingDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientNotificationSettingDto extends NotificationSettingDto {
    private Boolean newAdvertisementBidEmailSending=Boolean.FALSE;
    private Boolean newAdvertisementBidMobileSending=Boolean.FALSE;
}
