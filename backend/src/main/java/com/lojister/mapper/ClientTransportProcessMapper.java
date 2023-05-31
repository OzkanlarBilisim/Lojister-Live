package com.lojister.mapper;

import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {ClientAdvertisementBidMapper.class, VehicleMapper.class})
public interface ClientTransportProcessMapper extends BaseMapper<ClientTransportProcessDto, ClientTransportProcess>{

}
