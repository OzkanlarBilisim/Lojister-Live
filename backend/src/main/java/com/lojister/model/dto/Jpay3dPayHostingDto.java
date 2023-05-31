package com.lojister.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Jpay3dPayHostingDto {

    private String clientid;
    private String storetype;
    private String islemtipi;
    private String amount;
    private String currency;
    private String oid;
    private String okUrl;
    private String okUrlAbroud;
    private String failUrl;
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
