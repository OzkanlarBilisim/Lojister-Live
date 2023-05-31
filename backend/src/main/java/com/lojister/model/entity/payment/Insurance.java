package com.lojister.model.entity.payment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String insuranceCurency;
    String insuranceType;
    Double insurancePrice;
    Double insurancePriceTl;

    Double rate;
    String transactionType;

    public Double getInsurancePriceTl() {
        return insurancePriceTl;
    }

    public void setInsurancePriceTl(Double insurancePriceTl) {
        this.insurancePriceTl = insurancePriceTl;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceCurency() {
        return insuranceCurency;
    }

    public void setInsuranceCurency(String insuranceCurency) {
        this.insuranceCurency = insuranceCurency;
    }

    public Double getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(Double insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
