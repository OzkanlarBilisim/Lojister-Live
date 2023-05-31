import { Button, Col, Row, Form, Input, DatePicker, Select, Modal, List, Alert, Divider } from 'antd';
import React, { useEffect, useState } from 'react'
import NumberFormat from 'react-number-format';
import { createGeocodeFromAddressRequest, getDistrictByProvinceIdRequest, getNeighborhoodRequest, getProvinceRequest, reverseGeocodeRequest } from '../../../../api/controllers/addresses-controller';
import { CustomButton } from '../../../CustomButton';
import moment from 'moment';
import { createSavedAddressesRequest, getMyAddressesRequest } from '../../../../api/controllers/saved-address-controller';
import MapBoxForCreateTransport from '../../../MapBoxForCreateTransport';
import AntNotification from '../../../AntNotification';
import { useSelector } from 'react-redux';
import locale from "antd/es/date-picker/locale/tr_TR";
import { getAccountSettingsRequest, getMyDefaultAddressRequest } from '../../../../api/controllers/client-controller';
import RequestError from '../../../RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../../Countries';

//Partial STEP 1
const PartialStep1 = ({ handleClickNextStep, handleClickPrevStep, partialForm, setPartialForm, adStartingDate, setAdStartingDate }) => {
    const { t, i18n } = useTranslation("common");/* {t()} */
    const { Option } = Select;

    const user = useSelector((state) => state.user);

    const formValues = {
        adStartingDate: adStartingDate,
        commercialTitle: partialForm.startRecipient.commercialTitle,
        firstName: partialForm.startRecipient.firstName,
        lastName: partialForm.startRecipient.lastName,
        phoneNumber: partialForm.startRecipient.phoneNumber,
        province: partialForm.startingAddress.province,
        district: partialForm.startingAddress.district,
        neighborhood: partialForm.startingAddress.neighborhood,
        fullAddress: partialForm.startingAddress.fullAddress,
        address: partialForm.startingAddress.province && partialForm.startingAddress.district && partialForm.startingAddress.neighborhood && partialForm.startingAddress.fullAddress ? partialForm.startingAddress.fullAddress + " " + partialForm.startingAddress.neighborhood + " - " + partialForm.startingAddress.district + "/" + partialForm.startingAddress.province : "",
        selectedCountryCode: partialForm.startRecipient.selectedCountryCode ? partialForm.startRecipient.selectedCountryCode : "+90"
    };

    const saveAddressForm = {
        addressName: "",
        commercialTitle: partialForm.startRecipient.commercialTitle,
        district: partialForm.startingAddress.district,
        firstName: partialForm.startRecipient.firstName,
        fullAddress: partialForm.startingAddress.fullAddress,
        lastName: partialForm.startRecipient.lastName,
        lat: null,
        lng: null,
        neighborhood: partialForm.startingAddress.neighborhood,
        phone: partialForm.startRecipient.selectedCountryCode ? partialForm.startRecipient.selectedCountryCode : "+90" + "~" + partialForm.startRecipient.phoneNumber,
        province: partialForm.startingAddress.province,
        street: partialForm.startingAddress.street,
        regionType: 'DOMESTIC',
        country: "türkiye"
    }

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
                setMyAddressess(res.data.filter(v => v.regionType === 'DOMESTIC'))
            }
        } catch (error) {
            RequestError(error);
        }
    }

    //get my default saved address
    const fetchAccountSettings = async () => {
        try {
            let res = await getAccountSettingsRequest();
            if (res) {
                if (res.data.createAdvertisementStartingAddressAutoFill) {
                    fetchMyDefaultAddress()
                }
            }
        } catch (error) {
            if(user !== null){
                RequestError(error);
            }
        }
    }

    useEffect(() => {
        fetchAccountSettings()
    }, [])

    //get my default saved address
    const fetchMyDefaultAddress = async () => {
        try {
            let res = await getMyDefaultAddressRequest();
            if (res) {
                if (res.data) {
                    onSelectSavedAddress(res.data)
                }
            }
        } catch (error) {
        }
    }

    //open saved address modal
    const clickOpenSavedAddressesModal = async () => {
        await fetchSavedAddresses();
        setSavedAdressModal(true)
    }

    const onSelectSavedAddress = (savedAddress) => {
        partialForm.startRecipient.commercialTitle = savedAddress.commercialTitle;
        partialForm.startRecipient.firstName = savedAddress.firstName;
        partialForm.startRecipient.lastName = savedAddress.lastName;
        partialForm.startRecipient.phoneNumber = savedAddress.phone.split(/[~]/g)[1];
        partialForm.startRecipient.selectedCountryCode = Countries.find(v => v.dial_code === savedAddress?.phone?.slice(0, 3))?.dial_code;
        partialForm.startingAddress.province = savedAddress.province;
        partialForm.startingAddress.district = savedAddress.district;
        partialForm.startingAddress.neighborhood = savedAddress.neighborhood;
        partialForm.startingAddress.fullAddress = savedAddress.fullAddress;
        partialForm.startingAddress.lat = savedAddress.lat;
        partialForm.startingAddress.lng = savedAddress.lng;
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
                district: partialForm.startingAddress.district,
                neighborhood: partialForm.startingAddress.neighborhood,
                province: partialForm.startingAddress.province,
                fullAddress: partialForm.startingAddress.fullAddress,
            })
            if (res && !isDragged) {
                partialForm.startingAddress.lat = res.data.latitude;
                partialForm.startingAddress.lng = res.data.longitude;
                setPartialForm({ ...partialForm })
            }
        } catch (error) {
            AntNotification({ type: "error", message: t("Bir sorun oluştu!"), description: t("Lütfen yeniden deneyin.") })
        }
    }

    useEffect(() => {
        if (partialForm.startingAddress.district || partialForm.startingAddress.neighborhood || partialForm.startingAddress.province) {
            fetchGeocode()
        }
    }, [partialForm.startingAddress.district, partialForm.startingAddress.neighborhood, partialForm.startingAddress.province, partialForm.startingAddress.fullAddress])

    //get address by dragged marker
    const onDragMarker = async ({ latitude, longitude }) => {
        setIsDragged(true)
        partialForm.startingAddress.lat = latitude;
        partialForm.startingAddress.lng = longitude;
        setPartialForm({ ...partialForm })
        try {
            let res = await reverseGeocodeRequest({
                latitude: latitude,
                longitude: longitude,
            })
            if (res) {
                partialForm.startingAddress.province = res.data.province;
                partialForm.startingAddress.district = res.data.district;
                partialForm.startingAddress.neighborhood = res.data.neighborhood;
                partialForm.startingAddress.fullAddress = res.data.fullAddress;
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

    const range = (start, end) => {
        const result = [];
        for (let i = start; i < end; i++) {
            result.push(i);
        }
        return result;
    };

    const disabledDateTime = (current) => {
        if (moment(current).format("YYYY-MM-DD") === moment().format("YYYY-MM-DD")) {
            return (
                {
                    disabledHours: () => range(0, 24).splice(0, moment().add(2, 'hours').format('HH')),
                    disabledMinutes: () => moment(current).format('HH') === moment().add(2, 'hours').format('HH') && range(0, 60).splice(0, moment().format('mm')),
                }
            )
        }
    };

    //control for address
    const setAddress = async () => {
        setIsAddressLoading(true)
        //fetch province
        try {
            let provinceData = await getProvinceRequest();
            if (provinceData) {
                setProvinces(provinceData.data)
                //fetch district
                if (partialForm.startingAddress.province) {
                    setIsAddressLoading(true)
                    let findProvince = provinceData.data.find(v => v.provinceName === partialForm.startingAddress.province);
                    try {
                        let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                        if (districtData) {
                            setDistricts(districtData.data)
                            //fetch neighborhood
                            if (partialForm.startingAddress.district) {
                                setIsAddressLoading(true)
                                let findDistrict = districtData.data.find(v => v.districtName === partialForm.startingAddress.district);
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
                }
            }
            setIsAddressLoading(false)
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        setAddress()
    }, [partialForm.startingAddress.province, partialForm.startingAddress.district])

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
                                label={t("Yükleme Zamanı")}
                                name="adStartingDate"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <DatePicker
                                    showNow={false}
                                    size="large"
                                    placeholder={t("Yüklemenin yapılacağı zamanı seçin")}
                                    style={{ width: "100%" }}
                                    format="DD/MM/YYYY HH:mm"
                                    disabledDate={current => moment().startOf('day') >= current}
                                    disabledTime={disabledDateTime}
                                    showTime={{ defaultValue: moment("LT", "HH:mm") }}
                                    onChange={date => setAdStartingDate(date)}
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
                                    placeholder={t("Yüklemeyi yapan şirket ünvanını girin")}
                                    onChange={e => {
                                        partialForm.startRecipient.commercialTitle = e.target.value
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
                                    placeholder={t("Yükleme yetkilisinin ismini girin")}
                                    onChange={e => {
                                        partialForm.startRecipient.firstName = e.target.value
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
                                    placeholder={t("Yükleme yetkilisinin soyismini girin")}
                                    onChange={e => {
                                        partialForm.startRecipient.lastName = e.target.value
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yetkili Telefon Numarası")}
                                name="phoneNumber"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }, { min: 10, message: t('Lütfen numaranızı eksiksiz doldurun!') }]}
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCode'
                                        showSearch
                                        size='large'
                                        value={partialForm.startRecipient.selectedCountryCode ? partialForm.startRecipient.selectedCountryCode : '+90'}
                                        onChange={(value) => {
                                            partialForm.startRecipient.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
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
                                    format="### ### ## ##"
                                    placeholder={t("Yükleme yetkilisinin telefonunu girin")}
                                    customInput={Input}
                                    onValueChange={({ value }) => {
                                        partialForm.startRecipient.phoneNumber = `${value}`
                                        setPartialForm({ ...partialForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Adres")}
                                name="address"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Yükleme adresini seçin")}
                                    readOnly
                                    onClick={() => setAddressModal(true)}
                                />
                            </Form.Item>
                        </Col>
                        <Modal
                            title={t("Yükleme adresini seçin")}
                            centered
                            visible={addressModal}
                            onCancel={() => setAddressModal(false)}
                            footer={
                                <div>
                                    <Button onClick={() => setAddressModal(false)}>{t("Kapat")}</Button>
                                    <Button
                                        type="primary"
                                        disabled={!partialForm.startingAddress.province || !partialForm.startingAddress.district || !partialForm.startingAddress.neighborhood || !partialForm.startingAddress.fullAddress}
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
                                    <MapBoxForCreateTransport latitude={partialForm.startingAddress.lat} longitude={partialForm.startingAddress.lng} onDragMarker={onDragMarker} />
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
                                                    placeholder={t("Yükleme yapılacak şehri seçin")}
                                                    onChange={provinceName => {
                                                        partialForm.startingAddress.province = provinceName
                                                        partialForm.startingAddress.district = null
                                                        partialForm.startingAddress.neighborhood = null
                                                        partialForm.startingAddress.fullAddress = ""
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
                                                    disabled={!partialForm.startingAddress.province}
                                                    placeholder={t("Yükleme yapılacak ilçeyi seçin")}
                                                    onChange={districtName => {
                                                        partialForm.startingAddress.district = districtName
                                                        partialForm.startingAddress.neighborhood = null
                                                        partialForm.startingAddress.fullAddress = ""
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
                                                    disabled={!partialForm.startingAddress.district}
                                                    placeholder={t("Yükleme yapılacak mahalleyi seçin")}
                                                    onChange={neighborhood => {
                                                        partialForm.startingAddress.neighborhood = neighborhood
                                                        partialForm.startingAddress.fullAddress = ""
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
                                                    placeholder={t("Yükleme yapılacak adresin detaylarını girin")}
                                                    onChange={e => {
                                                        partialForm.startingAddress.fullAddress = e.target.value
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

export default PartialStep1;