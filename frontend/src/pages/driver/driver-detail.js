import React, { useEffect, useState } from 'react'
import { Form, Row, Col, Input, InputNumber, Button, Select } from 'antd';
import { useNavigate, useParams } from 'react-router';
import CustomTitle from '../../components/CustomTitle';
import NumberFormat from 'react-number-format';
import { getAllDriverDocumentTypeRequest } from '../../api/controllers/driver-document-type-controller';
import UploadItem from '../../components/UploadItem';
import AntNotification from '../../components/AntNotification'
import { getDriverByIdRequest, updateDriverRequest } from '../../api/controllers/driver-controller';
import { createDriverDocumentRequest, getDriverDocumentInfoListByUserIdRequest } from '../../api/controllers/driver-document-file-controller';
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline';
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../components/Countries';

function DriverDetail() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Sürücü Detayı")
    const { Option } = Select;

    const navigate = useNavigate();
    const [formRef] = Form.useForm();
    const { driverId } = useParams();

    //states
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [documentTypes, setDocumentTypes] = useState([]);
    const [driverDocuments, setDriverDocuments] = useState([])
    const [form, setForm] = useState()
    const [filesForSend, setFilesForSend] = useState([])
    const [selectedCountryCode, setSelectedCountryCode] = useState("+90")

    const fetchDriver = async () => {
        setIsLoading(true)
        try {
            let res = await getDriverByIdRequest(driverId);

            if (res) {
                setForm(res.data)
                setSelectedCountryCode(Countries.find(v => v.dial_code === res.data.phone.split(/[~]/g)[0])?.dial_code)
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false)
    };

    const fetchDocumentTypes = async () => {
        try {
            let res = await getAllDriverDocumentTypeRequest();

            if (res) {
                setDocumentTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchDriverDocuments = async () => {
        try {
            let res = await getDriverDocumentInfoListByUserIdRequest(driverId)

            if (res) {
                setDriverDocuments(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const onInputChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const onSubmit = async () => {
        setIsSubmitting(true);
        try {
            let res = await updateDriverRequest(driverId, form);
            if (res && res.data.id && filesForSend.length > 0) {
                AntNotification({ type: "info", message: t("Belgeler yükleniyor...") })
                for (const documentTypeId of Object.keys(filesForSend)) {
                    let file = filesForSend[documentTypeId];
                    const formData = new FormData();
                    formData.append('result', file);
                    await createDriverDocumentRequest(documentTypeId, driverId, formData)
                }
            }
            AntNotification({ type: "success", message: t("İşlem başarıyla gerçekleştirildi!") })
            navigate("/driver/drivers")
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    };

    useEffect(() => {
        fetchDriver();
        fetchDriverDocuments();
        fetchDocumentTypes();
    }, []);

    return (
        isLoading ?
            <SuspenseFallbackInline /> :
            <div className="layout-content-padding">
                <Form
                    form={formRef}
                    layout="vertical"
                    labelCol={{ span: 24 }}
                    initialValues={{
                        ...form,
                        phone: form.phone.slice(3),
                    }}
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
                            >
                                <Input
                                    placeholder={t("Şoför ismini yazın.")}
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
                            >
                                <Input
                                    placeholder={t("Şoför soyismini yazın.")}
                                    size="large"
                                    name="lastName"
                                    onChange={e => onInputChange(e)}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} md={12}>
                            <Form.Item
                                label={t("Telefon Numarası")}
                                name="phone"
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        disabled
                                        style={{ width: 100 }}
                                        name='selectedCountryCode'
                                        showSearch
                                        size='large'
                                        value={selectedCountryCode}
                                        filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                    >
                                        {
                                            Countries.map((v, i) =>
                                                <Option key={i} value={v.dial_code}>{v.code + " " + v.dial_code}</Option>
                                            )
                                        }
                                    </Select>}
                                    readOnly
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
                            >
                                <Input
                                    placeholder={t("Şoför mail adresini yazın.")}
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
                            >
                                <NumberFormat
                                    customInput={Input}
                                    onValueChange={({ value }) => setForm({ ...form, citizenId: value })}
                                    controls={false}
                                    maxLength={11}
                                    style={{ width: "100%" }}
                                    placeholder={t("Şoför kimlik numarasını yazın.")}
                                    size="large"
                                    name="email"
                                    onChange={e => setForm({ ...form, citizenId: e })}
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                    <br />
                    <CustomTitle>{t("Belgeler")}</CustomTitle>

                    {
                        form?.status === 'REVIEW_SENT' ?
                            < div div className={`alert-red`}>
                                <div className='leftSide'>
                                    <img style={{ paddingRight: ".5rem" }} src={"/assets/img/error.svg"} alt="" />
                                    <div className={`text-red`}>{form?.statusDescription ? form?.statusDescription : t("Lütfen eksik belgeleri ekleyin!")}</div>
                                </div>
                            </div>
                            : form?.status === 'ACCEPTED' ?
                                < div div className={`alert-green`}>
                                    <div className='leftSide'>
                                        <img style={{ paddingRight: ".5rem" }} src={"/assets/img/success.svg"} alt="" />
                                        <div className={`text-green`}>{form?.statusDescription ? form?.statusDescription : t("Sürücü onaylandı!")}</div>
                                    </div>
                                </div>
                                : form?.status === 'REVIEW' ?
                                    < div div className={`alert-blue`}>
                                        <div className='leftSide'>
                                            <img style={{ paddingRight: ".5rem" }} src={"/assets/img/info.svg"} alt="" />
                                            <div className={`text-blue`}>{form?.statusDescription ? form?.statusDescription : t("Yönetici tarafından değerlendiriliyor!")}</div>
                                        </div>
                                    </div>
                                    : ""
                    }

                    <br />

                    <Row gutter={[24, 24]}>
                        {
                            documentTypes.map((val, i) =>
                                <Col key={i} xs={12} md={6} lg={4} xl={3}>
                                    <UploadItem
                                        multiple={false}
                                        name={val.typeName}
                                        id={val.id}
                                        documentId={driverDocuments.find(v => v.driverDocumentType.id === val.id)?.documentId}
                                        filesForSend={filesForSend}
                                        setFilesForSend={setFilesForSend}
                                        type="driver"
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
                            loading={isSubmitting}
                        >
                            {t("Kaydet")}
                        </Button>
                    </div>
                </Form >
            </div >
    )
}

export default DriverDetail;