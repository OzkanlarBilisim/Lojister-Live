package com.lojister.mapper;

import com.lojister.model.dto.client.ClientDto;
import com.lojister.model.entity.client.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CompanyMapper.class})
public interface ClientMapper extends BaseMapper<ClientDto,Client> {


}
