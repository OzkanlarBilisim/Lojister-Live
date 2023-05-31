package com.lojister.model.entity.payment;

import com.lojister.model.dto.CardDatesDto;
import com.lojister.model.entity.client.Client;

import javax.persistence.*;

@Entity
public class RegisteredCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardHolderFullName;
    private String cardNumber;
    @Embedded
    private CardDatesDto expDates;
    private String cvcNumber;
    private String cardName;
    private String cardType;
    @ManyToOne
    private Client userId;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getUserId() {
        return userId;
    }

    public void setUserId(Client userId) {
        this.userId = userId;
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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
