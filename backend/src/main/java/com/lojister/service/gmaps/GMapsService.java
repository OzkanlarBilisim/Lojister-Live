package com.lojister.service.gmaps;

import com.lojister.model.dto.GeoCodeAddressDto;
import com.lojister.model.dto.PositionDto;
import com.lojister.model.entity.AdvertisementAddress;

import java.util.List;

public interface GMapsService {

    PositionDto getPosition(String str);

    PositionDto geocodeFromAddress(GeoCodeAddressDto address);

    AdvertisementAddress reverseGeocode(PositionDto position);


}
