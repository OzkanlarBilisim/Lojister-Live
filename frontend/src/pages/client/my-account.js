import { Avatar, Badge, Button, Col, Collapse, Form, Input, InputNumber, List, Modal, Row, Select, Tabs, Tag, Tooltip } from 'antd';
import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';
import { useSelector } from 'react-redux';
import { getDistrictByProvinceIdRequest, getNeighborhoodRequest, getProvinceRequest } from '../../api/controllers/addresses-controller';
import { getClientByIdRequest } from '../../api/controllers/client-controller';
import { getMyCompanyRequest, updateCompanyRequest } from '../../api/controllers/company-controller';
import { disableDefaultAddressRequest, getMyAddressesRequest, getSavedAddressByIdRequest, updateDefaultAddressRequest, updateSavedAddressRequest } from '../../api/controllers/saved-address-controller';
import AntNotification from '../../components/AntNotification';
import { Countries } from '../../components/Countries';
import ProfilePhoto from '../../components/ProfilePhoto';
import RequestError from '../../components/RequestError';

const MyAccountClient = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Hesabım")
    const { Option } = Select;

    let params = (new URL(document.location)).searchParams;

    const currentUser = useSelector((state) => state.user);
    const [person, setPerson] = useState({});
    //const [editPerson, setEditPerson] = useState({});
    const [company, setCompany] = useState({});
    const [editCompany, setEditCompany] = useState({});
    const [myAddressess, setMyAddressess] = useState([]);
    const [addressModal, setAddressModal] = useState(false);
    //const [personEditModal, setPersonEditModal] = useState(false);
    const [corporateEditModal, setCorporateEditModal] = useState(false);
    const [selectedAddress, setSelectedAddress] = useState({});
    const [provinces, setProvinces] = useState([]);
    const [districts, setDistricts] = useState([]);
    const [neighborhoods, setNeighborhoods] = useState([]);
    const [form] = Form.useForm();

    const editSavedAddressForm = {
        addressName: selectedAddress?.addressName,
        commercialTitle: selectedAddress?.commercialTitle,
        district: selectedAddress?.district,
        firstName: selectedAddress?.firstName,
        fullAddress: selectedAddress?.fullAddress,
        id: selectedAddress?.id,
        lastName: selectedAddress?.lastName,
        lat: selectedAddress?.lat,
        lng: selectedAddress?.lng,
        neighborhood: selectedAddress?.neighborhood,
        phone: selectedAddress?.phone,
        province: selectedAddress?.province,
        selectedCountryCode: selectedAddress?.selectedCountryCode ? selectedAddress?.selectedCountryCode : "+90"
    }

    const editCompanyForm = {
        commercialTitle: editCompany?.commercialTitle,
        taxAdministration: editCompany?.taxAdministration,
        taxNumber: editCompany?.taxNumber,
        fullAddress: editCompany?.address?.fullAddress,
        phone: editCompany?.phone,
        mail: editCompany?.mail,
        financialStaffFirstname: editCompany?.financialStaffFirstname,
        financialStaffLastname: editCompany?.financialStaffLastname,
        financialStaffPhone: editCompany?.financialStaffPhone,
        bankName: editCompany?.bankInformation?.bankName,
        branch: editCompany?.bankInformation?.branch,
        accountNumber: editCompany?.bankInformation?.accountNumber,
        iban: editCompany?.bankInformation?.iban,
        selectedCountryCode: editCompany?.selectedCountryCode ? editCompany?.selectedCountryCode : "+90",
        selectedCountryCodeFinancialStaff: editCompany?.selectedCountryCodeFinancialStaff ? editCompany?.selectedCountryCodeFinancialStaff : "+90"
    }

    //Saved Address Func
    const fetchSavedAddresses = async () => {
        try {
            let res = await getMyAddressesRequest();
            if (res) {
                setMyAddressess(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const openAddressModal = async (address) => {
        try {
            let res = await getSavedAddressByIdRequest(address.id);
            if (res) {
                setSelectedAddress({ ...res.data, selectedCountryCode: res.data.phone.split(/[~]/g)[0], phone: res.data.phone.split(/[~]/g)[1] })
                setAddressModal(true)
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const closeAddressModal = () => {
        setSelectedAddress({});
        setAddressModal(false);
    };

    const handleUpdateAddress = async () => {
        selectedAddress.phone = selectedAddress.selectedCountryCode + "~" + selectedAddress.phone
        setSelectedAddress({ ...selectedAddress })
        try {
            let res = await updateSavedAddressRequest(selectedAddress);
            if (res) {
                fetchSavedAddresses();
                AntNotification({ type: "success", message: t("Kayıtlı bilgi başarıyla güncellendi.") });
                setAddressModal(false);
            }
        } catch (error) {
            RequestError(error);
            selectedAddress.phone = selectedAddress.phone.split(/[~]/g)[1]
            setSelectedAddress({ ...selectedAddress })
        }
    };

    //Update default saved address
    const handleUpdateDefaultAddress = async (id) => {
        try {
            let res = await updateDefaultAddressRequest(id);
            if (res) {
                fetchSavedAddresses();
                AntNotification({ type: "success", message: t("Varsayılan adres değiştirildi.") });
                setAddressModal(false);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    // Disable default address
    const handleDisableDefaultAddress = async () => {

        try {
            let res = await disableDefaultAddressRequest()
            if (res) {
                AntNotification({ type: "success", message: t("Varsayılan adres kaldırıldı.") });
                fetchSavedAddresses();
            }
        } catch (error) {

        }

    }

    //Company Func
    const fetchMyCompany = async () => {
        try {
            let res = await getMyCompanyRequest();
            if (res) {
                setCompany(res.data);
                setEditCompany({ ...res.data, selectedCountryCode: res.data?.phone === "" || res.data?.phone === null ? "+90" : res.data?.phone?.split(/[~]/g)[0], phone: res.data?.phone === "" || res.data?.phone === null ? "" : res.data?.phone?.split(/[~]/g)[1], selectedCountryCodeFinancialStaff: res.data?.financialStaffPhone === "" || res.data?.financialStaffPhone === null ? "+90" : res.data?.financialStaffPhone?.split(/[~]/g)[0], financialStaffPhone: res.data?.financialStaffPhone === "" || res.data?.financialStaffPhone === null ? "" : res.data?.financialStaffPhone?.split(/[~]/g)[1] });
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const closeEditCompanyModal = () => {
        setCorporateEditModal(false);
        setEditCompany(company);
    };

    const handleUpdateCompany = async () => {
        editCompany.phone = editCompany.selectedCountryCode + "~" + editCompany.phone
        editCompany.financialStaffPhone = editCompany.selectedCountryCodeFinancialStaff + "~" + editCompany.financialStaffPhone
        setEditCompany({ ...editCompany })
        try {
            let res = await updateCompanyRequest(editCompany);
            if (res) {
                fetchMyCompany();
                closeEditCompanyModal();
                AntNotification({ type: "success", message: t("Başarıyla kaydedildi.") });
            }
        } catch (error) {
            RequestError(error);
            editCompany.phone = editCompany.phone.split(/[~]/g)[1]
            editCompany.financialStaffPhone = editCompany.financialStaffPhone.split(/[~]/g)[1]
            setEditCompany({ ...editCompany })
        }
    };

    //Person Func
    const fetchPerson = async () => {
        try {
            let res = await getClientByIdRequest(currentUser.id);
            if (res) {
                setPerson(res.data);
                //setEditPerson(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    /* const closeEditPersonModal = () => {
            setPersonEditModal(false);
        };
     
        const handleUpdatePerson = async () => {
            try {
                let res = await updateSavedAddressRequest(person);
                if (res) {
                    fetchSavedAddresses()
                    AntNotification({ type: "success", message: "Kayıtlı bilgi başarıyla güncellendi." })
                    setAddressModal(false)
                }
            } catch (error) {
              RequestError(error);
            }
        }; */

    //control for address
    const setAddress = async () => {
        //fetch province
        try {
            let provinceData = await getProvinceRequest();
            if (provinceData) {
                setProvinces(provinceData.data)
                //fetch district
                if (selectedAddress.province) {
                    let findProvince = provinceData.data.find(v => v.provinceName === selectedAddress.province);
                    try {
                        let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                        if (districtData) {
                            setDistricts(districtData.data)
                            //fetch neighborhood
                            if (selectedAddress.district) {
                                let findDistrict = districtData.data.find(v => v.districtName === selectedAddress.district);
                                try {
                                    let res = await getNeighborhoodRequest(findDistrict.id);
                                    if (res) {
                                        setNeighborhoods(res.data)
                                    }
                                } catch (error) {
                                    RequestError(error);
                                }
                            }
                        }
                    } catch (error) {
                        RequestError(error);
                    }
                }
            }
        } catch (error) {
            RequestError(error);
        }
    };

    useEffect(() => {
        setAddress()
    }, [selectedAddress.province, selectedAddress.district]);

    useEffect(() => {
        fetchPerson();
        fetchMyCompany();
        fetchSavedAddresses();
    }, []);

    useEffect(() => {
        if (selectedAddress) {
            form.setFieldsValue(editSavedAddressForm)
        }
    }, [selectedAddress])

    useEffect(() => {
        if (editCompany) {
            form.setFieldsValue(editCompanyForm)
        }
    }, [editCompany])

    const label = {
        fontWeight: 500,
        opacity: .7,
    };

    const labelValue = {
        fontSize: "1rem"
    };

    return (
        <div style={{ display: "flex", flexDirection: "column", gap: "1rem" }} className="layout-content-padding">
            <div style={{ display: "flex", flexDirection: "column", gap: ".5rem", justifyContent: "center", alignItems: "center" }}>
                <ProfilePhoto />
                <p
                    style={{
                        textAlign: "center",
                        fontSize: "1.1rem"
                    }}>
                    {currentUser.firstName + " " + currentUser.lastName}
                </p>
            </div>
            <Tabs defaultActiveKey={params.get('tabKey') ? params.get('tabKey') : "1"} style={{ padding: "1rem 1.5rem", backgroundColor: "var(--white)", borderRadius: "1rem" }}>
                <Tabs.TabPane tab={t("Kişisel Bilgilerim")} key="1">
                    <Row gutter={[24, 24]}>
                        {/* <Col span={24}>
                            <div style={{ display: "flex", justifyContent: "flex-end" }}>
                                <Button
                                    icon={<i style={{ marginRight: ".5rem" }} className="bi bi-pencil-square"></i>}
                                    type="primary"
                                    onClick={() => setPersonEditModal(true)}>Düzenle</Button>
                            </div>
                        </Col> */}
                        <Col xs={24} md={12}>
                            <p style={label}>{t("İsim")}</p>
                            <p style={labelValue}>{person.firstName}</p>
                        </Col>
                        <Col xs={24} md={12}>
                            <p style={label}>{t("Soyisim")}</p>
                            <p style={labelValue}>{person.lastName}</p>
                        </Col>
                        <Col xs={24} md={12}>
                            <p style={label}>{t("Mail Adresi")}</p>
                            <p style={labelValue}>{person.email}</p>
                        </Col>
                        <Col xs={24} md={12}>
                            <p style={label}>{t("Telefon")}</p>
                            <p style={labelValue}>{person?.phone?.split(/[~]/g)[0] + " " + person?.phone?.split(/[~]/g)[1]}</p>
                        </Col>
                    </Row>
                </Tabs.TabPane>
                {
                    person?.clientType === "CORPORATE" &&
                    <Tabs.TabPane tab={t("Şirket Bilgilerim")} key="2">
                        <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "1rem" }}>
                            <Button
                                icon={<i style={{ marginRight: ".5rem" }} className="bi bi-pencil-square"></i>}
                                type="primary"
                                onClick={() => setCorporateEditModal(true)}>{t("Düzenle")}</Button>
                        </div>
                        <Collapse accordion defaultActiveKey="1">
                            <Collapse.Panel header={t("Şirket Genel Bilgileri")} key="1">
                                <Row gutter={[24, 24]}>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Ticari Ünvan")}</p>
                                        <p style={labelValue}>{company?.commercialTitle ? company?.commercialTitle : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Vergi Dairesi")}</p>
                                        <p style={labelValue}>{company?.taxAdministration ? company?.taxAdministration : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Vergi Numarası")}</p>
                                        <p style={labelValue}>{company?.taxNumber ? company?.taxNumber : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Fatura Adresi")}</p>
                                        <p style={labelValue}>{company?.address?.fullAddress ? company?.address?.fullAddress : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Telefon Numarası")}</p>
                                        <p style={labelValue}>{company?.phone === '' || company?.phone === null || company?.phone === '+90~' ? "-" : company?.phone?.split(/[~]/g)[0] + " " + company?.phone?.split(/[~]/g)[1]}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Mail Adresi")}</p>
                                        <p style={labelValue}>{company?.mail ? company?.mail : "-"}</p>
                                    </Col>
                                </Row>
                            </Collapse.Panel>
                            <Collapse.Panel header={t("Finansal Bilgiler")} key="2">
                                <Row>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Finansal Yetkili İsmi")}</p>
                                        <p style={labelValue}>{company?.financialStaffFirstname ? company?.financialStaffFirstname : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Finansal Yetkili Soyismi")}</p>
                                        <p style={labelValue}>{company?.financialStaffLastname ? company?.financialStaffLastname : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Finansal Yetkili Telefonu")}</p>
                                        <p style={labelValue}>{company?.financialStaffPhone === '' || company?.financialStaffPhone === null || company?.financialStaffPhone === '+90~' ? "-" : company?.financialStaffPhone?.split(/[~]/g)[0] + " " + company?.financialStaffPhone?.split(/[~]/g)[1]}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Banka Adı")}</p>
                                        <p style={labelValue}>{company?.bankInformation?.bankName ? company?.bankInformation?.bankName : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Şube Adı")}</p>
                                        <p style={labelValue}>{company?.bankInformation?.branch ? company?.bankInformation?.branch : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("Hesap Numarası")}</p>
                                        <p style={labelValue}>{company?.bankInformation?.accountNumber ? company?.bankInformation?.accountNumber : "-"}</p>
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <p style={label}>{t("IBAN")}</p>
                                        <p style={labelValue}>{company?.bankInformation?.iban ? company?.bankInformation?.iban : "-"}</p>
                                    </Col>
                                </Row>
                            </Collapse.Panel>
                        </Collapse>
                    </Tabs.TabPane>
                }
                <Tabs.TabPane tab={t("Kayıtlı Bilgilerim")} key="3">
                    <List
                        className="my-address"
                        dataSource={myAddressess}
                        renderItem={item => (
                            <List.Item key={item.id}>
                                <List.Item.Meta
                                    title={item.addressName}
                                    description={item.neighborhood !== null ? item.fullAddress + " " + item.neighborhood + " - " + item.district + "/" + item.province : item.fullAddress + " " + item.province + "/" + item.country}
                                />
                                {
                                    item?.isDefaultAddress ?
                                        <Tag style={{ cursor: "pointer" }} onClick={() => handleDisableDefaultAddress()} color="processing"><i className="bi bi-check2-circle"></i> {t("Varsayılan")} </Tag> :
                                        <Tooltip title={t("Varsayılan olarak ayarla")}>
                                            <Button size="small" onClick={() => handleUpdateDefaultAddress(item.id)} shape='circle' icon={<i className="bi bi-check-lg"></i>}></Button>
                                        </Tooltip>
                                }
                                <Button
                                    style={{ marginLeft: "1rem" }}
                                    icon={<i style={{ marginRight: ".5rem" }} className="bi bi-pencil-square"></i>}
                                    type="primary"
                                    onClick={() => openAddressModal(item)}>
                                    {t("Düzenle")}
                                </Button>
                            </List.Item>
                        )}
                    />
                </Tabs.TabPane>
            </Tabs>

            <Modal
                visible={addressModal}
                title={t("Adresi düzenleyin")}
                onCancel={closeAddressModal}
                onOk={handleUpdateAddress}
                okText={t("Tamam")}
                cancelText={t("İptal")}
                initialValues={editSavedAddressForm}
                centered
            >
                <Form
                    form={form}
                    layout='vertical'
                    initialValues={editSavedAddressForm}
                    onFinish={handleUpdateAddress}
                    scrollToFirstError={true}
                >
                    <Row gutter={[12, 12]}>
                        <Col span={24}>
                            <Form.Item
                                label={t("Adres Başlığı")}
                                name="addressName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Yükleme yapılacak adresin detaylarını girin")}
                                    onChange={e => {
                                        setSelectedAddress({ ...selectedAddress, addressName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("İsim")}
                                name="firstName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Kişi ismini yazın")}
                                    onChange={e => {
                                        setSelectedAddress({ ...selectedAddress, firstName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Soyisim")}
                                name="lastName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Kişi soyismini yazın")}
                                    onChange={e => {
                                        setSelectedAddress({ ...selectedAddress, lastName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Telefon Numarası")}
                                name="phone"
                                rules={[
                                    { required: true, message: t('Lütfen bu alanı doldurun!') },
                                ]}
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCode'
                                        showSearch
                                        size='large'
                                        value={selectedAddress.selectedCountryCode ? selectedAddress.selectedCountryCode : '+90'}
                                        onChange={(value) => {
                                            selectedAddress.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                            setSelectedAddress({ ...selectedAddress })
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
                                    placeholder={t('Telefon numarası yazın')}
                                    customInput={Input}
                                    onValueChange={({ value }) => {
                                        selectedAddress.phone = `${value}`
                                        setSelectedAddress({ ...selectedAddress })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Şirket Ünvanı")}
                                name="commercialTitle"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Şirket ünvanını yazın")}
                                    onChange={e => {
                                        setSelectedAddress({ ...selectedAddress, commercialTitle: e.target.value })
                                    }}
                                />
                            </Form.Item>
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
                                        selectedAddress.province = provinceName
                                        selectedAddress.district = null
                                        selectedAddress.neighborhood = null
                                        selectedAddress.fullAddress = ""
                                        setSelectedAddress({ ...selectedAddress })
                                    }}
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
                                    disabled={!selectedAddress.province}
                                    placeholder={t("Yükleme yapılacak ilçeyi seçin")}
                                    onChange={districtName => {
                                        selectedAddress.district = districtName
                                        selectedAddress.neighborhood = null
                                        selectedAddress.fullAddress = ""
                                        setSelectedAddress({ ...selectedAddress })
                                    }}
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
                                    disabled={!selectedAddress.district}
                                    placeholder={t("Yükleme yapılacak mahalleyi seçin")}
                                    onChange={neighborhood => {
                                        selectedAddress.neighborhood = neighborhood
                                        selectedAddress.fullAddress = ""
                                        setSelectedAddress({ ...selectedAddress })
                                    }}
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
                                    placeholder={t("Yükleme yapılacak adresin detaylarını yazın")}
                                    onChange={e => {
                                        setSelectedAddress({ ...selectedAddress, fullAddress: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            {/*  <Modal
                visible={personEditModal}
                title="Kişisel bilgilerinizi düzenleyin"
                onCancel={closeEditPersonModal}
                onOk={handleUpdatePerson}
                centered
            >
                <Form
                    form={form}
                    layout='vertical'
                    initialValues={person}
                    scrollToFirstError={true}
                >
                    <Row gutter={[12, 12]}>
                        <Col span={24}>
                            <Form.Item
                                label="İsim"
                                name="firstName"
                                rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                            >
                                <Input
                                    size="large"
                                    placeholder="İsminizi yazın"
                                    onChange={e => {
                                        setPerson({ ...person, firstName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label="Soyisim"
                                name="lastName"
                                rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                            >
                                <Input
                                    size="large"
                                    placeholder="Soyisminizi yazın"
                                    onChange={e => {
                                        setPerson({ ...person, lastName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label="Mail adresi"
                                name="email"
                                rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                            >
                                <Input
                                    size="large"
                                    placeholder="Mail adresinizi yazın"
                                    onChange={e => {
                                        setPerson({ ...person, email: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label="Telefon Numarası"
                                name="phone"
                                rules={[
                                    { required: true, message: 'Lütfen bu alanı doldurun!' },
                                ]}
                            >
                                <NumberFormat
                                    name="phone"
                                    format="+90 ### ### ## ##"
                                    size="large"
                                    placeholder='Telefon numaranızı yazın.'
                                    customInput={Input}
                                    value={person.phone}
                                    onValueChange={({ value }) => {
                                        setPerson({ ...person, phone: `+90${value}` })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal> */}

            <Modal
                visible={corporateEditModal}
                title={t("Şirket bilgilerinizi düzenleyin")}
                onCancel={closeEditCompanyModal}
                onOk={handleUpdateCompany}
                cancelText={t("İptal")}
                okText={t("Kaydet")}
                centered
            >
                <Form
                    form={form}
                    layout='vertical'
                    initialValues={editCompanyForm}
                    onFinish={handleUpdateCompany}
                    scrollToFirstError={true}
                >
                    <Row gutter={[12, 12]}>
                        <Col span={24}>
                            <Form.Item
                                label={t("Ticari Ünvan")}
                                name="commercialTitle"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Ticari ünvanı yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, commercialTitle: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Vergi Dairesi")}
                                name="taxAdministration"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Vergi dairesini yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, taxAdministration: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Vergi Numarası")}
                                name="taxNumber"
                            >
                                <NumberFormat
                                    customInput={Input}
                                    onValueChange={({ value }) => setEditCompany({ ...editCompany, taxNumber: value })}
                                    controls={false}
                                    size="large"
                                    placeholder={t("Vergi numarasını yazın")}
                                    style={{ width: "100%" }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Fatura Adresi")}
                                name="fullAddress"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Fatura Adresi yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, address: { ...editCompany.address, fullAddress: e.target.value } })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Telefon Numarası")}
                                name="phone"
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCode'
                                        showSearch
                                        size='large'
                                        value={editCompany.selectedCountryCode ? editCompany.selectedCountryCode : '+90'}
                                        onChange={(value) => {
                                            editCompany.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                            setEditCompany({ ...editCompany })
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
                                    placeholder={t('Telefon numarası yazın')}
                                    customInput={Input}
                                    onValueChange={({ value }) => {
                                        editCompany.phone = `${value}`
                                        setEditCompany({ ...editCompany })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Mail Adresi")}
                                name="mail"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Mail adresini yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, mail: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Finansal Yetkili İsmi")}
                                name="financialStaffFirstname"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Finansal yetkili ismini yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, financialStaffFirstname: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Finansal Yetkili Soyismi")}
                                name="financialStaffLastname"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Finansal yetkili soyismini yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, financialStaffLastname: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Finansal Yetkili Telefonu")}
                                name="financialStaffPhone"
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCodeFinancialStaff'
                                        showSearch
                                        size='large'
                                        value={editCompany.selectedCountryCodeFinancialStaff ? editCompany.selectedCountryCodeFinancialStaff : '+90'}
                                        onChange={(value) => {
                                            editCompany.selectedCountryCodeFinancialStaff = Countries.find(v => v.dial_code === value)?.dial_code
                                            setEditCompany({ ...editCompany })
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
                                    placeholder={t('Finansal yetkili telefonunu yazın.')}
                                    customInput={Input}
                                    onValueChange={({ value }) => {
                                        editCompany.financialStaffPhone = `${value}`
                                        setEditCompany({ ...editCompany })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Banka Adı")}
                                name="bankName"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Banka adını yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, bankInformation: { ...editCompany.bankInformation, bankName: e.target.value } })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Şube Adı")}
                                name="branch"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Şube adını yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, bankInformation: { ...editCompany.bankInformation, branch: e.target.value } })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Hesap Numarası")}
                                name="accountNumber"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Hesap numarasını yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, bankInformation: { ...editCompany.bankInformation, accountNumber: e.target.value } })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("IBAN")}
                                name="iban"
                            >
                                <Input
                                    size="large"
                                    placeholder={t("IBAN yazın")}
                                    onChange={e => {
                                        setEditCompany({ ...editCompany, bankInformation: { ...editCompany.bankInformation, iban: e.target.value } })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>
        </div >
    )
}

export default MyAccountClient;