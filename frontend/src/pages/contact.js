import React, { useEffect, useState } from 'react'
import { Col, Input, Row, Form, Button, Divider } from 'antd';
import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';
import AntNotification from '../components/AntNotification';
import { createContactMailRequest } from '../api/controllers/contact-controller';
import RequestError from '../components/RequestError';
import { useTranslation } from 'react-i18next';

const Contact = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("İletişim")

    const [form, setForm] = useState({
        email: "",
        message: "",
        name: "",
    })

    const onSubmit = async () => {
        try {
            let res = await createContactMailRequest(form);
            if (res) {
                AntNotification({ type: "success", message: t("Başarılı"), description: t("Mesajınız başarıyla iletildi.") })
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        window.scrollTo(0, 0)

    }, [])

    return (
        <>
            <PublicNavbar />
            <section className="page">
                <h4 className="page-title"><i className="bi bi-chevron-double-right"></i>{t("İletişim")} </h4>
                <div className="page-content">
                    <Row gutter={[24, 24]}>
                        <Col xs={24} md={12}>
                            <ul className="contact">
                                <li>
                                    <h6>ÖZKANLAR BİLİŞİM İÇ VE DIŞ TİCARET LİMİTED ŞİRKETİ</h6>
                                </li>
                                <li>
                                    <i className="bi bi-geo-alt"></i>
                                    {t("İNCİLİPINAR MAHALLESİ FEVZİ ÇAKMAK BULVARI NO:280/A ÖZKANLAR PETROL SİTESİ PAMUKKALE/DENİZLİ")}
                                </li>
                                <li>
                                    <i className="bi bi-envelope"></i>
                                    info@lojister.com
                                </li>
                                <Divider />
                                <Form initialValues={form} layout="vertical" labelCol={{ span: 24 }} onFinish={onSubmit} scrollToFirstError={true}>
                                    <Form.Item name="name" label={t("Adınız")}>
                                        <Input onChange={(e) => setForm({ ...form, name: e.target.value })}
                                            size="large" placeholder={t("Adınızı yazın")} />
                                    </Form.Item>
                                    <Form.Item name="email" label={t("Mail adresiniz")}>
                                        <Input onChange={(e) => setForm({ ...form, email: e.target.value })} size="large" type="email" placeholder={t("Mail adresinizi yazın")} />
                                    </Form.Item>
                                    <Form.Item name="message" label={t("Mesajınız")}>
                                        <Input.TextArea onChange={(e) => setForm({ ...form, message: e.target.value })} rows="2" size="large" placeholder={t("Mesajınızı yazın")} />
                                    </Form.Item>
                                    <Form.Item>
                                        <Button htmlType="submit" type="primary">{t("Gönder")}</Button>
                                    </Form.Item>
                                </Form>
                            </ul>
                        </Col>
                        <Col xs={24} md={12}>
                            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3154.524368330517!2d29.100238950962513!3d37.75430122101074!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x14c73f2a9515526d%3A0xc33d7ff429783fc7!2zw5Z6a2FubGFyIEJpbGnFn2lt!5e0!3m2!1str!2str!4v1658299461716!5m2!1str!2str" style={{ width: "100%", minHeight: 450, height: "100%", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)", border: 0 }} allowFullScreen="" loading="lazy" referrerPolicy="no-referrer-when-downgrade"></iframe>
                        </Col>
                    </Row>
                </div>
            </section>
            <PublicFooter />
        </>
    )
}

export default Contact;