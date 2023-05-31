package com.lojister.mapper;

import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessForClientAdvertisementDto;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {VehicleMapper.class,ClientAdvertisementBidNIClientAdvertisementMapper.class})
public interface ClientTransportProcessForClientAdvertisementMapper extends BaseMapper<ClientTransportProcessForClientAdvertisementDto, ClientTransportProcess> {
}
