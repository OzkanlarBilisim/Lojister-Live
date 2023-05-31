import { Button, Col, Result, Row } from 'antd';
import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { endConfirmationTransportRequest, getTransportByTransportCodeRequest, startConfirmationTransportRequest } from '../api/controllers/client-transport-process-controller';
import AntNotification from '../components/AntNotification'
import RequestError from '../components/RequestError';

const AuthDeliver = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Teslimat Onayı")

    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const transportCode = searchParams.get("transportCode");
    const token = searchParams.get("token");
    const type = searchParams.get("type");
    const [transport, setTransport] = useState({});
    const [isAuth, setIsAuth] = useState(false);

    const Label = ({ title, text }) => (
        <div>
            <p style={{ fontSize: 12, opacity: .7 }}>{title}</p>
            <p>{text}</p>
        </div>
    );

    const fetchTransport = async () => {
        try {
            let res = await getTransportByTransportCodeRequest(transportCode);
            if (res) {
                setTransport(res.data.acceptedClientAdvertisementBid.clientAdvertisement);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    useEffect(() => {
        if (transportCode) {
            fetchTransport();
        } else {
            navigate("/")
        }
    }, [transportCode]);

    const startingConfirmationTransport = async () => {
        try {
            let res = await startConfirmationTransportRequest(token);
            if (res) {
                setIsAuth(true);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const endConfirmationTransport = async () => {
        try {
            let res = await endConfirmationTransportRequest(token);
            if (res) {
                setIsAuth(true);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    return (
        <div style={{
            width: "100%",
            height: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            background: "url(/assets/img/auth-bg.png)",
            backgroundPosition: "center center",
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover"
        }}>
            {
                isAuth ?
                    <Result
                        style={{
                            backgroundColor: "rgba(255, 255, 255, .8)",
                            borderRadius: "2rem",
                            backdropFilter: "blur(2rem)",
                            boxShadow: "0px 8px 24px rgba(0, 0, 0, 0.1)",
                            maxWidth: 700,
                            width: "90%",
                        }}
                        status="success"
                        title={t("Yükünüz başarıyla teslim edildi!")}
                        subTitle={
                            <p>
                                <b>{transportCode}</b>{t("numaralı taşımanızın teslimatı başarıyla gerçekleştirilmiştir. Bu süreçte yaşadığınız deneyimi ilan sayfasında")}  <b>{t("Süreci Değerlendir")}</b> {t("alanından değerlendirmeyi unutmayın!")}
                            </p>}
                        extra={[
                            <Button onClick={() => navigate("/")} type="primary" key="buy">{t("Lojister.com'a git")}</Button>,
                        ]}
                    /> :
                    <div style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "2rem",
                        alignItems: "center",
                        maxWidth: 700,
                        width: "90%",
                        backgroundColor: "rgba(255, 255, 255, .8)",
                        boxSizing: "border-box",
                        padding: "2rem",
                        borderRadius: "2rem",
                        backdropFilter: "blur(2rem)",
                        boxShadow: "0px 8px 24px rgba(0, 0, 0, 0.1)",
                    }}>
                        <img src="/assets/img/logo.svg" alt="" style={{ height: 60 }} />
                        <Row gutter={[24, 24]} style={{ width: "100%" }}>
                            <Col xs={24} md={12}>
                                <div style={{ display: "flex", flexDirection: "column", gap: "1rem" }}>
                                    <h6 style={{ fontSize: 14, fontWeight: 600 }}>{t("Yükleme Bilgileri")}</h6>
                                    <Label
                                        title={t("Şirket Ünvanı")}
                                        text={transport?.startRecipient?.commercialTitle}
                                    />
                                    <Label
                                        title={t("Yetkili İsim Soyisim")}
                                        text={transport?.startRecipient?.firstName + " " + transport?.startRecipient?.lastName}
                                    />
                                    <Label
                                        title={t("Adres")}
                                        text={transport?.startingAddress?.fullAddress + " " + transport?.startingAddress?.neighborhood + " - " + transport?.startingAddress?.district + "/" + transport?.startingAddress?.province}
                                    />
                                </div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div style={{ display: "flex", flexDirection: "column", gap: "1rem" }}>
                                    <h6 style={{ fontSize: 14, fontWeight: 600 }}>{t("Teslimat Bilgileri")}</h6>
                                    <Label
                                        title={t("Şirket Ünvanı")}
                                        text={transport?.dueRecipient?.commercialTitle}
                                    />
                                    <Label
                                        title={t("Yetkili İsim Soyisim")}
                                        text={transport?.dueRecipient?.firstName + " " + transport?.dueRecipient?.lastName}
                                    />
                                    <Label
                                        title={t("Adres")}
                                        text={transport?.dueAddress?.fullAddress + " " + transport?.dueAddress?.neighborhood + " - " + transport?.dueAddress?.district + "/" + transport?.dueAddress?.province}
                                    />
                                </div>
                            </Col>
                        </Row>
                        <p style={{ color: "var(--primary)", textAlign: "center" }}>{t("Yukarıda bilgileri bulunan taşımanın")} {type === "STARTING" ? <b>{t("teslim alınmasını")}</b> : type === "ENDING" ? <b>{t("teslim alınmasını")} </b> : ""}{t("onaylıyor musunuz?")} </p>
                        <div style={{ display: "flex", gap: "1rem", marginTop: "-1rem" }}>
                            <Button onClick={() =>
                                navigate("/")
                            }>{t("Hayır")}</Button>
                            <Button onClick={() =>
                                type === "STARTING" ?
                                    startingConfirmationTransport() :
                                    type === "ENDING" ?
                                        endConfirmationTransport() :
                                        null
                            } type="primary">{t("Evet, onaylıyorum")}</Button>
                        </div>
                    </div>
            }
        </div >
    )
}

export default AuthDeliver;