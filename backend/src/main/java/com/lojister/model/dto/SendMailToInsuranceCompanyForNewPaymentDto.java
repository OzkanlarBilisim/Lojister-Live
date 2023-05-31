package com.lojister.model.dto;

import com.lojister.model.entity.client.ClientTransportProcess;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendMailToInsuranceCompanyForNewPaymentDto {

    private String insuranceType;
    private String transportCode;
    private Double priceWithVat;
    private String startingAddress;
    private String dueAddress;
    private String volume;
    private String desi;
    private String ldm;
    private Double goodsPrice;
    private String tonnage;
    private String isPorter;
    private String isStacking;
    private String documentNo;
    private String explanation;
    private Double height; // yükseklik
    private Double length; // uzunluk
    private Double width;  // en
    private String clientAdvertisementType;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String commercialTitle;

    public static SendMailToInsuranceCompanyForNewPaymentDto fromClientTransportProcess(ClientTransportProcess clientTransportProcess) {

        SendMailToInsuranceCompanyForNewPaymentDto dto = new SendMailToInsuranceCompanyForNewPaymentDto();
        dto.insuranceType = clientTransportProcess.getInsuranceType().toString();
        dto.transportCode = clientTransportProcess.getTransportCode();
        dto.priceWithVat = clientTransportProcess.getPriceWithVat();
        dto.startingAddress = clientTransportProcess.getAcceptedClientAdvertisementBid().getSummaryAdvertisementData().getStartingAddress();
        dto.dueAddress = clientTransportProcess.getAcceptedClientAdvertisementBid().getSummaryAdvertisementData().getDueAddress();
        dto.volume = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getVolume();
        dto.desi = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDesi();
        dto.ldm = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getLdm();
        dto.goodsPrice = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getGoodsPrice();
        dto.tonnage = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getTonnage();
        dto.isPorter = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getIsPorter() ? "Var" : "Yok";
        dto.isStacking = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getIsStacking() ? "Var" : "Yok";
        dto.documentNo = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getDocumentNo();
        dto.explanation = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getExplanation();
       // dto.height = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getHeight(); // yükseklik
       // dto.length = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getLength(); // uzunluk
      //  dto.width = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getWidth();  // en
        dto.clientAdvertisementType = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClientAdvertisementType().toString();
        dto.firstName = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getFirstName();
        dto.lastName = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getLastName();
        dto.phone = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getPhone();
        dto.email = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getEmail();
        dto.commercialTitle = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getCompany() == null ? "Bireysel Kullanıcı" : clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getCompany().getCommercialTitle();
        return dto;
    }

}
