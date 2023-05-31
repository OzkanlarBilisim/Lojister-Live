package com.lojister.model.entity.client;

import com.lojister.core.util.LocalDateTimeParseUtil;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidSaveDto;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.BidStatus;
import com.lojister.model.entity.SummaryAdvertisementData;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientAdvertisementBid extends AbstractTimestampEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientAdvertisement_id")
    private ClientAdvertisement clientAdvertisement;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driverBidder;

    //İlan silinirse tekliflerin özetindeki bilgiler buradan çekilecek.
    @Embedded
    private SummaryAdvertisementData summaryAdvertisementData;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;

    private Double bid;

    private Double bidWithVat;

    private String explanation;

    private LocalDateTime expiration;


    @PostLoad
    private void getEnabled() {
        if(getBidStatus()==BidStatus.WAITING){
            if (Optional.ofNullable(expiration).isPresent()) {
               if(LocalDateTime.now().isAfter(expiration)){
                   setBidStatus(BidStatus.TIMEOUT);
               }

            }
        }
    }

    public static ClientAdvertisementBid createClientAdvertisementBid(ClientAdvertisement clientAdvertisement, SummaryAdvertisementData summaryAdvertisementData, Driver currentDriver, ClientAdvertisementBidSaveDto clientAdvertisementBidSaveDto) {
        ClientAdvertisementBid clientAdvertisementBid = new ClientAdvertisementBid();
        clientAdvertisement.setAdvertisementProcessStatus(AdvertisementProcessStatus.BID_GIVEN);
        clientAdvertisementBid.setClientAdvertisement(clientAdvertisement);
        clientAdvertisementBid.setSummaryAdvertisementData(summaryAdvertisementData);
        clientAdvertisementBid.setDriverBidder(currentDriver);
        clientAdvertisementBid.setBidStatus(BidStatus.WAITING);
        clientAdvertisementBid.setBid(clientAdvertisementBidSaveDto.getBid());
        clientAdvertisementBid.setExpiration(clientAdvertisementBidSaveDto.getExpiration());
        clientAdvertisementBid.setExplanation(clientAdvertisementBidSaveDto.getExplanation());
        clientAdvertisementBid.setBidWithVat(calculateVAT(clientAdvertisementBidSaveDto.getBid(), 2,clientAdvertisement.getRegionAdvertisementType()));
        clientAdvertisementBid.setBid(clientAdvertisementBid.getBid());

        return clientAdvertisementBid;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double calculateVAT(double value, int places,RegionAdvertisementType regionAdvertisementType) {
        switch (regionAdvertisementType)
        {
            case INTERNATIONAL:
                return round(value , places);

            case DOMESTIC:
               return round(value * 1.18D, places);

            default:
                return round(value * 1.18D, places);

        }
    }

}
