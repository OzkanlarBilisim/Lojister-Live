package com.lojister.mapper;


import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessWithClientAdvertisementDto;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientTransportProcessWithClientAdvertisementDtoMapper {

    @Mapping(source="acceptedClientAdvertisementBid.clientAdvertisement",target="clientAdvertisement")
    @Mapping(source = "acceptedClientAdvertisementBid.id",target = "bidId")
    @Mapping(source = "acceptedClientAdvertisementBid.bidStatus",target = "bidStatus")
    @Named("DtoToEntity")
    ClientTransportProcessWithClientAdvertisementDto entityToDto(ClientTransportProcess clientTransportProcess);

    @IterableMapping(qualifiedByName = "DtoToEntity")
    List<ClientTransportProcessWithClientAdvertisementDto> entityListToDtoList(List<ClientTransportProcess> entityList);

}
