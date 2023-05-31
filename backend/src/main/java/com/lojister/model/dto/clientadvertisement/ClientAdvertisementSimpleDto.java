package com.lojister.model.dto.clientadvertisement;

import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.enums.AdvertisementTransportType;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class ClientAdvertisementSimpleDto extends ClientAdvertisementMinimalDto {
    private String volume;
    private String desi;
    private String ldm;
    private Double goodsPrice;
    private String tonnage;
    private String explanation;
    private Double height;
    private Double width;
    private Double length;
    private Long piece;
    private RegionAdvertisementType regionAdvertisementType;
    private Set<TrailerFloorType> trailerFloorTypes = new LinkedHashSet<>();
    private Set<TrailerFeature> trailerFeatures = new LinkedHashSet<>();
    private Set<VehicleType> vehicleTypes = new LinkedHashSet<>();
    private Set<LoadType> loadType = new LinkedHashSet<>();
    private Set<TrailerType> trailerTypes = new LinkedHashSet<>();
    private PackagingType packagingType;
    private Long vehicleCount;
    private CurrencyUnit currencyUnit;
    private AdvertisementTransportType advertisementTransportType;
}

