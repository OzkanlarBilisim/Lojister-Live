import { Divider, Modal, Steps } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { createFtlAdvertisementRequest } from '../../api/controllers/client-advertisiment-ftl-controller';
import { createFtlOutsideAdvertisementRequest } from '../../api/controllers/client-advertisment-ftl-outide-controller';
import { createPartialAdvertisementRequest } from '../../api/controllers/client-advertisiment-partial-controller';

import AdFirstStep from '../../components/createTransportSteps/AdFirstStep';
import AdLastStep from '../../components/createTransportSteps/AdLastStep';

import FTLStep1In from '../../components/createTransportSteps/FTLSteps/INSIDE/FTLStep1';
import FTLStep2In from '../../components/createTransportSteps/FTLSteps/INSIDE/FTLStep2';
import FTLStep3In from '../../components/createTransportSteps/FTLSteps/INSIDE/FTLStep3';
import FTLStep4 from '../../components/createTransportSteps/FTLSteps/INSIDE/FTLStep4';
import FTLStep5In from '../../components/createTransportSteps/FTLSteps/INSIDE/FTLStep5';

import AracStep1 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/AracStep1';
import AracStep2 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/AracStep2';
import AracStep3 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/AracStep3';
import AracStep4 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/AracStep4';

import AracStepPart1 from '../../components/createTransportSteps/PartialSteps/OUTSIDE/AracSteps/AracStepPart1';
import AracStepPart2 from '../../components/createTransportSteps/PartialSteps/OUTSIDE/AracSteps/AracStepPart2';
import AracStepPart3 from '../../components/createTransportSteps/PartialSteps/OUTSIDE/AracSteps/AracStepPart3';

import ContainerOutsideStepPart1 from '../../components/createTransportSteps/PartialSteps/OUTSIDE/ContainerSteps/ContainerOutsideStepPart1';
import ContainerOutsideStepPart2 from '../../components/createTransportSteps/PartialSteps/OUTSIDE/ContainerSteps/ContainerOutsideStepPart2';
import ContainerOutsideStepPart3 from '../../components/createTransportSteps/PartialSteps/OUTSIDE/ContainerSteps/ContainerOutsideStepPart3';

import ContainerOutsideStep1 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/ContainerStepsOutside/ContainerOutsideStep1';
import ContainerOutsideStep2 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/ContainerStepsOutside/ContainerOutsideStep2';
import ContainerOutsideStep3 from '../../components/createTransportSteps/FTLSteps/OUTSIDE/ContainerStepsOutside/ContainerOutsideStep3';

import ContainerStep1 from '../../components/createTransportSteps/ContainerSteps/ContainerStep1';
import ContainerStep2 from '../../components/createTransportSteps/ContainerSteps/ContainerStep2';
import ContainerStep3 from '../../components/createTransportSteps/ContainerSteps/ContainerStep3';
import ContainerStep4 from '../../components/createTransportSteps/ContainerSteps/ContainerStep4';
import ContainerStep5 from '../../components/createTransportSteps/ContainerSteps/ContainerStep5';

import PartialStep1In from '../../components/createTransportSteps/PartialSteps/INSIDE/PartialStep1';
import PartialStep1Out from '../../components/createTransportSteps/PartialSteps/OUTSIDE/PartialStep1';
import PartialStep2In from '../../components/createTransportSteps/PartialSteps/INSIDE/PartialStep2';
import PartialStep2Out from '../../components/createTransportSteps/PartialSteps/OUTSIDE/PartialStep2';
import PartialStep3In from '../../components/createTransportSteps/PartialSteps/INSIDE/PartialStep3';
import PartialStep3Out from '../../components/createTransportSteps/PartialSteps/OUTSIDE/PartialStep3';
import PartialStep4 from '../../components/createTransportSteps/PartialSteps/INSIDE/PartialStep4';
import PartialStep5In from '../../components/createTransportSteps/PartialSteps/INSIDE/PartialStep5';
import PartialStep5Out from '../../components/createTransportSteps/PartialSteps/OUTSIDE/PartialStep5';

import { setSavedTransport } from '../../redux/actions/transportActions';
import { createContainerAdvertisementRequest } from '../../api/controllers/client-advertisiment-container-controller';
import RequestError from '../../components/RequestError';
import SupportBanner from '../../components/SupportBanner';
import { useTranslation } from 'react-i18next';
import { BASE_URL_FRONTEND } from '../../api/ApiProvider';

const CreateAd = () => {
    const { t } = useTranslation("common");
    document.title = 'Lojister - ' + t("İlan Oluştur")

    const [isSubmitting, setIsSubmitting] = useState(false)
    const savedTransport = useSelector((state) => state.savedTransport);
    const dispatch = useDispatch();

    const [adInfoForm, setAdInfoForm] = useState({
        transportArea: "", //types: INSIDE , OUTSIDE
        transportType: "", //types: FTL , PARTIAL , CONTAINER , CARGO
        transportMethod: "", // Araç konteyner, 
        transportRota: "", // yol, 
        importExport: "", //types: IMPORT , EXPORT
        step: 0,
    })

    const [ftlForm, setFtlForm] = useState(defaultFtlForm);
    const [partialForm, setPartialForm] = useState(defaultPartialForm);
    const [containerForm, setContainerForm] = useState(defaultContainerForm);
    const [aracForm, setAracForm] = useState(defaultAracForm);
    const [containeroutsideForm, setContaineroutsideForm] = useState(defaultContaineroutsideForm);
    const [containeroutsidepartForm, setContaineroutsidepartForm] = useState(defaultContaineroutsidepartForm);
    const [aracpartForm, setAracpartForm] = useState(defaultSetAracpartForm);
    const [adStartingDate, setAdStartingDate] = useState(moment().add(2, 'hours'));
    const [adDueDate, setAdDueDate] = useState(null);
    const currentUser = useSelector((state) => state.user);

    
    if(currentUser?.role !== "ROLE_CLIENT"){
        window.location = BASE_URL_FRONTEND
    }

    const handleClickNextStep = () => {
        setAdInfoForm({ ...adInfoForm, step: adInfoForm.step + 1 })
    }

    const handleClickPrevStep = () => {
        console.log(adInfoForm);
        if (adInfoForm.step === 1) {
            setContaineroutsidepartForm({defaultContaineroutsidepartForm});
            setAracpartForm({defaultSetAracpartForm});
            setContaineroutsideForm({defaultContaineroutsideForm});        
            setAracForm({defaultAracForm}); 
        }
    

        setAdInfoForm({ ...adInfoForm, step: adInfoForm.step - 1 })
    }
    const handleSubmitFtlForm = async () => {
        if (adInfoForm.transportArea === 'INSIDE') {
            ftlForm.regionAdvertisementType = 'DOMESTIC'
            ftlForm.dueAddress.country = 'TURKEY'
            ftlForm.startingAddress.country = 'TURKEY'
        } else {
            ftlForm.regionAdvertisementType = 'INTERNATIONAL'
        }
        if (ftlForm.advertisementTransportType === '') {
            ftlForm.advertisementTransportType = 'NO_MATTER'
        }
        setIsSubmitting(true);
        ftlForm.adStartingDate = moment(adStartingDate).format("YYYY-MM-DD");
        ftlForm.adStartingTime = moment(adStartingDate).format("HH:mm");
        ftlForm.adDueDate = moment(adDueDate).format("YYYY-MM-DD");
        ftlForm.adDueTime = moment(adDueDate).format("HH:mm");
        ftlForm.startRecipient.phoneNumber = ftlForm.startRecipient.selectedCountryCode + '~' + ftlForm.startRecipient.phoneNumber
        ftlForm.dueRecipient.phoneNumber = ftlForm.dueRecipient.selectedCountryCode + '~' + ftlForm.dueRecipient.phoneNumber
        setFtlForm({ ...ftlForm })
        try {
            let res = await createFtlAdvertisementRequest(ftlForm);
            if (res) {
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setFtlForm(defaultFtlForm);
            }
        } catch (error) {
            RequestError(error);
            ftlForm.startRecipient.phoneNumber = ftlForm.startRecipient.phoneNumber.split(/[~]/g)[1]
            ftlForm.dueRecipient.phoneNumber = ftlForm.dueRecipient.phoneNumber.split(/[~]/g)[1]
            setFtlForm({ ...ftlForm })
        }
        setIsSubmitting(false);
    }

    // FTL/OUTSİDE için api tetikleme ve veri gönderme
    const handleSubmitFtlOutsideForm = async () => {
        if(currentUser !== null){
            aracForm.client_id = currentUser.id;
        }
        setIsSubmitting(true);
        let res = await createFtlOutsideAdvertisementRequest(aracForm);
            if (res) {
                setIsSubmitting(false);
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setAracForm(defaultAracForm);
            }
    }

    const handleSubmitFtlContainerOutsideForm = async () => {
        if(currentUser !== null){
            containeroutsideForm.client_id = currentUser.id;
        }
        setIsSubmitting(true);
        let res = await createFtlOutsideAdvertisementRequest(containeroutsideForm);
            if (res) {
                setIsSubmitting(false);
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setAracForm(defaultContaineroutsideForm);
            }
    }



    // Partial/OUTSİDE için api tetikleme ve veri gönderme
    const handleSubmitaracpartForm = async () => {
        if(currentUser !== null){
            aracpartForm.client_id = currentUser.id;
        }
        setIsSubmitting(true);
        let res = await createFtlOutsideAdvertisementRequest(aracpartForm);
            if (res) {
                setIsSubmitting(false);
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setAracForm(defaultSetAracpartForm);
            }
    }
    const handleSubmitcontaineroutsidepartForm = async () => {
        if(currentUser !== null){
            containeroutsidepartForm.client_id = currentUser.id;
        }
        setIsSubmitting(true);
        let res = await createFtlOutsideAdvertisementRequest(containeroutsidepartForm);
            if (res) {
                setIsSubmitting(false);
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setAracForm(containeroutsidepartForm);
            }
    }
    // const handleSubmitcontaineroutsideForm = async () => {
    //     if (adInfoForm.transportArea === 'INSIDE') {
    //         aracForm.regionAdvertisementType = 'DOMESTIC'
    //         aracForm.dueAddress.country = 'TURKEY'
    //         aracForm.startingAddress.country = 'TURKEY'
    //     } else {
    //         aracForm.regionAdvertisementType = 'INTERNATIONAL'
    //     }
    //     if (aracForm.advertisementTransportType === '') {
    //         aracForm.advertisementTransportType = 'NO_MATTER'
    //     }
    //     setIsSubmitting(true);
    //     aracForm.adStartingDate = moment(adStartingDate).format("YYYY-MM-DD");
    //     aracForm.adStartingTime = moment(adStartingDate).format("HH:mm");
    //     aracForm.adDueDate = moment(adDueDate).format("YYYY-MM-DD");
    //     aracForm.adDueTime = moment(adDueDate).format("HH:mm");
    //     aracForm.startRecipient.phoneNumber = aracForm.startRecipient.selectedCountryCode + '~' + aracForm.startRecipient.phoneNumber
    //     aracForm.dueRecipient.phoneNumber = aracForm.dueRecipient.selectedCountryCode + '~' + aracForm.dueRecipient.phoneNumber
    //     setAracForm({ ...aracForm })
    //     try {
    //         let res = await createFtlAdvertisementRequest(aracForm);
    //         if (res) {
    //             handleClickNextStep();
    //             dispatch(setSavedTransport(null));
    //             setAracForm(defaultAracForm);
    //         }
    //     } catch (error) {
    //         RequestError(error);
    //         aracForm.startRecipient.phoneNumber = aracForm.startRecipient.phoneNumber.split(/[~]/g)[1]
    //         aracForm.dueRecipient.phoneNumber = aracForm.dueRecipient.phoneNumber.split(/[~]/g)[1]
    //         setAracForm({ ...aracForm })
    //     }
    //     setIsSubmitting(false);
    // }

    const handleSubmitPartialForm = async () => {
        if (adInfoForm.transportArea === 'INSIDE') {
            partialForm.regionAdvertisementType = 'DOMESTIC'
            partialForm.dueAddress.country = 'TURKEY'
            partialForm.startingAddress.country = 'TURKEY'
        } else {
            partialForm.regionAdvertisementType = 'INTERNATIONAL'
        }
        if (partialForm.advertisementTransportType === '') {
            partialForm.advertisementTransportType = 'NO_MATTER'
        }
        setIsSubmitting(true);
        partialForm.adStartingDate = moment(adStartingDate).format("YYYY-MM-DD");
        partialForm.adStartingTime = moment(adStartingDate).format("HH:mm");
        partialForm.adDueDate = moment(adDueDate).format("YYYY-MM-DD");
        partialForm.adDueTime = moment(adDueDate).format("HH:mm");
        partialForm.startRecipient.phoneNumber = partialForm.startRecipient.selectedCountryCode + '~' + partialForm.startRecipient.phoneNumber
        partialForm.dueRecipient.phoneNumber = partialForm.dueRecipient.selectedCountryCode + '~' + partialForm.dueRecipient.phoneNumber
        setPartialForm({ ...partialForm })
        try {
            let res = await createPartialAdvertisementRequest(partialForm);
            if (res) {
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setPartialForm(defaultPartialForm);
            }
        } catch (error) {
            partialForm.startRecipient.phoneNumber = partialForm.startRecipient.phoneNumber.split(/[~]/g)[1]
            partialForm.dueRecipient.phoneNumber = partialForm.dueRecipient.phoneNumber.split(/[~]/g)[1]
            setPartialForm({ ...partialForm })
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const handleSubmitContainerForm = async () => {
        if (adInfoForm.transportArea === 'INSIDE') {
            partialForm.dueAddress.country = 'TURKEY'
            partialForm.startingAddress.country = 'TURKEY'
        }
        setIsSubmitting(true);
        containerForm.adStartingDate = moment(adStartingDate).format("YYYY-MM-DD");
        containerForm.adStartingTime = moment(adStartingDate).format("HH:mm");
        containerForm.adDueDate = moment(adDueDate).format("YYYY-MM-DD");
        containerForm.adDueTime = moment(adDueDate).format("HH:mm");
        setContainerForm({ ...containerForm })
        try {
            let res = await createContainerAdvertisementRequest(containerForm);
            if (res) {
                handleClickNextStep();
                dispatch(setSavedTransport(null));
                setContainerForm(defaultContainerForm);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    
    const StepAreaArac = () => {
        return (
            <div>
                <div style={{ margin: "2rem 1rem" }}>
                    <Steps current={adInfoForm.step - 1}>
                        <Steps.Step title={t("Yükleme Bilgileri")} />
                        <Steps.Step title={t("Yük Bilgileri")} />
                        <Steps.Step title={t("Araç Bilgileri")} />
                        <Steps.Step title={t("Özet")} />
                    </Steps>
                </div>
                <Divider style={{ borderTop: ".5rem solid #fff" }} />
            </div>
        )
    }
    const StepcContainerOutside = () => {
        return (
            <div>
                <div style={{ margin: "2rem 1rem" }}>
                    <Steps current={adInfoForm.step - 1}>
                        <Steps.Step title={t("Yükleme Bilgileri")} />
                        <Steps.Step title={t("Yük Bilgileri")} />
                        <Steps.Step title={t("Özet")} />
                    </Steps>
                </div>
                <Divider style={{ borderTop: ".5rem solid #fff" }} />
            </div>
        )
    }
    const StepArea = () => {
        return (
            <div>
                <div style={{ margin: "2rem 1rem" }}>
                    <Steps current={adInfoForm.step - 1}>
                        <Steps.Step title={t("Yükleme Bilgileri")} />
                        <Steps.Step title={t("Teslimat Bilgileri")} />
                        <Steps.Step title={t("Yük Bilgileri")} />
                        <Steps.Step title={t("Araç Bilgileri")} />
                        <Steps.Step title={t("Özet")} />
                    </Steps>
                </div>
                <Divider style={{ borderTop: ".5rem solid #fff" }} />
            </div>
        )
    }

    const StepAreaContainer = () => {
        return (
            <div>
                <div style={{ margin: "2rem 1rem" }}>
                    <Steps current={adInfoForm.step - 1}>
                        <Steps.Step title={t("Konteyner")} />
                        <Steps.Step title={t("Yükleme Bilgileri")} />
                        <Steps.Step title={t("Teslimat Bilgileri")} />
                        <Steps.Step title={t("Yük Bilgileri")} />
                        <Steps.Step title={t("Özet")} />
                    </Steps>
                </div>
                <Divider style={{ borderTop: ".5rem solid #fff" }} />
            </div>
        )
    };

    useEffect(() => {
        if (savedTransport !== null) {
          console.log(savedTransport.adInfoForm);
          if (savedTransport === "closed") { // check if savedTransport is already closed
            dispatch(setSavedTransport(null));
          } else {
            Modal.confirm({
              centered: true,
              title: `${t("Yarım kalan--")} ${savedTransport.adInfoForm.transportArea !== "OUTSIDE"? savedTransport.form.startingAddress.province: savedTransport.form.startSelectCountryName} -> ${savedTransport.adInfoForm.transportArea !== "OUTSIDE" ? savedTransport.form.dueAddress.province : savedTransport.form.dueSelectCountryName} ${t("rotalı ilanızı tamamlamak ister misiniz?--")}`,
              okText: t('Evet, tamamlayalım'),
              okType: 'primary',
              cancelText: t('Hayır, silebiliriz'),
              onOk() {
                setAdInfoForm(savedTransport.adInfoForm);
                setAdStartingDate(savedTransport.adStartingDate);
                setAdDueDate(savedTransport.adDueDate);
                if(savedTransport.adInfoForm.transportArea !== "OUTSIDE" ){
                    if (savedTransport.adInfoForm.transportType === "FTL") {
                        setFtlForm(savedTransport.form);
                      } else if (savedTransport.adInfoForm.transportType === "PARTIAL") {
                        setPartialForm(savedTransport.form);
                      } else if (savedTransport.adInfoForm.transportType === "CONTAINER") {
                        setContainerForm(savedTransport.form);
                      }
                }else{
                    if(savedTransport.adInfoForm.transportMethod === "Araç" && savedTransport.adInfoForm.transportType === "FTL/OUTSIDE"){
                        setAracForm(savedTransport.form);
                    }
                    if(savedTransport.adInfoForm.transportMethod === "Araç" && savedTransport.adInfoForm.transportType === "PARTIAL/OUTSIDE"){
                        setAracpartForm(savedTransport.form);
                    }
                    if(savedTransport.adInfoForm.transportMethod === "Konteyner" && savedTransport.adInfoForm.transportType === "FTL/OUTSIDE"){
                        setContaineroutsideForm(savedTransport.form);
                    }
                    if(savedTransport.adInfoForm.transportMethod === "Konteyner" && savedTransport.adInfoForm.transportType === "PARTIAL/OUTSIDE"){
                        setContaineroutsidepartForm(savedTransport.form);
                    }
                }
                setTimeout(() => {
                  dispatch(setSavedTransport(null));
                }, 2000);
              },
              onCancel() {
                dispatch(setSavedTransport("closed")); // set savedTransport to closed
              },
            });
          }
        }
      }, [savedTransport]);

    switch (true) {
        //FTL STEP 1 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "FTL" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <FTLStep1In handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} ftlForm={ftlForm} setFtlForm={setFtlForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );
        //FTL STEP 2 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "FTL" && adInfoForm.step === 2:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <FTLStep2In handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} ftlForm={ftlForm} setFtlForm={setFtlForm} adStartingDate={adStartingDate} adDueDate={adDueDate} setAdDueDate={setAdDueDate} />
                    <SupportBanner />
                </div>
            );

        //FTL STEP 3 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "FTL" && adInfoForm.step === 3:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <FTLStep3In handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} ftlForm={ftlForm} setFtlForm={setFtlForm} />
                    <SupportBanner />
                </div>
            );

        //FTL STEP 4
        case adInfoForm.transportType === "FTL" && adInfoForm.step === 4:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <FTLStep4 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} ftlForm={ftlForm} setFtlForm={setFtlForm} />
                    <SupportBanner />
                </div>
            );

        //FTL STEP 5 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "FTL" && adInfoForm.step === 5:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <FTLStep5In handleSubmitFtlForm={handleSubmitFtlForm} handleClickPrevStep={handleClickPrevStep} ftlForm={ftlForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting} />
                    <SupportBanner />
                </div>
            );
        // Arac STEP 1 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepAreaArac />
                    <AracStep1 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} aracForm={aracForm} setAracForm={setAracForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );

        //Arac Step2 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 2:
        return (
                <div className="layout-content-padding">
                    <StepAreaArac />
                    <AracStep2 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} aracForm={aracForm} setAracForm={setAracForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );
          //Arac Step3 OUTSIDE
          case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 3:
            return (
                    <div className="layout-content-padding">
                        <StepAreaArac />
                        <AracStep3 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} aracForm={aracForm} setAracForm={setAracForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                    </div>
                );
            
            
          //Arac Step4 OUTSIDE
          case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 4:
            return (
                    <div className="layout-content-padding">
                        <StepAreaArac />
                        <AracStep4 handleSubmitaracForm={handleSubmitFtlOutsideForm} handleClickPrevStep={handleClickPrevStep} aracForm={aracForm} setAracForm={setAracForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting}  />
                    </div>
                );
        // Container STEP 1 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Konteyner" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <ContainerOutsideStep1 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containeroutsideForm={containeroutsideForm} setContaineroutsideForm={setContaineroutsideForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );

        //Container Step2 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Konteyner" && adInfoForm.step === 2:
        return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <ContainerOutsideStep2 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containeroutsideForm={containeroutsideForm} setContaineroutsideForm={setContaineroutsideForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );
                
            
          //Container Step3 OUTSIDE
          case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "FTL/OUTSIDE" &&  adInfoForm.transportMethod === "Konteyner" && adInfoForm.step === 3:
            return (
                    <div className="layout-content-padding">
                        <StepcContainerOutside />
                        <ContainerOutsideStep3 handleSubmitFtlContainerOutsideForm={handleSubmitFtlContainerOutsideForm} handleClickPrevStep={handleClickPrevStep} containeroutsideForm={containeroutsideForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting}  />
                    </div>
                );







                // Parsiyel Araç STEP 1 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <AracStepPart1 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} aracpartForm={aracpartForm} setAracpartForm={setAracpartForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );

        //Parsiyel Araç Step2 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 2:
        return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <AracStepPart2 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} aracpartForm={aracpartForm} setAracpartForm={setAracpartForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );
                
            
          //Parsiyel Araç Step3 OUTSIDE
          case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL/OUTSIDE" &&  adInfoForm.transportMethod === "Araç" && adInfoForm.step === 3:
            return (
                    <div className="layout-content-padding">
                        <StepcContainerOutside />
                        <AracStepPart3 handleSubmitaracpartForm={handleSubmitaracpartForm} handleClickPrevStep={handleClickPrevStep} aracpartForm={aracpartForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting}  />
                    </div>
                );

                  // Parsiyel Konteyner STEP 1 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL/OUTSIDE" &&  adInfoForm.transportMethod === "Konteyner" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <ContainerOutsideStepPart1 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containeroutsidepartForm={containeroutsidepartForm} setContaineroutsidepartForm={setContaineroutsidepartForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );

        //Parsiyel Konteyner Step2 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL/OUTSIDE" &&  adInfoForm.transportMethod === "Konteyner" && adInfoForm.step === 2:
        return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <ContainerOutsideStepPart2 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containeroutsidepartForm={containeroutsidepartForm} setContaineroutsidepartForm={setContaineroutsidepartForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                </div>
            );
                
            
          //Parsiyel Konteyner Step3 OUTSIDE
          case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL/OUTSIDE" &&  adInfoForm.transportMethod === "Konteyner" && adInfoForm.step === 3:
            return (
                    <div className="layout-content-padding">
                        <StepcContainerOutside />
                        <ContainerOutsideStepPart3 handleSubmitcontaineroutsidepartForm={handleSubmitcontaineroutsidepartForm} handleClickPrevStep={handleClickPrevStep} containeroutsidepartForm={containeroutsidepartForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting}  />
                    </div>
                );




        //Partial STEP 1 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep1In handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 1 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep1Out handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 2 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 2:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep2In handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} adStartingDate={adStartingDate} adDueDate={adDueDate} setAdDueDate={setAdDueDate} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 2 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 2:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep2Out handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} adStartingDate={adStartingDate} adDueDate={adDueDate} setAdDueDate={setAdDueDate} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 3 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 3:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep3In handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 3 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 3:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep3Out handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 4
        case adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 4:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep4 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} setPartialForm={setPartialForm} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 5 INSIDE
        case adInfoForm.transportArea === 'INSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 5:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep5In handleSubmitPartialForm={handleSubmitPartialForm} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting} />
                    <SupportBanner />
                </div>
            );

        //Partial STEP 5 OUTSIDE
        case adInfoForm.transportArea === 'OUTSIDE' && adInfoForm.transportType === "PARTIAL" && adInfoForm.step === 5:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <PartialStep5Out handleSubmitPartialForm={handleSubmitPartialForm} handleClickPrevStep={handleClickPrevStep} partialForm={partialForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting} />
                    <SupportBanner />
                </div>
            );


        // CONTAINER STEP 1
        case adInfoForm.transportType === "CONTAINER" && adInfoForm.step === 1:
            return (
                <div className="layout-content-padding">
                    <StepAreaContainer />
                    <ContainerStep1 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containerForm={containerForm} setContainerForm={setContainerForm} />
                    <SupportBanner />
                </div>
            );

        //CONTAINER STEP 2
        case adInfoForm.transportType === "CONTAINER" && adInfoForm.step === 2:
            return (
                <div className="layout-content-padding">
                    <StepAreaContainer />
                    <ContainerStep2 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containerForm={containerForm} setContainerForm={setContainerForm} adStartingDate={adStartingDate} setAdStartingDate={setAdStartingDate} />
                    <SupportBanner />
                </div>
            );

        //CONTAINER STEP 3
        case adInfoForm.transportType === "CONTAINER" && adInfoForm.step === 3:
            return (
                <div className="layout-content-padding">
                    <StepAreaContainer />
                    <ContainerStep3 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containerForm={containerForm} setContainerForm={setContainerForm} adStartingDate={adStartingDate} adDueDate={adDueDate} setAdDueDate={setAdDueDate} />
                    <SupportBanner />
                </div>
            );

        //CONTAINER STEP 4
        case adInfoForm.transportType === "CONTAINER" && adInfoForm.step === 4:
            return (
                <div className="layout-content-padding">
                    <StepAreaContainer />
                    <ContainerStep4 handleClickNextStep={handleClickNextStep} handleClickPrevStep={handleClickPrevStep} containerForm={containerForm} setContainerForm={setContainerForm} />
                    <SupportBanner />
                </div>
            );

        //CONTAINER STEP 5
        case adInfoForm.transportType === "CONTAINER" && adInfoForm.step === 5:
            return (
                <div className="layout-content-padding">
                    <StepAreaContainer />
                    <ContainerStep5 handleSubmitContainerForm={handleSubmitContainerForm} handleClickPrevStep={handleClickPrevStep} containerForm={containerForm} adStartingDate={adStartingDate} adDueDate={adDueDate} isSubmitting={isSubmitting} />
                    <SupportBanner />
                </div>
            );


        //LAST STEP
        case adInfoForm.step === 6:
            return (
                <div className="layout-content-padding">
                    <StepArea />
                    <AdLastStep />
                    <SupportBanner />
                </div>
            );
        case adInfoForm.step === 5 &&  adInfoForm.transportArea === 'OUTSIDE' &&  adInfoForm.transportType === "FTL/OUTSIDE" :
            return (
                <div className="layout-content-padding">
                    <StepAreaArac />
                    {/* <StepArea /> */}
                    <AdLastStep />
                    <SupportBanner />
                </div>
            );
        case adInfoForm.step === 4 &&  adInfoForm.transportArea === 'OUTSIDE' &&  adInfoForm.transportType !== "FTL/OUTSIDE" :
            return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <AdLastStep />
                    <SupportBanner />
                </div>
            );
        case adInfoForm.step === 4 &&  adInfoForm.transportArea === 'OUTSIDE' &&  adInfoForm.transportType !== "PARTIAL" && adInfoForm.transportMethod === "Konteyner" :
            return (
                <div className="layout-content-padding">
                    <StepcContainerOutside />
                    <AdLastStep />
                    <SupportBanner />
                </div>
            );


        default:
            return (
                <div className="layout-content-padding">
                    <AdFirstStep aracForm={aracForm} containeroutsideForm={containeroutsideForm} aracpartForm={aracpartForm} containeroutsidepartForm={containeroutsidepartForm}   adInfoForm={adInfoForm}   setAdInfoForm={setAdInfoForm} handleClickNextStep={handleClickNextStep} containerForm={containerForm} setContainerForm={setContainerForm} ftlForm={ftlForm} setFtlForm={setFtlForm} partialForm={partialForm} setPartialForm={setPartialForm} defaultFtlForm={defaultFtlForm} defaultPartialForm={defaultPartialForm} defaultContainerForm={defaultContainerForm} />
                    <SupportBanner />
                </div>
            );
    };


};

export default CreateAd;



const defaultFtlForm = {
    adDueDate: "",
    adDueTime: "",
    adStartingDate: "",
    adStartingTime: "",
    cargoTypeIdList: [],
    currencyUnitId: 1,
    desi: "",
    documentNo: "",
    dueAddress: {
        buildingInformation: "",
        country: "",
        district: null,
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: null,
        province: null,
        street: "",
        zipCode: null,
    },
    dueRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        selectedCountryCode: "+90"
    },
    explanation: "",
    goodsPrice: null,
    isPorter: false,
    isStacking: false,
    advertisementTransportType: "",
    regionAdvertisementType: "",
    ldm: "",
    loadTypeIdList: [],
    packagingTypeId: null,
    startRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        selectedCountryCode: "+90"
    },
    startingAddress: {
        buildingInformation: "",
        country: "",
        district: null,
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: null,
        province: null,
        street: "",
        zipCode: null,
    },
    tonnage: null,
    trailerFeatureIdList: [],
    trailerFloorTypeIdList: [],
    trailerTypeIdList: [],
    vehicleCount: null,
    vehicleTypeIdList: [],
    volume: ""
}

const defaultAracForm = {
    // Bu değişkenleri backende gönderiyoruz, Backend aynı değişken isimlerinde mysql tabloları oluşturup verileri kaydediyolar...

    currencyUnitId: "₺",
    cargoTypeIdList: null, // new
    hsCode: "",
    deliveryType: null,
    explanation: "",
    goodsPrice: null,
    unCode: null,
    kontrol: null,
    loadTypeIdList: null, // new
    packagingTypeId: null,
    tonnage: null,
    trailerFeatureName: "", // new
    trailerFeatureIdList: [], // new  frontend için
    trailerFloorTypeIdList: null, // new
    trailerTypeIdList: null, // new
    vehicleCount: null,
    vehicleTypeIdList: null,
    fullDate: null,
    
    // starting
    startFullAddress : null,
    startDate: null,
    startDeliveryMethod: "d2d",
    startSelectCountryName: null,
    startSelectCountryCode: null,
                
    // due
    dueSelectCountryName : null,
    dueFullAddress: null,
    dueDate: null,
    dueSelectCountryCode: null,

    clientAdvertisementType: "FTL",
    vehicleOrContainer: "",
    advertisementStatus: "",
    addDate: "",
    client_id: null,
}
const defaultContaineroutsideForm = {
    cargoTypeIdList: null,
    currencyUnitId: "₺",
    hsCode: "",
    deliveryType: null,
    explanation: "",
    goodsPrice: null,
    containerType: null,
    tonnage: null,
    vehicleCount: null, 
    unCode: null,  

    // starting
    startFullAddress : null,
    startDate: null,
    startDeliveryMethod: null,
    startSelectCountryName: null,
    startSelectCountryCode: null,
                
    // due
    dueSelectCountryName : null,
    dueFullAddress: null,
    dueDate: null,
    dueSelectCountryCode: null,

    clientAdvertisementType: "FTL",
    vehicleOrContainer: "",
    advertisementStatus: "",
    client_id: null,
}
const defaultSetAracpartForm = {
    cargoTypeIdList: null,
    currencyUnitId: "₺",
    hsCode: "",
    deliveryType: null,
    explanation: "",
    goodsPrice: null,
    containerType: null,
    tonnage: null,
    vehicleCount: null, 
    unCode: null,  

    // starting
    startFullAddress : null,
    startDate: null,
    startDeliveryMethod: "d2d",
    startSelectCountryName: null,
    startSelectCountryCode: null,
    
    // due
    dueSelectCountryName : null,
    dueFullAddress: null,
    dueDate: null,
    dueSelectCountryCode: null,

    clientAdvertisementType: "PARSİYEL",
    vehicleOrContainer: "",
    advertisementStatus: "",
    client_id: null,
}
const defaultContaineroutsidepartForm = {
    cargoTypeIdList: null,
    currencyUnitId: "₺",
    hsCode: "",
    deliveryType: null,
    explanation: "",
    goodsPrice: null,
    tonnage: null,
    vehicleCount: null, 
    unCode: null,  
    fullDate: null,


    // starting
    startFullAddress : null,
    startDate: null,
    startDeliveryMethod: null,
    startSelectCountryName: null,
    startSelectCountryCode: null,
    
    // due
    dueSelectCountryName : null,
    dueFullAddress: null,
    dueDate: null,
    dueSelectCountryCode: null,
    
    
    desi: null,
    documentNo: "",
    kontrol: null,
    height: null,
    length: null,
    loadTypeIdList: null,
    packagingTypeId: null,
    piece: null,
    width: null,

    clientAdvertisementType: "PARSİYEL",
    vehicleOrContainer: "",
    advertisementStatus: "",
    client_id: null,
}
const defaultPartialForm = {
    adDueDate: "",
    adDueTime: "",
    adStartingDate: "",
    adStartingTime: "",
    cargoTypeIdList: [],
    currencyUnitId: 1,
    desi: null,
    documentNo: "",
    dueAddress: {
        buildingInformation: "",
        country: "",
        district: null,
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: null,
        province: null,
        street: "",
        zipCode: null,
    },
    dueRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        selectedCountryCode: "+90"
    },
    explanation: "",
    goodsPrice: null,
    height: null,
    isPorter: false,
    isStacking: false,
    advertisementTransportType: "",
    regionAdvertisementType: "",
    ldm: "",
    length: null,
    loadTypeIdList: [],
    packagingTypeId: null,
    piece: null,
    startRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        selectedCountryCode: "+90"
    },
    startingAddress: {
        buildingInformation: "",
        country: "",
        district: null,
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: null,
        province: null,
        street: "",
        zipCode: null,
    },
    tonnage: null,
    trailerFeatureIdList: [],
    trailerFloorTypeIdList: [],
    trailerTypeIdList: [],
    vehicleTypeIdList: [],
    volume: "",
    width: null
}


const defaultContainerForm = {
    adDueDate: "",
    adDueTime: "",
    adStartingDate: "",
    adStartingTime: "",
    containerAddress: {
        buildingInformation: "",
        country: "",
        district: "",
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: "",
        province: "",
        street: ""
    },
    containerRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: ""
    },
    containerType: null,
    currencyUnitId: 1,
    customsOfficerPhone: "",
    selectedCountryCodeOfficer: "+90",
    documentNo: "",
    dueAddress: {
        buildingInformation: "",
        country: "",
        district: "",
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: "",
        province: "",
        street: ""
    },
    dueRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: ""
    },
    explanation: "",
    goodsPrice: null,
    startRecipient: {
        commercialTitle: "",
        firstName: "",
        lastName: "",
        phoneNumber: ""
    },
    startingAddress: {
        buildingInformation: "",
        country: "",
        district: "",
        fullAddress: "",
        lat: null,
        lng: null,
        neighborhood: "",
        province: "",
        street: ""
    },
    tonnage: null,
    tradeType: ""
}