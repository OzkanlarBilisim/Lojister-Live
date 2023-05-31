package com.lojister.model.abroudModel;

import com.lojister.model.entity.Company;
import com.lojister.model.entity.User;
import com.lojister.model.entity.client.ClientAdvertisement;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@Entity
public class RaitingAbroud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String comment;
    private int rating;
    @ManyToOne
    private User user_Client;
    @ManyToOne
    private Company user_Driver;
    @ManyToOne
    private AdAbroud advertAbroud;
    @ManyToOne
    private ClientAdvertisement advertDomastic;
    private String abroudOrDomastic;
    private Date dateNow = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser_Client() {
        return user_Client;
    }

    public void setUser_Client(User user_Client) {
        this.user_Client = user_Client;
    }

    public Company getUser_Driver() {
        return user_Driver;
    }

    public void setUser_Driver(Company user_Driver) {
        this.user_Driver = user_Driver;
    }

    public AdAbroud getAdvertAbroud() {
        return advertAbroud;
    }

    public void setAdvertAbroud(AdAbroud advertAbroud) {
        this.advertAbroud = advertAbroud;
    }

    public ClientAdvertisement getAdvertDomastic() {
        return advertDomastic;
    }

    public void setAdvertDomastic(ClientAdvertisement advertDomastic) {
        this.advertDomastic = advertDomastic;
    }

    public String getAbroudOrDomastic() {
        return abroudOrDomastic;
    }

    public void setAbroudOrDomastic(String abroudOrDomastic) {
        this.abroudOrDomastic = abroudOrDomastic;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }
}
