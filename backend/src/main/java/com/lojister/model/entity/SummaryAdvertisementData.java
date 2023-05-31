package com.lojister.model.entity;

import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class SummaryAdvertisementData implements Serializable {

    private String explanation;
    private String startingAddress;
    private String dueAddress;
    private String adStartingDate;
    private String adDueDate;
    private String adStartingTime;
    private String adDueTime;
    private String advertisementType;
    private String clientAdvertisementCode;
    private String regionAdvertisementType;

    public static SummaryAdvertisementData createSummaryAdvertisementData(ClientAdvertisement clientAdvertisement) {

        SummaryAdvertisementData summaryAdvertisementData = new SummaryAdvertisementData();

        summaryAdvertisementData.setExplanation(clientAdvertisement.getExplanation());
        summaryAdvertisementData.setStartingAddress(clientAdvertisement.getStartingAddress().getProvince() + ", " +
                clientAdvertisement.getStartingAddress().getDistrict());
        summaryAdvertisementData.setDueAddress(clientAdvertisement.getDueAddress().getProvince() + ", " +
                clientAdvertisement.getDueAddress().getDistrict());
        summaryAdvertisementData.setAdStartingDate(clientAdvertisement.getAdStartingDate().toString());
        summaryAdvertisementData.setAdDueDate(clientAdvertisement.getAdDueDate().toString());
        summaryAdvertisementData.setAdStartingTime(clientAdvertisement.getAdStartingTime().toString());
        summaryAdvertisementData.setAdDueTime(clientAdvertisement.getAdDueTime().toString());
        summaryAdvertisementData.setAdvertisementType(clientAdvertisement.getClientAdvertisementType().toString());
        summaryAdvertisementData.setClientAdvertisementCode(clientAdvertisement.getClientAdvertisementCode());
        summaryAdvertisementData.setRegionAdvertisementType(clientAdvertisement.getRegionAdvertisementType().toString());
        return summaryAdvertisementData;
    }

}
