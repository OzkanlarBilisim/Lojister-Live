import { Col, Modal, Row, Form, Input, InputNumber, DatePicker, Tooltip, Button } from 'antd';
import moment from 'moment';
import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import {createClientAdvertisimentBidAbroudRequest } from '../api/controllers/client-advertisiment-bid-controller';
import locale from "antd/es/date-picker/locale/tr_TR";
import AntNotification from './AntNotification';
import { useNavigate } from 'react-router';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';

const CreateBid = ({ visible, setVisible, transportId, setFirstBid, revision, transport }) => {
    const { t, i18n } = useTranslation("common");

    const navigate = useNavigate();
    const [form] = Form.useForm();
    const currentUser = useSelector((state) => state.user);

    const defaultBidForm = {
        bid: null,
        expiration: null,
        explanation: "",
        status: null,
        adAbroud: transportId,
        companyId:  currentUser?.companyId,
        companyRate: currentUser?.commissionRate,
        companyName: currentUser?.companyCommercialTitle,
        adAbroud: {
            id: transportId
        },
        userDriver: {
            id: currentUser?.id
        }

    }

    const [bidForm, setBidForm] = useState(defaultBidForm)
    const [isLoading, setIsLoading] = useState(false)

    const onSubmit = async () => {
        setIsLoading(true)
        try {
            console.log(bidForm);
            let res = await createClientAdvertisimentBidAbroudRequest(bidForm);
            if (res) {
                handleCancel();
                setFirstBid(false);
                AntNotification({ type: "success", message: t("Başarılı"), description: t("Teklifiniz başarıyla gönderildi.") })
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false)
    };

    const handleCancel = () => {
        form.setFieldsValue(defaultBidForm)
        setBidForm(defaultBidForm);
        setVisible(false);
        if (revision) {
            navigate('/driver/my-offers')
        }
    }

    const range = (start, end) => {
        const result = [];
        for (let i = start; i < end; i++) {
            result.push(i);
        }
        return result;
    };

    const disabledDateTime = (current) => {
        if (moment(current).format("YYYY-MM-DD") === moment().format("YYYY-MM-DD")) {
            return (
                {
                    disabledHours: () => range(0, 24).splice(0, moment().add(1, 'hours').format('HH')),
                    disabledMinutes: () => moment(current).format('HH') === moment().add(2, 'hours').format('HH') && range(0, 60).splice(0, moment().format('mm')),
                }
            )
        }
    };

    return (
        currentUser?.role === "ROLE_DRIVER" ?
            <Modal
                title={t("Hemen teklif oluşturun!")}
                visible={visible}
                centered
                footer={
                    <Button Button loading={isLoading} disabled={!bidForm.bid
                    } type="primary" onClick={() => onSubmit()}> {t("Teklifi gönder")}</Button >
                }
                onCancel={handleCancel}
                okText={t("Teklifi gönder!")}
                cancelText={t("İptal Et")}
                width={700}
            >
                {
                    < Form
                        form={form}
                        layout="vertical"
                        labelCol={{ span: 24 }}
                        initialValues={bidForm}
                        onFinish={onSubmit}
                        scrollToFirstError={true}
                    >
                        <Row gutter={[24, 0]}>
                            <Col xs={24} md={12}>
                                <Form.Item
                                    label={t("Vermek istediğiniz teklif")}
                                    name="bid"
                                >
                                    <NumberFormat
                                        customInput={Input}
                                        onValueChange={({ value }) => setBidForm({ ...bidForm, bid: value })}
                                        style={{ width: "100%" }}
                                        size="large"
                                        placeholder={t("Teklif tutarını yazın")}
                                        addonAfter={transport?.regionAdvertisementType === 'DOMESTIC' ? t("₺ + KDV") : "$"}
                                        min={0}
                                    />
                                </Form.Item>
                                <Form.Item
                                    label={<span>
                                        {t("Teklif geçerlilik süresi")}
                                        <Tooltip title={t("Geçerlilik süresi açıklaması")}>
                                            <span> <i className="bi bi-info-circle-fill"></i></span>
                                        </Tooltip>
                                    </span>}
                                    name="expiration"
                                >
                                    <DatePicker
                                        showNow={false}
                                        style={{ width: "100%" }}
                                        size="large"
                                        format="DD/MM/YYYY HH:mm"
                                        placeholder={t("İlan için süre belirlemek isterseniz süreyi seçin")}
                                        disabledDate={current => moment().startOf('day') >= current}
                                        disabledTime={disabledDateTime}
                                        showTime={{ defaultValue: moment("LT", "HH:mm") }}
                                        onChange={e => setBidForm({ ...bidForm, expiration: moment(e).format("YYYY-MM-DD" + "T" + "HH:mm:ss") })}
                                        locale={{
                                            ...locale,
                                            lang: {
                                                ...locale.lang,
                                                ok: t("Seç"),
                                            }
                                        }}
                                    />
                                </Form.Item>
                            </Col>
                            <Col xs={24} md={12}>
                                <div style={{ display: "flex", flexDirection: "column", gap: ".5rem", justifyContent: "center", height: "100%" }}>
                                    <div>
                                        <div style={{ display: "flex", justifyContent: "space-between" }}>
                                            <span>{t("Komisyon")}
                                                <Tooltip title={t("Komisyon açıklaması")}>
                                                    <span> <i className="bi bi-info-circle-fill"></i></span>
                                                </Tooltip>
                                            </span>
                                            <span>{(bidForm.bid * (currentUser?.commissionRate / 100)).toFixed(2)}{transport?.regionAdvertisementType === 'DOMESTIC' ? t("₺ + KDV") : "$"}</span>
                                        </div>
                                        {transport?.regionAdvertisementType === 'DOMESTIC' && <div style={{ display: "flex", justifyContent: "space-between" }}>
                                            <span>{t("Tevkifat")}
                                                <Tooltip title={t("Tevkifat açıklaması")}>
                                                    <span> <i className="bi bi-info-circle-fill"></i></span>
                                                </Tooltip>
                                            </span>
                                            <span>{bidForm.bid >= 1695 ? (bidForm.bid * 0.18 * 0.2).toFixed(2) : 0}₺</span>
                                        </div>}
                                    </div>
                                    <div style={{ backgroundColor: "var(--blue-light)", color: "var(--blue)", padding: ".75rem 1rem", borderRadius: ".5rem" }}>
                                        <p>{t("Hesabınıza yatacak net tutar")}
                                            {transport?.regionAdvertisementType === 'DOMESTIC' && <Tooltip title={t("KDV dahil net fiyattır.")}>
                                                <span> <i className="bi bi-info-circle-fill"></i></span>
                                            </Tooltip>}
                                        </p>
                                        <b>
                                            {
                                                bidForm.bid ?
                                                    (transport?.regionAdvertisementType === 'DOMESTIC' ?
                                                        (
                                                            bidForm.bid >= 1695 ? //Tevkifat
                                                                parseFloat(bidForm.bid * 1.18 - (bidForm.bid * (currentUser?.commissionRate / 100) * 1.18) - (bidForm.bid * 0.18 * 0.2)).toFixed(2)
                                                                :
                                                                parseFloat(bidForm.bid * 1.18 - (bidForm.bid * (currentUser?.commissionRate / 100) * 1.18)).toFixed(2)
                                                        ) :
                                                        (
                                                            parseFloat(bidForm.bid - (bidForm.bid * (currentUser?.commissionRate / 100))).toFixed(2)
                                                        ))
                                                    : "0.00"
                                            }
                                            {transport?.regionAdvertisementType === 'DOMESTIC' ? " ₺" : " $"}
                                        </b>
                                    </div>
                                </div>
                            </Col>
                            <Col span={24}>
                                <Form.Item
                                    label={t("Açıklama")}
                                    name="explanation"
                                    rules={[{ max: 255, message: t("En fazla 255 karakter yazabilirsiniz!") }]}
                                >
                                    <Input.TextArea
                                        size="large"
                                        rows={2}
                                        placeholder={t("Eklemek istediklerinizi bu alana yazabilirsiniz.")}
                                        onChange={e => setBidForm({ ...bidForm, explanation: e.target.value })}
                                    />
                                </Form.Item>
                            </Col>
                        </Row>
                    </Form >
                }
            </Modal > :
            <Modal
                title={t("Hemen teklif oluşturun!")}
                visible={visible}
                onCancel={handleCancel}
                centered
                width={700}
                footer={false}
            >
                <div className="create-ad-wrapper">
                    <div className="ad-finish-card">
                        <Row align="middle" gutter={[24, 24]}>
                            <Col xs={24} md={8}>
                                <img className="ad-finish-image" src="/assets/img/not-login.svg" alt="" />
                            </Col>
                            <Col xs={24} md={16}>
                                <p className="ad-finish-title">{t("Fırsatlar için son adım!")}</p>
                                <p className="ad-finish-description">{t("Teklifinizin yayınlanabilmesi ve daha fazla ilana erişebilmek için hemen sizde üye olun. Eğer üyeyseniz giriş yapın!")}</p>
                                <div style={{ marginTop: "1rem", display: "flex", gap: ".5rem" }}>
                                    <Button style={{ color: "var(--primary)", borderColor: "var(--primary)" }} onClick={() => navigate("/login")}>{t("Giriş Yap")}</Button>
                                    <Button onClick={() => navigate("/register?registerType=driver")} type="primary">{t("Üye Ol")}</Button>
                                </div>
                            </Col>
                        </Row>
                    </div>
                </div>
            </Modal>
    )
}

export default CreateBid;