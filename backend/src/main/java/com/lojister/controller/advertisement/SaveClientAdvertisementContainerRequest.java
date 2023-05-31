package com.lojister.controller.advertisement;

import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.enums.AdvertisementTransportType;
import com.lojister.model.enums.ContainerType;
import com.lojister.model.enums.RegionAdvertisementType;
import com.lojister.model.enums.TradeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SaveClientAdvertisementContainerRequest {
    @NotNull
    private String adStartingDate;

    @DecimalMin(value = "0.0", message = "{lojister.constraint.goodsPrice.MinValue.message}")
    private Double goodsPrice;

    @NotNull
    private String adStartingTime;

    @NotNull
    private String adDueDate;

    @NotNull
    private String adDueTime;

    @NotNull
    private Recipient startRecipient;

    @NotNull
    private Recipient dueRecipient;

    private String tonnage;

    private String explanation;

    private String documentNo;

    @NotNull
    private AdvertisementAddress startingAddress;

    @NotNull
    private AdvertisementAddress dueAddress;

    @NotNull
    private AdvertisementAddress containerAddress;

    @NotNull
    private ContainerType containerType;

    @NotNull
    private Recipient containerRecipient;

    private RegionAdvertisementType regionAdvertisementType;

    @NotNull
    private Long currencyUnitId;

    @NotNull
    TradeType tradeType;

    @NotNull
    private String customsOfficerPhone;

    private AdvertisementTransportType advertisementTransportType;

}
