import { Button, Col, Row, Divider } from 'antd';
import React, { useState } from 'react'
import CustomTitle from '../../CustomTitle';
import moment from 'moment';
import { useTranslation } from 'react-i18next';
import 'moment/locale/tr'
import 'moment/locale/en-nz'

//CONTAINER STEP 5
const ContainerStep5 = ({ handleClickPrevStep, containerForm, handleSubmitContainerForm, adStartingDate, adDueDate, isSubmitting }) => {
    const { t, i18n } = useTranslation("common");

    const containerTypes = [{ value: "TWENTY", title: "20'lik" }, { value: "FOURTY", title: "40'lık" }, { value: "FOURTY_HC", title: "40'lık HC" }, { value: "FOURTY_FIVE", title: "45'lik" }]

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
                        onClick={() => handleSubmitContainerForm()}
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
                        <CustomTitle small>{t("Konteyner Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Şirket Ünvanı")}>
                            {containerForm.containerRecipient.commercialTitle}
                        </CustomLabel>
                        <CustomLabel title={t("Gümrük Telefon Numarası")}>
                            {containerForm.selectedCountryCodeOfficer + containerForm.customsOfficerPhone}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili İsmi Soyismi")}>
                            {containerForm.containerRecipient.firstName + " " + containerForm.containerRecipient.lastName}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili Telefon Numarası")}>
                            {containerForm.containerRecipient.selectedCountryCode + containerForm.containerRecipient.phoneNumber}
                        </CustomLabel>
                        <CustomLabel title={t("Adres")}>
                            {containerForm.containerAddress.fullAddress + " " + containerForm.containerAddress.neighborhood + " - " + containerForm.containerAddress.district + "/" + containerForm.containerAddress.province}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yükleme Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Yükleme Tarihi")}>
                            {moment(adStartingDate).format("LLL")}
                        </CustomLabel>
                        <CustomLabel title={t("Şirket Ünvanı")}>
                            {containerForm.startRecipient.commercialTitle}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili İsmi Soyismi")}>
                            {containerForm.startRecipient.firstName + " " + containerForm.startRecipient.lastName}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili Telefon Numarası")}>
                            {containerForm.startRecipient.selectedCountryCode + containerForm.startRecipient.phoneNumber}
                        </CustomLabel>
                        <CustomLabel title={t("Adres")}>
                            {containerForm.startingAddress.fullAddress + " " + containerForm.startingAddress.neighborhood + " - " + containerForm.startingAddress.district + "/" + containerForm.startingAddress.province}
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
                            {containerForm.dueRecipient.commercialTitle}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili İsmi Soyismi")}>
                            {containerForm.dueRecipient.firstName + " " + containerForm.dueRecipient.lastName}
                        </CustomLabel>
                        <CustomLabel title={t("Yetkili Telefon Numarası")}>
                            {containerForm.dueRecipient.selectedCountryCode + containerForm.dueRecipient.phoneNumber}
                        </CustomLabel>
                        <CustomLabel title={t("Adres")}>
                            {containerForm.dueAddress.fullAddress + " " + containerForm.dueAddress.neighborhood + " - " + containerForm.dueAddress.district + "/" + containerForm.dueAddress.province}
                        </CustomLabel>
                    </div>
                </Col>
                <Col xs={24} md={12} lg={6}>
                    <div style={{ boxSizing: "border-box", display: "flex", flexDirection: "column", padding: "1rem", backgroundColor: "#fff", borderRadius: "1rem", gap: "1rem" }}>
                        <CustomTitle small>{t("Yük Bilgileri")}</CustomTitle>
                        <Divider style={{ margin: 0 }} />
                        <CustomLabel title={t("Konteyner Tipi")}>
                            {
                                containerForm.containerType ? containerTypes.find((v) => v.value === containerForm.containerType).title : "-"
                            }
                        </CustomLabel>
                        <CustomLabel title={t("Toplam Ağırlık")}>
                            {containerForm.tonnage / 1000 + " " + t("TON")}
                        </CustomLabel>
                        <CustomLabel title={t("Açıklama")}>
                            {containerForm.explanation ? containerForm.explanation : "-"}
                        </CustomLabel>
                    </div>
                </Col>
            </Row>
        </div >
    );
};

export default ContainerStep5;