import { Button, Col, Row, Form, Input, Select, Modal, List, Alert, Divider } from 'antd';
import React, { useEffect, useState } from 'react'
import NumberFormat from 'react-number-format';
import { createGeocodeFromAddressRequest, getDistrictByProvinceIdRequest, getNeighborhoodRequest, getProvinceRequest, reverseGeocodeRequest } from '../../../api/controllers/addresses-controller';
import { CustomButton } from '../../CustomButton';
import { createSavedAddressesRequest, getMyAddressesRequest } from '../../../api/controllers/saved-address-controller';
import MapBoxForCreateTransport from '../../MapBoxForCreateTransport';
import AntNotification from '../../AntNotification';
import { useSelector } from 'react-redux';
import RequestError from '../../RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../Countries';

//CONTAINER STEP 1
const ContainerStep1 = ({ handleClickNextStep, handleClickPrevStep, containerForm, setContainerForm }) => {
    const { t, i18n } = useTranslation("common");
    const { Option } = Select;

    const user = useSelector((state) => state.user);

    const formValues = {
        commercialTitle: containerForm.containerRecipient.commercialTitle,
        firstName: containerForm.containerRecipient.firstName,
        lastName: containerForm.containerRecipient.lastName,
        phoneNumber: containerForm.containerRecipient.phoneNumber,
        customsOfficerPhone: containerForm.customsOfficerPhone,
        province: containerForm.containerAddress.province,
        district: containerForm.containerAddress.district,
        neighborhood: containerForm.containerAddress.neighborhood,
        fullAddress: containerForm.containerAddress.fullAddress,
        address: containerForm.containerAddress.province && containerForm.containerAddress.district && containerForm.containerAddress.neighborhood && containerForm.containerAddress.fullAddress ? containerForm.containerAddress.fullAddress + " " + containerForm.containerAddress.neighborhood + " - " + containerForm.containerAddress.district + "/" + containerForm.containerAddress.province : "",
        selectedCountryCode: containerForm.containerRecipient.selectedCountryCode ? containerForm.containerRecipient.selectedCountryCode : "+90",
        selectedCountryCodeOfficer: containerForm.selectedCountryCodeOfficer ? containerForm.selectedCountryCodeOfficer : "+90"
    };

    const saveAddressForm = {
        addressName: "",
        commercialTitle: containerForm.containerRecipient.commercialTitle,
        district: containerForm.containerAddress.district,
        firstName: containerForm.containerRecipient.firstName,
        fullAddress: containerForm.containerAddress.fullAddress,
        lastName: containerForm.containerRecipient.lastName,
        lat: null,
        lng: null,
        neighborhood: containerForm.containerAddress.neighborhood,
        phone: containerForm.containerRecipient.selectedCountryCode ? containerForm.containerRecipient.selectedCountryCode : "+90" + "~" + containerForm.containerRecipient.phoneNumber,
        province: containerForm.containerAddress.province,
        street: containerForm.containerAddress.street,
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

    //open saved address modal
    const clickOpenSavedAddressesModal = async () => {
        await fetchSavedAddresses();
        setSavedAdressModal(true)
    }

    const onSelectSavedAddress = (savedAddress) => {
        containerForm.containerRecipient.commercialTitle = savedAddress.commercialTitle;
        containerForm.containerRecipient.firstName = savedAddress.firstName;
        containerForm.containerRecipient.lastName = savedAddress.lastName;
        containerForm.containerRecipient.phoneNumber = savedAddress.phone.split(/[~]/g)[1];
        containerForm.containerRecipient.selectedCountryCode = Countries.find(v => v.dial_code === savedAddress?.phone?.slice(0, 3))?.dial_code; containerForm.startingAddress.province = savedAddress.province;
        containerForm.containerAddress.province = savedAddress.province;
        containerForm.containerAddress.district = savedAddress.district;
        containerForm.containerAddress.neighborhood = savedAddress.neighborhood;
        containerForm.containerAddress.fullAddress = savedAddress.fullAddress;
        containerForm.containerAddress.lat = savedAddress.lat;
        containerForm.containerAddress.lng = savedAddress.lng;
        setContainerForm({ ...containerForm });
        setSavedAdressModal(false);
    }

    //create saved address
    const createSavedAddress = async () => {
        try {
            let res = await createSavedAddressesRequest(saveAddressForm);
            if (res) {
                setSaveAddressModal(false);
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
                district: containerForm.containerAddress.district,
                neighborhood: containerForm.containerAddress.neighborhood,
                province: containerForm.containerAddress.province,
                fullAddress: containerForm.containerAddress.fullAddress
            })
            if (res && !isDragged) {
                containerForm.containerAddress.lat = res.data.latitude;
                containerForm.containerAddress.lng = res.data.longitude;
                setContainerForm({ ...containerForm })
            }
        } catch (error) {
            AntNotification({ type: "error", message: t("Bir sorun oluştu!"), description: t("Lütfen yeniden deneyin.") })
        }
    }

    useEffect(() => {
        if (containerForm.containerAddress.district || containerForm.containerAddress.neighborhood || containerForm.containerAddress.province) {
            fetchGeocode()
        }
    }, [containerForm.containerAddress.district, containerForm.containerAddress.neighborhood, containerForm.containerAddress.province, containerForm.containerAddress.fullAddress])

    //get address by dragged marker
    const onDragMarker = async ({ latitude, longitude }) => {
        setIsDragged(true)
        containerForm.containerAddress.lat = latitude;
        containerForm.containerAddress.lng = longitude;
        setContainerForm({ ...containerForm })
        try {
            let res = await reverseGeocodeRequest({
                latitude: latitude,
                longitude: longitude,
            })
            if (res) {
                containerForm.containerAddress.province = res.data.province;
                containerForm.containerAddress.district = res.data.district;
                containerForm.containerAddress.neighborhood = res.data.neighborhood;
                containerForm.containerAddress.fullAddress = res.data.fullAddress;
                setContainerForm({ ...containerForm })
            }
        } catch (error) {
            AntNotification({ type: "error", message: t("Adres tanımlanamadı!"), description: t("Lütfen yeniden deneyin veya adres alanından seçim yaparak devam edin.") })
        }
    }


    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [containerForm])


    //control for address
    const setAddress = async () => {
        setIsAddressLoading(true)
        //fetch province
        try {
            let provinceData = await getProvinceRequest();
            if (provinceData) {
                setProvinces(provinceData.data)
                //fetch district
                if (containerForm.containerAddress.province) {
                    setIsAddressLoading(true)
                    let findProvince = provinceData.data.find(v => v.provinceName === containerForm.containerAddress.province);
                    try {
                        let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                        if (districtData) {
                            setDistricts(districtData.data)
                            //fetch neighborhood
                            if (containerForm.containerAddress.district) {
                                setIsAddressLoading(true)
                                let findDistrict = districtData.data.find(v => v.districtName === containerForm.containerAddress.district);
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
    }, [containerForm.containerAddress.province, containerForm.containerAddress.district])

    return (
        <>
            <Form
                form={form}
                onFinish={handleClickNextStep}
                layout="vertical"
                labelCol={{ span: 24 }}
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
                                    onClick={() => user ? setSaveAddressModal(true) : AntNotification({ type: "warning", message: t("Bu özelliği kullanabilmek için giriş yapmalısınız!") })}
                                    color="light"
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
                                label={t("Şirket Ünvanı")}
                                name="commercialTitle"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Yüklemeyi yapan şirket ünvanını girin")}
                                    onChange={e => {
                                        containerForm.containerRecipient.commercialTitle = e.target.value
                                        setContainerForm({ ...containerForm })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Gümrük Telefon Numarası")}
                                name="customsOfficerPhone"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCodeOfficer'
                                        showSearch
                                        size='large'
                                        value={containerForm.selectedCountryCodeOfficer ? containerForm.selectedCountryCodeOfficer : '+90'}
                                        onChange={(value) => {
                                            containerForm.selectedCountryCodeOfficer = Countries.find(v => v.dial_code === value)?.dial_code
                                            setContainerForm({ ...containerForm })
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
                                        containerForm.customsOfficerPhone = `${value}`
                                        setContainerForm({ ...containerForm })
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
                                        containerForm.containerRecipient.firstName = e.target.value
                                        setContainerForm({ ...containerForm })
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
                                        containerForm.containerRecipient.lastName = e.target.value
                                        setContainerForm({ ...containerForm })
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
                                        value={containerForm.startRecipient.selectedCountryCode ? containerForm.startRecipient.selectedCountryCode : '+90'}
                                        onChange={(value) => {
                                            containerForm.startRecipient.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                            setContainerForm({ ...containerForm })
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
                                        containerForm.containerRecipient.phoneNumber = `${value}`
                                        setContainerForm({ ...containerForm })
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
                                        disabled={!containerForm.containerAddress.province || !containerForm.containerAddress.district || !containerForm.containerAddress.neighborhood || !containerForm.containerAddress.fullAddress}
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
                                    <MapBoxForCreateTransport latitude={containerForm.containerAddress.lat} longitude={containerForm.containerAddress.lng} onDragMarker={onDragMarker} />
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
                                                        containerForm.containerAddress.province = provinceName
                                                        containerForm.containerAddress.district = null
                                                        containerForm.containerAddress.neighborhood = null
                                                        containerForm.containerAddress.fullAddress = ""
                                                        setContainerForm({ ...containerForm })
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
                                                    disabled={!containerForm.containerAddress.province}
                                                    placeholder={t("Yükleme yapılacak ilçeyi seçin")}
                                                    onChange={districtName => {
                                                        containerForm.containerAddress.district = districtName
                                                        containerForm.containerAddress.neighborhood = null
                                                        containerForm.containerAddress.fullAddress = ""
                                                        setContainerForm({ ...containerForm })
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
                                                    disabled={!containerForm.containerAddress.district}
                                                    placeholder={t("Yükleme yapılacak mahalleyi seçin")}
                                                    onChange={neighborhood => {
                                                        containerForm.containerAddress.neighborhood = neighborhood
                                                        containerForm.containerAddress.fullAddress = ""
                                                        setContainerForm({ ...containerForm })
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
                                                        containerForm.containerAddress.fullAddress = e.target.value
                                                        setContainerForm({ ...containerForm })
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

export default ContainerStep1;