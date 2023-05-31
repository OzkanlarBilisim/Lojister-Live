package com.lojister.model.entity.client;

import com.lojister.controller.advertisement.SaveClientAdvertisementPartialRequest;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

@Entity
@DiscriminatorValue(value = ClientAdvertisementType.Values.PARTIAL)
@Data
public class ClientAdvertisementPartial extends ClientAdvertisement {
    private Double height; // yükseklik
    private Double width;  // en
    private Long piece;

    public static ClientAdvertisementPartial save(
            SaveClientAdvertisementPartialRequest saveClientAdvertisementPartialRequest
            , Client client
            , Set<TrailerType> trailerTypeSet
            , Set<TrailerFloorType> trailerFloorTypeSet
            , Set<TrailerFeature> trailerFeatureSet
            , Recipient startRecipient
            , Recipient dueRecipient
            , PackagingType packagingType
            , Set<CargoType> cargoTypeSet
            , Set<LoadType> loadTypes
            , CurrencyUnit currencyUnit
            , SimpleAdvertisementAddress simpleStartingAddress
            , SimpleAdvertisementAddress simpleDueAddress
            , String clientAdvertisementCode
    ) {
        ClientAdvertisementPartial clientAdvertisementPartial = new ClientAdvertisementPartial();
        clientAdvertisementPartial.setClient(client);
        clientAdvertisementPartial.setAdvertisementStatus(AdvertisementStatus.ACTIVE);
        clientAdvertisementPartial.setAdvertisementProcessStatus(AdvertisementProcessStatus.WAITING);
        clientAdvertisementPartial.setTrailerTypes(trailerTypeSet);
        clientAdvertisementPartial.setTrailerFloorTypes(trailerFloorTypeSet);
        clientAdvertisementPartial.setTrailerFeatures(trailerFeatureSet);
        clientAdvertisementPartial.setStartingAddress(saveClientAdvertisementPartialRequest.getStartingAddress());
        clientAdvertisementPartial.setDueAddress(saveClientAdvertisementPartialRequest.getDueAddress());
        clientAdvertisementPartial.setStartRecipient(startRecipient);
        clientAdvertisementPartial.setDueRecipient(dueRecipient);
        clientAdvertisementPartial.setAdStartingDate(LocalDateTimeParseUtil.localDateConverter(saveClientAdvertisementPartialRequest.getAdStartingDate()));
        clientAdvertisementPartial.setAdDueDate(LocalDateTimeParseUtil.localDateConverter(saveClientAdvertisementPartialRequest.getAdDueDate()));
        clientAdvertisementPartial.setAdStartingTime(LocalDateTimeParseUtil.localTimeConverter(saveClientAdvertisementPartialRequest.getAdStartingTime()));
        clientAdvertisementPartial.setAdDueTime(LocalDateTimeParseUtil.localTimeConverter(saveClientAdvertisementPartialRequest.getAdDueTime()));
        clientAdvertisementPartial.setPackagingType(packagingType);
        clientAdvertisementPartial.setCargoTypes(cargoTypeSet);
        clientAdvertisementPartial.setLoadType(loadTypes);
        clientAdvertisementPartial.setTonnage(saveClientAdvertisementPartialRequest.getTonnage());
        clientAdvertisementPartial.setGoodsPrice(saveClientAdvertisementPartialRequest.getGoodsPrice());
        clientAdvertisementPartial.setIsPorter(saveClientAdvertisementPartialRequest.getIsPorter());
        clientAdvertisementPartial.setIsStacking(saveClientAdvertisementPartialRequest.getIsStacking());
        clientAdvertisementPartial.setDocumentNo(saveClientAdvertisementPartialRequest.getDocumentNo());
        clientAdvertisementPartial.setExplanation(saveClientAdvertisementPartialRequest.getExplanation());
        clientAdvertisementPartial.setCurrencyUnit(currencyUnit);

        clientAdvertisementPartial.setRegionAdvertisementType(Optional.ofNullable(saveClientAdvertisementPartialRequest.getRegionAdvertisementType()).isPresent() ? saveClientAdvertisementPartialRequest.getRegionAdvertisementType() : RegionAdvertisementType.DOMESTIC);
        clientAdvertisementPartial.setAdvertisementTransportType(saveClientAdvertisementPartialRequest.getAdvertisementTransportType());
        clientAdvertisementPartial.setVolume(saveClientAdvertisementPartialRequest.getVolume());
        clientAdvertisementPartial.setDesi(saveClientAdvertisementPartialRequest.getDesi());
        clientAdvertisementPartial.setLdm(saveClientAdvertisementPartialRequest.getLdm());
        clientAdvertisementPartial.setPiece(saveClientAdvertisementPartialRequest.getPiece());
        clientAdvertisementPartial.setWidth(saveClientAdvertisementPartialRequest.getWidth());
        clientAdvertisementPartial.setLength(saveClientAdvertisementPartialRequest.getLength());
        clientAdvertisementPartial.setHeight(saveClientAdvertisementPartialRequest.getHeight());
        clientAdvertisementPartial.setSimpleStartingAddress(simpleStartingAddress);
        clientAdvertisementPartial.setSimpleDueAddress(simpleDueAddress);
        // clientAdvertisementPartial.setVehicleTypes(vehicleTypes);
        clientAdvertisementPartial.setClientAdvertisementCode(clientAdvertisementCode);
        clientAdvertisementPartial.setSimpleStartingAddress(simpleStartingAddress);
        clientAdvertisementPartial.setSimpleDueAddress(simpleDueAddress);
        return clientAdvertisementPartial;

    }

}
