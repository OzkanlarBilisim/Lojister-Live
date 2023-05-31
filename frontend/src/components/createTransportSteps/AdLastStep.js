import { Col, Row } from 'antd';
import React from 'react'
import { useTranslation } from 'react-i18next';


//LAST STEP
const AdLastStep = () => {
    const { t, i18n } = useTranslation("common");/* */

    return (
        <div className="create-ad-wrapper">
            <div className="ad-finish-card">
                <Row align="middle" gutter={[24, 24]}>
                    <Col xs={24} md={8}>
                        <img className="ad-finish-image" src="/assets/img/finish-success.svg" alt="" />
                    </Col>
                    <Col xs={24} md={16}>
                        <p className="ad-finish-title"> {t("Tebrikler!")}</p>
                        <p className="ad-finish-sub-title"> {t("İlanınınız başarıyla oluşturuldu.")}</p>
                        <p className="ad-finish-description"> {t("İlanın yayına alınabilmesi için yöneticilerimiz tarafından onay alması gerekmektedir. İlan onay işleminin ardından hemen yayına alınacaktır. Bu işlem ortalama 1-2 saat sürmektedir.")}</p>
                    </Col>
                </Row>
            </div>
            <div className="ad-finish-card bg-white">
                <Row align="middle" gutter={[24, 24]}>
                    <Col xs={24} md={16}>
                        <p className="ad-finish-title"> {t("Taşımanızı daha güvenli hale getirin!")}</p>
                        <p className="ad-finish-sub-title"> {t("Sunduğumuz avantajlı sigorta paketleriyle gözünüz arkada kalmasın.")}</p>
                        <p className="ad-finish-description"> {t("Ödeme işleminizi yaparken size özel sunduğunuz avantajlı sigorta paketlerinden birini seçerek taşımanızı güvenli hale getirin.")}</p>
                    </Col>
                    <Col xs={24} md={8}>
                        <img className="ad-finish-image" src="/assets/img/finish-security.svg" alt="" />
                    </Col>
                </Row>
            </div>
        </div >
    );
};

export default AdLastStep;