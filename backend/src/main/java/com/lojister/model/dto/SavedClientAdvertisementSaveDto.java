package com.lojister.model.dto;

import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.ClientAdvertisementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedClientAdvertisementSaveDto {

    private String advertisementName;

    private ClientAdvertisementType clientAdvertisementType;

    private Long piece;

    private List<Long> vehicleTypeIdList;

    private List<Long> trailerTypeIdList;

    private List<Long> trailerFloorTypeIdList;

    private List<Long> trailerFeatureIdList;

    private Long packagingTypeId;

    private List<Long> cargoTypeIdList;

    private String volume;

    private String desi;

    private String ldm;

    private Double goodsPrice;

    private String tonnage;

    private List<Long> loadTypeIdList;

    private Boolean isPorter;

    private Boolean isStacking;

    private String documentNo;

    private String explanation;

    //PARSİYEL SİPARİŞ VERME ÖZEL ALANLAR
    private Double height; // yükseklik

    private Double length; // uzunluk

    private Double width;  // en

    private Long currencyUnitId;

    private AdvertisementAddress startingAddress;

    private AdvertisementAddress dueAddress;

    private Recipient startRecipient;

    private Recipient dueRecipient;

    private Long vehicleCount;

}
