package com.lojister.business.concretes;

import com.lojister.core.exception.UndefinedClientAdvertisementTypeException;
import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.GeoCodeAddressDto;
import com.lojister.model.dto.PositionDto;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.SimpleAdvertisementAddress;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.repository.advertisement.ClientAdvertisementRepository;
import com.lojister.service.gmaps.GMapsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementLogic {

    @Value("${lojister.advertisement.code}")
    private String clientAdvertisementCodeStartString;

    private final ClientAdvertisementRepository clientAdvertisementRepository;
    private final GMapsService gMapsService;

    public ClientAdvertisementType stringToClientAdvertisementType(String typeName) {

        if (StringUtils.isBlank(typeName)) {
            throw new UndefinedClientAdvertisementTypeException(Translator.toLocale("lojister.advertisementLogic.UndefinedClientAdvertisementTypeException"));
        }

        if (typeName.equals("FTL")) {
            return ClientAdvertisementType.FTL;
        } else if (typeName.equals("PARTIAL")) {
            return ClientAdvertisementType.PARTIAL;
        } else {
            throw new UndefinedClientAdvertisementTypeException(Translator.toLocale("lojister.advertisementLogic.UndefinedClientAdvertisementTypeException"));
        }

    }

    SimpleAdvertisementAddress createSimpleAdvertisementAddress(AdvertisementAddress advertisementAddress){
        SimpleAdvertisementAddress simpleAdvertisementAddress= new SimpleAdvertisementAddress();
        simpleAdvertisementAddress.setCountry(advertisementAddress.getCountry());
        simpleAdvertisementAddress.setProvince(advertisementAddress.getProvince());
        simpleAdvertisementAddress.setDistrict(advertisementAddress.getDistrict());
        simpleAdvertisementAddress.setZipCode(advertisementAddress.getZipCode());
        simpleAdvertisementAddress.setNeighborhood(advertisementAddress.getNeighborhood());
        GeoCodeAddressDto geoCodeAddressDto = new GeoCodeAddressDto();
        geoCodeAddressDto.setProvince(advertisementAddress.getProvince());
        geoCodeAddressDto.setDistrict(advertisementAddress.getDistrict());
        geoCodeAddressDto.setNeighborhood(advertisementAddress.getNeighborhood());
        PositionDto positionDto= gMapsService.geocodeFromAddress(geoCodeAddressDto);
       /* PositionDto positionDto= gMapsService.geocodeFromAddress(GeoCodeAddressDto.builder()
                .province(advertisementAddress.getProvince())
                .district(advertisementAddress.getDistrict())
                .neighborhood(advertisementAddress.getNeighborhood())
                .build());*/
        simpleAdvertisementAddress.setLat(positionDto.getLatitude());
        simpleAdvertisementAddress.setLng(positionDto.getLongitude());
        return simpleAdvertisementAddress;

    }

     //TODO BURAYI KONROL ET
   public String createAdvertisementCode(){
     String currentDate= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
       StringBuilder stringBuilder=new StringBuilder();
       Long value=0L;
     Optional<ClientAdvertisement> clientAdvertisementOptional=clientAdvertisementRepository.findFirstByOrderByIdDesc();

     if (clientAdvertisementOptional.isPresent())
     {

         String code=clientAdvertisementOptional.get().getClientAdvertisementCode();
         if (code!=null){
             value= Long.valueOf(code.substring(11,code.length()));
             if(currentDate.equals(code.substring(3,11))){
                 value++;
             }
             else {
                 value=0L;
             }
         }
         else {

         }
     }
     else {

     }
       String result = String.join("", Collections.nCopies(6-String.valueOf(value).length(), "0"))+value;
       stringBuilder.append(clientAdvertisementCodeStartString).append(currentDate).append(result);

     return stringBuilder.toString();

    }

}
