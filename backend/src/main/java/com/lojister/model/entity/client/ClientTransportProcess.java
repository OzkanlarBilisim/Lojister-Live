package com.lojister.model.entity.client;

import com.lojister.model.enums.InsuranceType;
import com.lojister.model.entity.TransportProcess;
import com.lojister.model.entity.Vehicle;
import com.lojister.business.concretes.AdvertisementBidLogic;
import com.lojister.service.api.TcmbApiService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@DiscriminatorValue("ClientTransportProcess")
public class ClientTransportProcess extends TransportProcess {

    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

    private  Double euro;
    private  Double dolar;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private ClientAdvertisementBid acceptedClientAdvertisementBid;

    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean isRating = false;


    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean isExistInsuredFile = false;


    public ClientTransportProcess insuredFileStatusChangeTrue() {
        this.isExistInsuredFile = true;
        return this;
    }


    public void calculatePriceForInsuranceByRatio(Double ratio) {
        this.setPriceWithVat(AdvertisementBidLogic.round(this.getPriceWithVat() + this.getAcceptedClientAdvertisementBid().getClientAdvertisement().getGoodsPrice() * ratio, 2));
    }

    public String getPayAmount() {
        String currencyUnit = getAcceptedClientAdvertisementBid().getClientAdvertisement().getCurrencyUnit().getCurrencyAbbreviation();
        Double goodPrice = getAcceptedClientAdvertisementBid().getClientAdvertisement().getGoodsPrice();

        double insurance = 0.0;
        if(goodPrice != null){
            if(currencyUnit.equals("TL")){
                goodPrice = goodPrice * 1;
            }if(currencyUnit.equals("USD")){
                goodPrice = goodPrice * getDolar();
            }if(currencyUnit.equals("EUR")){
                goodPrice = goodPrice * getEuro();
            }

            insurance = getInsuranceType() == InsuranceType.NARROW ? goodPrice > 32000 ? (goodPrice * 0.001155) : 35 : 0;
            insurance = Math.ceil(insurance);
        }

        double amount =  (getPrice() * 1.18) + insurance;
        String amountString = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();

        return amountString;
    }


}
