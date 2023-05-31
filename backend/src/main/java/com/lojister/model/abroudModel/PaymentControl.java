package com.lojister.model.abroudModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class PaymentControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateNow;
    @ManyToOne
    private AdAbroud adAbroud;
    @ManyToOne
    private abroudBid abroudBid;
    private String BankKey;
    private Boolean BankKeyStatus;
    private Boolean insuranceStatus;
    private Double insuranceCost;
    private String domesticOrAbroad;

    public String getDomesticOrAbroad() {
        return domesticOrAbroad;
    }

    public void setDomesticOrAbroad(String domesticOrAbroad) {
        this.domesticOrAbroad = domesticOrAbroad;
    }

    public Boolean getBankKeyStatus() {
        return BankKeyStatus;
    }

    public void setBankKeyStatus(Boolean bankKeyStatus) {
        BankKeyStatus = bankKeyStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }

    public AdAbroud getAdAbroud() {
        return adAbroud;
    }

    public void setAdAbroud(AdAbroud adAbroud) {
        this.adAbroud = adAbroud;
    }

    public com.lojister.model.abroudModel.abroudBid getAbroudBid() {
        return abroudBid;
    }

    public void setAbroudBid(com.lojister.model.abroudModel.abroudBid abroudBid) {
        this.abroudBid = abroudBid;
    }

    public String getBankKey() {
        return BankKey;
    }

    public void setBankKey(String bankKey) {
        BankKey = bankKey;
    }

    public Boolean getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(Boolean insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public Double getInsuranceCost() {
        return insuranceCost;
    }

    public void setInsuranceCost(Double insuranceCost) {
        this.insuranceCost = insuranceCost;
    }
}