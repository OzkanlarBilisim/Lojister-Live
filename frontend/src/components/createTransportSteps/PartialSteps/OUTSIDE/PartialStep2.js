import { Button, Col, Row, Form, Input, DatePicker, Select, Modal, List, Alert, Divider } from 'antd';
import React, { useEffect, useState } from 'react'
import NumberFormat from 'react-number-format';
import { createGeocodeFromAddressRequest, getDistrictByProvinceIdRequest, getNeighborhoodRequest, getProvinceRequest, reverseGeocodeRequest } from '../../../../api/controllers/addresses-controller';
import { CustomButton } from '../../../CustomButton';
import { createSavedAddressesRequest, getMyAddressesRequest } from '../../../../api/controllers/saved-address-controller';
import MapBoxForCreateTransport from '../../../MapBoxForCreateTransport';
import moment from 'moment';
import AntNotification from '../../../AntNotification';
import { useSelector } from 'react-redux';
import locale from "antd/es/date-picker/locale/tr_TR";
import RequestError from '../../../RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../../Countries';

//Partial STEP 2
const PartialStep2 = ({ handleClickNextStep, handleClickPrevStep, partialForm, setPartialForm, adStartingDate, adDueDate, setAdDueDate }) => {
    const { t, i18n } = useTranslation("common")
    const { Option } = Select;

    const user = useSelector((state) => state.user);

    const formValues = {
        adDueDate: adDueDate,
        commercialTitle: partialForm.dueRecipient.commercialTitle,
        firstName: partialForm.dueRecipient.firstName,
        lastName: partialForm.dueRecipient.lastName,
        phoneNumber: partialForm.dueRecipient.phoneNumber,
        province: partialForm.dueAddress.province,
        /*  district: partialForm.dueAddress.district,
         neighborhood: partialForm.dueAddress.neighborhood, */
        fullAddress: partialForm.dueAddress.fullAddress,
        /*  address: partialForm.dueAddress.province && partialForm.dueAddress.district && partialForm.dueAddress.neighborhood && partialForm.dueAddress.fullAddress ? partialForm.dueAddress.fullAddress + " " + partialForm.dueAddress.neighborhood + " - " + partialForm.dueAddress.district + "/" + partialForm.dueAddress.province : "", */
        zipCode: partialForm.dueAddress.zipCode,
        country: partialForm.dueAddress.country,
        selectedCountryCode: partialForm.dueRecipient.selectedCountryCode ? partialForm.dueRecipient.selectedCountryCode : "+90"
    };

    const saveAddressForm = {
        addressName: "",
        commercialTitle: partialForm.dueRecipient.commercialTitle,
        /* district: partialForm.dueAddress.district, */
        firstName: partialForm.dueRecipient.firstName,
        fullAddress: partialForm.dueAddress.fullAddress,
        lastName: partialForm.dueRecipient.lastName,
        lat: null,
        lng: null,
        /* neighborhood: partialForm.dueAddress.neighborhood, */
        phone: partialForm.dueRecipient.selectedCountryCode ? partialForm.dueRecipient.selectedCountryCode : "+90" + "~" + partialForm.dueRecipient.phoneNumber,
        province: partialForm.dueAddress.province,
        /* street: partialForm.dueAddress.street, */
        zipCode: partialForm.dueAddress.zipCode,
        country: partialForm.dueAddress.country,
        regionType: 'INTERNATIONAL'
    };

    const [isDragged, setIsDragged] = useState(false)

    //data states
    const [provinces, setProvinces] = useState([])
    const [districts, setDistricts] = useState([])
    const [neighborhoods, setNeighborhoods] = useState([])
    const [myAddressess, setMyAddressess] = useState([])

    //modal states
    const [savedAdressModal, setSavedAdressModal] = useState(false)
    const [addressModal, setAddressModal] = useState(false)
    const [saveAddressModal, setSaveAddressModal] = useState(false)

    //loading states
    const [isAddressLoading, setIsAddressLoading] = useState(false)

    //get saved address
    const fetchSavedAddresses = async () => {
        try {
            let res = await getMyAddressesRequest();
            if (res) {
                setMyAddressess(res.data.filter(v => v.regionType === 'INTERNATIONAL'))
            }
        } catch (error) {
            RequestError(error);
        }
    }

    //open saved address modal
    const clickOpenSavedAddressesModal = async () => {
        await fetchSavedAddresses();
        setSavedAdressModal(true)
    }

    //on selected saved address
    const onSelectSavedAddress = (savedAddress) => {
        partialForm.dueRecipient.commercialTitle = savedAddress.commercialTitle;
        partialForm.dueRecipient.firstName = savedAddress.firstName;
        partialForm.dueRecipient.lastName = savedAddress.lastName;
        partialForm.dueRecipient.phoneNumber = savedAddress.phone.split(/[~]/g)[1];
        partialForm.dueRecipient.selectedCountryCode = Countries.find(v => v.dial_code === savedAddress?.phone?.slice(0, 3))?.dial_code;
        partialForm.startingAddress.province = savedAddress.province;
        partialForm.startingAddress.country = savedAddress.country;
        partialForm.startingAddress.zipCode = savedAddress.zipCode;
        /* partialForm.dueAddress.district = savedAddress.district;
        partialForm.dueAddress.neighborhood = savedAddress.neighborhood; */
        partialForm.dueAddress.fullAddress = savedAddress.fullAddress;
        /* partialForm.dueAddress.lat = savedAddress.lat;
        partialForm.dueAddress.lng = savedAddress.lng; */
        setPartialForm({ ...partialForm });
        setSavedAdressModal(false);
    }

    //create saved address
    const createSavedAddress = async () => {
        try {
            let res = await createSavedAddressesRequest(saveAddressForm);
            if (res) {
                setSaveAddressModal(false)
                saveAddressForm.addressName = ""
                AntNotification({ type: "success", message: t("Adres başarıyla kaydedildi.") })
            }
        } catch (error) {
            RequestError(error);
        }
    }

    //get geo code -lat, log
    const fetchGeocode = async () => {
        try {
            let res = await createGeocodeFromAddressRequest({
                district: partialForm.dueAddress.district,
                neighborhood: partialForm.dueAddress.neighborhood,
                province: partialForm.dueAddress.province,
                fullAddress: partialForm.dueAddress.fullAddress,
            })
            if (res && !isDragged) {
                partialForm.dueAddress.lat = res.data.latitude;
                partialForm.dueAddress.lng = res.data.longitude;
                setPartialForm({ ...partialForm })
            }
        } catch (error) {
            AntNotification({ type: "error", message: t("Bir sorun oluştu!"), description: t("Lütfen yeniden deneyin.") })
        }
    }

    /*  useEffect(() => {
         if (partialForm.dueAddress.district || partialForm.dueAddress.neighborhood || partialForm.dueAddress.province) {
             fetchGeocode()
         }
     }, [partialForm.dueAddress.district, partialForm.dueAddress.neighborhood, partialForm.dueAddress.province, partialForm.dueAddress.fullAddress]) */

    //get address by dragged marker
    const onDragMarker = async ({ latitude, longitude }) => {
        setIsDragged(true)
        partialForm.dueAddress.lat = latitude;
        partialForm.dueAddress.lng = longitude;
        setPartialForm({ ...partialForm })
        try {
            let res = await reverseGeocodeRequest({
                latitude: latitude,
                longitude: longitude,
            })
            if (res) {
                partialForm.dueAddress.province = res.data.province;
                partialForm.dueAddress.district = res.data.district;
                partialForm.dueAddress.neighborhood = res.data.neighborhood;
                partialForm.dueAddress.fullAddress = res.data.fullAddress;
                setPartialForm({ ...partialForm })
            }
        } catch (error) {
            AntNotification({ type: "error", message: t("Adres tanımlanamadı!"), description: t("Lütfen yeniden deneyin veya adres alanından seçim yaparak devam edin.") })
        }
    }

    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [partialForm])

    //control for address
    const setAddress = async () => {
        setIsAddressLoading(true)
        //fetch province
        try {
            let provinceData = await getProvinceRequest();
            if (provinceData) {
                setProvinces(provinceData.data)
                //fetch district
                /*  if (partialForm.dueAddress.province) {
                     setIsAddressLoading(true)
                     let findProvince = provinceData.data.find(v => v.provinceName === partialForm.dueAddress.province);
                     try {
                         let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                         if (districtData) {
                             setDistricts(districtData.data)
                             //fetch neighborhood
                             if (partialForm.dueAddress.district) {
                                 setIsAddressLoading(true)
                                 let findDistrict = districtData.data.find(v => v.districtName === partialForm.dueAddress.district);
                                 try {
                                     let res = await getNeighborhoodRequest(findDistrict.id);
                                     if (res) {
                                         setNeighborhoods(res.data)
                                     }
                                     setIsAddressLoading(false)
                                 } catch (error) {
                                     RequestError(error);
                                 }
                             }
                         }
                         setIsAddressLoading(false)
                     } catch (error) {
                         RequestError(error);
                     }
                 } */
            }
            setIsAddressLoading(false)
        } catch (error) {
            RequestError(error);
        }
    }

    /* useEffect(() => {
        setAddress()
    }, [partialForm.dueAddress.province, partialForm.dueAddress.district]) */

    return (
        <>
            <Form
                form={form}
                onFinish={handleClickNextStep}
                labelCol={{ span: 24 }}
                wrapperCol={{ span: 24 }}
                initialValues={formValues}
                scrollToFirstError={true}
            >
                <div className="create-ad-wrapper">
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <div style={{ display: "flex", alignItems: "center", gap: ".5rem" }}>
                            <Button
                                onClick={() => handleClickPrevStep()}
                                shape="circle"
                                type="primary"
                                icon={<i className="bi bi-arrow-left"></i>}></Button>
                            <CustomButton
                                type="outlined"
                                onClick={() => user ? clickOpenSavedAddressesModal() : AntNotification({ type: "warning", message: t("Bu özelliği kullanabilmek için giriş yapmalısınız!") })}
                            >
                                {t("Kayıtlı Bilgilerden Doldur")}
                            </CustomButton>
                        </div>

                        <div style={{ display: "flex", gap: ".5rem" }}>
                            {
                                Object.keys(formValues).some((k) => {
                                    return formValues[k] === null;
                                }) || Object.keys(formValues).some((k) => {
                                    return formValues[k] === '';
                                }) ||
                                <CustomButton
                                    color="light"
                                    onClick={() => user ? setSaveAddressModal(true) : AntNotification({ type: "warning", message: t("Bu özelliği kullanabilmek için giriş yapmalısınız!") })}
                                >
                                    {t("Bilgileri Kaydet")}
                                </CustomButton>
                            }
                            <Form.Item style={{ margin: 0 }}>
                                <CustomButton
                                    color="primary"
                                    htmlType="submit"
                                >
                                    {t("Sonraki Adıma Geç")}
                                </CustomButton>
                            </Form.Item>
                        </div>
                    </div>
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Teslimat Zamanı")}
                                name="adDueDate"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <DatePicker
                                    showNow={false}
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı zamanı seçin")}
                                    style={{ width: "100%" }}
                                    format="DD/MM/YYYY"
                                    disabledDate={current => moment(adStartingDate)  >= current}
                                    onChange={date => setAdDueDate(date)}
                                    locale={{
                                        ...locale,
                                        lang: {
                                            ...locale.lang,
                                            ok: t("Seç"),
                                        }
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Şirket Ünvanı")}
                                name="commercialTitle"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı şirket ünvanını girin")}
                                    onChange={e => {
                                        partialForm.dueRecipient.commercialTitle = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yetkili İsmi")}
                                name="firstName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı yetkili ismini girin")}
                                    onChange={e => {
                                        partialForm.dueRecipient.firstName = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yetkili Soyismi")}
                                name="lastName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı yetkili soyismini girin")}
                                    onChange={e => {
                                        partialForm.dueRecipient.lastName = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yetkili Telefon Numarası")}
                                name="phoneNumber"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCode'
                                        showSearch
                                        size='large'
                                        value={partialForm.dueRecipient.selectedCountryCode ? partialForm.dueRecipient.selectedCountryCode : '+90'}
                                        onChange={(value) => {
                                            partialForm.dueRecipient.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                            setPartialForm({ ...partialForm })
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
                                    placeholder={t("Yükleme yetkilisinin telefonunu girin")}
                                    customInput={Input}
                                    onValueChange={({ value }) => {
                                        partialForm.dueRecipient.phoneNumber = `${value}`
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Ülke")}
                                name="country"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı şehri seçin")}
                                    onChange={e => {
                                        partialForm.dueAddress.country = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Şehir")}
                                name="province"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı şehri seçin")}
                                    onChange={e => {
                                        partialForm.dueAddress.province = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Posta Kodu")}
                                name="zipCode"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <NumberFormat
                                    size="large"
                                    /*  minLength={13} */
                                    placeholder={t("Teslimatın yapılacağı şehrin posta kodunu girin")}
                                    customInput={Input}
                                    onValueChange={({ floatValue }) => {
                                        partialForm.dueAddress.zipCode = floatValue
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Tam Adres")}
                                name="fullAddress"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Teslimatın yapılacağı ülkeyi seçin")}
                                    onChange={e => {
                                        partialForm.dueAddress.fullAddress = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Modal
                            title={t("Teslimat adresini seçin")}
                            centered
                            visible={addressModal}
                            onCancel={() => setAddressModal(false)}
                            footer={
                                <div>
                                    <Button onClick={() => setAddressModal(false)}>{t("Kapat")}</Button>
                                    <Button
                                        type="primary"
                                        disabled={!partialForm.dueAddress.province || !partialForm.dueAddress.district || !partialForm.dueAddress.neighborhood || !partialForm.dueAddress.fullAddress}
                                        onClick={() => setAddressModal(false)}>
                                        {t("Kaydet")}
                                    </Button>
                                </div>
                            }
                            width={1000}
                            bodyStyle={{
                                maxHeight: "75vh",
                                overflowY: "auto",
                            }}
                        >
                            <Row gutter={[24, 24]}>
                                <Col xs={24} sm={24} md={12} lg={16} xl={16}>
                                    <MapBoxForCreateTransport latitude={partialForm.dueAddress.lat} longitude={partialForm.dueAddress.lng} onDragMarker={onDragMarker} />
                                </Col>
                                <Col xs={24} sm={24} md={12} lg={8} xl={8}>
                                    <Row gutter={[12, 12]}>
                                        <Col span={24}>
                                            <Alert message={t("Haritadaki konum işaretini kaydırarak adresinizi kolayca seçebilir ve teyit edebilirsiniz.")} type="info" showIcon />
                                        </Col>
                                        <Col span={24}>
                                            <Divider style={{ margin: 0 }} />
                                        </Col>
                                        <Col span={24}>
                                            <Form.Item
                                                label={t("Şehir")}
                                                name="province"
                                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                                            >
                                                <Select
                                                    size="large"
                                                    placeholder={t("Teslimatın yapılacağı şehri seçin")}
                                                    onChange={provinceName => {
                                                        partialForm.dueAddress.province = provinceName
                                                        partialForm.dueAddress.district = null
                                                        partialForm.dueAddress.neighborhood = null
                                                        partialForm.dueAddress.fullAddress = ""
                                                        setPartialForm({ ...partialForm })
                                                        setIsDragged(false)
                                                    }}
                                                    loading={isAddressLoading}
                                                    allowClear
                                                    showSearch
                                                    optionFilterProp="children"
                                                    filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                                >
                                                    {
                                                        provinces.map(val => (
                                                            <Select.Option key={val.id} value={val.provinceName}>{val.provinceName}</Select.Option>
                                                        ))
                                                    }
                                                </Select>
                                            </Form.Item>
                                        </Col>
                                        <Col span={24}>
                                            <Form.Item
                                                label={t("İlçe")}
                                                name="district"
                                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                                            >
                                                <Select
                                                    size="large"
                                                    disabled={!partialForm.dueAddress.province}
                                                    placeholder={t("Teslimatın yapılacağı ilçeyi seçin")}
                                                    onChange={districtName => {
                                                        partialForm.dueAddress.district = districtName
                                                        partialForm.dueAddress.neighborhood = null
                                                        partialForm.dueAddress.fullAddress = ""
                                                        setPartialForm({ ...partialForm })
                                                        setIsDragged(false)
                                                    }}
                                                    loading={isAddressLoading}
                                                    allowClear
                                                    showSearch
                                                    optionFilterProp="children"
                                                    filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                                >
                                                    {
                                                        districts.map(val => (
                                                            <Select.Option key={val.id} value={val.districtName}>{val.districtName}</Select.Option>
                                                        ))
                                                    }
                                                </Select>
                                            </Form.Item>
                                        </Col>
                                        <Col span={24}>
                                            <Form.Item
                                                label={t("Mahalle")}
                                                name="neighborhood"
                                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                                            >
                                                <Select
                                                    size="large"
                                                    disabled={!partialForm.dueAddress.district}
                                                    placeholder={t("Teslimatın yapılacağı mahalleyi seçin")}
                                                    onChange={neighborhood => {
                                                        partialForm.dueAddress.neighborhood = neighborhood
                                                        partialForm.dueAddress.fullAddress = ""
                                                        setPartialForm({ ...partialForm })
                                                        setIsDragged(false)
                                                    }}
                                                    loading={isAddressLoading}
                                                    allowClear
                                                    showSearch
                                                    optionFilterProp="children"
                                                    filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                                >
                                                    {
                                                        neighborhoods.map(val => (
                                                            <Select.Option key={val.id} value={val.neighborhoodName}>{val.neighborhoodName}</Select.Option>
                                                        ))
                                                    }
                                                </Select>
                                            </Form.Item>
                                        </Col>
                                        <Col span={24}>
                                            <Form.Item
                                                label={t("Adres Tarifi")}
                                                name="fullAddress"
                                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                                            >
                                                <Input
                                                    size="large"
                                                    placeholder={t("Teslimatın yapılacağı adresin detaylarını girin")}
                                                    onChange={e => {
                                                        partialForm.dueAddress.fullAddress = e.target.value
                                                        setPartialForm({ ...partialForm })
                                                    }}
                                                />
                                            </Form.Item>
                                        </Col>
                                    </Row>
                                </Col>
                            </Row>
                        </Modal >
                    </Row>
                </div >
            </Form>

            <Modal
                title={t("Kayıtlı bilgileriniz arasından seçim yapın")}
                centered
                visible={savedAdressModal}
                footer={null}
                onCancel={() => setSavedAdressModal(false)}
                bodyStyle={{
                    maxHeight: "75vh",
                    overflowY: "auto",
                }}
            >
                <List
                    dataSource={myAddressess}
                    renderItem={item => (
                        <List.Item key={item.id}>
                            <List.Item.Meta
                                title={item.addressName}
                                description={item.neighborhood !== null ? item.fullAddress + " " + item.neighborhood + " - " + item.district + "/" + item.province : item.fullAddress + " " + item.province + "/" + item.country}
                            />
                            <Button type="primary" onClick={() => onSelectSavedAddress(item)}>{t("Seç")}</Button>
                        </List.Item>
                    )}
                />
            </Modal >

            <Modal
                title={t("Bilgileri kaydedin")}
                centered
                visible={saveAddressModal}
                onOk={createSavedAddress}
                okText={t("Kaydet")}
                cancelText={t("İptal")}
                onCancel={() => setSaveAddressModal(false)}
            >
                <Input
                    onChange={e => saveAddressForm.addressName = e.target.value}
                    placeholder={t("Bilgileri kaydedebilmek için bir isim belirleyin. Örnek: 'Depo'")}
                />
            </Modal>

        </>
    );
};

export default PartialStep2;