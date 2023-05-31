import React from 'react'
import { Steps } from 'antd';
import { useSelector } from 'react-redux';
import { ParsedDriver, ParsedClient } from "./ParsedValues"
import SuspenseFallbackInline from './SuspenseFallbackInline';
import { useTranslation } from 'react-i18next';

const DetailSteps = ({ data, transportDocuments, status, isLoading }) => {
    const { t, i18n } = useTranslation("common");/*  */

    const currentUser = useSelector((state) => state.user);
    const { role } = currentUser;
    const parsed = role === "ROLE_DRIVER" ? ParsedDriver : role === "ROLE_CLIENT" ? ParsedClient : null;
    const { Step } = Steps;

    return (
        isLoading ?
            <SuspenseFallbackInline /> :
            <Steps responsive={false} className='process' current={parsed({ transportDocuments, status })?.step} progressDot >
                <Step title={<div className={parsed({ transportDocuments, status })?.step === 0 ? 'provinceActive' : 'provincePassive'}>{role === "ROLE_DRIVER" ? data?.clientAdvertisement?.startingAddress?.district ? data?.clientAdvertisement?.startingAddress?.province + ", " + data?.clientAdvertisement?.startingAddress?.district : data?.clientAdvertisement?.startingAddress?.province + ", " + data?.clientAdvertisement?.startingAddress?.country : data?.startingAddress?.district ? data?.startingAddress?.province + ", " + data?.startingAddress?.district : data?.startingAddress?.province + ", " + data?.startingAddress?.country}</div>} description={parsed({ transportDocuments, status })?.step === 0 ? <div>{t(parsed({ transportDocuments, status })?.title)} </div> : <></>} />
                <Step title={<div className={parsed({ transportDocuments, status })?.step === 1 ? 'provinceActive' : 'provincePassive'}>{t("Yolda")}</div>} description={parsed({ transportDocuments, status })?.step === 1 ? <div>{t(parsed({ transportDocuments, status })?.title)} </div> : <></>} />
                <Step title={<div className={parsed({ transportDocuments, status })?.step === 2 ? 'provinceActive' : 'provincePassive'}>{role === "ROLE_DRIVER" ? data?.clientAdvertisement?.dueAddress?.district ? data?.clientAdvertisement?.dueAddress?.province + ", " + data?.clientAdvertisement?.dueAddress?.district : data?.clientAdvertisement?.dueAddress?.province + ", " + data?.clientAdvertisement?.dueAddress?.country : data?.dueAddress?.district ? data?.dueAddress?.province + ", " + data?.dueAddress?.district : data?.dueAddress?.province + ", " + data?.dueAddress?.country}</div>}
                    description={parsed({ transportDocuments, status })?.step === 2 ? <div>{t(parsed({ transportDocuments, status })?.title)} </div> : <></>} />
            </Steps >
    )

}

export default React.memo(DetailSteps);