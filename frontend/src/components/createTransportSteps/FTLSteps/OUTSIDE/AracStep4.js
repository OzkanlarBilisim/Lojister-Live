import { Button, Col, Row, Divider } from 'antd';
import React from 'react'
import CustomTitle from '../../../CustomTitle';
import { useTranslation } from 'react-i18next';


//FTL STEP 5
const AracStep5 = ({ handleClickPrevStep, aracForm, setAracForm, handleSubmitaracForm, adStartingDate, adDueDate, isSubmitting }) => {

    const { t } = useTranslation("common");/* {t()} */

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
                        onClick={() => handleSubmitaracForm()}
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
                            {aracForm.startDate+" / "+aracForm.dueDate}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Şekli")}>
                            {
                                (aracForm.startDeliveryMethod === "d2d")? t("Kapıdan Kapıya"): ""+
                                (aracForm.startDeliveryMethod === "p2p")? t("Limandan Limana"): ""+
                                (aracForm.startDeliveryMethod === "d2p")? t("Kapıdan Limana"): ""+
                                (aracForm.startDeliveryMethod === "p2d")? t("Limandan Kapıya"): ""
                            } 
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Yapılacak Ülke")}>
                            {aracForm.startSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Noktası")}>
                            {aracForm.startFullAddress}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Ülke")}>
                        {aracForm.dueSelectCountryName}
                        </CustomLabel>
                    <CustomLabel title={t("Teslimat Yapılacak Nokta")}>
                            {aracForm.dueFullAddress}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yük Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yük Tipi")}>
                        {aracForm.cargoTypeIdList !== null ?
                                t(aracForm.cargoTypeIdList)
                                :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Paketleme Tipi")}>
                        {aracForm.packagingTypeId !== null ?
                                t(aracForm.packagingTypeId) 
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Şekli")}>
                            {aracForm.loadTypeIdList !== null ?
                                t(aracForm.loadTypeIdList)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Hs Kod")}>
                            {aracForm.hsCode}
                        </CustomLabel>
                        <CustomLabel title={t("Un Kod")}>
                            {aracForm.unCode}
                        </CustomLabel>
                        <CustomLabel title={t("Taşıma Şekli")}>
                            {aracForm.deliveryType}
                        </CustomLabel>
                        <CustomLabel title={t("Toplam Ağırlık")}>
                            {aracForm.tonnage + " " + t("TON")}
                        </CustomLabel>
                        <CustomLabel title={t("Mal Değeri")}>
                            {aracForm.goodsPrice ? aracForm.goodsPrice + " " + aracForm.currencyUnitId : "-"}
                        </CustomLabel>
                        <CustomLabel title={t("Açıklama")}>
                            {aracForm.explanation ? aracForm.explanation : "-"}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Araç Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Araç Tipi")}>
                            {aracForm.vehicleTypeIdList !== null ?
                                t(aracForm.vehicleTypeIdList)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Araç Sayısı")}>
                            {aracForm.vehicleCount + " " + t("Adet")}
                        </CustomLabel>
                        <CustomLabel title={t("Kasa Tipi")}>
                            {aracForm.trailerTypeIdList !== null ?
                                t(aracForm.trailerTypeIdList) 
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Zemin Tipi")}>
                            {aracForm.trailerFloorTypeIdList !== null ?
                                t(aracForm.trailerFloorTypeIdList)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Kasa Özellikleri")}>
                            {aracForm.trailerFeatureName !== "" ?
                                    t(aracForm.trailerFeatureName)
                                    :
                                    t("Farketmez")
                            }
                        </CustomLabel>
                    </div>
                </Col>
            </Row>
        </div >
    );
};

export default AracStep5;