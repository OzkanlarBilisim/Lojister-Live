package com.lojister.model.dto.driver;

import com.lojister.model.dto.NotificationSettingDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverNotificationSettingDto extends NotificationSettingDto {
    private Boolean newAdvertisementMailSending=Boolean.FALSE;
    private Boolean newAdvertisementMobileSending=Boolean.FALSE;
}
