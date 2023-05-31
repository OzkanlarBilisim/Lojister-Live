package com.lojister.model.abroudModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DocumentsAbroud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String documents;
    private int clientID;
    private int driverID;
    private int transportId;

    private String wanting;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getTransportId() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    public String getWanting() {
        return wanting;
    }

    public void setWanting(String wanting) {
        this.wanting = wanting;
    }
}
