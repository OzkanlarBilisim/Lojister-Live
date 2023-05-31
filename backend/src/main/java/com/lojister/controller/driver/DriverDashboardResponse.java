package com.lojister.controller.driver;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessWithClientAdvertisementDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DriverDashboardResponse {
    Long activeAdvertisementBidCount;
    Long transportAdvertisementBidCount;
    Long finishedTransportCount;
    Long lastMonthCount;
    List<ClientAdvertisementBidDto> clientAdvertisementBidList;
}
