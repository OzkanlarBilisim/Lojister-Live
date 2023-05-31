package com.lojister.model.dto.abroudDto;


import lombok.Data;

@Data
public class DriverBidListResult {
    private int advertisementId;
    private String bidStatus;
    private String transportType;
    private String loadingDate;
    private String loadingZone;
    private String destinationZone;

    public int getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(int advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getLoadingZone() {
        return loadingZone;
    }

    public void setLoadingZone(String loadingZone) {
        this.loadingZone = loadingZone;
    }

    public String getDestinationZone() {
        return destinationZone;
    }

    public void setDestinationZone(String destinationZone) {
        this.destinationZone = destinationZone;
    }
}
