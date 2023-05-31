package com.lojister.model.dto.clienttransportprocess;

import com.lojister.model.dto.VehicleDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidNIClientAdvertisementDto;
import com.lojister.model.entity.SummaryDriverData;
import com.lojister.model.entity.SummaryVehicleData;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.model.enums.TransportProcessType;
import lombok.Data;

import javax.persistence.Embedded;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ClientTransportProcessForClientAdvertisementDto {

    private Long id;

    private TransportProcessStatus transportProcessStatus;

    private TransportProcessType transportProcessType;

    private String transportCode;

    private ClientAdvertisementBidNIClientAdvertisementDto acceptedClientAdvertisementBid;

    private VehicleDto vehicle;

    //Aracın içindeki driver silinir veya değiştirilirse bilgilerini tutmak için.
    @Embedded
    private SummaryDriverData summaryDriverData;

    //Araç silinir veya değiştirilirse bilgilerini tutmak için.
    @Embedded
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


}
