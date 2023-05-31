import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next';
import { getAboutUsRequest } from '../api/controllers/about-us-controller';
import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';
import RequestError from '../components/RequestError';


const AboutUs = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Hakkımızda")

    const [aboutUs, setAboutUs] = useState({})

    const fetchAboutUs = async () => {
        try {
            let res = await getAboutUsRequest();

            if (res) {
                setAboutUs(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        window.scrollTo(0, 0)

        fetchAboutUs();
    }, [])

    return (
        <>
            <PublicNavbar />
            <section className="page">
                <h4 className="page-title"><i className="bi bi-chevron-double-right"></i> {t("Lojister Hakkında")}</h4>
                <img src="/assets/img/about-us.jpg" />
                <div className="page-content">
                    <div dangerouslySetInnerHTML={{
                        __html:
                            localStorage.getItem('currentLanguage') === 'TURKISH' ? aboutUs.tr_explanation :
                                localStorage.getItem('currentLanguage') === 'ENGLISH' ? aboutUs.en_explanation :
                                    aboutUs.tr_explanation
                    }} />
                </div>
            </section>
            <PublicFooter />
        </>
    )
};

export default AboutUs;