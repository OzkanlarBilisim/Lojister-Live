package com.lojister.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TebPayment3dDto {

    private String clientid;
    private String storetype;
    private String islemtipi;
    private String amount;
    private String currency;
    private String oid;
    private String okUrl;
    private String failUrl;
    private String okUrlAbroud;
    private String failUrlAbroud;
    private String lang;
    private String rnd;
    private String hash;
    private String refreshtime;
    private String taksit;
    private String tel;
    private String faturaFirma;
    private String fulkekod;
    private String tulkekod;

}
