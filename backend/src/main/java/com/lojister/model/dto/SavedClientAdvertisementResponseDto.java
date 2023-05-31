package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.dynamic.*;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.ClientAdvertisementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedClientAdvertisementResponseDto extends BaseDto {

    private String advertisementName;

    private ClientAdvertisementType clientAdvertisementType;

    private Long piece;

    private Set<VehicleTypeDto> vehicleTypeSet = new LinkedHashSet<>();

    private Set<TrailerTypeDto> trailerTypeSet = new LinkedHashSet<>();

    private Set<TrailerFloorTypeDto> trailerFloorTypeSet = new LinkedHashSet<>();

    private Set<TrailerFeatureDto> trailerFeatureSet = new LinkedHashSet<>();

    private PackagingTypeDto packagingType;

    private Set<CargoTypeDto> cargoTypeSet = new LinkedHashSet<>();

    private String volume;

    private String desi;

    private String ldm;

    private Double goodsPrice;

    private String tonnage;

    private Set<LoadTypeDto> loadTypeSet = new LinkedHashSet<>();

    private Boolean isPorter;

    private Boolean isStacking;

    private String documentNo;

    private String explanation;

    private Double height; // yükseklik

    private Double length; // uzunluk

    private Double width;  // en

    private AdvertisementAddress startingAddress;

    private AdvertisementAddress dueAddress;

    private CurrencyUnitDto currencyUnit;

    private Recipient startRecipient;

    private Recipient dueRecipient;

    private Long vehicleCount;

}
