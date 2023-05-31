package com.lojister.model.dto.clientadvertisementbid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.DateTime;
import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementNIClientTransportProcessDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.entity.SummaryAdvertisementData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAdvertisementBidDto extends BaseDto {

    private ClientAdvertisementDto clientAdvertisement;

    private SummaryAdvertisementData summaryAdvertisementData;

    private BidStatus bidStatus;

    private Double bid;

    private Double bidWithVat;

    private DriverDto driverBidder;

    private LocalDateTime createdDateTime;

    private String explanation;

    private LocalDateTime expiration;



}
