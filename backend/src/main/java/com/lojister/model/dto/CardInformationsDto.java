package com.lojister.model.dto;

public class CardInformationsDto {
    private String cardHolderFullName;
    private String cardNumber;
    private CardDatesDto expDates;
    private String cvcNumber;
    private String insuranceType;
    private boolean registery;
    private String cardName;
    private String abroadOrDomestic;
    private String transportID;

    public String getTransportID() {
        return transportID;
    }

    public void setTransportID(String transportID) {
        this.transportID = transportID;
    }

    public String getAbroadOrDomestic() {
        return abroadOrDomestic;
    }

    public void setAbroadOrDomestic(String abroadOrDomestic) {
        this.abroadOrDomestic = abroadOrDomestic;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardHolderFullName() {
        return cardHolderFullName;
    }

    public void setCardHolderFullName(String cardHolderFullName) {
        this.cardHolderFullName = cardHolderFullName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardDatesDto getExpDates() {
        return expDates;
    }

    public void setExpDates(CardDatesDto expDates) {
        this.expDates = expDates;
    }

    public String getCvcNumber() {
        return cvcNumber;
    }

    public void setCvcNumber(String cvcNumber) {
        this.cvcNumber = cvcNumber;
    }


    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public boolean isRegistery() {
        return registery;
    }

    public void setRegistery(boolean registery) {
        this.registery = registery;
    }
}
