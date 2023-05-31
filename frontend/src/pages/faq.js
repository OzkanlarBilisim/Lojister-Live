import React, { useEffect } from 'react'
import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';
import FAQ from '../components/FAQ';
import { useTranslation } from 'react-i18next';

const Faq = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Sıkça Sorulan Sorular")
    return (
        <>
            <PublicNavbar />
            <section className="page">
                <FAQ />
            </section>
            <PublicFooter />
        </>
    )
}

export default Faq;