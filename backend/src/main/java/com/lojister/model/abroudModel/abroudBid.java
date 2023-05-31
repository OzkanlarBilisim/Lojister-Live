package com.lojister.model.abroudModel;

import com.lojister.model.entity.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class abroudBid{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    private String explanation;
    private String expiration;
    private String bid;
    private String companyRate;
    private String companyName;
    private int companyId;
    @ManyToOne
    private AdAbroud adAbroud;
    @ManyToOne
    private User userDriver;
    private Date dateNow = new Date();

    public User getUserDriver() {
        return userDriver;
    }

    public void setUserDriver(User userDriver) {
        this.userDriver = userDriver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCompanyRate() {
        return companyRate;
    }

    public void setCompanyRate(String companyRate) {
        this.companyRate = companyRate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public AdAbroud getAdAbroud() {
        return adAbroud;
    }

    public void setAdAbroud(AdAbroud adAbroud) {
        this.adAbroud = adAbroud;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }
}
