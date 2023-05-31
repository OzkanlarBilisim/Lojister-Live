package com.lojister.model.dto.clienttransportprocess;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lojister.model.dto.VehicleDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.entity.SummaryDriverData;
import com.lojister.model.entity.SummaryVehicleData;
import com.lojister.model.enums.InsuranceType;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.enums.TransportProcessType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Data
public class ClientTransportProcessDto {

    private Long id;

    private TransportProcessStatus transportProcessStatus;

    private TransportProcessType transportProcessType;

    private String transportCode;

    private ClientAdvertisementBidDto acceptedClientAdvertisementBid;

    private VehicleDto vehicle;

    private SummaryDriverData summaryDriverData;

    private SummaryVehicleData summaryVehicleData;

    private String driverExplanation;

    private String clientExplanation;

    private Double price;

    private Double priceWithVat;

    private String receiveQrCode;

    private String deliverQrCode;

    private LocalDate admissionDate;

    private LocalDate deliveryDate;

    private LocalTime admissionTime ;

    private LocalTime deliveryTime ;

    private Boolean isRating;

    private Boolean isExistInsuredFile;

    private InsuranceType insuranceType;


    @JsonProperty("admissionLocalDateTime")
    private String getAdmissionLocalDateTime(){
        if(Optional.ofNullable(admissionDate).isPresent()){
         if (Optional.ofNullable(admissionTime).isPresent()) {
           return LocalDateTime.of(admissionDate,admissionTime).toString();
         }
         else {
             return LocalDateTime.of(admissionDate,LocalTime.of(0,0,0)).toString();
         }
        }
        return "";
    }
    @JsonProperty("deliveryLocalDateTime")
    private String getDeliveryLocalDateTime(){
        if(Optional.ofNullable(deliveryDate).isPresent()){
            if (Optional.ofNullable(deliveryTime).isPresent()) {
                return LocalDateTime.of(deliveryDate,deliveryTime).toString();
            }
            else {
                return LocalDateTime.of(deliveryDate,LocalTime.of(0,0,0)).toString();
            }
        }
        return "";
    }

}
