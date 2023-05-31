package com.lojister.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class NotificationSetting {
  protected Boolean statusChangeAdvertisementEmailSending=Boolean.FALSE;
   protected Boolean statusChangeAdvertisementMobileSending=Boolean.FALSE;
}
