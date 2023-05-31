package com.lojister.model.entity.payment;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.dto.CardDatesDto;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;

import javax.annotation.Nullable;
import javax.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double TotalPrice;
    private String PaymentStatus;
    private String AbroadOrDomestic;
    private Double advertPrice;
    private String advertCurrencySymbol;
    private Double rate;
    @ManyToOne
    private AdAbroud adAbroud;
    @ManyToOne
    private ClientAdvertisement clientAdvertisement;
    @ManyToOne
    private Insurance insurance;
    @ManyToOne
    private Client user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getAbroadOrDomestic() {
        return AbroadOrDomestic;
    }

    public void setAbroadOrDomestic(String abroadOrDomestic) {
        AbroadOrDomestic = abroadOrDomestic;
    }

    public Double getAdvertPrice() {
        return advertPrice;
    }

    public void setAdvertPrice(Double advertPrice) {
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

    @Nullable
    public AdAbroud getAdAbroud() {
        return adAbroud;
    }

    public void setAdAbroud(@Nullable AdAbroud adAbroud) {
        this.adAbroud = adAbroud;
    }

    @Nullable
    public ClientAdvertisement getClientAdvertisement() {
        return clientAdvertisement;
    }

    public void setClientAdvertisement(@Nullable ClientAdvertisement clientAdvertisement) {
        this.clientAdvertisement = clientAdvertisement;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }
}