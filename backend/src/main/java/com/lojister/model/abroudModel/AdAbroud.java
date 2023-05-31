package com.lojister.model.abroudModel;

import javax.persistence.*;
import java.util.Date;



@Entity
// BU sayfa veri tabanına tablo oluşturmak ve veri kaydetmek için yazıldı.
// create-transport.js'den gelecek değişken adlarına göre oluşturuldu
public class AdAbroud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Hepsi İçin
    private String hsCode; // FTL/Araç,FTL/Container
    private String deliveryType; // FTL/Araç,FTL/Container
    private String explanation; // FTL/Araç,FTL/Container
    private Double goodsPrice; // FTL/Araç,FTL/Container
    private String unCode; // FTL/Araç,FTL/Container
    private String tonnage; // FTL/Araç,FTL/Container
    private String vehicleCount; // FTL/Araç
    private String vehicleTypeIdList; // FTL/Araç
    private String startFullAddress; // FTL/Araç,FTL/Container
    private String startDeliveryMethod; // FTL/Araç,FTL/Container
    private String startDate; // FTL/Araç,FTL/Container
    private String dueDate; // FTL/Araç,FTL/Container
    private String startSelectCountryName; // FTL/Araç,FTL/Container
    private String startSelectCountryCode; // FTL/Araç,FTL/Container
    private String dueSelectCountryName; // FTL/Araç,FTL/Container
    private String dueSelectCountryCode;
    private String dueFullAddress; // FTL/Araç,FTL/Container
    private String cargoTypeIdList; // FTL/Araç,FTL/Container
    private String loadTypeIdList; // FTL/Araç
    private String trailerFeatureName; // FTL/Araç
    private String trailerFloorTypeIdList; // FTL/Araç
    private String trailerTypeIdList; // FTL/Araç
    private String currencyUnitId; // FTL/Container
    private String containerType; // FTL/Container
    private String desi; // Partial/Container
    private String height; // Partial/Container
    private String length; // Partial/Container
    private String packagingTypeId; // Partial/Container
    private String piece; // Partial/Container
    private String width; // Partial/Container
    private String clientAdvertisementType;
    private String vehicleOrContainer;
    private String advertisementStatus; // ilan durumu
    private String client_id;
    private Date dateNow = new Date();

    private String bankID;

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getUnCode() {
        return unCode;
    }

    public void setUnCode(String unCode) {
        this.unCode = unCode;
    }

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public String getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(String vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public String getVehicleTypeIdList() {
        return vehicleTypeIdList;
    }

    public void setVehicleTypeIdList(String vehicleTypeIdList) {
        this.vehicleTypeIdList = vehicleTypeIdList;
    }

    public String getStartFullAddress() {
        return startFullAddress;
    }

    public void setStartFullAddress(String startFullAddress) {
        this.startFullAddress = startFullAddress;
    }

    public String getStartDeliveryMethod() {
        return startDeliveryMethod;
    }

    public void setStartDeliveryMethod(String startDeliveryMethod) {
        this.startDeliveryMethod = startDeliveryMethod;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStartSelectCountryName() {
        return startSelectCountryName;
    }

    public void setStartSelectCountryName(String startSelectCountryName) {
        this.startSelectCountryName = startSelectCountryName;
    }

    public String getStartSelectCountryCode() {
        return startSelectCountryCode;
    }

    public void setStartSelectCountryCode(String startSelectCountryCode) {
        this.startSelectCountryCode = startSelectCountryCode;
    }

    public String getDueSelectCountryName() {
        return dueSelectCountryName;
    }

    public void setDueSelectCountryName(String dueSelectCountryName) {
        this.dueSelectCountryName = dueSelectCountryName;
    }

    public String getDueSelectCountryCode() {
        return dueSelectCountryCode;
    }

    public void setDueSelectCountryCode(String dueSelectCountryCode) {
        this.dueSelectCountryCode = dueSelectCountryCode;
    }

    public String getDueFullAddress() {
        return dueFullAddress;
    }

    public void setDueFullAddress(String dueFullAddress) {
        this.dueFullAddress = dueFullAddress;
    }

    public String getCargoTypeIdList() {
        return cargoTypeIdList;
    }

    public void setCargoTypeIdList(String cargoTypeIdList) {
        this.cargoTypeIdList = cargoTypeIdList;
    }

    public String getLoadTypeIdList() {
        return loadTypeIdList;
    }

    public void setLoadTypeIdList(String loadTypeIdList) {
        this.loadTypeIdList = loadTypeIdList;
    }

    public String getTrailerFeatureName() {
        return trailerFeatureName;
    }

    public void setTrailerFeatureName(String trailerFeatureName) {
        this.trailerFeatureName = trailerFeatureName;
    }

    public String getTrailerFloorTypeIdList() {
        return trailerFloorTypeIdList;
    }

    public void setTrailerFloorTypeIdList(String trailerFloorTypeIdList) {
        this.trailerFloorTypeIdList = trailerFloorTypeIdList;
    }

    public String getTrailerTypeIdList() {
        return trailerTypeIdList;
    }

    public void setTrailerTypeIdList(String trailerTypeIdList) {
        this.trailerTypeIdList = trailerTypeIdList;
    }

    public String getCurrencyUnitId() {
        return currencyUnitId;
    }

    public void setCurrencyUnitId(String currencyUnitId) {
        this.currencyUnitId = currencyUnitId;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getDesi() {
        return desi;
    }

    public void setDesi(String desi) {
        this.desi = desi;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPackagingTypeId() {
        return packagingTypeId;
    }

    public void setPackagingTypeId(String packagingTypeId) {
        this.packagingTypeId = packagingTypeId;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getClientAdvertisementType() {
        return clientAdvertisementType;
    }

    public void setClientAdvertisementType(String clientAdvertisementType) {
        this.clientAdvertisementType = clientAdvertisementType;
    }

    public String getVehicleOrContainer() {
        return vehicleOrContainer;
    }

    public void setVehicleOrContainer(String vehicleOrContainer) {
        this.vehicleOrContainer = vehicleOrContainer;
    }

    public String getAdvertisementStatus() {
        return advertisementStatus;
    }

    public void setAdvertisementStatus(String advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }
}
