import { Button, Col, Row, Form, Input, DatePicker, Select, Modal, List, Space, Alert, Divider } from 'antd';
import React, { useEffect, useState, useMemo } from 'react'
import { createGeocodeFromAddressRequest, getProvinceRequest, reverseGeocodeRequest } from '../../../../../api/controllers/addresses-controller';
import { CustomButton } from '../../../../CustomButton';
import moment from 'moment';
import { createSavedAddressesRequest, getMyAddressesRequest } from '../../../../../api/controllers/saved-address-controller';
import AntNotification from '../../../../AntNotification';
import { useSelector } from 'react-redux';
import locale from "antd/es/date-picker/locale/tr_TR";
import { getAccountSettingsRequest, getMyDefaultAddressRequest } from '../../../../../api/controllers/client-controller';
import RequestError from '../../../../RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../../../Countries';
import GooglePlacesAutocomplete from 'react-google-places-autocomplete';
import portData from "../../../../../components/ports.json"



//Araç STEP 1   containeroutsidepartForm, setContaineroutsidepartForm
const ContainerOutsideStep1 = ({ handleClickNextStep, handleClickPrevStep, containeroutsidepartForm, setContaineroutsidepartForm, }) => {
    const currentUser = useSelector((state) => state.user);

    if(currentUser !== null){
        containeroutsidepartForm.client_id = currentUser.id;
    }


    //json port verisinınde kısıtlama yapmak için gereken fonksiyonlar
    const onChange = (value) => {
        console.log(`selected ${value}`);
    };
    const onSearch = (value) => {
        console.log('search:', value);
    };
    console.log(containeroutsidepartForm);


    const { t, i18n } = useTranslation("common");
    const { Option } = Select;
    const [deger, setDeger] = useState(null);
    // Kutular dolu mu yoksa değilmi kontrol ediyor
    const [trueOrFalse, setTrueOrFalse] = useState(0)
    const [trueOrFalseDue, setTrueOrFalseDue] = useState(0)

    const [valuePort, setValuePort] = useState(containeroutsidepartForm?.startFullAddress? containeroutsidepartForm.startFullAddress : "")
    const [valuePortDue, setValuePortDue] = useState(containeroutsidepartForm?.dueFullAddress? containeroutsidepartForm.dueFullAddress : "")


    

    const requiredControl = () =>{

        if(valuePort["label"] !== undefined){
            containeroutsidepartForm.startFullAddress = valuePort["label"];
        }else{
            containeroutsidepartForm.startFullAddress = " " + valuePort
        }

        if(valuePortDue["label"] !== undefined){
            containeroutsidepartForm.dueFullAddress = valuePortDue["label"];
        }else{
            containeroutsidepartForm.dueFullAddress = " " + valuePortDue
        }

        if(valuePort !== ""){
            setTrueOrFalse(1)
          }else{
            setTrueOrFalse(0)
          }
          
        if(valuePortDue !== ""){
            setTrueOrFalseDue(1)
          }else{
            setTrueOrFalseDue(0)
          }
    }
    


const [countryPortFilter1, setCountryPortFilter1] = useState([])
const [countryPortFilter2, setCountryPortFilter2] = useState([])

// nakliye yolu seçildikten sonra veriyikaydetmek ve aksiyon almak için
const [rota, setRota] = useState("");



// Switch-Case Taşıma Tipi İçin   
    const handleChange = (value) => {
        containeroutsidepartForm.startDeliveryMethod = value;
        console.log(`selected ${value}`);

        setContaineroutsidepartForm({ ...containeroutsidepartForm })
        
        setTrueOrFalse(0)
        setTrueOrFalseDue(0)
        setValuePort("")
        setValuePortDue("")

    };

    const user = useSelector((state) => state.user);

    const formValues = {
        //adStartingDate: adStartingDate,
        // commercialTitle: containeroutsidepartForm.startRecipient.commercialTitle,
        // firstName: containeroutsidepartForm.startRecipient.firstName,
        // lastName: containeroutsidepartForm.startRecipient.lastName,
        // phoneNumber: containeroutsidepartForm.startRecipient.phoneNumber,
        // province: containeroutsidepartForm.startingAddress.province,
        /*  district: containeroutsidepartForm.startingAddress.district,
         neighborhood: containeroutsidepartForm.startingAddress.neighborhood, */
        // fullAddress: containeroutsidepartForm.startFullAddress,
        /*  address: containeroutsidepartForm.startingAddress.province && containeroutsidepartForm.startingAddress.district && containeroutsidepartForm.startingAddress.neighborhood && containeroutsidepartForm.startFullAddress ? containeroutsidepartForm.startFullAddress + " " + containeroutsidepartForm.startingAddress.neighborhood + " - " + containeroutsidepartForm.startingAddress.district + "/" + containeroutsidepartForm.startingAddress.province : "",*/
        // zipCode: containeroutsidepartForm.startingAddress.zipCode,
        countryStart: containeroutsidepartForm.startSelectCountryCode,
        // portStart: containeroutsidepartForm.startingAddress.port,
        // portDue: containeroutsidepartForm.dueAddress.port,
        countryDue: containeroutsidepartForm.dueSelectCountryCode,
        Dates: containeroutsidepartForm.startDate,
        countryDue2: containeroutsidepartForm?.dueSelectCountryName,
        countryStart2: containeroutsidepartForm?.startSelectCountryName,
        DeliveryMethod: containeroutsidepartForm.startDeliveryMethod,
        FullDate: containeroutsidepartForm.fullDate,

     
        // selectedCountryCode: containeroutsidepartForm.startRecipient.selectedCountryCode ? containeroutsidepartForm.startRecipient.selectedCountryCode : "+90"
    };

    // const saveAddressForm = {
    //     addressName: "",
    //     /*commercialTitle: containeroutsidepartForm.startRecipient.commercialTitle, */
    //     /* district: containeroutsidepartForm.startingAddress.district, */
    //     // firstName: containeroutsidepartForm.startRecipient.firstName,
    //     // fullAddress: containeroutsidepartForm.startFullAddress,
    //     // lastName: containeroutsidepartForm.startRecipient.lastName,
    //     // lat: null,
    //     // lng: null,
    //     /* neighborhood: containeroutsidepartForm.startingAddress.neighborhood, */
    //     // phone: containeroutsidepartForm.startRecipient.selectedCountryCode ? containeroutsidepartForm.startRecipient.selectedCountryCode : "+90" + "~" + containeroutsidepartForm.startRecipient.phoneNumber,
    //     province: containeroutsidepartForm.startingAddress.province,
    //     /* street: containeroutsidepartForm.startingAddress.street, */
    //     zipCode: containeroutsidepartForm.startingAddress.zipCode,
    //     countryStart: containeroutsidepartForm.startSelectCountryCode,
    //     portStart: containeroutsidepartForm.startingAddress.port, // Burada hata alacağız
    //     portDue: containeroutsidepartForm.dueAddress.port, // Burada hata alacağız
    //     countryDue: containeroutsidepartForm.dueSelectCountryCode,
    //     Dates: containeroutsidepartForm.startDate,
    //     dueCity: containeroutsidepartForm.dueAddress.province,
    //     DeliveryMethod: containeroutsidepartForm.startDeliveryMethod,
    //     FullDate: containeroutsidepartForm.fullDate,

    //     regionType: 'INTERNATIONAL'
    // }

    const [isDragged, setIsDragged] = useState(false)

    //data states
    const [provinces, setProvinces] = useState([])
    // const [districts, setDistricts] = useState([])
    // const [neighborhoods, setNeighborhoods] = useState([])
    const [myAddressess, setMyAddressess] = useState([])

    //modal states
    const [savedAdressModal, setSavedAdressModal] = useState(false)
    const [addressModal, setAddressModal] = useState(false)
    const [saveAddressModal, setSaveAddressModal] = useState(false)
    const portDataFliter = null;
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
        feacthPortList1()
        feacthPortList2()
        fetchAccountSettings()
    }, [])

    const [savedAddress, setSavedAddress] = useState()
    //get my default saved address
    const fetchMyDefaultAddress = async () => {
        try {
            let res = await getMyDefaultAddressRequest();
            if (res) {
                setSavedAddress(res.data)
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
        containeroutsidepartForm.startRecipient.commercialTitle = savedAddress.commercialTitle;
        containeroutsidepartForm.startRecipient.firstName = savedAddress.firstName;
        containeroutsidepartForm.startRecipient.lastName = savedAddress.lastName;
        containeroutsidepartForm.startRecipient.phoneNumber = savedAddress.phone.split(/[~]/g)[1];
        containeroutsidepartForm.startRecipient.selectedCountryCode = Countries.find(v => v.dial_code === savedAddress?.phone?.slice(0, 3))?.dial_code;
        //containeroutsidepartForm.startingAddress.province = savedAddress.province;
        containeroutsidepartForm.startSelectCountryCode = savedAddress.country;
        //containeroutsidepartForm.startingAddress.zipCode = savedAddress.zipCode;
        /* containeroutsidepartForm.startingAddress.district = savedAddress.district;
        containeroutsidepartForm.startingAddress.neighborhood = savedAddress.neighborhood; */
        containeroutsidepartForm.startFullAddress = savedAddress.fullAddress;
        /* containeroutsidepartForm.startingAddress.lat = savedAddress.lat;
        containeroutsidepartForm.startingAddress.lng = savedAddress.lng; */
        setContaineroutsidepartForm({ ...containeroutsidepartForm });
        setSavedAdressModal(false);
    }
    //create saved address
    // const createSavedAddress = async () => {
    //     try {
    //         let res = await createSavedAddressesRequest(saveAddressForm);
    //         if (res) {
    //             setSaveAddressModal(false);
    //             saveAddressForm.addressName = ""
    //             AntNotification({ type: "success", message: t("Adres başarıyla kaydedildi.") })
    //         }
    //     } catch (error) {
    //         RequestError(error);
    //     }
    // }

    //get geo code -lat, log

    /*  useEffect(() => {
         if (containeroutsidepartForm.startingAddress.district || containeroutsidepartForm.startingAddress.neighborhood || containeroutsidepartForm.startingAddress.province) {
             fetchGeocode()
         }
     }, [containeroutsidepartForm.startingAddress.district, containeroutsidepartForm.startingAddress.neighborhood, containeroutsidepartForm.startingAddress.province, containeroutsidepartForm.startFullAddress]) */

    //get address by dragged marker
    

    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [containeroutsidepartForm])

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
                /*  if (containeroutsidepartForm.startingAddress.province) {
                     setIsAddressLoading(true)
                     let findProvince = provinceData.data.find(v => v.provinceName === containeroutsidepartForm.startingAddress.province);
                     try {
                         let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                         if (districtData) {
                             setDistricts(districtData.data)
                             //fetch neighborhood
                             if (containeroutsidepartForm.startingAddress.district) {
                                 setIsAddressLoading(true)
                                 let findDistrict = districtData.data.find(v => v.districtName === containeroutsidepartForm.startingAddress.district);
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
    const feacthPortList1 = () =>{
        if(containeroutsidepartForm.startDeliveryMethod !== "d2d" || containeroutsidepartForm.startDeliveryMethod !== "d2p"){
            if(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu"){
                if(containeroutsidepartForm?.startSelectCountryCode !== null){
                    setValuePort(containeroutsidepartForm?.startFullAddress)
                    const portDataFliter = portData.PORTS.filter(PORT => PORT.code === containeroutsidepartForm.startSelectCountryCode)
                    containeroutsidepartForm.startSelectCountryName = Countries.find(Countries => Countries.code === containeroutsidepartForm.startSelectCountryCode)?.name
                    setCountryPortFilter1(portDataFliter)
                    setContaineroutsidepartForm({ ...containeroutsidepartForm })
                    setIsDragged(false)
                }
            }
        }
    }
    const feacthPortList2 = () =>{
        if(containeroutsidepartForm.startDeliveryMethod !== "d2d" || containeroutsidepartForm.startDeliveryMethod !== "p2d"){
            if(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu"){
                if(containeroutsidepartForm?.dueSelectCountryCode !== null){
                    setValuePortDue(containeroutsidepartForm?.dueFullAddress)
                    const portDataFliter = portData.PORTS.filter(PORT => PORT.code === containeroutsidepartForm.dueSelectCountryCode)
                    setCountryPortFilter2(portDataFliter)
                    containeroutsidepartForm.dueSelectCountryCode = containeroutsidepartForm.dueSelectCountryCode
                    containeroutsidepartForm.dueSelectCountryName = Countries.find(Countries => Countries.code === containeroutsidepartForm.dueSelectCountryCode)?.name
                    setContaineroutsidepartForm({ ...containeroutsidepartForm })
                    setIsDragged(false)
                }
            }
        }
    }
    /*  useEffect(() => {
         setAddress()
     }, [containeroutsidepartForm.startingAddress.province, containeroutsidepartForm.startingAddress.district]) */
    
    
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
                            {/* <CustomButton
                                type="outlined"
                                onClick={() => user ? clickOpenSavedAddressesModal() : AntNotification({ type: "warning", message: t("Bu özelliği kullanabilmek için giriş yapmalısınız!") })}
                            >
                                {t("Kayıtlı Bilgilerden Doldur")}
                            </CustomButton> */}
                        </div>

                        <div style={{ display: "flex", gap: ".5rem" }}>
                            {/* {
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
                            } */}
                            <Form.Item style={{ margin: 0 }}>
                                <CustomButton
                                    onClick={requiredControl}
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
                                name="FullDate"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            
                            >
                                <DatePicker.RangePicker
                                    showNow={false}  
                                    size="large"
                                    placeholder={["Yükleme Tarihi", "Boşaltma Tarihi"]}
                                    style={{ width: "100%" }}
                                    format="DD/MM/YYYY"
                                    disabledDate={current => moment().startOf('day') >= current}
                                    disabledTime={disabledDateTime}
                                    onChange={(values) =>{
                                        
                                        containeroutsidepartForm.fullDate = values
                                        containeroutsidepartForm.startDate = values.map(item=>{
                                            return moment(item).format('DD-MM-YYYY  ')
                                        })
                                        containeroutsidepartForm.dueDate   = containeroutsidepartForm.startDate[1]
                                        containeroutsidepartForm.startDate = containeroutsidepartForm.startDate[0]
                                        console.log(containeroutsidepartForm.startDate);
                                        console.log(containeroutsidepartForm.dueDate);
                                    }}
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
                                label={t("Teslimat Şekli")}
                                name="DeliveryMethod"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >    
                                    <Select 
                                        size="large"
                                        placeholder={t("Teslimat Şeklini seçin")}
                                        allowClear
                                        onChange={handleChange}
                                        options={[
                                            {
                                            value: 'd2d',
                                            label: 'Kapıdan Kapıya',
                                            },
                                            {
                                            value: 'p2p',
                                            label: 'Limandan Limana',
                                            },
                                            {
                                            value: 'd2p',
                                        
                                            label: 'Kapıdan Limana ',
                                            },
                                            {
                                            value: 'p2d',
                                            label: 'Limandan Kapıya',
                                            },
                                        ]}
                                    /> 
                            </Form.Item>            
                        </Col>
                    </Row>
                    <Row gutter={[24, 24]}>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                                label={t("Yükleme Yapılacak Ülke")}
                                name="countryStart2"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                            <Select
                               size="large"
                               placeholder={t("Yükleme Yapılacak Ülke")}
                               onChange={countryName => {
                                containeroutsidepartForm.startSelectCountryCode = countryName
                                const portDataFliter = portData.PORTS.filter(PORT => PORT.code === countryName)
                                containeroutsidepartForm.startSelectCountryName = Countries.find(Countries => Countries.code === countryName)?.name
                                setCountryPortFilter1(portDataFliter)
                                setContaineroutsidepartForm({ ...containeroutsidepartForm })
                                setIsDragged(false)
                            }}
                               allowClear
                               showSearch
                               optionFilterProp="children"
                               filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                               
                            >
                            {
                                Countries.map((v, i) =>
                                    <Option key={i} value={v.code}>{v.name}</Option>
                                )
                            }
                            </Select>
                        
                         </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                                label={t("Teslimat Yapılacak Ülke")}
                                name="countryDue2"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}                               
                            >   
                            <Select
                               size="large"
                               placeholder={t("Teslimat Yapılacak Ülke")}
                               onChange={countryName => {
                                const portDataFliter = portData.PORTS.filter(PORT => PORT.code === countryName)
                                setCountryPortFilter2(portDataFliter)
                                containeroutsidepartForm.dueSelectCountryCode = countryName
                                containeroutsidepartForm.dueSelectCountryName = Countries.find(Countries => Countries.code === countryName)?.name
                                setContaineroutsidepartForm({ ...containeroutsidepartForm })
                                setIsDragged(false)
                                console.log(countryName)
                            }}
                               allowClear
                               showSearch
                               optionFilterProp="children"
                               filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                               
                            >
                                {
                                    Countries.map((v, i) =>
                                        <Option key={i} value={v.code}>{v.name}</Option>
                                    )
                                }
                            </Select>
                        
                         </Form.Item>
                        </Col>
                    </Row>
                    {/* Şehir Posta Kodu Ve Limanlar */}
                    {(containeroutsidepartForm.startDeliveryMethod === "d2d"  && containeroutsidepartForm.dueSelectCountryCode !== null && containeroutsidepartForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yükleme Noktası")}
                                name="city1"
                                rules={[{  required: (containeroutsidepartForm.startDeliveryMethod === "d2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                            <GooglePlacesAutocomplete
                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}
                                
                                selectProps={{
                                   
                                    placeholder: 'Şehir veya Posta Kodu Giriniz.',
                                    onChange: setValuePort,
                                    styles: {
                                        input: (provided) => ({
                                          ...provided,
                                        }),
                                        option: (provided) => ({
                                          ...provided,
                                        }),
                                        singleValue: (provided) => ({
                                          ...provided,
                                        }),
                                      },
                                }}
                                autocompletionRequest={{
                                    componentRestrictions: {
                                    
                                    country: containeroutsidepartForm.startSelectCountryCode,
                                    }
                                }}
                             />
                        
                         </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="city2"
                                rules={[{  required: (containeroutsidepartForm.startDeliveryMethod === "d2d")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                           
                           <GooglePlacesAutocomplete
                           
                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}
                                selectProps={{
                                    placeholder: 'Şehir veya Posta Kodu Giriniz.',
                                    onChange: setValuePortDue,
                                }}
                                autocompletionRequest={{
                                    componentRestrictions: {
                                    country: containeroutsidepartForm.dueSelectCountryCode,
                                    }
                                }}
                             />

                         </Form.Item>
                        </Col>
                    </Row>
                    }
                    
                    {(containeroutsidepartForm.startDeliveryMethod === "p2p" && containeroutsidepartForm.dueSelectCountryCode !== null && containeroutsidepartForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                        {containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" && containeroutsidepartForm.startDeliveryMethod === "p2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                                >   
                                <Input.Group compact>
                                    
                                    <Col
                                        placeholder="Lütfen Bir Liman Seçin"
                                        style={{
                                            width: '100%',
                                        }}
                                    >
                                            <GooglePlacesAutocomplete
                                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}
                                                     
                                                selectProps={{ 
                                                valuePort,
                                                onChange: setValuePort,       
                                                placeholder: 'Liman İsmi Girin..',

                                                styles: {
                                                    control: (baseStyles) => ({
                                                    ...baseStyles,
                                                    border: "none",
                                                                                                
                                                    
                                                    }),
                                            
                                                    input: (provided) => ({
                                                        ...provided,
                                                        height:32,    
                                                                                                    
                                            
                                                    }),
                                                    option: (provided) => ({
                                                        ...provided,

                                                    

                                                    }),
                                                    singleValue: (provided) => ({
                                                        ...provided,
                                                    }),
                                                    },
                                                }}
                                                autocompletionRequest={{
                                                    types: ["airport"],
                                                    
                                                    componentRestrictions: {
                                                                            
                                                    country: containeroutsidepartForm.startSelectCountryCode,
                                                    }
                                                }}
                                            />
                                    </Col>
                                </Input.Group>
                                
                            
                            </Form.Item>
                        }
                        {(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" || containeroutsidepartForm.vehicleOrContainer === "") &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "" && containeroutsidepartForm.startDeliveryMethod === "p2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true :(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" && containeroutsidepartForm.startDeliveryMethod === "p2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                                >   
                                <Input.Group compact>
                                    <Col
                                        placeholder="Lütfen Bir Liman Seçin"
                                        style={{
                                            width: '100%',
                                        }}
                                    >
                                            <Select
                                            
                                            onSelect={setValuePort}
                                            size='large'
                                            showSearch
                                            style={{
                                                width: '100%',
                                            }}
                                            placeholder="Lütfen Bir Liman Seçin"
                                            optionFilterProp="children"
                                            defaultValue={containeroutsidepartForm?.startFullAddress !== null ? containeroutsidepartForm.startFullAddress : null}
                                            onChange={onChange}
                                            onSearch={onSearch}
                                            filterOption={(input, option) =>
                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                            }
                                        >
                                            {countryPortFilter1.map((countryPortFilter1) => {
                                                    return <Option label={countryPortFilter1.name} value={countryPortFilter1.name} key={countryPortFilter1.name}> {countryPortFilter1.name}</Option>
                                                })
                                            }
                                        </Select>
                                    </Col>
                                </Input.Group>
                            
                            </Form.Item>
                        }
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        {containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" &&
                            <Form.Item
                                    label={t("Teslimat Noktası")}
                                    name="DueCity"
                                    rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" && containeroutsidepartForm.startDeliveryMethod === "p2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                                >   
                                
                                <Input.Group compact>
                                        <Col
                                            placeholder="Lütfen Bir Liman Seçin"
                                            style={{
                                                width: '100%',
                                            }}
                                        >
                                                <GooglePlacesAutocomplete
                                                    apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}             
                                                    selectProps={{
                                                                         
                                                    placeholder: 'Liman İsmi Girin.',
                                                    onChange: setValuePortDue,
                                                    styles: {
                                                        control: (baseStyles) => ({
                                                            ...baseStyles,
                                                            border: "none",}),

                                                        input: (provided) => ({
                                                            ...provided,
                                                            height:32,
                                                
                                                        }),
                                                        option: (provided) => ({
                                                            ...provided,
                                                        }),
                                                        singleValue: (provided) => ({
                                                            ...provided,
                                                        }),
                                                        },
                                                    }}
                                                    autocompletionRequest={{
                                                        types: ["airport"],
                                                        componentRestrictions: {
                                                                                
                                                        country: containeroutsidepartForm.dueSelectCountryCode,
                                                        }
                                                    }}
                                                />
                                        </Col>
                                    </Input.Group>
                            
                            </Form.Item>
                        }
                        {(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" || containeroutsidepartForm.vehicleOrContainer === "") &&
                            <Form.Item
                                    label={t("Teslimat Noktası")}
                                    name="DueCity"
                                    rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "" && containeroutsidepartForm.startDeliveryMethod === "p2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true :(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" && containeroutsidepartForm.startDeliveryMethod === "p2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                                >   
                                
                                <Input.Group compact>
                                        <Col
                                            placeholder="Lütfen Bir Liman Seçin"
                                            style={{
                                                width: '100%',
                                            }}
                                        >
                                                <Select
                                                onSelect={setValuePortDue}
                                                size='large'
                                                showSearch
                                                style={{
                                                    width: '100%',
                                                }}
                                                placeholder="Lütfen Bir Liman Seçin"
                                                defaultValue={containeroutsidepartForm?.dueFullAddress !== null ? containeroutsidepartForm.dueFullAddress : null}
                                                optionFilterProp="children"
                                                onChange={onChange}
                                                onSearch={onSearch}
                                                filterOption={(input, option) =>
                                                (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                                }
                                            >
                                                {countryPortFilter2.map((countryPortFilter2) => {
                                                        return <Option label={countryPortFilter2.name} key={countryPortFilter2.name}> {countryPortFilter2.name}</Option>
                                                    })
                                                }
                                            </Select>
                                        </Col>
                                    </Input.Group>
                            
                            </Form.Item>
                        }
                        </Col>
                    </Row>
                    }
                    {(containeroutsidepartForm.startDeliveryMethod === "d2p" && containeroutsidepartForm.dueSelectCountryCode !== null && containeroutsidepartForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yükleme Noktası")}
                                name="City"
                                rules={[{  required: (containeroutsidepartForm.startDeliveryMethod === "d2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                            <GooglePlacesAutocomplete
                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}

                                selectProps={{
                                    placeholder: 'Şehir veya Posta Kodu Giriniz.',
                                    onChange: setValuePort,
                                    styles: {
                                        input: (provided) => ({
                                          ...provided,
                                        }),
                                        option: (provided) => ({
                                          ...provided,
                                        }),
                                        singleValue: (provided) => ({
                                          ...provided,
                                        }),
                                      },
                                }}
                                autocompletionRequest={{
                                                                     
                                    componentRestrictions: {
                                    country: containeroutsidepartForm.startSelectCountryCode,
                                    }
                                }}
                             />
                        
                         </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        {containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" &&
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="DueCity"
                                rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" && containeroutsidepartForm.startDeliveryMethod === "d2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                            <Input.Group compact>
                                <Col
                                    placeholder="Lütfen Bir Liman Seçin"
                                    style={{
                                        width: '100%',
                                    }}
                                >
                                        <GooglePlacesAutocomplete
                                            apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}                  
                                            selectProps={{                       
                                            placeholder: 'Liman İsmi Girin..',
                                            onChange: setValuePortDue,
                                            styles: {
                                                control: (baseStyles) => ({
                                                  ...baseStyles,
                                                  border: "none",
                                                                                               
                                                  
                                                }),
                                           
                                                input: (provided) => ({
                                                    ...provided,
                                                    height:32,    
                                                                                                 
                                        
                                                }),
                                                option: (provided) => ({
                                                    ...provided,

                                                  

                                                }),
                                                singleValue: (provided) => ({
                                                    ...provided,
                                                }),
                                                },
                                            }}
                                            autocompletionRequest={{
                                                types: ["airport"],
                                                
                                                componentRestrictions: {
                                                                        
                                                country: containeroutsidepartForm.dueSelectCountryCode,
                                                }
                                            }}
                                        />
                                </Col>
                            </Input.Group>
                        
                         </Form.Item>
                        }
                        {(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" || containeroutsidepartForm.vehicleOrContainer === "") &&
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="DueCity"
                                
                                rules={[{ required: (containeroutsidepartForm.vehicleOrContainer === "" && containeroutsidepartForm.startDeliveryMethod === "d2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true :(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" && containeroutsidepartForm.startDeliveryMethod === "d2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                            <Input.Group compact>
                                <Col
                                    placeholder="Lütfen Bir Liman Seçin"
                                    style={{
                                        width: '100%',
                                    }}
                                >
                                        <Select
                                        size='large'
                                        showSearch
                                        style={{
                                            width: '100%',
                                        }}
                                        placeholder="Lütfen Bir Liman Seçin"
                                        optionFilterProp="children"
                                        onSelect={setValuePortDue}
                                        onChange={onChange}
                                        defaultValue={containeroutsidepartForm?.dueFullAddress !== null ? containeroutsidepartForm.dueFullAddress : null}
                                        onSearch={onSearch}
                                        filterOption={(input, option) =>
                                        (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                        }
                                    >
                                        {countryPortFilter2.map((countryPortFilter2) => {
                                                return <Option label={countryPortFilter2.name} key={countryPortFilter2.name}> {countryPortFilter2.name}</Option>
                                            })
                                        }
                                    </Select>
                                </Col>
                            </Input.Group>
                        
                         </Form.Item>
                        }
                        </Col>
                    </Row>
                    }
                    {(containeroutsidepartForm.startDeliveryMethod === "p2d"  && containeroutsidepartForm.dueSelectCountryCode !== null && containeroutsidepartForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            {containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "Hava Yolu" && containeroutsidepartForm.startDeliveryMethod === "p2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                                >   
                                <Input.Group compact>
                                    <Col
                                        placeholder="Lütfen Bir Liman Seçin"
                                        style={{
                                            width: '100%',
                                        }}
                                    >
                                            <GooglePlacesAutocomplete
                                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}                  
                                                selectProps={{                       
                                                placeholder: 'Liman İsmi Girin..',
                                                onChange: setValuePort,
                                                styles: {
                                                    control: (baseStyles) => ({
                                                    ...baseStyles,
                                                    border: "none",
                                                                                                
                                                    
                                                    }),
                                            
                                                    input: (provided) => ({
                                                        ...provided,
                                                        height:32,    
                                                                                                    
                                            
                                                    }),
                                                    option: (provided) => ({
                                                        ...provided,

                                                    

                                                    }),
                                                    singleValue: (provided) => ({
                                                        ...provided,
                                                    }),
                                                    },
                                                }}
                                                autocompletionRequest={{
                                                    types: ["airport"],
                                                    
                                                    componentRestrictions: {
                                                                            
                                                    country: containeroutsidepartForm.startSelectCountryCode,
                                                    }
                                                }}
                                            />
                                    </Col>
                                </Input.Group>
                            
                            </Form.Item>
                        }
                        {(containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" || containeroutsidepartForm.vehicleOrContainer === "") &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    
                                    rules={[{  required: (containeroutsidepartForm.vehicleOrContainer === "Deniz Yolu" && containeroutsidepartForm.startDeliveryMethod === "p2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                                >  
                                <Input.Group compact>
                                    <Col
                                        placeholder="Lütfen Bir Liman Seçin"
                                        style={{
                                            width: '100%',
                                        }}
                                    >
                                            <Select
                                            size='large'
                                            showSearch
                                            style={{
                                                width: '100%',
                                            }}
                                            placeholder="Lütfen Bir Liman Seçin"
                                            optionFilterProp="children"
                                            onSelect={setValuePort}
                                            defaultValue={containeroutsidepartForm?.startFullAddress !== null ? containeroutsidepartForm.startFullAddress : null}
                                            onChange={onChange}
                                            onSearch={onSearch}
                                            filterOption={(input, option) =>
                                            (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                            }
                                        >
                                            {countryPortFilter1.map((countryPortFilter1) => {
                                                    return <Option label={countryPortFilter1.name} key={countryPortFilter1.name}> {countryPortFilter1.name}</Option>
                                                })
                                            }
                                        </Select>
                                    </Col>
                                </Input.Group>
                            
                            </Form.Item>
                        }
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="DueCity"
                                rules={[{  required: (containeroutsidepartForm.startDeliveryMethod === "p2d")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                            <GooglePlacesAutocomplete
                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}

                                selectProps={{
                                    placeholder: 'Şehir veya Posta Kodu Giriniz.',
                                    onChange: setValuePortDue,
                                    styles: {
                                        input: (provided) => ({
                                          ...provided,
                                        }),
                                        option: (provided) => ({
                                          ...provided,
                                        }),
                                        singleValue: (provided) => ({
                                          ...provided,
                                        }),
                                      },
                                }}
                                autocompletionRequest={{
                                    componentRestrictions: {
                                    country: containeroutsidepartForm.dueSelectCountryCode,
                                    }
                                }}
                             />
                        
                         </Form.Item>
                        </Col>
                    </Row>
                } 
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
                            <Button type="primary" onClick={() => onSelectSavedAddress(item)}> {t("Seç")}</Button>
                        </List.Item>
                    )}
                />
            </Modal >

            {/* <Modal
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
            </Modal> */}

        </>
    );
};

export default ContainerOutsideStep1;