package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.RaitingAbroud;
import com.lojister.model.dto.abroudDto.RatingDto;

import java.util.List;

public interface RaitingAbroudService {
    public String save(RaitingAbroud raiting);

    public List<RatingDto>getDriverRating(long DriverId);

    public List<RaitingAbroud> getAllRaitings();
}
