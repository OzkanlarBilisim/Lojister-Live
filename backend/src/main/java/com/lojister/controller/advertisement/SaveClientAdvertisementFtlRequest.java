package com.lojister.controller.advertisement;

import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.AdvertisementTransportType;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveClientAdvertisementFtlRequest {
    @NotNull
    private String adStartingDate;

    @NotNull
    private String adStartingTime;

    @NotNull
    private String adDueDate;

    @NotNull
    private String adDueTime;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.vehicleCount.MinValue.message}")
    private Long vehicleCount;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.goodsPrice.MinValue.message}")
    private Double goodsPrice;

    @NotNull
    private Long currencyUnitId;


    private List<Long> vehicleTypeIdList;


    private List<Long> trailerTypeIdList;


    private List<Long> trailerFloorTypeIdList;


    private List<Long> trailerFeatureIdList;


    private List<Long> cargoTypeIdList;


    private List<Long> loadTypeIdList;

    @NotNull
    private Long packagingTypeId;

    @NotNull
    private Recipient startRecipient;

    @NotNull
    private Recipient dueRecipient;

    private String volume;

    private String desi;

    private String ldm;

    private String tonnage;

    private Boolean isPorter;

    private Boolean isStacking;

    private String explanation;

    private String documentNo;

    private RegionAdvertisementType regionAdvertisementType;

    @NotNull
    private AdvertisementAddress startingAddress;

    @NotNull
    private AdvertisementAddress dueAddress;

    private AdvertisementTransportType advertisementTransportType;
}
