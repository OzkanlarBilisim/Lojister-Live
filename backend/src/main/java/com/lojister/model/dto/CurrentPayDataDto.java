package com.lojister.model.dto;

public class CurrentPayDataDto {
    public String abroudOrDomastic;
    public String transportId;

    public String insuranceType;
    public String insuranceCurency;

    public String getAbroudOrDomastic() {
        return abroudOrDomastic;
    }

    public void setAbroudOrDomastic(String abroudOrDomastic) {
        this.abroudOrDomastic = abroudOrDomastic;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
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
}
