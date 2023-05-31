import React, { useEffect } from 'react'
import { useTranslation } from 'react-i18next';
import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';

function YetkiBelgesi() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Yetki Belgesi")

    useEffect(() => {
        window.scrollTo(0, 0)
    }, [])
    return (
        <>
            <PublicNavbar />
            <section className="page">
                <h4 className="page-title"><i className="bi bi-chevron-double-right"></i> {t("Yetki Belgesi")}</h4>
                <div className="page-content">
                    <iframe style={{ width: "100%", minHeight: "70vh", border: "none", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)" }} src="/assets/document/yetki-belgesi.pdf"></iframe>
                </div>
            </section>
            <PublicFooter />
        </>
    )
}

export default YetkiBelgesi;