import React from 'react'
import { Button, Steps } from 'antd';
import { useSelector } from 'react-redux';
import { ParsedDriverAbroud, ParsedClientAbroud } from "./ParsedValuesAbroad"
import SuspenseFallbackInline from './SuspenseFallbackInline';
import { useTranslation } from 'react-i18next';

const DetailSteps = ({ data, transportDocuments, status, isLoading }) => {
    const { t, i18n } = useTranslation("common");/*  */

    const currentUser = useSelector((state) => state.user);
    const { role } = currentUser;
    const parsed = role === "ROLE_DRIVER" ? ParsedDriverAbroud : ParsedClientAbroud;
    const { Step } = Steps;

    const advertisementStatus = (status) =>{
        if(role === "ROLE_DRIVER"){
            switch(status){
                case "WAITING":
                    return t("Teklifiniz Onay Bekliyor");
                case "APPROVED":
                    return t("Ödeme Bekleniyor");
                case "DENIED":
                    return t("Teklifiniz Reddedilmiştir");
                case "PAYMENT_SUCCESSFUL":
                    return t("Ödeme Alındı");
                case "WAITING_FOR_TRANSPORT":
                    return t("Yükün Teslim Almanız Gerekiyor");
                case "TRANSPORT":
                    return t("Tasıma Sürecinde");
                case "COMPLETED":
                    return t("Yük Teslim Edildi");
                default:
                    return t("Durum bilinmiyor lütfen bizimle iletişime geçin.");
            }
        }else{
            switch(status){
                case "WAITING":
                    return t("Teklif Bekliyor");
                case "APPROVED":
                    return t("Ödemeyapmanız Gerekiyor");
                case "PAYMENT_SUCCESSFUL":
                    return t("Ödeme Alındı");
                case "WAITING_FOR_TRANSPORT":
                    return t("Yükün Teslim Alınması Bekleniyor");
                case "TRANSPORT":
                    return t("Tasıma Sürecinde");
                case "COMPLETED":
                    return t("Yük Teslim Edildi");
                default:
                    return t("Durum bilinmiyor lütfen bizimle iletişime geçin.");
            }
        }    
    }
    
    

    return (
        isLoading ?
            <SuspenseFallbackInline /> :
            <Steps responsive={false} className='process' current={parsed({ transportDocuments, status })?.step} progressDot >
                <Step title={<div className={parsed({ transportDocuments, status })?.step === 0 ? 'provinceActive' : 'provincePassive'}>{data?.startFullAddress}</div>} description={parsed({ transportDocuments, status })?.step === 0 ? <div>{advertisementStatus(data?.advertisementStatus)} </div> : <></>} />
                <Step title={<div className={parsed({ transportDocuments, status })?.step === 1 ? 'provinceActive' : 'provincePassive'}>{t("Yolda")}</div>} description={parsed({ transportDocuments, status })?.step === 1 ? <div>{t(parsed({ transportDocuments, status })?.title)} </div> : <></>} />
                <Step title={<div className={parsed({ transportDocuments, status })?.step === 0 ? 'provinceActive' : 'provincePassive'}>{role === "ROLE_DRIVER" ? data?.dueFullAddress : data?.dueFullAddress}</div>}  />
            </Steps>
    )
}

export default React.memo(DetailSteps);