package com.lojister.model.entity.payment;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;

import javax.persistence.*;

@Entity
public class CurrentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String AbroudOrDomastic;
    @ManyToOne
    private AdAbroud adAbroud;
    @ManyToOne
    private ClientAdvertisement clientAdvertisement;
    private String advertPrice;
    private String advertCurrencySymbol;
    private Double rate;
    private String transactionType;
    private Double totalPrice;
    @ManyToOne
    private Insurance insurance;
    @ManyToOne
    private Client user;

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbroudOrDomastic() {
        return AbroudOrDomastic;
    }

    public void setAbroudOrDomastic(String abroudOrDomastic) {
        AbroudOrDomastic = abroudOrDomastic;
    }

    public AdAbroud getAdAbroud() {
        return adAbroud;
    }

    public void setAdAbroud(AdAbroud adAbroud) {
        this.adAbroud = adAbroud;
    }

    public ClientAdvertisement getClientAdvertisement() {
        return clientAdvertisement;
    }

    public void setClientAdvertisement(ClientAdvertisement clientAdvertisement) {
        this.clientAdvertisement = clientAdvertisement;
    }

    public String getAdvertPrice() {
        return advertPrice;
    }

    public void setAdvertPrice(String advertPrice) {
        this.advertPrice = advertPrice;
    }

    public String getAdvertCurrencySymbol() {
        return advertCurrencySymbol;
    }

    public void setAdvertCurrencySymbol(String advertCurrencySymbol) {
        this.advertCurrencySymbol = advertCurrencySymbol;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }
}
