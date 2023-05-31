import React, { useState,useRef } from 'react'
import { CustomModal } from './CustomModal';
import DetailInfoAbroad from './DetailInfoAbroad';


function AdvertiseAgain({showModel2, unShowModel2, advertDetail, adInfoForm, unShowModel, handleClickNextStep, aracForm, containeroutsideForm, aracpartForm, containeroutsidepartForm}) {
  const handelOk = () =>{
    adInfoForm.transportArea = "OUTSIDE";
    //adInfoForm.transportMethod = "Araç";

    adInfoForm.transportRota = advertDetail.vehicleOrContainer;
    if(advertDetail.clientAdvertisementType === "FTL"){
        adInfoForm.transportType = "FTL/OUTSIDE";
    }
    if(advertDetail.clientAdvertisementType === "PARSİYEL"){
        adInfoForm.transportType = "PARTIAL/OUTSIDE";
    }
    if(advertDetail.vehicleOrContainer === "Deniz Yolu" || advertDetail.vehicleOrContainer === "Hava Yolu"){
        adInfoForm.transportMethod = "Konteyner";
    }
    if(advertDetail.vehicleOrContainer === "Kara Yolu"){
        adInfoForm.transportMethod = "Araç";
    }


    if(adInfoForm.transportType === "FTL/OUTSIDE" && adInfoForm.transportMethod === "Araç"){

        // step 1
        aracForm.startSelectCountryCode = advertDetail.startSelectCountryCode;
        aracForm.startSelectCountryName = advertDetail.startSelectCountryName;
        aracForm.dueSelectCountryCode = advertDetail.dueSelectCountryCode;
        aracForm.dueSelectCountryName = advertDetail.dueSelectCountryName;
        aracForm.vehicleOrContainer = advertDetail.vehicleOrContainer;
        aracForm.startFullAddress = advertDetail.startFullAddress;
        aracForm.dueFullAddress = advertDetail.dueFullAddress;

        // step 2
        aracForm.cargoTypeIdList = advertDetail.cargoTypeIdList;
        if(advertDetail.unCode){
            aracForm.kontrol = true;
        }else{
            aracForm.kontrol = false;
        }
        
        aracForm.startDeliveryMethod = advertDetail.startDeliveryMethod;
        aracForm.packagingTypeId = advertDetail.packagingTypeId;
        aracForm.loadTypeIdList = advertDetail.loadTypeIdList;
        aracForm.hsCode = advertDetail.hsCode;
        aracForm.unCode = advertDetail.unCode;
        aracForm.deliveryType = advertDetail.deliveryType;
        aracForm.tonnage = advertDetail.tonnage;
        aracForm.goodsPrice = advertDetail.goodsPrice;
        aracForm.explanation = advertDetail.explanation;

        // step 3

        aracForm.vehicleTypeIdList = advertDetail.vehicleTypeIdList;
        aracForm.vehicleCount = advertDetail.vehicleCount;
        aracForm.trailerTypeIdList = advertDetail.trailerTypeIdList;
        aracForm.trailerFloorTypeIdList = advertDetail.trailerFloorTypeIdList;
        aracForm.trailerFeatureIdList = advertDetail.trailerFeatureName.split(",");
        aracForm.trailerFeatureName = advertDetail.trailerFeatureName;
    }

    if(adInfoForm.transportType === "FTL/OUTSIDE" && adInfoForm.transportMethod === "Konteyner"){

        // step 1
        containeroutsideForm.startSelectCountryCode = advertDetail.startSelectCountryCode;
        containeroutsideForm.startSelectCountryName = advertDetail.startSelectCountryName;
        containeroutsideForm.dueSelectCountryCode = advertDetail.dueSelectCountryCode;
        containeroutsideForm.dueSelectCountryName = advertDetail.dueSelectCountryName;
        containeroutsideForm.startDeliveryMethod = advertDetail.startDeliveryMethod;
        containeroutsideForm.vehicleOrContainer = advertDetail.vehicleOrContainer;
        containeroutsideForm.startFullAddress = advertDetail.startFullAddress;
        containeroutsideForm.dueFullAddress = advertDetail.dueFullAddress;

        // step 2
        containeroutsideForm.cargoTypeIdList = advertDetail.cargoTypeIdList;
        if(advertDetail.unCode){
            containeroutsideForm.kontrol = true;
        }else{
            containeroutsideForm.kontrol = false;
        }
        containeroutsideForm.containerType = advertDetail.containerType;
        containeroutsideForm.loadTypeIdList = advertDetail.loadTypeIdList;
        containeroutsideForm.hsCode = advertDetail.hsCode;
        containeroutsideForm.unCode = advertDetail.unCode;
        containeroutsideForm.deliveryType = advertDetail.deliveryType;
        containeroutsideForm.tonnage = advertDetail.tonnage;
        containeroutsideForm.goodsPrice = advertDetail.goodsPrice;
        containeroutsideForm.explanation = advertDetail.explanation;

        console.log(containeroutsideForm);
    }
    if(adInfoForm.transportType === "PARTIAL/OUTSIDE" && adInfoForm.transportMethod === "Araç"){

        // step 1
        aracpartForm.startSelectCountryCode = advertDetail.startSelectCountryCode;
        aracpartForm.startSelectCountryName = advertDetail.startSelectCountryName;
        aracpartForm.dueSelectCountryCode = advertDetail.dueSelectCountryCode;
        aracpartForm.dueSelectCountryName = advertDetail.dueSelectCountryName;
        aracpartForm.startDeliveryMethod = advertDetail.startDeliveryMethod;
        aracpartForm.vehicleOrContainer = advertDetail.vehicleOrContainer;
        aracpartForm.startFullAddress = advertDetail.startFullAddress;
        aracpartForm.dueFullAddress = advertDetail.dueFullAddress;


        // step 2
        aracpartForm.cargoTypeIdList = advertDetail.cargoTypeIdList;
        if(advertDetail.unCode){
            aracpartForm.kontrol = true;
        }else{
            aracpartForm.kontrol = false;
        }
        aracpartForm.containerType = advertDetail.containerType;
        aracpartForm.loadTypeIdList = advertDetail.loadTypeIdList;
        aracpartForm.hsCode = advertDetail.hsCode;
        aracpartForm.unCode = advertDetail.unCode;
        aracpartForm.deliveryType = advertDetail.deliveryType;
        aracpartForm.tonnage = advertDetail.tonnage;
        aracpartForm.goodsPrice = advertDetail.goodsPrice;
        aracpartForm.explanation = advertDetail.explanation;
    }
    if(adInfoForm.transportType === "PARTIAL/OUTSIDE" && adInfoForm.transportMethod === "Konteyner"){

        // step 1
        containeroutsidepartForm.startSelectCountryCode = advertDetail.startSelectCountryCode;
        containeroutsidepartForm.startSelectCountryName = advertDetail.startSelectCountryName;
        containeroutsidepartForm.dueSelectCountryCode = advertDetail.dueSelectCountryCode;
        containeroutsidepartForm.dueSelectCountryName = advertDetail.dueSelectCountryName;
        containeroutsidepartForm.startDeliveryMethod = advertDetail.startDeliveryMethod;
        containeroutsidepartForm.vehicleOrContainer = advertDetail.vehicleOrContainer;
        containeroutsidepartForm.startFullAddress = advertDetail.startFullAddress;
        containeroutsidepartForm.dueFullAddress = advertDetail.dueFullAddress;

        // step 2
        containeroutsidepartForm.cargoTypeIdList = advertDetail.cargoTypeIdList;
        if(advertDetail.unCode){
            containeroutsidepartForm.kontrol = true;
        }else{
            containeroutsidepartForm.kontrol = false;
        }
        containeroutsidepartForm.packagingTypeId = advertDetail.packagingTypeId;
        containeroutsidepartForm.loadTypeIdList = advertDetail.loadTypeIdList;
        containeroutsidepartForm.hsCode = advertDetail.hsCode;
        containeroutsidepartForm.unCode = advertDetail.unCode;
        containeroutsidepartForm.deliveryType = advertDetail.deliveryType;
        containeroutsidepartForm.tonnage = advertDetail.tonnage;
        containeroutsidepartForm.goodsPrice = advertDetail.goodsPrice;
        containeroutsidepartForm.explanation = advertDetail.explanation;
        containeroutsidepartForm.width = advertDetail.width;
        containeroutsidepartForm.length = advertDetail.length;
        containeroutsidepartForm.height = advertDetail.height;
        containeroutsidepartForm.piece = advertDetail.piece;
    }



    unShowModel2();
    unShowModel();
    handleClickNextStep();
    console.log(adInfoForm);
    console.log(advertDetail);
  }
  const content = () =>{
    return(
        <DetailInfoAbroad
            transport={advertDetail}
            transportId={advertDetail.id}
            AdvertiseAgain={true}
        />        
    )
  }  
    
  return (
    <CustomModal
        visible={showModel2}
        onCancel={unShowModel2}
        onOk={handelOk}
        content={content()}
        width="60%"
    />
  )
}
export default AdvertiseAgain;