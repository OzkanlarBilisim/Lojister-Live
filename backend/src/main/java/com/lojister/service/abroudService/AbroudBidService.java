package com.lojister.service.abroudService;

import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.abroudDto.AbroudBidDto;
import com.lojister.model.dto.abroudDto.AbroudBidRatingDto;
import com.lojister.model.dto.abroudDto.BidAndAdvertRequestDto;
import com.lojister.model.dto.abroudDto.DriverBidList;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AbroudBidService {

    public abroudBid saveStudent(abroudBid student);
    public List<abroudBid> findbidAndabraud(int id);
    public List<DriverBidList> driverBidList(int driverID);
    public BidAndAdvertRequestDto bidAndAdvertRequestDto(int bidID);
    public List<abroudBid> findClientAbroud(int companyId, int adwertisamentId);
    public List<AbroudBidRatingDto> getAllAdwertismantId(int adwertisamentId);
    public AbroudBidDto getFindMyBid(int bidID, int companyID);

    public List<abroudBid> findbidPaymentSuccessAproved(int abroudBidId);


}
