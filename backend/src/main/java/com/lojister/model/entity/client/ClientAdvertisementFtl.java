package com.lojister.model.entity.client;

import com.lojister.controller.advertisement.SaveClientAdvertisementFtlRequest;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Optional;
import java.util.Set;

@Entity
@DiscriminatorValue(value = ClientAdvertisementType.Values.FTL)
@Data
public class ClientAdvertisementFtl extends ClientAdvertisement {
    private Long vehicleCount;

    public static ClientAdvertisementFtl save(
            SaveClientAdvertisementFtlRequest saveClientAdvertisementFtlRequest
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
            , Set<VehicleType> vehicleTypes
            , String clientAdvertisementCode

    ) {

        ClientAdvertisementFtl clientAdvertisementFtl = new ClientAdvertisementFtl();
        clientAdvertisementFtl.setClient(client);
        clientAdvertisementFtl.setAdvertisementStatus(AdvertisementStatus.ACTIVE);
        clientAdvertisementFtl.setAdvertisementProcessStatus(AdvertisementProcessStatus.WAITING);
        clientAdvertisementFtl.setTrailerTypes(trailerTypeSet);
        clientAdvertisementFtl.setTrailerFloorTypes(trailerFloorTypeSet);
        clientAdvertisementFtl.setTrailerFeatures(trailerFeatureSet);
        clientAdvertisementFtl.setStartingAddress(saveClientAdvertisementFtlRequest.getStartingAddress());
        clientAdvertisementFtl.setDueAddress(saveClientAdvertisementFtlRequest.getDueAddress());
        clientAdvertisementFtl.setStartRecipient(startRecipient);
        clientAdvertisementFtl.setDueRecipient(dueRecipient);
        clientAdvertisementFtl.setAdStartingDate(LocalDateTimeParseUtil.localDateConverter(saveClientAdvertisementFtlRequest.getAdStartingDate()));
        clientAdvertisementFtl.setAdDueDate(LocalDateTimeParseUtil.localDateConverter(saveClientAdvertisementFtlRequest.getAdDueDate()));
        clientAdvertisementFtl.setAdStartingTime(LocalDateTimeParseUtil.localTimeConverter(saveClientAdvertisementFtlRequest.getAdStartingTime()));
        clientAdvertisementFtl.setAdDueTime(LocalDateTimeParseUtil.localTimeConverter(saveClientAdvertisementFtlRequest.getAdDueTime()));
        clientAdvertisementFtl.setPackagingType(packagingType);
        clientAdvertisementFtl.setCargoTypes(cargoTypeSet);
        clientAdvertisementFtl.setLoadType(loadTypes);
        clientAdvertisementFtl.setTonnage(saveClientAdvertisementFtlRequest.getTonnage());
        clientAdvertisementFtl.setGoodsPrice(saveClientAdvertisementFtlRequest.getGoodsPrice());
        clientAdvertisementFtl.setIsPorter(saveClientAdvertisementFtlRequest.getIsPorter());
        clientAdvertisementFtl.setIsStacking(saveClientAdvertisementFtlRequest.getIsStacking());
        clientAdvertisementFtl.setDocumentNo(saveClientAdvertisementFtlRequest.getDocumentNo());
        clientAdvertisementFtl.setExplanation(saveClientAdvertisementFtlRequest.getExplanation());
        clientAdvertisementFtl.setCurrencyUnit(currencyUnit);
        clientAdvertisementFtl.setAdvertisementTransportType(saveClientAdvertisementFtlRequest.getAdvertisementTransportType());
        clientAdvertisementFtl.setRegionAdvertisementType( Optional.ofNullable(saveClientAdvertisementFtlRequest.getRegionAdvertisementType()).isPresent() ? saveClientAdvertisementFtlRequest.getRegionAdvertisementType() : RegionAdvertisementType.DOMESTIC);

        clientAdvertisementFtl.setVolume(saveClientAdvertisementFtlRequest.getVolume());
        clientAdvertisementFtl.setDesi(saveClientAdvertisementFtlRequest.getDesi());
        clientAdvertisementFtl.setLdm(saveClientAdvertisementFtlRequest.getLdm());
        clientAdvertisementFtl.setSimpleStartingAddress(simpleStartingAddress);
        clientAdvertisementFtl.setSimpleDueAddress(simpleDueAddress);
        clientAdvertisementFtl.setVehicleCount(saveClientAdvertisementFtlRequest.getVehicleCount());
        clientAdvertisementFtl.setVehicleTypes(vehicleTypes);
        clientAdvertisementFtl.setClientAdvertisementCode(clientAdvertisementCode);
        clientAdvertisementFtl.setSimpleStartingAddress(simpleStartingAddress);
        clientAdvertisementFtl.setSimpleDueAddress(simpleDueAddress);
        return clientAdvertisementFtl;
    }

}
