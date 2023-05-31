package com.lojister.service.abroudService;

import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.ShipsmentInfo;
import com.lojister.model.dto.abroudDto.ShipsmentInfoDto;
import com.lojister.model.entity.User;
import com.lojister.repository.abroudRepository.ShipsmentInfoRepository;
import com.lojister.repository.account.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipsmentInfoServiceImpl implements ShipsmentInfoService{
    @Autowired
    private ShipsmentInfoRepository shipsmentInfoRepository;
    @Autowired
    private AbroudServiceImpl abroudServiceImpl;

    @Autowired
    private SecurityContextUtil securityContextUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String add(ShipsmentInfo data) {
        abroudServiceImpl.advertStatusStep(data.getAdAbroud().getId());

        Boolean permission = abroudServiceImpl.authorityStatus("PAYMENT_SUCCESSFUL", data.getAdAbroud().getId());
        if(permission){
            shipsmentInfoRepository.save(data);
        }

        return "Veri kaydedildi";
    }

    @Override
    public ShipsmentInfoDto getTransportInfo(int advertID){
        ShipsmentInfo shipsmentInfo =  shipsmentInfoRepository.getTransportInfo(advertID);
        ShipsmentInfoDto shipsmentInfoDto = new ShipsmentInfoDto();

        if(shipsmentInfo != null){
            shipsmentInfoDto.setId(shipsmentInfo.getId());
            shipsmentInfoDto.setPlate(shipsmentInfo.getPlate());
            shipsmentInfoDto.setTrailerPlate(shipsmentInfo.getTrailerPlate());
            shipsmentInfoDto.setName(shipsmentInfo.getName());
            shipsmentInfoDto.setTc(shipsmentInfo.getTc());
            shipsmentInfoDto.setTel(shipsmentInfo.getTel());
            shipsmentInfoDto.setAwb(shipsmentInfo.getAwb());
            shipsmentInfoDto.setAirlane(shipsmentInfo.getAirlane());
            shipsmentInfoDto.setOrginAirport(shipsmentInfo.getOrginAirport());
            shipsmentInfoDto.setDestination(shipsmentInfo.getDestination());
            shipsmentInfoDto.setAmbar(shipsmentInfo.getAmbar());
            shipsmentInfoDto.setBayrak(shipsmentInfo.getBayrak());
            shipsmentInfoDto.setOpsiyon(shipsmentInfo.getOpsiyon());
            shipsmentInfoDto.setIskele(shipsmentInfo.getIskele());
            shipsmentInfoDto.setBooking(shipsmentInfo.getBooking());
            shipsmentInfoDto.setSeal(shipsmentInfo.getSeal());
            shipsmentInfoDto.setCotainer(shipsmentInfo.getCotainer());
        }


        return shipsmentInfoDto;
    }
}
