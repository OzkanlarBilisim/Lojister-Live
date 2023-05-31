package com.lojister.mapper;

import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidNIClientAdvertisementDto;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = DriverMapper.class)
public interface ClientAdvertisementBidNIClientAdvertisementMapper extends BaseMapper<ClientAdvertisementBidNIClientAdvertisementDto, ClientAdvertisementBid>{
}
