import React, { useEffect, useState } from 'react'
import { Form, Row, Col, Input, InputNumber, Button, Select } from 'antd';
import { useNavigate } from 'react-router';
import CustomTitle from '../../components/CustomTitle';
import NumberFormat from 'react-number-format';
import { getAllDriverDocumentTypeRequest } from '../../api/controllers/driver-document-type-controller';
import UploadItem from '../../components/UploadItem';
import AntNotification from '../../components/AntNotification'
import { createDriverRequest } from '../../api/controllers/driver-controller';
import { createDriverDocumentRequest } from '../../api/controllers/driver-document-file-controller';
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../components/Countries';

function AddDriver() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Sürücü Ekle")
    const { Option } = Select;

    const navigate = useNavigate();
    const defaultForm = {
        citizenId: "",
        email: "",
        firstName: "",
        lastName: "",
        password: "",
        phone: "",
        selectedCountryCode: "+90",
    }

    //states
    const [documentTypes, setDocumentTypes] = useState([]);
    const [form, setForm] = useState(defaultForm)
    const [filesForSend, setFilesForSend] = useState([])

    const fetchDocumentTypes = async () => {
        try {
            let res = await getAllDriverDocumentTypeRequest();

            if (res) {
                setDocumentTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const onInputChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const onSubmit = async () => {
        if (form.password !== form.rePassword) {
            AntNotification({ type: "warning", message: t("Parolalar uyuşmuyor!"), description: t("Lütfen kontrol ederek tekrar deneyin") })
            return
        } else if (filesForSend.length < 1) {
            AntNotification({ type: "warning", message: t("Eksik olan belgeler bulunuyor!"), description: t("Lütfen kontrol ederek tekrar deneyin") })
            return
        } else {
            form.phone = form.selectedCountryCode + "~" + form.phone
            setForm({ ...form })
            try {
                setForm({ ...form, phone: form.selectedCountryCode + "~" + form.phone })
                let res = await createDriverRequest(form);
                if (res && res.data.id) {
                    AntNotification({ type: "info", message: t("Belgeler yükleniyor...") })
                    for (const documentTypeId of Object.keys(filesForSend)) {
                        let file = filesForSend[documentTypeId];
                        const formData = new FormData();
                        formData.append('result', file);
                        await createDriverDocumentRequest(documentTypeId, res.data.id, formData)
                    }
                    AntNotification({ type: "success", message: t("Sürücü başarıyla eklendi!") })
                    navigate("/driver/drivers")
                }
            } catch (error) {
                RequestError(error);
                form.phone = form.phone.split(/[~]/g)[1]
                setForm({ ...form })
            }

        }
    };

    useEffect(() => {
        fetchDocumentTypes();
    }, []);

    return (
        <div className="layout-content-padding">
            <Form
                layout="vertical"
                labelCol={{ span: 24 }}
                initialValues={form}
                onFinish={onSubmit}
                scrollToFirstError={true}
            >
                <div style={{ display: "flex", marginBottom: "1rem" }}>
                    <Button
                        /* disabled={isLoading} */
                        onClick={() => navigate(-1)}
                        type="primary"
                        icon={<i style={{ marginRight: ".5rem" }} className="bi bi-arrow-left-circle-fill"></i>}
                    >{t("Geri dön")}</Button>
                </div>
                <CustomTitle>{t("Şoför Bilgileri")}</CustomTitle>
                <Row gutter={[24, 12]}>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("İsim")}
                            name="firstName"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input
                                placeholder={t("Şoför ismini yazın")}
                                size="large"
                                name="firstName"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Soyisim")}
                            name="lastName"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input
                                placeholder={t("Şoför soyismini yazın")}
                                size="large"
                                name="lastName"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Parola")}
                            name="password"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input.Password
                                placeholder={t("Şoför parolasını yazın")}
                                size="large"
                                name="password"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Parola (Tekrar)")}
                            name="rePassword"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input.Password
                                placeholder={t("Şoför parolasını tekrar yazın")}
                                size="large"
                                name="rePassword"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Telefon Numarası")}
                            name="phone"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <NumberFormat
                                addonBefore={<Select
                                    style={{ width: 100 }}
                                    name='selectedCountryCode'
                                    showSearch
                                    size='large'
                                    value={form.selectedCountryCode}
                                    onChange={(value) => {
                                        form.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                        setForm({ ...form })
                                    }}
                                    filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                >
                                    {
                                        Countries.map((v, i) =>
                                            <Option key={i} value={v.dial_code}>{v.code + " " + v.dial_code}</Option>
                                        )
                                    }
                                </Select>}
                                size="large"
                                format="### ### ## ## ## ## ## ## ##"
                                placeholder={t("Şoför telefon numarasını yazın")}
                                customInput={Input}
                                onValueChange={({ value }) => {
                                    form.phone = `${value}`
                                    setForm({ ...form })
                                }}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Email Adresi")}
                            name="email"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input
                                placeholder={t("Şoför mail adresini yazın")}
                                size="large"
                                type="email"
                                name="email"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("T.C. Kimlik Numarası")}
                            name="citizenId"
                            rules={[{
                                required: true, message: t('Lütfen bu alanı doldurun!')
                            }]}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setForm({ ...form, citizenId: value })}
                                controls={false}
                                maxLength={11}
                                style={{ width: "100%" }}
                                placeholder={t("Şoförün kimlik numarasını yazın")}
                                size="large"
                                name="email"
                            />
                        </Form.Item>
                    </Col>
                </Row>
                <br />
                <CustomTitle>{t("Belgeler")}</CustomTitle>
                <Row gutter={[24, 24]}>
                    {
                        documentTypes.map((val, i) =>
                            <Col xs={12} md={6} lg={4} xl={3} xxl={2}>
                                <UploadItem
                                    multiple={false}
                                    name={val.typeName}
                                    key={i}
                                    id={val.id}
                                    filesForSend={filesForSend}
                                    setFilesForSend={setFilesForSend}
                                />
                            </Col>
                        )
                    }
                </Row>
                <div style={{
                    display: "flex",
                    justifyContent: "flex-end",
                    marginTop: "1rem",
                }}>
                    <Button
                        type="primary"
                        htmlType="submit"
                    >
                        {t("Şoförü Ekle")}
                    </Button>
                </div>
            </Form>
        </div >
    )
}

export default AddDriver;