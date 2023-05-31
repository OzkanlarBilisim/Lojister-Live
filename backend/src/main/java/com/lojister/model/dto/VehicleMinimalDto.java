package com.lojister.model.dto;

import com.lojister.model.entity.adminpanel.VehicleType;
import lombok.Data;

@Data
public class VehicleMinimalDto {

    private Long vehicleId;

    private String vehicleName;

    private String brand;

    private String vehicleModel;

    private String licencePlate;

    private Double maxCapacity;

    private Long companyId;

    private String companyName;

    private VehicleType vehicleType;

}
