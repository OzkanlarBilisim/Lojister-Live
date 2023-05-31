package com.lojister.service.abroudService;

import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.core.exception.DriverStatusException;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.abroudDto.*;
import com.lojister.model.entity.User;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.DriverStatus;
import com.lojister.model.enums.Role;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.repository.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AbroudBidServiceImpl implements AbroudBidService {

    @Autowired
    private AbroudBidRepository studentRepository;

    @Autowired
    private AbroudRepository abroudRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Override
    public abroudBid saveStudent(abroudBid student) {

        abroudBid abroudBid1 = null;
        if(securityContextUtil.getCurrentUserRole().equals(Role.ROLE_DRIVER)){
            if (securityContextUtil.getCurrentDriver().getStatus().equals(DriverStatus.ACCEPTED)){
                student.setStatus("WAITING");
                mailNotificationService.addBidSendMail(student.getAdAbroud().getId());
                abroudBid1 = studentRepository.save(student);
            }else{
                throw new DriverStatusException("Henüz onaylı sürücü bulunmamaktadır.");
            }

        }
        return abroudBid1;
    }


    public List<DriverBidList> driverBidList(int driverId) {
        List<abroudBid> abroudBid2 = studentRepository.driverBidList(driverId);

        List<DriverBidList> driverBidLists = new ArrayList<>();
        for (abroudBid driverBidList2 : abroudBid2) {
            DriverBidList driverBidList3 = new DriverBidList();


            driverBidList3.setAdvertisementId(driverBidList2.getAdAbroud().getId());
            if(driverBidList2.getStatus().equals("DENIED")){
                driverBidList3.setBidStatus(driverBidList2.getStatus());
            }else {
                driverBidList3.setBidStatus(driverBidList2.getAdAbroud().getAdvertisementStatus());
            }
            driverBidList3.setLoadingDate(driverBidList2.getAdAbroud().getStartDate()+"/ "+driverBidList2.getAdAbroud().getDueDate());
            driverBidList3.setDestinationZone(driverBidList2.getAdAbroud().getDueFullAddress());
            driverBidList3.setLoadingZone(driverBidList2.getAdAbroud().getStartFullAddress());
            driverBidList3.setTransportType(driverBidList2.getAdAbroud().getClientAdvertisementType()+" / "+driverBidList2.getAdAbroud().getVehicleOrContainer());


            driverBidLists.add(driverBidList3);
        }
        return driverBidLists;
    }

    public List<AbroudBidRatingDto> getAllAdwertismantId(int advertID) {
        List<abroudBid> abroudBid2 = studentRepository.getAllAdwertismantId(advertID);

        List<AbroudBidRatingDto> driverBidLists = new ArrayList<>();
        for (abroudBid driverBidList2 : abroudBid2) {
            AbroudBidRatingDto driverBidList3 = new AbroudBidRatingDto();

            Double rating = companyRepository.findById(Long.valueOf(driverBidList2.getCompanyId())).get().getRating();
            driverBidList3.setId(driverBidList2.getId());
            driverBidList3.setStatus(driverBidList2.getStatus());
            driverBidList3.setExpiration(driverBidList2.getExpiration());
            driverBidList3.setExplanation(driverBidList2.getExplanation());
            driverBidList3.setBid(driverBidList2.getBid());
            driverBidList3.setCompanyRate(String.valueOf(rating));
            driverBidList3.setCompanyName(driverBidList2.getCompanyName());
            driverBidList3.setCompanyId(driverBidList2.getCompanyId());
            driverBidList3.setAdAbroud(driverBidList2.getAdAbroud());
            driverBidList3.setDateNow(driverBidList2.getDateNow());


            driverBidLists.add(driverBidList3);
        }
        return driverBidLists;
    }

    public BidAndAdvertRequestDto bidAndAdvertRequestDto(int bidID) {

        abroudBid abroudBid2 = studentRepository.bidAndAdvertRequestDto(successBidFind2(bidID)).get(0);

        BidAndAdvertRequestDto bidAndAdvertRequestDto = new BidAndAdvertRequestDto();

        bidAndAdvertRequestDto.setAdvertisementStatus(abroudBid2.getStatus());
        bidAndAdvertRequestDto.setAdvertisementStatus2(abroudBid2.getAdAbroud().getAdvertisementStatus());
        bidAndAdvertRequestDto.setClient_id(abroudBid2.getAdAbroud().getClient_id());
        bidAndAdvertRequestDto.setDesi(abroudBid2.getAdAbroud().getDesi());
        bidAndAdvertRequestDto.setContainerType(abroudBid2.getAdAbroud().getContainerType());
        bidAndAdvertRequestDto.setClientAdvertisementType(abroudBid2.getAdAbroud().getClientAdvertisementType());
        bidAndAdvertRequestDto.setCurrencyUnitId(abroudBid2.getAdAbroud().getCurrencyUnitId());
        bidAndAdvertRequestDto.setDateNow(abroudBid2.getAdAbroud().getDateNow());
        bidAndAdvertRequestDto.setDeliveryType(abroudBid2.getAdAbroud().getDeliveryType());
        bidAndAdvertRequestDto.setDueDate(abroudBid2.getAdAbroud().getDueDate());
        bidAndAdvertRequestDto.setDueDate(abroudBid2.getAdAbroud().getDueDate());
        bidAndAdvertRequestDto.setDueFullAddress(abroudBid2.getAdAbroud().getDueFullAddress());
        bidAndAdvertRequestDto.setDueSelectCountryName(abroudBid2.getAdAbroud().getDueSelectCountryName());
        bidAndAdvertRequestDto.setExplanation(abroudBid2.getAdAbroud().getExplanation());
        bidAndAdvertRequestDto.setGoodsPrice(abroudBid2.getAdAbroud().getGoodsPrice());
        bidAndAdvertRequestDto.setHeight(abroudBid2.getAdAbroud().getHeight());
        bidAndAdvertRequestDto.setHsCode(abroudBid2.getAdAbroud().getHsCode());
        bidAndAdvertRequestDto.setLength(abroudBid2.getAdAbroud().getLength());
        bidAndAdvertRequestDto.setLoadTypeIdList(abroudBid2.getAdAbroud().getLoadTypeIdList());
        bidAndAdvertRequestDto.setPackagingTypeId(abroudBid2.getAdAbroud().getPackagingTypeId());
        bidAndAdvertRequestDto.setPiece(abroudBid2.getAdAbroud().getPiece());
        bidAndAdvertRequestDto.setStartDate(abroudBid2.getAdAbroud().getStartDate());
        bidAndAdvertRequestDto.setStartDeliveryMethod(abroudBid2.getAdAbroud().getStartDeliveryMethod());
        bidAndAdvertRequestDto.setStartFullAddress(abroudBid2.getAdAbroud().getStartFullAddress());
        bidAndAdvertRequestDto.setStartSelectCountryCode(abroudBid2.getAdAbroud().getStartSelectCountryCode());
        bidAndAdvertRequestDto.setStartSelectCountryName(abroudBid2.getAdAbroud().getStartSelectCountryName());
        bidAndAdvertRequestDto.setTonnage(abroudBid2.getAdAbroud().getTonnage());
        bidAndAdvertRequestDto.setTrailerFeatureName(abroudBid2.getAdAbroud().getTrailerFeatureName());
        bidAndAdvertRequestDto.setTrailerFloorTypeIdList(abroudBid2.getAdAbroud().getTrailerFloorTypeIdList());
        bidAndAdvertRequestDto.setTrailerTypeIdList(abroudBid2.getAdAbroud().getTrailerTypeIdList());
        bidAndAdvertRequestDto.setUnCode(abroudBid2.getAdAbroud().getUnCode());
        bidAndAdvertRequestDto.setVehicleCount(abroudBid2.getAdAbroud().getVehicleCount());
        bidAndAdvertRequestDto.setVehicleOrContainer(abroudBid2.getAdAbroud().getVehicleOrContainer());
        bidAndAdvertRequestDto.setVehicleTypeIdList(abroudBid2.getAdAbroud().getVehicleTypeIdList());
        bidAndAdvertRequestDto.setWidth(abroudBid2.getAdAbroud().getWidth());
        bidAndAdvertRequestDto.setId(abroudBid2.getAdAbroud().getId());



        return bidAndAdvertRequestDto;
    }

    public Optional<abroudBid> findById(int id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<abroudBid> findbidAndabraud(int id) {
        return studentRepository.findbidAndabraud(id);
    }
    public List<abroudBid> findClientAbroud(int companyId, int adwertisamentId) {
        return studentRepository.findClientAbroud(companyId, adwertisamentId);
    }

    private int successBidFind(int bidID){
        int bidID2 = 0;
        List<abroudBid> abroudBid1 = studentRepository.getAllAdwertismantId(bidID);
        for (abroudBid bid : abroudBid1) {
            switch (bid.getStatus()){
                case "APPROVED":
                    bidID2 = bid.getId();
                case "WAITING":
                    bidID2 = bid.getId();
                case "PAYMENT_SUCCESSFUL":
                    bidID2 = bid.getId();
                default:
                    bidID2 = bidID;
                    break;
            }
        }
        return bidID2;
    }

    private int successBidFind2(int bidID){
        int bidID2 = 0;
        List<abroudBid> abroudBid1 = studentRepository.getAllAdwertismantId(bidID);
        for (abroudBid bid : abroudBid1) {
            switch (bid.getStatus()){
                case "APPROVED":
                    bidID2 = bid.getId();
                case "PAYMENT_SUCCESSFUL":
                    bidID2 = bid.getId();
                case "WAITING":
                    bidID2 = bid.getId();
                default:
                    break;
            }
        }
        return bidID2;
    }
    public AbroudBidDto getFindMyBid(int bidID, int companyID) {

        abroudBid abroudBid2 = studentRepository.bidAndAdvertRequest2Dto(successBidFind(bidID), companyID).get(0);

        AbroudBidDto abroudBidDto = new AbroudBidDto();

        abroudBidDto.setId(abroudBid2.getId());
        abroudBidDto.setBid(abroudBid2.getBid());
        abroudBidDto.setCompanyId(abroudBid2.getCompanyId());
        abroudBidDto.setExpiration(abroudBid2.getExpiration());
        abroudBidDto.setExplanation(abroudBid2.getExplanation());
        abroudBidDto.setStatus(abroudBid2.getStatus());

        return abroudBidDto;
    }
    public List<abroudBid> findbidPaymentSuccessAproved(int abroudBidId){
        return studentRepository.findbidPaymentSuccessAproved(abroudBidId);
    }

}
