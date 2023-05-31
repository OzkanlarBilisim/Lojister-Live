package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.ShipsmentInfo;
import com.lojister.model.dto.abroudDto.ShipsmentInfoDto;

public interface ShipsmentInfoService {
    public String add(ShipsmentInfo data);

    public ShipsmentInfoDto getTransportInfo(int advertID);
}
