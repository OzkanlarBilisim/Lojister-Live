package com.lojister.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class SummaryDriverData {

    private String firstName;

    private String lastName;

    private String companyName;

    private String citizenId;

    private String phone;

    private String mail;

}
