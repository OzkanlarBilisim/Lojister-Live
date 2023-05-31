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



//Araç STEP 1
const ContainerOutsideStep1 = ({ handleClickNextStep, handleClickPrevStep, containeroutsideForm, setContaineroutsideForm, }) => {
    const currentUser = useSelector((state) => state.user);

    if(currentUser !== null){
        containeroutsideForm.client_id = currentUser.id;
    }

    //json port verisinınde kısıtlama yapmak için gereken fonksiyonlar
    const onChange = (value) => {
        console.log(`selected ${value}`);
    };
    const onSearch = (value) => {
        console.log('search:', value);
    };


    const { t, i18n } = useTranslation("common");
    const { Option } = Select;
    const [deger, setDeger] = useState(null);
    // Kutular dolu mu yoksa değilmi kontrol ediyor
    const [trueOrFalse, setTrueOrFalse] = useState(0)
    const [trueOrFalseDue, setTrueOrFalseDue] = useState(0)
    
    const [valuePort, setValuePort] = useState(containeroutsideForm?.startFullAddress? containeroutsideForm.startFullAddress : "")
    const [valuePortDue, setValuePortDue] = useState(containeroutsideForm?.dueFullAddress? containeroutsideForm.dueFullAddress : "")


    

    const requiredControl = () =>{

        if(valuePort["label"] !== undefined){
            console.log(valuePort["label"])
            containeroutsideForm.startFullAddress = valuePort["label"];
        }else{
            console.log(valuePort)
            containeroutsideForm.startFullAddress = " " + valuePort
        }

        if(valuePortDue["label"] !== undefined){
            console.log(valuePortDue["label"])
            containeroutsideForm.dueFullAddress = valuePortDue["label"];
        }else{
            console.log(valuePortDue)
            containeroutsideForm.dueFullAddress = " " + valuePortDue
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
        containeroutsideForm.startDeliveryMethod = value;
        console.log(`selected ${value}`);

        setContaineroutsideForm({ ...containeroutsideForm })
        
        setTrueOrFalse(0)
        setTrueOrFalseDue(0)
        setValuePort("")
        setValuePortDue("")

    };

    const user = useSelector((state) => state.user);

    const formValues = {
        //adStartingDate: adStartingDate,
        // commercialTitle: containeroutsideForm.startRecipient.commercialTitle,
        // firstName: containeroutsideForm.startRecipient.firstName,
        // lastName: containeroutsideForm.startRecipient.lastName,
        // phoneNumber: containeroutsideForm.startRecipient.phoneNumber,
        // province: containeroutsideForm.startingAddress.province,
        /*  district: containeroutsideForm.startingAddress.district,
         neighborhood: containeroutsideForm.startingAddress.neighborhood, */
        // fullAddress: containeroutsideForm.startFullAddress,
        /*  address: containeroutsideForm.startingAddress.province && containeroutsideForm.startingAddress.district && containeroutsideForm.startingAddress.neighborhood && containeroutsideForm.startFullAddress ? containeroutsideForm.startFullAddress + " " + containeroutsideForm.startingAddress.neighborhood + " - " + containeroutsideForm.startingAddress.district + "/" + containeroutsideForm.startingAddress.province : "",*/
        // zipCode: containeroutsideForm.startingAddress.zipCode,
        countryStart: containeroutsideForm.startSelectCountryCode,
        // portStart: containeroutsideForm.startingAddress.port,
        // portDue: containeroutsideForm.dueAddress.port,
        countryDue: containeroutsideForm.dueSelectCountryCode,
        Dates: containeroutsideForm.startDate,
        // dueCity: containeroutsideForm.dueAddress.province,
        DeliveryMethod: containeroutsideForm.startDeliveryMethod,
        FullDate: containeroutsideForm.fullDate,
        
        countryDue2: containeroutsideForm?.dueSelectCountryName,
        countryStart2: containeroutsideForm?.startSelectCountryName,

     
        // selectedCountryCode: containeroutsideForm.startRecipient.selectedCountryCode ? containeroutsideForm.startRecipient.selectedCountryCode : "+90"
    };

    // const saveAddressForm = {
    //     addressName: "",
    //     /*commercialTitle: containeroutsideForm.startRecipient.commercialTitle, */
    //     /* district: containeroutsideForm.startingAddress.district, */
    //     // firstName: containeroutsideForm.startRecipient.firstName,
    //     // fullAddress: containeroutsideForm.startFullAddress,
    //     // lastName: containeroutsideForm.startRecipient.lastName,
    //     // lat: null,
    //     // lng: null,
    //     /* neighborhood: containeroutsideForm.startingAddress.neighborhood, */
    //     // phone: containeroutsideForm.startRecipient.selectedCountryCode ? containeroutsideForm.startRecipient.selectedCountryCode : "+90" + "~" + containeroutsideForm.startRecipient.phoneNumber,
    //     province: containeroutsideForm.startingAddress.province,
    //     /* street: containeroutsideForm.startingAddress.street, */
    //     zipCode: containeroutsideForm.startingAddress.zipCode,
    //     countryStart: containeroutsideForm.startSelectCountryCode,
    //     portStart: containeroutsideForm.startingAddress.port, // Burada hata alacağız
    //     portDue: containeroutsideForm.dueAddress.port, // Burada hata alacağız
    //     countryDue: containeroutsideForm.dueSelectCountryCode,
    //     Dates: containeroutsideForm.startDate,
    //     dueCity: containeroutsideForm.dueAddress.province,
    //     DeliveryMethod: containeroutsideForm.startDeliveryMethod,
    //     FullDate: containeroutsideForm.fullDate,

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
    const feacthPortList1 = () =>{
        if(containeroutsideForm.startDeliveryMethod !== "d2d" || containeroutsideForm.startDeliveryMethod !== "d2p"){
            if(containeroutsideForm.vehicleOrContainer === "Deniz Yolu"){
                if(containeroutsideForm?.startSelectCountryCode !== null){
                    setValuePort(containeroutsideForm?.startFullAddress)
                    const portDataFliter = portData.PORTS.filter(PORT => PORT.code === containeroutsideForm.startSelectCountryCode)
                    containeroutsideForm.startSelectCountryName = Countries.find(Countries => Countries.code === containeroutsideForm.startSelectCountryCode)?.name
                    setCountryPortFilter1(portDataFliter)
                    setContaineroutsideForm({ ...containeroutsideForm })
                    setIsDragged(false)
                }
            }
        }
    }
    const feacthPortList2 = () =>{
        if(containeroutsideForm.startDeliveryMethod !== "d2d" || containeroutsideForm.startDeliveryMethod !== "p2d"){
            if(containeroutsideForm.vehicleOrContainer === "Deniz Yolu"){
                if(containeroutsideForm?.dueSelectCountryCode !== null){
                    setValuePortDue(containeroutsideForm?.dueFullAddress)
                    const portDataFliter = portData.PORTS.filter(PORT => PORT.code === containeroutsideForm.dueSelectCountryCode)
                    setCountryPortFilter2(portDataFliter)
                    containeroutsideForm.dueSelectCountryCode = containeroutsideForm.dueSelectCountryCode
                    containeroutsideForm.dueSelectCountryName = Countries.find(Countries => Countries.code === containeroutsideForm.dueSelectCountryCode)?.name
                    setContaineroutsideForm({ ...containeroutsideForm })
                    setIsDragged(false)
                }
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
        containeroutsideForm.startRecipient.commercialTitle = savedAddress.commercialTitle;
        containeroutsideForm.startRecipient.firstName = savedAddress.firstName;
        containeroutsideForm.startRecipient.lastName = savedAddress.lastName;
        containeroutsideForm.startRecipient.phoneNumber = savedAddress.phone.split(/[~]/g)[1];
        containeroutsideForm.startRecipient.selectedCountryCode = Countries.find(v => v.dial_code === savedAddress?.phone?.slice(0, 3))?.dial_code;
        //containeroutsideForm.startingAddress.province = savedAddress.province;
        containeroutsideForm.startSelectCountryCode = savedAddress.country;
        //containeroutsideForm.startingAddress.zipCode = savedAddress.zipCode;
        /* containeroutsideForm.startingAddress.district = savedAddress.district;
        containeroutsideForm.startingAddress.neighborhood = savedAddress.neighborhood; */
        containeroutsideForm.startFullAddress = savedAddress.fullAddress;
        /* containeroutsideForm.startingAddress.lat = savedAddress.lat;
        containeroutsideForm.startingAddress.lng = savedAddress.lng; */
        setContaineroutsideForm({ ...containeroutsideForm });
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
         if (containeroutsideForm.startingAddress.district || containeroutsideForm.startingAddress.neighborhood || containeroutsideForm.startingAddress.province) {
             fetchGeocode()
         }
     }, [containeroutsideForm.startingAddress.district, containeroutsideForm.startingAddress.neighborhood, containeroutsideForm.startingAddress.province, containeroutsideForm.startFullAddress]) */

    //get address by dragged marker
    

    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [containeroutsideForm])

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
                /*  if (containeroutsideForm.startingAddress.province) {
                     setIsAddressLoading(true)
                     let findProvince = provinceData.data.find(v => v.provinceName === containeroutsideForm.startingAddress.province);
                     try {
                         let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                         if (districtData) {
                             setDistricts(districtData.data)
                             //fetch neighborhood
                             if (containeroutsideForm.startingAddress.district) {
                                 setIsAddressLoading(true)
                                 let findDistrict = districtData.data.find(v => v.districtName === containeroutsideForm.startingAddress.district);
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

    /*  useEffect(() => {
         setAddress()
     }, [containeroutsideForm.startingAddress.province, containeroutsideForm.startingAddress.district]) */
    
    
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
                                        
                                        containeroutsideForm.fullDate = values
                                        containeroutsideForm.startDate = values.map(item=>{
                                            return moment(item).format('DD-MM-YYYY  ')
                                        })
                                        containeroutsideForm.dueDate   = containeroutsideForm.startDate[1]
                                        containeroutsideForm.startDate = containeroutsideForm.startDate[0]
                                        console.log(containeroutsideForm.startDate);
                                        console.log(containeroutsideForm.dueDate);
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
                                containeroutsideForm.startSelectCountryCode = countryName
                                const portDataFliter = portData.PORTS.filter(PORT => PORT.code === countryName)
                                containeroutsideForm.startSelectCountryName = Countries.find(Countries => Countries.code === countryName)?.name
                                setCountryPortFilter1(portDataFliter)
                                setContaineroutsideForm({ ...containeroutsideForm })
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
                                containeroutsideForm.dueSelectCountryCode = countryName
                                containeroutsideForm.dueSelectCountryName = Countries.find(Countries => Countries.code === countryName)?.name
                                setContaineroutsideForm({ ...containeroutsideForm })
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
                    {(containeroutsideForm.startDeliveryMethod === "d2d"  && containeroutsideForm.dueSelectCountryCode !== null && containeroutsideForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yükleme Noktası")}
                                name="city1"
                                rules={[{  required: (containeroutsideForm.startDeliveryMethod === "d2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                    
                                    country: containeroutsideForm.startSelectCountryCode,
                                    }
                                }}
                             />
                        
                         </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="city2"
                                rules={[{  required: (containeroutsideForm.startDeliveryMethod === "d2d")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                           
                           <GooglePlacesAutocomplete
                           
                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}
                                selectProps={{
                                    placeholder: 'Şehir veya Posta Kodu Giriniz.',
                                    onChange: setValuePortDue,
                                }}
                                autocompletionRequest={{
                                    componentRestrictions: {
                                    country: containeroutsideForm.dueSelectCountryCode,
                                    }
                                }}
                             />

                         </Form.Item>
                        </Col>
                    </Row>
                    }
                    
                    {(containeroutsideForm.startDeliveryMethod === "p2p" && containeroutsideForm.dueSelectCountryCode !== null && containeroutsideForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                        {containeroutsideForm.vehicleOrContainer === "Hava Yolu" &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{  required: (containeroutsideForm.vehicleOrContainer === "Hava Yolu" && containeroutsideForm.startDeliveryMethod === "p2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                                                            
                                                    country: containeroutsideForm.startSelectCountryCode,
                                                    }
                                                }}
                                            />
                                    </Col>
                                </Input.Group>
                                
                            
                            </Form.Item>
                        }
                        {(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" || containeroutsideForm.vehicleOrContainer === "") &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{  required: (containeroutsideForm.vehicleOrContainer === "" && containeroutsideForm.startDeliveryMethod === "p2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true :(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" && containeroutsideForm.startDeliveryMethod === "p2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                            onChange={onChange}
                                            defaultValue={containeroutsideForm?.startFullAddress !== null ? containeroutsideForm.startFullAddress : null}
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
                        {containeroutsideForm.vehicleOrContainer === "Hava Yolu" &&
                            <Form.Item
                                    label={t("Teslimat Noktası")}
                                    name="DueCity"
                                    rules={[{  required: (containeroutsideForm.vehicleOrContainer === "Hava Yolu" && containeroutsideForm.startDeliveryMethod === "p2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                                                                
                                                        country: containeroutsideForm.dueSelectCountryCode,
                                                        }
                                                    }}
                                                />
                                        </Col>
                                    </Input.Group>
                            
                            </Form.Item>
                        }
                        {(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" || containeroutsideForm.vehicleOrContainer === "") &&
                            <Form.Item
                                    label={t("Teslimat Noktası")}
                                    name="DueCity"
                                    rules={[{  required: (containeroutsideForm.vehicleOrContainer === "" && containeroutsideForm.startDeliveryMethod === "p2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true :(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" && containeroutsideForm.startDeliveryMethod === "p2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                                optionFilterProp="children"
                                                defaultValue={containeroutsideForm?.dueFullAddress !== null ? containeroutsideForm.dueFullAddress : null}
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
                    {(containeroutsideForm.startDeliveryMethod === "d2p" && containeroutsideForm.dueSelectCountryCode !== null && containeroutsideForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yükleme Noktası")}
                                name="City"
                                rules={[{  required: (containeroutsideForm.startDeliveryMethod === "d2p")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                    country: containeroutsideForm.startSelectCountryCode,
                                    }
                                }}
                             />
                        
                         </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        {containeroutsideForm.vehicleOrContainer === "Hava Yolu" &&
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="DueCity"
                                rules={[{  required: (containeroutsideForm.vehicleOrContainer === "Hava Yolu" && containeroutsideForm.startDeliveryMethod === "d2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                                                        
                                                country: containeroutsideForm.dueSelectCountryCode,
                                                }
                                            }}
                                        />
                                </Col>
                            </Input.Group>
                        
                         </Form.Item>
                        }
                        {(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" || containeroutsideForm.vehicleOrContainer === "") &&
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="DueCity"
                                
                                rules={[{ required: (containeroutsideForm.vehicleOrContainer === "" && containeroutsideForm.startDeliveryMethod === "d2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true :(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" && containeroutsideForm.startDeliveryMethod === "d2p")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                        onSearch={onSearch}
                                        defaultValue={containeroutsideForm?.dueFullAddress !== null ? containeroutsideForm.dueFullAddress : null}
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
                    {(containeroutsideForm.startDeliveryMethod === "p2d"  && containeroutsideForm.dueSelectCountryCode !== null && containeroutsideForm.startSelectCountryCode !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            {containeroutsideForm.vehicleOrContainer === "Hava Yolu" &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{  required: (containeroutsideForm.vehicleOrContainer === "Hava Yolu" && containeroutsideForm.startDeliveryMethod === "p2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                                                            
                                                    country: containeroutsideForm.startSelectCountryCode,
                                                    }
                                                }}
                                            />
                                    </Col>
                                </Input.Group>
                            
                            </Form.Item>
                        }
                        {(containeroutsideForm.vehicleOrContainer === "Deniz Yolu" || containeroutsideForm.vehicleOrContainer === "") &&
                                <Form.Item
                                    label={t("Yükleme Noktası")}
                                    name="City"
                                    rules={[{ required: (containeroutsideForm.vehicleOrContainer === "" && containeroutsideForm.startDeliveryMethod === "p2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true :(rota === "p2d" && containeroutsideForm.startDeliveryMethod === "P2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                            onChange={onChange}
                                            defaultValue={containeroutsideForm?.startFullAddress !== null ? containeroutsideForm.startFullAddress : null}
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
                                rules={[{  required: (containeroutsideForm.startDeliveryMethod === "p2d")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                    country: containeroutsideForm.dueSelectCountryCode,
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