import { Button, Col, Row, Divider } from 'antd';
import React from 'react'
import CustomTitle from '../../../../CustomTitle';
import { useTranslation } from 'react-i18next';


//FTL STEP 5
const ContainerOutsideStep5 = ({ handleClickPrevStep, containeroutsideForm, handleSubmitFtlContainerOutsideForm, isSubmitting }) => {

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
                        onClick={() => handleSubmitFtlContainerOutsideForm()}
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
                            {containeroutsideForm.startDate+" / "+containeroutsideForm.dueDate}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Şekli")}>
                            {
                                (containeroutsideForm.startDeliveryMethod === "d2d")? t("Kapıdan Kapıya"): ""+
                                (containeroutsideForm.startDeliveryMethod === "p2p")? t("Limandan Limana"): ""+
                                (containeroutsideForm.startDeliveryMethod === "d2p")? t("Kapıdan Limana"): ""+
                                (containeroutsideForm.startDeliveryMethod === "p2d")? t("Limandan Kapıya"): ""
                            } 
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Yapılacak Ülke")}>
                            {containeroutsideForm.startSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Yükleme Noktası")}>
                            {containeroutsideForm.startFullAddress}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Ülke")}>
                        {containeroutsideForm.dueSelectCountryName}
                        </CustomLabel>
                        <CustomLabel title={t("Teslimat Yapılacak Nokta")}>
                            {containeroutsideForm.dueFullAddress}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yük Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yük Tipi")}>
                        {containeroutsideForm.cargoTypeIdList !== null ?
                                t(containeroutsideForm.cargoTypeIdList)
                                :
                                t("Farketmez")
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Konteyner Tipi")}>
                        {containeroutsideForm.containerType}
                        </CustomLabel>
                        <CustomLabel title={t("Hs Kod")}>
                            {containeroutsideForm.hsCode}
                        </CustomLabel>
                        <CustomLabel title={t("Un Kod")}>
                            {containeroutsideForm.unCode}
                        </CustomLabel>
                        <CustomLabel title={t("Taşıma Şekli")}>
                            {containeroutsideForm.deliveryType}
                        </CustomLabel>
                        <CustomLabel title={t("Toplam Ağırlık")}>
                            {containeroutsideForm.tonnage + " " + t("TON")}
                        </CustomLabel>
                        <CustomLabel title={t("Mal Değeri")}>
                            {containeroutsideForm.goodsPrice ? containeroutsideForm.goodsPrice + " " + containeroutsideForm.currencyUnitId : "-"}
                        </CustomLabel>
                        <CustomLabel title={t("Açıklama")}>
                            {containeroutsideForm.explanation ? containeroutsideForm.explanation : "-"}
                        </CustomLabel>
                    </div>
                </Col>
            </Row>
        </div >
    );
};

export default ContainerOutsideStep5;