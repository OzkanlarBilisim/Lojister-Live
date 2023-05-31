package com.lojister.model.abroudModel;

import javax.persistence.*;
import java.sql.Blob;

@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] Receipt;
    @ManyToOne
    private AdAbroud adAbroud;
    private Double price;
    private Boolean status;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getReceipt() {
        return Receipt;
    }

    public void setReceipt(byte[] receipt) {
        Receipt = receipt;
    }

    public AdAbroud getAdAbroud() {
        return adAbroud;
    }

    public void setAdAbroud(AdAbroud adAbroud) {
        this.adAbroud = adAbroud;
    }
}
