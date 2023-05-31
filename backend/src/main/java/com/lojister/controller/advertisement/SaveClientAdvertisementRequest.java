package com.lojister.controller.advertisement;

import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.ClientAdvertisementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveClientAdvertisementRequest {

    @NotNull
    private String adStartingDate;

    @NotNull
    private String adStartingTime;

    @NotNull
    private String adDueDate;

    @NotNull
    private String adDueTime;

    @NotNull
    private ClientAdvertisementType clientAdvertisementType;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.vehicleCount.MinValue.message}")
    private Long vehicleCount;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.goodsPrice.MinValue.message}")
    private Double goodsPrice;

    @NotNull
    private Long currencyUnitId;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.piece.MinValue.message}")
    private Long piece;

    private List<Long> vehicleTypeIdList;

    @NotNull
    private List<Long> trailerTypeIdList;

    @NotNull
    private List<Long> trailerFloorTypeIdList;

    @NotNull
    private List<Long> trailerFeatureIdList;

    @NotNull
    private List<Long> cargoTypeIdList;

    @NotNull
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

    @DecimalMin(value = "0.0",message ="{lojister.constraint.height.MinValue.message}")
    private Double height; // yükseklik

    @DecimalMin(value = "0.0",message ="{lojister.constraint.length.MinValue.message}")
    private Double length; // uzunluk

    @DecimalMin(value = "0.0",message ="{lojister.constraint.width.MinValue.message}")
    private Double width;  // en

    private Boolean isPorter;

    private Boolean isStacking;

    private String explanation;

    private String documentNo;

    @NotNull
    private AdvertisementAddress startingAddress;

    @NotNull
    private AdvertisementAddress dueAddress;

}
