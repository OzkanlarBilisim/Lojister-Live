import { Button } from 'antd'
import React, { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { Link } from 'react-router-dom'
import AntNotification from './AntNotification'

const PublicFooter = () => {
    const { t, i18n } = useTranslation("common");

    const [loading, setLoading] = useState(false)

    const liveSupport = () => {
        setLoading(true)
        setTimeout(() => {
            setLoading(false)
        }, 1500);
        AntNotification({ type: "info", message: t("Çok yakında!") })
    }

    return (
        <>
            <footer>
                <div className="footer-info">
                    <div className="footer-info-content">
                        {localStorage.getItem('currentLanguage') === "TURKISH" ?
                            <p>
                                Lojister, <b>"Türkiye İhracatçılar Meclisi"</b> ve <b>"Türk Ekonomi Bankası"</b> "ortaklığında oluşturulan"<b>"TİM & TEB Girişim Evi"</b>"tarafından desteklenmektedir."
                            </p>
                            :
                            localStorage.getItem('currentLanguage') === "ENGLISH" ?
                                <p>
                                    Logister is supported by <b>"TIM & TEB Enterprise House"</b>, established in partnership with the <b>"Turkish Exporters Assembly"</b> and the <b>"Turkish Economy Bank."</b>
                                </p>
                                : <></>
                        }
                        <div className="footer-info-image">
                            <img src="/assets/img/lojister-tim-teb.png" alt={t("Lojister - Tim-Teb Girişim Evi")} />
                        </div>
                    </div>
                </div>
                <div className="footer">
                    <div className="footer-content">
                        <div className="footer-left">
                            <img height="50px" src="/assets/img/logo.svg" alt="Lojister" />
                            <div style={{ display: "flex", gap: ".5rem" }}>
                                <a href="https://apps.apple.com/us/app/lojister/id1575330819" target="_blank" rel="noreferrer" >
                                    <img height="32px" src="/assets/img/ios.png" alt="" />
                                </a>
                                <a href="https://play.google.com/store/apps/details?id=com.lojister.app" target="_blank" rel="noreferrer">
                                    <img height="32px" src="/assets/img/android.png" alt="" />
                                </a>
                            </div>

                            <div style={{ display: "flex", gap: ".5rem" }}>
                                <img height="24px" src="/assets/img/master.png" alt="" />
                                <img height="24px" src="/assets/img/troy.png" alt="" />
                            </div>
                        </div>
                        <div className="footer-right">
                            <div className="footer-menu">
                                <h6>
                                    <i className="bi bi-chevron-double-right"></i>
                                    Lojister
                                </h6>
                                <ul>
                                    <li>
                                        <Link to="/about-us">{t("Hakkımızda")}</Link>
                                    </li>
                                    <li>
                                        <Link to="/contact">{t("İletişim")}</Link>
                                    </li>
                                </ul>
                            </div>
                            <div className="footer-menu">
                                <h6>
                                    <i className="bi bi-chevron-double-right"></i>
                                    {t("Yardım")}
                                </h6>
                                <ul>
                                    <li>
                                        <Link to="/faq">{t("Sıkça Sorulan Sorular")}</Link>
                                    </li>
                                </ul>
                            </div>
                            <div className="footer-menu">
                                <h6>
                                    <i className="bi bi-chevron-double-right"></i>
                                    {t("Sözleşme ve Belgeler")}
                                </h6>
                                <ul>
                                    <li>
                                        <Link to="/yetki-belgesi">{t("Yetki Belgesi")}</Link>
                                    </li>
                                    <li>
                                        <Link to="/gizlilik-ve-cerez-politikasi">{t("Gizlilik ve Çerez Politikası")}</Link>
                                    </li>
                                    <li>
                                        <Link to="/iptal-ve-iade-kosullari">{t("İptal ve İade Koşulları")}</Link>
                                    </li>
                                    <li>
                                        <Link to="/kvkk-hakkinda">{t("KVKK Hakkında")}</Link>
                                    </li>
                                </ul>
                            </div>
                            <div className="footer-menu">
                                <h6>
                                    <i className="bi bi-chevron-double-right"></i>
                                    {t("Destek Alın")}
                                </h6>
                                <div className="footer-support">
                                    <a href="tel:908508401056">
                                        <Button type="default" icon={<i className="bi bi-headset"></i>}>0850 840 10 56</Button>
                                    </a>
                                    <a href="mailto:info@lojister.com">
                                        <Button type="default" icon={<i className="bi bi-envelope"></i>}>
                                            info@lojister.com
                                        </Button>
                                    </a>
                                    <Button onClick={liveSupport} disabled={loading} type="default" icon={<i className="bi bi-chat-square-dots"></i>}>
                                        {t("Canlı Destek")}
                                    </Button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </footer >
            <div className="copyright">
                {t("2021 Lojister © | Tüm hakları saklıdır.")}
            </div>
        </>
    )
}

export default PublicFooter