import React from 'react'
import { Button, Col, Row } from 'antd';
import { useNavigate } from 'react-router';
import { useTranslation } from 'react-i18next';

const NotLogin = () => {
    const { t, i18n } = useTranslation("common");

    const navigate = useNavigate();
    return (
        <div className="create-ad-wrapper">
            <div className="ad-finish-card">
                <Row align="middle" gutter={[24, 24]}>
                    <Col xs={24} md={8}>
                        <img className="ad-finish-image" src="/assets/img/not-login.svg" alt="" />
                    </Col>
                    <Col xs={24} md={16}>
                        <p className="ad-finish-title"> {t("İlanınızı tamamlamak için son adım!")}</p>
                        <p className="ad-finish-description"> {t("İlanın yayınlanabilmesi ve gelen teklifleri değerlendirebilmek için hemen kolayca üye olun veya giriş yapın!")}</p>
                        <div style={{ marginTop: "1rem", display: "flex", gap: ".5rem" }}>
                            <Button style={{ color: "var(--primary)", borderColor: "var(--primary)" }} onClick={() => navigate("/login")}> {t("Giriş Yap")}</Button>
                            <Button onClick={() => navigate("/register")} type="primary"> {t("Üye Ol")}</Button>
                        </div>
                    </Col>
                </Row>
            </div>
        </div>
    )
}

export default NotLogin;