package com.lojister.controller.client;

import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientDashboardResponse {
    Long activeAdvertisementCount;
    Long transportAdvertisementCount;
    Long finishedAdvertisementCount;
    Long lastMonthCount;
    List<ClientAdvertisementMinimalDto> clientAdvertisements;
}
