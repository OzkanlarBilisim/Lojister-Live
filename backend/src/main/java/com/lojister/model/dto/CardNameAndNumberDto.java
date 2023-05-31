package com.lojister.model.dto;

public class CardNameAndNumberDto {
    private String cardName;
    private String maskedCardNumber;
    private String PaymentId;
    private String CardType;

    public CardNameAndNumberDto(String cardName, String cardNumber) {
        this.cardName = cardName;
        this.maskedCardNumber = maskCardNumber(cardNumber);
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    private String maskCardNumber(String cardNumber) {
        int visibleLength = 4;
        int maskedLength = cardNumber.length() - visibleLength;
        String visibleDigits = cardNumber.substring(maskedLength);
        return "xxxx xxxx xxxx " + visibleDigits;
    }
}
