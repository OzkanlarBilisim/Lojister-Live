package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.dto.dynamic.TrailerFeatureDto;
import com.lojister.model.dto.dynamic.TrailerFloorTypeDto;
import com.lojister.model.dto.dynamic.TrailerTypeDto;
import com.lojister.model.dto.dynamic.VehicleTypeDto;
import com.lojister.model.entity.Position;
import com.lojister.model.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto extends BaseDto {

    private String vehicleName;

    private Long vehicleCount;

    @NotNull(message ="{lojister.constraint.vehicle.brand.NotNull.message}")
    @NotBlank(message ="{lojister.constraint.vehicle.brand.NotBlank.message}")
    private String brand;

    @NotNull(message ="{lojister.constraint.vehicle.vehicleModel.NotNull.message}")
    @NotBlank(message ="{lojister.constraint.vehicle.vehicleModel.NotBlank.message}")
    private String vehicleModel;

    @NotNull(message ="{lojister.constraint.vehicle.licencePlate.NotNull.message}")
    @NotBlank(message ="{lojister.constraint.vehicle.licencePlate.NotBlank.message}")
    private String licencePlate;

    private String trailerPlate;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.vehicle.maxCapacity.MinValue.message}")
    private Double maxCapacity;

    private VehicleStatus vehicleStatus;

    private String statusDescription;

    private CompanyDto company;

    private DriverDto driver;

    private VehicleTypeDto vehicleType;

    private TrailerTypeDto trailerType;

    private TrailerFloorTypeDto trailerFloorType;

    private Set<TrailerFeatureDto> trailerFeature= new LinkedHashSet<>();

    private Position lastPosition;

}
