package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {DriverMapper.class,ClientAdvertisementNIClientTransportProcessMapper.class})
public interface ClientAdvertisementBidMapper extends BaseMapper<ClientAdvertisementBidDto, ClientAdvertisementBid> {

}
