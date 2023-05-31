import React from 'react';
import CustomTitle from './CustomTitle';
import { CustomButton } from './CustomButton';
import { Divider } from 'antd';
import { useTranslation } from 'react-i18next';

function SupportBanner({ style }) {
    const { t, i18n } = useTranslation("common");

    return (
        <>
            <Divider />
            <div className="support-banner" style={{ marginTop: "2rem", ...style }}>
                <img src="/assets/img/support.svg" alt="" />
                <div className="contact">
                    <p> {t("Lojister ile lojistik hizmetlerini zahmetsiz ve güvenli bir şekilde planlayın, kolayca takip edin!")}</p>
                    <div className="button-group">
                        <CustomTitle style={{ margin: 0 }} small> {t("Destek Alın")} </CustomTitle>
                        <CustomButton
                            color="primary-light"
                            icon={<i className="bi bi-headset" style={{ marginRight: ".5rem" }}></i>}
                        >0850 840 10 56
                        </CustomButton>
                        <CustomButton
                            color="primary-light"
                            icon={<i className="bi bi-envelope" style={{ marginRight: ".5rem" }}></i>}
                        >info@lojister.com
                        </CustomButton>
                        <CustomButton
                            color="primary-light"
                            icon={<i className="bi bi-chat-square-dots" style={{ marginRight: ".5rem" }}></i>}
                        > {t("Canlı Destek")}
                        </CustomButton>
                    </div>
                </div>
            </div>
        </>
    )
}

export default SupportBanner;