package com.lojister.service.PaymentService;

import com.lojister.business.abstracts.ClientAdvertisementBidService;
import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.business.abstracts.ClientService;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.CurrentPayDataDto;
import com.lojister.model.dto.CurrentPaymentRequestsDto;
import com.lojister.model.entity.payment.CurrentPayment;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.entity.client.ClientAdvertisementBid;
import com.lojister.model.entity.payment.Insurance;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.repository.payment.CurrentPaymentRepository;
import com.lojister.service.abroudService.AbroudBidService;
import com.lojister.service.abroudService.AbroudService;
import com.lojister.service.api.TcmbApiService;
import com.lojister.service.insurance.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentPaymentServiceImpl implements CurrentPaymentService{
    @Autowired
    private SecurityContextUtil securityContextUtil;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientAdvertisementService clientAdvertisementService;
    @Autowired
    private ClientAdvertisementBidService clientAdvertisementBidService;
    @Autowired
    private TcmbApiService tcmbApiService;
    @Autowired
    private CurrentPaymentRepository currentPaymentRepository;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private AbroudService abroudService;
    @Autowired
    private AbroudBidService abroudBidService;


    public String pay(CurrentPayDataDto data) throws Exception {
        Client user         = securityContextUtil.getCurrentClient();

        Double currentTL    = user.getTl();
        Double currentUsd   = user.getUsd();
        Double currentEuro  = user.getEuro();


        Double usd = Double.valueOf(tcmbApiService.getUsd());
        Double euro = Double.valueOf(tcmbApiService.getEuro());

        double amount =  0d;


        AdAbroud adAbroud                           = null;
        ClientAdvertisement clientAdvertisament     = null;
        String bidPrice                             = null;
        String bidCurrencySymbol                    = null;
        Insurance insurance                         = null;
        Double rate                                 = null;

        if(!user.getCurrent()){
            throw new Exception("Cari ödeme yapma yetkiniz yok. Lütfen bizimle iletişime geçin.");
        }

        if(data.getAbroudOrDomastic().equals("Domastic")){
            clientAdvertisament = clientAdvertisementService.findDataById(Long.valueOf(data.getTransportId()));
            ClientAdvertisementBid bid = clientAdvertisementBidService.findAllByClientAdvertisementIdRepo(Long.valueOf(data.getTransportId())).get(0);


            Double goodPrice = clientAdvertisament.getGoodsPrice();

            insurance = insuranceService.insurance(goodPrice, "TR", "TR", clientAdvertisament.getCurrencyUnit().getCurrencySymbol(), data.getInsuranceType(), "₺");
            bidPrice = String.valueOf(bid.getBidWithVat());


            amount =  bid.getBidWithVat() + insurance.getInsurancePrice();

            if(currentTL < amount){
                throw new Exception("Yeterli bakiyeniz yok");
            }

            double price = Math.round((currentTL - amount) * 100.0) / 100.0; // Virgülden sonra 2 basamak için yuvarlama

            clientAdvertisament.setAdvertisementProcessStatus(AdvertisementProcessStatus.PAYMENT_SUCCESSFUL);
            clientAdvertisementService.saveRepo(clientAdvertisament);
            user.setTl(price);
            bidCurrencySymbol = "₺";
        }

        if (data.getAbroudOrDomastic().equals("Abroud")) {
            abroudBid abid = abroudBidService.findbidPaymentSuccessAproved(Integer.valueOf(data.getTransportId())).get(0);
            adAbroud = abid.getAdAbroud();

            Double goodPrice = adAbroud.getGoodsPrice();
            bidPrice         = abid.getBid();

            insurance = insuranceService.insurance(goodPrice,adAbroud.getStartSelectCountryCode(),adAbroud.getDueSelectCountryCode(),adAbroud.getCurrencyUnitId(), data.getInsuranceType(), data.getInsuranceCurency());



            bidCurrencySymbol = "$";





            if (bidCurrencySymbol.equals("$")) {
                amount = Double.parseDouble(abid.getBid());
                rate = usd;

                if (currentUsd < amount) {
                    throw new Exception("Ödeme yapmak için yeterli dolar bakiyeniz yok. Bizimle iletişime geçin!");
                }


                double price = Math.round((currentUsd - amount) * 100.0) / 100.0;
                user.setUsd(price);
            }
            if (bidCurrencySymbol.equals("€")) {
                amount = Double.parseDouble(abid.getBid());
                rate = euro;


                if (currentEuro < amount) {
                    throw new Exception("Ödeme yapmak için yeterli euro bakiyeniz yok. Bizimle iletişime geçin!");
                }
                double price = Math.round((currentEuro - amount) * 100.0) / 100.0;
                user.setEuro(price);
            }

            if(data.getInsuranceCurency().equals("₺")){
                if (user.getTl() < insurance.getInsurancePrice()) {
                    throw new Exception("Ödeme yapmak için yeterli TL bakiyeniz yok. Bizimle iletişime geçin!");
                }
                double price = Math.round((user.getTl() - insurance.getInsurancePrice()) * 100.0) / 100.0;
                user.setTl(price);
            }
            if(data.getInsuranceCurency().equals("$")){
                if (user.getUsd() < insurance.getInsurancePrice()) {
                    throw new Exception("Ödeme yapmak için yeterli Dolar bakiyeniz yok. Bizimle iletişime geçin!");
                }
                double price = Math.round((user.getUsd() - insurance.getInsurancePrice()) * 100.0) / 100.0;
                user.setUsd(price);
            }
            if(data.getInsuranceCurency().equals("€")){
                if (user.getEuro() < insurance.getInsurancePrice()) {
                    throw new Exception("Ödeme yapmak için yeterli Euro bakiyeniz yok. Bizimle iletişime geçin!");
                }
                double price = Math.round((user.getEuro() - insurance.getInsurancePrice()) * 100.0) / 100.0;
                user.setUsd(price);
            }

            abroudService.advertStatusStep(Integer.parseInt(data.getTransportId()));

        }

       if(!insurance.getInsuranceType().equals("UNINSURED")){
            insurance = insuranceService.save(insurance);
        }

        clientService.saveRepo(user);

        CurrentPayment currentPayment = new CurrentPayment();
        currentPayment.setAbroudOrDomastic(data.getAbroudOrDomastic());
        currentPayment.setAdAbroud(adAbroud);
        currentPayment.setClientAdvertisement(clientAdvertisament);


        currentPayment.setTotalPrice(Double.valueOf(bidPrice) + insurance.getInsurancePrice());
        currentPayment.setAdvertPrice(bidPrice);
        currentPayment.setAdvertCurrencySymbol(bidCurrencySymbol);
        currentPayment.setRate(rate);

        if(!insurance.getInsuranceType().equals("UNINSURED")){
            currentPayment.setInsurance(insurance);
        }else {
            currentPayment.setInsurance(null);
        }
        currentPayment.setUser(user);

        currentPaymentRepository.save(currentPayment);
        insuranceService.sendMail(data.getTransportId(), insurance);

        return "Ödeme alındı bakiyeden düşüldü";
    }
    public List<CurrentPaymentRequestsDto> CurrentRequests(){


        Long userId         = securityContextUtil.getCurrentUserId();
        List<CurrentPayment> currentPaymentList=currentPaymentRepository.findClient(userId);


        List<CurrentPaymentRequestsDto> currentPaymentRequestsDtoList = new ArrayList<>();
        for (CurrentPayment currentPayment: currentPaymentList) {
            CurrentPaymentRequestsDto currentPaymentRequestsDto = new CurrentPaymentRequestsDto();
            if (currentPayment.getAbroudOrDomastic().equals("Domastic")){
                ClientAdvertisementBid bid = clientAdvertisementBidService.findAllByClientAdvertisementIdRepo(currentPayment.getClientAdvertisement().getId()).get(0);

                if(currentPayment.getInsurance()== null){
                    currentPaymentRequestsDto.setInsurancePrice("Yapılmadı");
                }else {
                    currentPaymentRequestsDto.setInsurancePrice(String.valueOf(currentPayment.getInsurance().getInsurancePrice()+currentPayment.getInsurance().getInsuranceCurency()));
                }

                currentPaymentRequestsDto.setStartingCountry(currentPayment.getClientAdvertisement().getStartingAddress().getProvince());
                currentPaymentRequestsDto.setDueCountry(currentPayment.getClientAdvertisement().getDueAddress().getProvince());
                currentPaymentRequestsDto.setDateofShipment(currentPayment.getClientAdvertisement().getAdStartingDate().toString());

                currentPaymentRequestsDto.setBidPrice(String.valueOf(bid.getBidWithVat()));
                currentPaymentRequestsDto.setTransportID(currentPayment.getClientAdvertisement().getId());
                currentPaymentRequestsDto.setAbroadOrDomestic("Domestic");
            } else if (currentPayment.getAbroudOrDomastic().equals("Abroud")) {
                abroudBid abid = abroudBidService.findbidPaymentSuccessAproved(currentPayment.getAdAbroud().getId()).get(0);


                if(currentPayment.getInsurance()== null){
                    currentPaymentRequestsDto.setInsurancePrice("Yapılmadı");
                }else {
                    currentPaymentRequestsDto.setInsurancePrice(String.valueOf(currentPayment.getInsurance().getInsurancePrice()+currentPayment.getInsurance().getInsuranceCurency()));
                }


                currentPaymentRequestsDto.setStartingCountry(currentPayment.getAdAbroud().getStartSelectCountryName());
                currentPaymentRequestsDto.setDueCountry(currentPayment.getAdAbroud().getDueSelectCountryName());
                currentPaymentRequestsDto.setDateofShipment(currentPayment.getAdAbroud().getStartDate());
                currentPaymentRequestsDto.setBidPrice(abid.getBid());
                currentPaymentRequestsDto.setTransportID(Long.valueOf(currentPayment.getAdAbroud().getId()));
                currentPaymentRequestsDto.setAbroadOrDomestic("Abroad");
            }

            currentPaymentRequestsDtoList.add(currentPaymentRequestsDto);
        }

        return currentPaymentRequestsDtoList;
    }
}
