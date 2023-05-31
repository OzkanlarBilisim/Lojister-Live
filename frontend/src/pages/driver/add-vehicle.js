import React, { useEffect, useState } from 'react'
import { Form, Row, Col, Input, InputNumber, Button, Select } from 'antd';
import { useNavigate } from 'react-router';
import CustomTitle from '../../components/CustomTitle';
import UploadItem from '../../components/UploadItem';
import AntNotification from '../../components/AntNotification'
import { getMyDriversAll } from '../../api/controllers/driver-controller';
import { createVehicleDocumentRequest } from '../../api/controllers/vehicle-document-file-controller';
import { createVehicleRequest } from '../../api/controllers/vehicle-controller'
import { getAllVehicleDocumentTypeRequest } from '../../api/controllers/vehicle-document-type-controller';
import { getVehicleTypesRequest } from '../../api/controllers/vehicle-type-controller'
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';
function AddVehicle() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Araç Ekle")

    const navigate = useNavigate();

    const defaultForm = {
        brand: "",
        driver: null,
        licencePlate: "",
        maxCapacity: 0,
        trailerPlate: "",
        vehicleModel: "",
        vehicleType: null,
    }

    //states
    const [documentTypes, setDocumentTypes] = useState([]);
    const [vehicleTypes, setVehicleTypes] = useState([]);
    const [drivers, setDrivers] = useState([]);
    const [form, setForm] = useState(defaultForm);
    const [filesForSend, setFilesForSend] = useState([]);

    const fetchDocumentTypes = async () => {
        try {
            let res = await getAllVehicleDocumentTypeRequest();

            if (res) {
                setDocumentTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const fetchVehiclesTypes = async () => {
        try {
            let res = await getVehicleTypesRequest();

            if (res) {
                setVehicleTypes(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const fetchDrivers = async () => {
        try {
            let res = await getMyDriversAll();

            if (res) {
                setDrivers(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const onInputChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const onSubmit = async () => {
        if (filesForSend.length < 1) {
            AntNotification({ type: "warning", message: t("Eksik olan belgeler bulunuyor!"), description: t("Lütfen kontrol ederek tekrar deneyin") })
            return
        } else {
            form.driver = drivers.find(v => v.id === form.driver);
            form.vehicleType = vehicleTypes.find(v => v.id === form.vehicleType);
            setForm({ ...form });
            try {
                let res = await createVehicleRequest(form);
                if (res && res.data.id) {
                    AntNotification({ type: "info", message: t("Belgeler yükleniyor...") })
                    for (const documentTypeId of Object.keys(filesForSend)) {
                        let file = filesForSend[documentTypeId];
                        const formData = new FormData();
                        formData.append('result', file);
                        await createVehicleDocumentRequest(documentTypeId, res.data.id, formData)
                    }
                    AntNotification({ type: "success", message: t("Araç başarıyla eklendi!") })
                    navigate("/driver/vehicles")
                }
            } catch (error) {
                RequestError(error);
                form.driver = form.driver.id
                form.vehicleType = form.vehicleType.id
                setForm({ ...form });
            }
        }
    };

    useEffect(() => {
        fetchDocumentTypes();
        fetchVehiclesTypes();
        fetchDrivers();
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
                <CustomTitle>{t("Araç Bilgileri")}</CustomTitle>
                <Row gutter={[24, 12]}>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Araç Plakası")}
                            name="licencePlate"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input
                                placeholder={t("Araç plakasını yazın")}
                                size="large"
                                name="licencePlate"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Marka")}
                            name="brand"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Input
                                placeholder={t("Araç markasını yazın")}
                                size="large"
                                name="brand"
                                onChange={e => onInputChange(e)}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Araç Tipi")}
                            name="vehicleType"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Select
                                allowClear
                                showSearch
                                optionFilterProp="children"
                                filterOption={(input, option) =>
                                    option.children.toLowerCase().includes(input.toLowerCase())
                                }
                                placeholder={t("Araç tipini seçin")}
                                size="large"
                                onChange={(e, newValue) => setForm({ ...form, vehicleType: e })}
                            >
                                {
                                    vehicleTypes.map(v =>
                                        <Select.Option
                                            key={v.id}
                                            value={v.id}
                                        >
                                            {v.typeName}
                                        </Select.Option>
                                    )
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    {
                        form.vehicleType === 1 &&
                        <Col xs={24} md={12}>
                            <Form.Item
                                label={t("Dorse Plakası")}
                                name="trailerPlate"
                            >
                                <Input
                                    placeholder={t("Aracın dorse plakasını yazın")}
                                    size="large"
                                    name="trailerPlate"
                                    onChange={e => onInputChange(e)}
                                />
                            </Form.Item>
                        </Col>
                    }
                    <Col xs={24} md={6}>
                        <Form.Item
                            label={t("Model Yılı")}
                            name="vehicleModel"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setForm({ ...form, vehicleModel: value })}
                                style={{ width: "100%" }}
                                maxLength={4}
                                placeholder={t("Araç modelini yazın")}
                                size="large"
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={6}>
                        <Form.Item
                            label={t("Araç Kapasitesi")}
                            name="maxCapacity"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setForm({ ...form, maxCapacity: value })}
                                style={{ width: "100%" }}
                                min={0}
                                placeholder={t("Aracın kapasitesini yazın")}
                                size="large"
                                addonAfter="Ton"
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} md={12}>
                        <Form.Item
                            label={t("Araç Sürücüsü")}
                            name="driver"
                        >
                            <Select
                                allowClear
                                showSearch
                                optionFilterProp="children"
                                filterOption={(input, option) =>
                                    option.children.toLowerCase().includes(input.toLowerCase())
                                }
                                placeholder={t("Aracın sürücüsünü seçin")}
                                size="large"
                                onChange={e =>
                                    setForm({ ...form, driver: e })
                                }
                            >
                                {
                                    drivers.map(e =>
                                        <Select.Option
                                            key={e.id}
                                            value={e.id}
                                        >
                                            {
                                                e.firstName + " " + e.lastName
                                            }
                                        </Select.Option>
                                    )
                                }
                            </Select>
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
                        {t("Aracı Ekle")}
                    </Button>
                </div>
            </Form>
        </div >
    )
}

export default AddVehicle;