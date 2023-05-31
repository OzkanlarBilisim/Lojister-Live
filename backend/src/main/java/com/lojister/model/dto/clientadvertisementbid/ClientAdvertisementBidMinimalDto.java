package com.lojister.model.dto.clientadvertisementbid;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.entity.SummaryAdvertisementData;
import com.lojister.model.enums.AdvertisementProcessStatus;
import com.lojister.model.enums.BidStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClientAdvertisementBidMinimalDto extends BaseDto {
    private SummaryAdvertisementData summaryAdvertisementData;
    private BidStatus bidStatus;
    private Double bid;
    private Double bidWithVat;
    private LocalDateTime expiration;
    private String explanation;
    private AdvertisementProcessStatus advertisementProcessStatus;
    private Boolean enabled;


/*    @JsonProperty("enabled")
    private Boolean getEnabled(){
        if(bidStatus==BidStatus.DENIED){
            return false;
        }
        if (Optional.ofNullable(expiration).isPresent()) {
            return LocalDateTime.now().isBefore(expiration);
        }
        return false;
    }*/
}
