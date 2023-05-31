import { Button, Col, Row, Divider } from 'antd';
import React, { useEffect, useState } from 'react'
import { getCargoTypesRequest } from '../../../../../api/controllers/cargo-type-controller';
import { getCurrencyUnitsRequest } from '../../../../../api/controllers/currency-unit-controller';
import CustomTitle from '../../../../CustomTitle';
import { getPackagingTypesRequest } from '../../../../../api/controllers/packaging-type-controller';
import { getLoadTypesRequest } from '../../../../../api/controllers/load-type-controller';
import { getVehicleTypesRequest } from '../../../../../api/controllers/vehicle-type-controller';
import { getTrailerFloorTypesRequest } from '../../../../../api/controllers/trailer-floor-type-controller';
import { getTrailerFeaturesRequest } from '../../../../../api/controllers/trailer-feature-controller';
import moment from 'moment';
import { getTrailerTypesRequest } from '../../../../../api/controllers/trailer-type-controller';
import RequestError from '../../../../RequestError';
import { useTranslation } from 'react-i18next';


//FTL STEP 5
const ContainerOutsideStep5 = ({ handleClickPrevStep, containeroutsidepartForm, handleSubmitcontaineroutsidepartForm, adStartingDate, isSubmitting }) => {

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
                        onClick={() => handleSubmitcontaineroutsidepartForm()}
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
                            {containeroutsidepartForm.startDate+" / "+containeroutsidepartForm.dueDate}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Şekli")}>
                            {
                                (containeroutsidepartForm.startDeliveryMethod === "d2d")? t("Kapıdan Kapıya"): ""+
                                (containeroutsidepartForm.startDeliveryMethod === "p2p")? t("Limandan Limana"): ""+
                                (containeroutsidepartForm.startDeliveryMethod === "d2p")? t("Kapıdan Limana"): ""+
                                (containeroutsidepartForm.startDeliveryMethod === "p2d")? t("Limandan Kapıya"): ""
                            } 
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Yapılacak Ülke")}>
                            {containeroutsidepartForm.startSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Noktası")}>
                            {containeroutsidepartForm.startFullAddress}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Ülke")}>
                        {containeroutsidepartForm.dueSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Nokta")}>
                            {containeroutsidepartForm.dueFullAddress}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yük Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yük Tipi")}>
                        {containeroutsidepartForm.cargoTypeIdList !== null ?
                                t(containeroutsidepartForm.cargoTypeIdList)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Paketleme Tipi")}>
                            {containeroutsidepartForm.packagingTypeId !== null ?
                                t(containeroutsidepartForm.packagingTypeId)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Şekli")} >
                            {
                                containeroutsidepartForm.loadTypeIdList !== null ?
                                    t(containeroutsidepartForm.loadTypeIdList)
                                    :
                                    t("Farketmez")
                            }
                        </CustomLabel >
                        <CustomLabel title={t("Hs Kod")}>
                            {containeroutsidepartForm.hsCode}
                        </CustomLabel>
                        <CustomLabel title={t("Un Kod")}>
                            {containeroutsidepartForm.unCode}
                        </CustomLabel>
                        <CustomLabel title={t("Taşıma Şekli")}>
                            {containeroutsidepartForm.deliveryType}
                        </CustomLabel>
                        <CustomLabel title={t("En")}>
                            {containeroutsidepartForm.width+" cm"}
                        </CustomLabel>
                        <CustomLabel title={t("Boy")}>
                            {containeroutsidepartForm.length+" cm"}
                        </CustomLabel>
                        <CustomLabel title={t("Yükseklik")}>
                            {containeroutsidepartForm.height+" cm"}
                        </CustomLabel>
                        <CustomLabel title={t("Adet")}>
                            {containeroutsidepartForm.piece}
                        </CustomLabel>
                        <CustomLabel title={t("Toplam Ağırlık")}>
                            {containeroutsidepartForm.tonnage + " " + t("kg")}
                        </CustomLabel>
                        <CustomLabel title={t("Mal Değeri")}>
                            {containeroutsidepartForm.goodsPrice ? containeroutsidepartForm.goodsPrice + " " + containeroutsidepartForm.currencyUnitId : "-"}
                        </CustomLabel>
                        <CustomLabel title={t("Açıklama")}>
                            {containeroutsidepartForm.explanation ? containeroutsidepartForm.explanation : "-"}
                        </CustomLabel>
                    </div>
                </Col>
            </Row>
        </div >
    );
};

export default ContainerOutsideStep5;