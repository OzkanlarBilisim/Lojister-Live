package com.lojister.model.dto.clientadvertisementbid;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.driver.DriverDto;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.entity.SummaryAdvertisementData;
import lombok.Data;

@Data
public class ClientAdvertisementBidNIClientAdvertisementDto extends BaseDto {

    private SummaryAdvertisementData summaryAdvertisementData;

    private BidStatus bidStatus;

    private Double bid;

    private Double bidWithVat;

    private DriverDto driverBidder;

}
