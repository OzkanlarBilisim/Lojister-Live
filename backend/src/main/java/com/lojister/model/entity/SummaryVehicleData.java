package com.lojister.model.entity;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class SummaryVehicleData {

    private String brand;

    private String vehicleModel;

    private String licencePlate;

    private String companyName;

    private String trailerPlate;

}
