import React from 'react'
import { useTranslation } from 'react-i18next';
import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';

function GizlilikVeCerezPolitikasi() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Gizlilik ve Çerez Politikası")

    return (
        <>
            <PublicNavbar />
            <section className="page">
                <h4 className="page-title"><i className="bi bi-chevron-double-right"></i>{t("Gizlilik ve Çerez Politikası")}</h4>
                <div className="page-content">
                    <iframe style={{ width: "100%", minHeight: "70vh", border: "none", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)" }} src="/assets/document/gizlilik-ve-cerez-politikasi.pdf"></iframe>
                </div>
            </section>
            <PublicFooter />
        </>
    )
}

export default GizlilikVeCerezPolitikasi;