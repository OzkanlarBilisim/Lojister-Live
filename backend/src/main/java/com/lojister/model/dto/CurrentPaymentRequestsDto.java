package com.lojister.model.dto;

public class CurrentPaymentRequestsDto {

    public String startingCountry;

    public String dueCountry;

    public String dateofShipment;

    public String insurancePrice;

    public String BidPrice;

    public Long TransportID;

    public String AbroadOrDomestic;

    public String getStartingCountry() {
        return startingCountry;
    }

    public void setStartingCountry(String startingCountry) {
        this.startingCountry = startingCountry;
    }

    public String getDueCountry() {
        return dueCountry;
    }

    public void setDueCountry(String dueCountry) {
        this.dueCountry = dueCountry;
    }

    public String getDateofShipment() {
        return dateofShipment;
    }

    public void setDateofShipment(String dateofShipment) {
        this.dateofShipment = dateofShipment;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getBidPrice() {
        return BidPrice;
    }

    public void setBidPrice(String bidPrice) {
        BidPrice = bidPrice;
    }

    public Long getTransportID() {
        return TransportID;
    }

    public void setTransportID(Long transportID) {
        TransportID = transportID;
    }

    public String getAbroadOrDomestic() {
        return AbroadOrDomestic;
    }

    public void setAbroadOrDomestic(String abroadOrDomestic) {
        AbroadOrDomestic = abroadOrDomestic;
    }
}
