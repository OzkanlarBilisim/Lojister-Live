package com.lojister.model.dto.clienttransportprocess;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementMinimalWithoutClientTransportProcessDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.enums.TransportProcessType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClientTransportProcessWithClientAdvertisementDto extends BaseDto {

    private TransportProcessStatus transportProcessStatus;
    private TransportProcessType transportProcessType;
    private String transportCode;
    private ClientAdvertisementMinimalWithoutClientTransportProcessDto clientAdvertisement;
    private Long BidId;
    private BidStatus bidStatus;
}
