package com.lojister.model.dto.abroudDto;

import java.sql.Blob;
import java.util.Date;

public class RatingDto {
    private Long clientID;
    private String comment;
    private Date dateNow;
    private int rating;
    private String ClientName;
    private String ClientSurname;

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientSurname() {
        return ClientSurname;
    }

    public void setClientSurname(String clientSurname) {
        ClientSurname = clientSurname;
    }
}
