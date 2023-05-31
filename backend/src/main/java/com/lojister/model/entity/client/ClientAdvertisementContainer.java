package com.lojister.model.entity.client;

import com.lojister.controller.advertisement.SaveClientAdvertisementContainerRequest;
import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.enums.*;
import lombok.Data;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Entity
@DiscriminatorValue(value = ClientAdvertisementType.Values.CONTAINER)
@Data
public class ClientAdvertisementContainer extends ClientAdvertisement {

    @Embedded
    private AdvertisementAddress containerAddress;

    @Enumerated(EnumType.STRING)
    private ContainerType containerType;

    private String customsOfficerPhone;

    @Embedded
    private Recipient containerRecipient;

    @Enumerated(EnumType.STRING)
    TradeType tradeType;

    @Embedded
    private SimpleAdvertisementAddress simpleContainerAddress;

    public static ClientAdvertisementContainer save(SaveClientAdvertisementContainerRequest saveClientAdvertisementContainerRequest
            , Client client
            , Recipient startRecipient
            , Recipient dueRecipient
            , Recipient containerRecipient
            , SimpleAdvertisementAddress simpleStartingAddress
            , SimpleAdvertisementAddress simpleDueAddress
            , SimpleAdvertisementAddress simpleContainerAddress
            , String clientAdvertisementCode
            , CurrencyUnit currencyUnit


    ) {
        ClientAdvertisementContainer clientAdvertisementContainer = new ClientAdvertisementContainer();
        clientAdvertisementContainer.setClient(client);
        clientAdvertisementContainer.setAdvertisementStatus(AdvertisementStatus.ACTIVE);
        clientAdvertisementContainer.setAdvertisementProcessStatus(AdvertisementProcessStatus.WAITING);
        clientAdvertisementContainer.setStartingAddress(saveClientAdvertisementContainerRequest.getStartingAddress());
        clientAdvertisementContainer.setDueAddress(saveClientAdvertisementContainerRequest.getDueAddress());
        clientAdvertisementContainer.setStartRecipient(startRecipient);
        clientAdvertisementContainer.setDueRecipient(dueRecipient);
        clientAdvertisementContainer.setContainerRecipient(containerRecipient);
        clientAdvertisementContainer.setAdStartingDate(LocalDateTimeParseUtil.localDateConverter(saveClientAdvertisementContainerRequest.getAdStartingDate()));
        clientAdvertisementContainer.setAdDueDate(LocalDateTimeParseUtil.localDateConverter(saveClientAdvertisementContainerRequest.getAdDueDate()));
        clientAdvertisementContainer.setAdStartingTime(LocalDateTimeParseUtil.localTimeConverter(saveClientAdvertisementContainerRequest.getAdStartingTime()));
        clientAdvertisementContainer.setAdDueTime(LocalDateTimeParseUtil.localTimeConverter(saveClientAdvertisementContainerRequest.getAdDueTime()));
        clientAdvertisementContainer.setTonnage(saveClientAdvertisementContainerRequest.getTonnage());
        clientAdvertisementContainer.setDocumentNo(saveClientAdvertisementContainerRequest.getDocumentNo());
        clientAdvertisementContainer.setExplanation(saveClientAdvertisementContainerRequest.getExplanation());
        clientAdvertisementContainer.setCurrencyUnit(currencyUnit);
        clientAdvertisementContainer.setSimpleStartingAddress(simpleStartingAddress);
        clientAdvertisementContainer.setSimpleDueAddress(simpleDueAddress);
        clientAdvertisementContainer.setSimpleContainerAddress(simpleContainerAddress);
        clientAdvertisementContainer.setCurrencyUnit(currencyUnit);
        clientAdvertisementContainer.setGoodsPrice(saveClientAdvertisementContainerRequest.getGoodsPrice());
        clientAdvertisementContainer.setTradeType(saveClientAdvertisementContainerRequest.getTradeType());
        clientAdvertisementContainer.setRegionAdvertisementType( Optional.ofNullable(saveClientAdvertisementContainerRequest.getRegionAdvertisementType()).isPresent() ? saveClientAdvertisementContainerRequest.getRegionAdvertisementType() : RegionAdvertisementType.DOMESTIC);
        clientAdvertisementContainer.setAdvertisementTransportType(saveClientAdvertisementContainerRequest.getAdvertisementTransportType());
        // clientAdvertisementContainer.setVehicleTypes(vehicleTypes);
        clientAdvertisementContainer.setClientAdvertisementCode(clientAdvertisementCode);
        clientAdvertisementContainer.setSimpleStartingAddress(simpleStartingAddress);
        clientAdvertisementContainer.setSimpleDueAddress(simpleDueAddress);
        clientAdvertisementContainer.setContainerType(saveClientAdvertisementContainerRequest.getContainerType());
        clientAdvertisementContainer.setCustomsOfficerPhone(PhoneFormatter.staticFormat(saveClientAdvertisementContainerRequest.getCustomsOfficerPhone()));
        return clientAdvertisementContainer;
    }

}
