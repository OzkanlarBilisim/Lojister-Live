import { Button, Col, Row, Divider } from 'antd';
import React, { useEffect, useState } from 'react'
import { getCargoTypesRequest } from '../../../../../api/controllers/cargo-type-controller';
import { getCurrencyUnitsRequest } from '../../../../../api/controllers/currency-unit-controller';
import CustomTitle from '../../../../CustomTitle';
import { getVehicleTypesRequest } from '../../../../../api/controllers/vehicle-type-controller';
import { getTrailerFloorTypesRequest } from '../../../../../api/controllers/trailer-floor-type-controller';
import { getTrailerFeaturesRequest } from '../../../../../api/controllers/trailer-feature-controller';
import moment from 'moment';
import { getTrailerTypesRequest } from '../../../../../api/controllers/trailer-type-controller';
import RequestError from '../../../../RequestError';
import { useTranslation } from 'react-i18next';


//FTL STEP 5
const ContainerOutsideStep5 = ({ handleClickPrevStep, aracpartForm, handleSubmitaracpartForm, adStartingDate, adDueDate, isSubmitting }) => {

    const { t, i18n } = useTranslation("common");/* {t()} */


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
                        onClick={() => handleSubmitaracpartForm()}
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
                            {aracpartForm.startDate+" / "+aracpartForm.dueDate}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Şekli")}>
                            {
                                (aracpartForm.startDeliveryMethod === "d2d")? t("Kapıdan Kapıya"): ""+
                                (aracpartForm.startDeliveryMethod === "p2p")? t("Limandan Limana"): ""+
                                (aracpartForm.startDeliveryMethod === "d2p")? t("Kapıdan Limana"): ""+
                                (aracpartForm.startDeliveryMethod === "p2d")? t("Limandan Kapıya"): ""
                            } 
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Yapılacak Ülke")}>
                            {aracpartForm.startSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Noktası")}>
                            {aracpartForm.startFullAddress}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Ülke")}>
                        {aracpartForm.dueSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Nokta")}>
                            {aracpartForm.dueFullAddress}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yük Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yük Tipi")}>
                        {aracpartForm.cargoTypeIdList !== null ?
                                t(aracpartForm.cargoTypeIdList)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Konteyner Tipi")}>
                        {aracpartForm.containerType}
                        </CustomLabel>
                        <CustomLabel title={t("Hs Kod")}>
                            {aracpartForm.hsCode}
                        </CustomLabel>
                        <CustomLabel title={t("Un Kod")}>
                            {aracpartForm.unCode}
                        </CustomLabel>
                        <CustomLabel title={t("Taşıma Şekli")}>
                            {aracpartForm.deliveryType}
                        </CustomLabel>
                        <CustomLabel title={t("Toplam Ağırlık")}>
                            {aracpartForm.tonnage + " " + t("TON")}
                        </CustomLabel>
                        <CustomLabel title={t("Mal Değeri")}>
                            {aracpartForm.goodsPrice ? aracpartForm.goodsPrice + " " + aracpartForm.currencyUnitId : "-"}
                        </CustomLabel>
                        <CustomLabel title={t("Açıklama")}>
                            {aracpartForm.explanation ? aracpartForm.explanation : "-"}
                        </CustomLabel>
                    </div>
                </Col>
            </Row>
        </div >
    );
};

export default ContainerOutsideStep5;