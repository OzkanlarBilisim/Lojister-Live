package com.lojister.service.abroudService;

import com.lojister.business.abstracts.ClientAdvertisementBidService;
import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.business.abstracts.CompanyService;
import com.lojister.business.abstracts.ProfilePhotoFileService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.RaitingAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.abroudDto.DriverBidList;
import com.lojister.model.dto.abroudDto.RatingDto;
import com.lojister.model.entity.Company;
import com.lojister.model.entity.ProfilePhotoFile;
import com.lojister.model.entity.User;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.AdvertisementStatus;
import com.lojister.model.enums.BidStatus;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.abroudRepository.RaitingAbroudRepository;
import com.lojister.repository.account.UserRepository;
import com.lojister.repository.advertisement.ClientAdvertisementBidRepository;
import com.lojister.repository.advertisement.ClientAdvertisementRepository;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RaitingAbroudServiceImpl implements RaitingAbroudService {

    @Autowired
    private RaitingAbroudRepository raitingAbroudRepository;
    @Autowired
    private AbroudRepository abroudRepository;
    @Autowired
    private AbroudService abroudService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AbroudBidRepository abroudBidRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ClientAdvertisementService clientAdvertisementService;
    @Autowired
    private ClientAdvertisementBidRepository clientAdvertisementBidRepository;

    @Autowired
    private ProfilePhotoFileService profilePhotoFileService;
    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Override
    public String save(RaitingAbroud raiting) {
        Long currentUserID = null;
        User user_Client = null;
        Company company_Driver = null;
        Company company = null;
        AdAbroud advertAbroud = null;
        ClientAdvertisement advertDomastic = null;
        RaitingAbroud raitingAbroud = null;

        if(raiting.getRating() < 1 || raiting.getRating() > 5){
            throw new RuntimeException("5'ten büyük yada 1'den küçük oy kullanamazsınız!");
        }
        if (raiting.getAbroudOrDomastic().equals("DOMASTIC")) {
            ClientAdvertisement advert2 = clientAdvertisementService.findDataById(raiting.getAdvertDomastic().getId());
            currentUserID = advert2.getClient().getId();
        }else {
            Optional<AdAbroud> advert2 = abroudRepository.findById(raiting.getAdvertAbroud().getId());
            currentUserID = Long.valueOf(advert2.get().getClient_id());
        }


        if (securityContextUtil.getCurrentUserId().equals(currentUserID)){
            if (raiting.getAbroudOrDomastic().equals("DOMASTIC")) {

                ClientAdvertisement advert = clientAdvertisementService.findDataById(raiting.getAdvertDomastic().getId());
                advertDomastic = raiting.getAdvertDomastic();

                /*user_Client = advert.getClient();*/
                user_Client = userRepository.findById(advert.getClient().getId()).get();
                Optional<ClientAdvertisementBid> bid = clientAdvertisementBidRepository.findSelectBidAdvertId(raiting.getAdvertDomastic().getId());

                company = bid.get().getDriverBidder().getCompany();
                company_Driver = company;

                advert.setAdvertisementStatus(AdvertisementStatus.FINISHED);
                advert.setAdvertisementProcessStatus(AdvertisementProcessStatus.FINISHED);

                clientAdvertisementService.saveRepo(advert);
            } else {
                Optional<AdAbroud> advert = abroudRepository.findById(raiting.getAdvertAbroud().getId());

                advertAbroud = raiting.getAdvertAbroud();
                abroudBid abroudBid2 = abroudBidRepository.findbidPaymentSuccessAproved(raiting.getAdvertAbroud().getId()).get(0);

                user_Client = userRepository.findById(Long.valueOf(advert.get().getClient_id())).get();
                company = companyService.findDataById(Long.valueOf(abroudBid2.getCompanyId()));
                company_Driver = company;

                abroudService.advertStatusStep(raiting.getAdvertAbroud().getId());




            }

            double rating = 0.0;
            if(company.getRating() > 5){
                rating = raiting.getRating();
            }else {
                rating = (company.getRating() + raiting.getRating()) / 2;
            }
            company.setRating(rating);
            companyService.saveRepo(company);

            raiting.setUser_Client(user_Client);
            raiting.setUser_Driver(company_Driver);
            raiting.setAdvertAbroud(advertAbroud);
            raiting.setAdvertDomastic(advertDomastic);



            raitingAbroud = raitingAbroudRepository.save(raiting);
        }
        return "complated";
    }
    @Override
    public List<RatingDto> getDriverRating(long DriverId) {
        List<RaitingAbroud> ratings = raitingAbroudRepository.findByDriverId(DriverId);

        List<RatingDto> ratingDtos = new ArrayList<>();
        for (RaitingAbroud rating : ratings) {
            RatingDto ratingDto = new RatingDto();
                String star = "*";
                String name = rating.getUser_Client().getFirstName();


                String hiddenName = String.valueOf(name.charAt(0));
                for(int i=0; i<name.length()-1; i++){
                    hiddenName += "*";
                }



                String surName = rating.getUser_Client().getLastName();
                String hiddenSurname = String.valueOf(surName.charAt(0));
                for(int i=0; i<surName.length()-1; i++){
                    hiddenSurname += "*";
                }

                ratingDto.setRating(rating.getRating());
                ratingDto.setComment(rating.getComment());
                ratingDto.setDateNow(rating.getDateNow());
                ratingDto.setClientName(hiddenName);
                ratingDto.setClientSurname(hiddenSurname);
                ratingDto.setClientID(rating.getUser_Client().getId());


            ratingDtos.add(ratingDto);
        }
        return ratingDtos;
    }

    @Override
    public List<RaitingAbroud> getAllRaitings() {
        return raitingAbroudRepository.findAll();
    }
}
