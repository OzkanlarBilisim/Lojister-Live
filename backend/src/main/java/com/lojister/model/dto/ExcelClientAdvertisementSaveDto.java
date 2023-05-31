package com.lojister.model.dto;

import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.ClientAdvertisementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelClientAdvertisementSaveDto {

    private ClientAdvertisementType clientAdvertisementType;
    private String volume;
    private String desi;
    private String ldm;
    private Double goodsPrice;
    private String tonnage;
    private Boolean isPorter;
    private Boolean isStacking;
    private String documentNo;
    private String explanation;
    private Double height; // yükseklik
    private Double length; // uzunluk
    private Double width;  // en
    private LocalDate adStartingDate;
    private LocalDate adDueDate;
    private LocalTime adStartingTime;
    private LocalTime adDueTime;
    private Recipient startRecipient;
    private Recipient dueRecipient;
    private List<String> vehicleTypeNameList;
    private List<String> trailerTypeNameList;
    private List<String> trailerFloorTypeList;
    private List<String> trailerFeatureList;
    private List<String> cargoTypeNameList;
    private List<String> loadTypeNameList;
    private String packagingTypeName;



}
