package com.lojister.model.dto;

import com.lojister.model.entity.Vehicle;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import lombok.Data;

@Data
public class VehicleDocumentNotDataDto {

    private Long id;

    private VehicleMinimalDto vehicle;

    private VehicleDocumentType vehicleDocumentType;

    private String fileName;


    public VehicleDocumentNotDataDto(Long id, Vehicle vehicle, VehicleDocumentType vehicleDocumentType, String fileName) {

        VehicleMinimalDto vehicleMinimalDto = new VehicleMinimalDto();
        vehicleMinimalDto.setVehicleId(vehicle.getId());
        vehicleMinimalDto.setVehicleName(vehicle.getVehicleName());
        vehicleMinimalDto.setBrand(vehicle.getBrand());
        vehicleMinimalDto.setVehicleModel(vehicle.getVehicleModel());
        vehicleMinimalDto.setLicencePlate(vehicle.getLicencePlate());
        vehicleMinimalDto.setMaxCapacity(vehicle.getMaxCapacity());
        vehicleMinimalDto.setCompanyId(vehicle.getCompany().getId());
        vehicleMinimalDto.setCompanyName(vehicle.getCompany().getCommercialTitle());
        vehicleMinimalDto.setVehicleType(vehicle.getVehicleType());
        this.id = id;
        this.vehicle = vehicleMinimalDto;
        this.vehicleDocumentType = vehicleDocumentType;
        this.fileName = fileName;
    }
}
