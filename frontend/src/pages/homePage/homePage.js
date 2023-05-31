import { Link } from 'react-router-dom';
import { Button, Col, Modal, Row } from 'antd';
import MapBoxForTransports from '../../components/MapBoxForTransports';
import PublicNavbar from '../../components/PublicNavbar';
import PublicFooter from '../../components/PublicFooter';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import MapBoxForTransportsAbroad from '../../components/MapBoxForTransportsAbroad';
import HomepageCreateAd from '../../components/createTransportSteps/HomepageCreateAd';

const Homepage = () => {
    
    const [createAdModal, setCreateAdModal] = useState(false)
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Ana Sayfa")

    const [transportRegion, setTransportRegion] = useState("inland")

    const IntroductionCard = ({ imageRight, imageSrc, title, decsription, buttonText, buttonLink }) => {

        return (
            <div className="introduction-card">
                <Row align="middle" style={{ flexDirection: imageRight ? "row-reverse" : "row" }}>
                    <Col xs={24} md={10}>
                        <div className="card-img" style={{ backgroundImage: imageSrc, filter: imageRight ? "drop-shadow(-10px 0px 24px rgba(0, 0, 0, 0.16))" : "drop-shadow(10px 0px 24px rgba(0, 0, 0, 0.16))" }} />
                    </Col>
                    <Col xs={24} md={14}>
                        <div className="card-content">
                            <h2 className="card-title">{t(title)}</h2>
                            <p className="card-text">{t(decsription)}</p>
                            <div>
                                    <Button onClick={() => setCreateAdModal(true)} className="card-button">{t(buttonText)}</Button>
                            </div>
                        </div>
                    </Col>
                </Row>
            </div>
        );
    };



    return (
        <>
            <PublicNavbar />

            <section id="map" className={transportRegion === 'abroad' && 'abroad'}>
                <div className="slogan">
                    <p><b>{t("Yük & Taşıyıcı bulmak sorun olmaktan çıkıyor!")} </b>{t("Lojistik işlerinizi güvenle Lojister’dan yönetin.")} </p>
                </div>

                {transportRegion === 'inland' ?
                    <div className="map-area">
                        <MapBoxForTransports scrollControl={true} setTransportRegion={setTransportRegion} />
                    </div> :
                    transportRegion === 'abroad' ?
                        <MapBoxForTransportsAbroad setTransportRegion={setTransportRegion} /> : <></>
                }

            </section>

            <section id="introduction">
                <IntroductionCard
                    imageSrc="url(/assets/img/introduction/1.png)"
                    title="Lojistik ihtiyacınızı kolayca yönetin, takip edin ve raporlayın!"
                    decsription="Lojister’ın sunduğu yük - taşıyıcı bulma sistemi ile hem zamandan hem de maliyetten tasarruf ederek diğer işlerinize daha rahat odaklanırsınız."
                    buttonText="İlan oluşturmaya başlayın"
                    buttonLink="/client/create-transport"
                />

                <img height="64px" src="/assets/img/introduction/divider.svg" alt="" />


                <IntroductionCard
                    imageRight
                    imageSrc="url(/assets/img/introduction/2.png)"
                    title="Artık seferlerden boş dönmek yok!"
                    decsription="Lojister’ın sunduğu yük - taşıyıcı bulma sistemi ile gidişte veya dönüşte yükünüzü kolayca bulabilirsiniz. Ayrıca ödemelerinizi güvenle alarak tahsilat derdiyle uğraşmazsınız."
                    buttonText="İlanlara göz atın"
                    buttonLink="/driver/transports"
                />

                <img height="64px" src="/assets/img/introduction/divider.svg" alt="" style={{ transform: "scale(-1, 1)" }} />

                <IntroductionCard
                    imageSrc="url(/assets/img/introduction/3.png)"
                    title="Soru işaretleri olmadan, güvenli lojistik!"
                    decsription="Kullanıcı dostu arayüzü ve sigortalatma gibi birçok özelliğiyle taşıma işlemlerinizi güvenle Lojister üzerinden gerçekleştirin."
                    buttonText="Hemen üye olun"
                    buttonLink="/register"
                />
            </section>

            <section id="partners">
                <h4 className="partners-title">
                    <i style={{ marginRight: "6px" }} className="bi bi-chevron-double-right"></i>
                    {t("Partnerlerimiz")}
                </h4>

                <Row gutter={[24, 24]}>
                    <Col xs={12} md={6}>
                        <a href="https://www.horoz.com.tr" target="_blank" rel="noreferrer" className="partner-card">
                            <img src="/assets/img/partners/horoz-lojistik.png" alt="Horoz Lojistik" />
                        </a>
                    </Col>

                    <Col xs={12} md={6}>
                        <a href="https://www.helmetbroker.com" target="_blank" rel="noreferrer" className="partner-card">
                            <img src="/assets/img/partners/helmet-broker.png" alt="Helmet Broker" />
                        </a>
                    </Col>

                    <Col xs={12} md={6}>
                        <a href="https://aselsis.com" target="_blank" rel="noreferrer" className="partner-card">
                            <img src="/assets/img/partners/aselsis.png" alt="Aselsis" />
                        </a>
                    </Col>

                    <Col xs={12} md={6}>
                        <a href="https://tim.org.tr/tr/duyurular-tim-teb-girisim-evi" target="_blank" rel="noreferrer" className="partner-card">
                            <img src="/assets/img/partners/tim-teb.png" alt="Tim Teb Girişim Evi" />
                        </a>
                    </Col>
                </Row>
            </section>

            <PublicFooter />
            
            <Modal width={1000} centered title={t("Hemen ilan oluşturun!")} visible={createAdModal} /* onOk={handleOk} */ onCancel={() => setCreateAdModal(false)} footer={null}>
                <HomepageCreateAd />
            </Modal>
        </>
    );
};

export default Homepage;