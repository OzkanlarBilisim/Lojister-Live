package com.lojister.model.entity;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TcmbData {
    @Id
    private Long id;
    @Nullable
    private String addDateUsd;
    @Nullable
    private String addDateEuro;
    @Nullable
    private String usd;
    @Nullable
    private String euro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddDateUsd() {
        return addDateUsd;
    }

    public void setAddDateUsd(String addDateUsd) {
        this.addDateUsd = addDateUsd;
    }

    public String getAddDateEuro() {
        return addDateEuro;
    }

    public void setAddDateEuro(String addDateEuro) {
        this.addDateEuro = addDateEuro;
    }

    @Nullable
    public String getUsd() {
        return usd;
    }

    public void setUsd(@Nullable String usd) {
        this.usd = usd;
    }

    @Nullable
    public String getEuro() {
        return euro;
    }

    public void setEuro(@Nullable String euro) {
        this.euro = euro;
    }
}
