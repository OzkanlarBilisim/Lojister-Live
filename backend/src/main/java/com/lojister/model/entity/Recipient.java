package com.lojister.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Data
public class Recipient implements Serializable {

    private String commercialTitle;

    private String firstName;

    private String lastName;

    @NotNull
    private String phoneNumber;

}
