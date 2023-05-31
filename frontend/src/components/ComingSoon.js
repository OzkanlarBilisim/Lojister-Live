import React from 'react'
import { useTranslation } from 'react-i18next';

const ComingSoon = () => {
    const { t, i18n } = useTranslation("common");

    return (
        <div style={{
            width: "100%",
            height: "100%",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            gap: "1rem"
        }}>
            <img style={{
                maxWidth: "500px",
                width: "90%",
            }} src="/assets/img/coming-soon.svg" alt="" />
            <p style={{
                fontSize: "1.5rem"
            }}>{t("Çok yakında...")}</p>
        </div>
    )
}

export default ComingSoon