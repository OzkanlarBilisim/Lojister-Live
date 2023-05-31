import { Button, Col, Row, Divider } from 'antd';
import React, { useEffect, useState } from 'react'
import { getCargoTypesRequest } from '../../../../api/controllers/cargo-type-controller';
import { getLoadTypesRequest } from '../../../../api/controllers/load-type-controller';
import { getPackagingTypesRequest } from '../../../../api/controllers/packaging-type-controller';
import { getCurrencyUnitsRequest } from '../../../../api/controllers/currency-unit-controller';
import CustomTitle from '../../../CustomTitle';
import { getVehicleTypesRequest } from '../../../../api/controllers/vehicle-type-controller';
import { getTrailerFloorTypesRequest } from '../../../../api/controllers/trailer-floor-type-controller';
import { getTrailerFeaturesRequest } from '../../../../api/controllers/trailer-feature-controller';
import moment from 'moment';
import { getTrailerTypesRequest } from '../../../../api/controllers/trailer-type-controller';
import RequestError from '../../../RequestError';
import { useTranslation } from 'react-i18next';


//FTL STEP 5
const FTLStep5 = ({ handleClickPrevStep, ftlForm, handleSubmitFtlForm, adStartingDate, adDueDate, isSubmitting }) => {
    const { t, i18n } = useTranslation("common");/* {t()} */

    const [vehicleTypes, setVehicleTypes] = useState([]);
    const [trailerTypes, setTrailerTypes] = useState([]);
    const [trailerFloorTypes, setTrailerFloorTypes] = useState([]);
    const [trailerFeatures, setTrailerFeatures] = useState([]);
    const [cargoTypes, setCargoTypes] = useState([]);
    const [packagingTypes, setPackagingTypes] = useState([]);
    const [loadTypes, setLoadTypes] = useState([])
    const [currencyUnits, setCurrencyUnits] = useState([])

    const fetchVehicleTypes = async () => {
        try {
            let res = await getVehicleTypesRequest();
            if (res) {
                let x = res.data.filter(type => ftlForm.vehicleTypeIdList.includes(type.id));
                setVehicleTypes(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchTrailerTypes = async () => {
        try {
            let res = await getTrailerTypesRequest();
            if (res) {
                let x = res.data.filter(type => ftlForm.trailerTypeIdList.includes(type.id));
                setTrailerTypes(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchTrailerFloorTypes = async () => {
        try {
            let res = await getTrailerFloorTypesRequest();
            if (res) {
                let x = res.data.filter(type => ftlForm.trailerFloorTypeIdList.includes(type.id));
                setTrailerFloorTypes(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchTrailerFeatures = async () => {
        try {
            let res = await getTrailerFeaturesRequest();
            if (res) {
                let x = res.data.filter(type => ftlForm.trailerFeatureIdList.includes(type.id));
                setTrailerFeatures(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchCargoTypes = async () => {
        try {
            let res = await getCargoTypesRequest();
            if (res) {
                let x = res.data.filter(type => ftlForm.cargoTypeIdList.includes(type.id));
                setCargoTypes(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchPackagingTypes = async () => {
        try {
            let res = await getPackagingTypesRequest();
            if (res) {
                let x = res.data.filter(type => type.id === ftlForm.packagingTypeId);
                setPackagingTypes(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchLoadTypes = async () => {
        try {
            let res = await getLoadTypesRequest();
            if (res) {
                let x = res.data.filter(type => ftlForm.loadTypeIdList.includes(type.id));
                setLoadTypes(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchCurrencyUnits = async () => {
        try {
            let res = await getCurrencyUnitsRequest();
            if (res) {
                let x = res.data.find(type => type.id === ftlForm.currencyUnitId);
                setCurrencyUnits(x);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    useEffect(() => {
        fetchCargoTypes();
        fetchPackagingTypes();
        fetchLoadTypes();
        fetchCurrencyUnits();
        fetchVehicleTypes();
        fetchTrailerTypes();
        fetchTrailerFloorTypes();
        fetchTrailerFeatures();
    }, []);

    const CustomLabel = ({ title, children }) => {
        return (
            <div>
                <p style={{ fontSize: 12, margin: 0, opacity: .7 }}>{title}</p>
                <p style={{ margin: 0, opacity: children === "Farketmez" && .5 }}>{children}</p>
            </div>
        )
    }

    return (
        <div className="create-ad-wrapper">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>

                <div>
                    <Button
                        onClick={() => handleClickPrevStep()}
                        shape="circle"
                        type="primary"
                        icon={<i className="bi bi-arrow-left"></i>}>
                    </Button>
                </div>
                <div>
                    <Button
                        size="large"
                        onClick={() => handleSubmitFtlForm()}
                        type="primary"
                        loading={isSubmitting}
                    >
                        {t("İlanı Yayınla")}
                    </Button>
                </div>
            </div>
            <Row gutter={[24, 24]}>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yükleme Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yükleme Tarihi")}>
                            {moment(adStartingDate).format("LLL")}
                        </CustomLabel>
                        <CustomLabel title={t("Şirket Ünvanı")} >
                            {ftlForm.startRecipient.commercialTitle}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili İsmi Soyismi")}>
                            {ftlForm.startRecipient.firstName + " " + ftlForm.startRecipient.lastName}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili Telefon Numarası")}>
                            {ftlForm.startRecipient.selectedCountryCode + ftlForm.startRecipient.phoneNumber}
                        </CustomLabel>
                        <CustomLabel title={t("Adres")}>
                            {ftlForm.startingAddress.fullAddress + " " + ftlForm.startingAddress.neighborhood + " - " + ftlForm.startingAddress.district + "/" + ftlForm.startingAddress.province}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Teslimat Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Teslimat Tarihi")}>
                            {moment(adDueDate).format("LLL")}
                        </CustomLabel>
                        <CustomLabel title={t("Şirket Ünvanı")}>
                            {ftlForm.dueRecipient.commercialTitle}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili İsmi Soyismi")}>
                            {ftlForm.dueRecipient.firstName + " " + ftlForm.dueRecipient.lastName}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili Telefon Numarası")}>
                            {ftlForm.dueRecipient.selectedCountryCode + ftlForm.dueRecipient.phoneNumber}
                        </CustomLabel>
                        <CustomLabel title={t("Adres")}>
                            {ftlForm.dueAddress.fullAddress + " " + ftlForm.dueAddress.neighborhood + " - " + ftlForm.dueAddress.district + "/" + ftlForm.dueAddress.province}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yük Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yük Cinsi")}>
                            {cargoTypes.length === 1 ?
                                cargoTypes.map(v => v.typeName) :
                                cargoTypes.length > 1 ?
                                    cargoTypes.map((v, i) => {
                                        if (cargoTypes.length === i + 1) {
                                            return (
                                                v.typeName
                                            )
                                        } else {
                                            return (
                                                v.typeName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Paketleme Tipi")}>
                            {packagingTypes.length === 1 ?
                                packagingTypes.map(v => v.typeName) :
                                packagingTypes.length > 1 ?
                                    packagingTypes.map((v, i) => {
                                        if (packagingTypes.length === i + 1) {
                                            return (
                                                v.typeName
                                            )
                                        } else {
                                            return (
                                                v.typeName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Şekli")}>
                            {loadTypes.length === 1 ?
                                loadTypes.map(v => v.typeName) :
                                loadTypes.length > 1 ?
                                    loadTypes.map((v, i) => {
                                        if (loadTypes.length === i + 1) {
                                            return (
                                                v.typeName
                                            )
                                        } else {
                                            return (
                                                v.typeName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Toplam Ağırlık")}>
                            {ftlForm.tonnage / 1000 + " " + t("TON")}
                        </CustomLabel>
                        <CustomLabel title={t("Mal Değeri")}>
                            {ftlForm.goodsPrice ? ftlForm.goodsPrice + " " + currencyUnits.currencySymbol : "-"}
                        </CustomLabel>
                        <CustomLabel title={t("Açıklama")}>
                            {ftlForm.explanation ? ftlForm.explanation : "-"}
                        </CustomLabel>
                        <Divider style={{ margin: 0 }} />
                        <p>{ftlForm.isPorter ? t("Hammaliye istiyorum.") : t("Hammaliye istemiyorum.")}</p>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Araç Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Araç Tipi")}>
                            {vehicleTypes.length === 1 ?
                                vehicleTypes.map(v => v.typeName) :
                                vehicleTypes.length > 1 ?
                                    vehicleTypes.map((v, i) => {
                                        if (vehicleTypes.length === i + 1) {
                                            return (
                                                v.typeName
                                            )
                                        } else {
                                            return (
                                                v.typeName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Araç Sayısı")}>
                            {ftlForm.vehicleCount + " " + t("Adet")}
                        </CustomLabel>
                        <CustomLabel title={t("Kasa Tipi")}>
                            {trailerTypes.length === 1 ?
                                trailerTypes.map(v => v.typeName) :
                                trailerTypes.length > 1 ?
                                    trailerTypes.map((v, i) => {
                                        if (trailerTypes.length === i + 1) {
                                            return (
                                                v.typeName
                                            )
                                        } else {
                                            return (
                                                v.typeName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Zemin Tipi")}>
                            {trailerFloorTypes.length === 1 ?
                                trailerFloorTypes.map(v => v.typeName) :
                                trailerFloorTypes.length > 1 ?
                                    trailerFloorTypes.map((v, i) => {
                                        if (trailerFloorTypes.length === i + 1) {
                                            return (
                                                v.typeName
                                            )
                                        } else {
                                            return (
                                                v.typeName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Kasa Özellikleri")}>
                            {trailerFeatures.length === 1 ?
                                trailerFeatures.map(v => v.featureName) :
                                trailerFeatures.length > 1 ?
                                    trailerFeatures.map((v, i) => {
                                        if (trailerFeatures.length === i + 1) {
                                            return (
                                                v.featureName
                                            )
                                        } else {
                                            return (
                                                v.featureName + ", "
                                            )
                                        }
                                    }) :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                    </div>
                </Col>
            </Row>
        </div >
    );
};

export default FTLStep5;