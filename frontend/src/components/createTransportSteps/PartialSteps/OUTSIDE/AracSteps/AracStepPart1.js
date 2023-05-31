import { Button, Col, Row, Form, DatePicker, Select} from 'antd';
import React, {useState} from 'react'
import { useSelector } from 'react-redux';
// import { createGeocodeFromAddressRequest, getProvinceRequest, reverseGeocodeRequest } from '../../../../../../../../api/controllers/addresses-controller';
import { CustomButton } from '../../../../CustomButton';
import moment from 'moment';
// import {getMyAddressesRequest } from '../../../../../../../../api/controllers/saved-address-controller';
// import MapBoxForCreateTransport from '../../../../../../MapBoxForCreateTransport';
// import AntNotification from '../../../../../../AntNotification';
// import { useSelector } from 'react-redux';
import locale from "antd/es/date-picker/locale/tr_TR";
// import { getAccountSettingsRequest, getMyDefaultAddressRequest } from '../../../../../../../../api/controllers/client-controller';
// import RequestError from '../../../../../../RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from '../../../../Countries';
import GooglePlacesAutocomplete from 'react-google-places-autocomplete';
// import FormItem from 'antd/es/form/FormItem';
// import { notInitialized } from 'react-redux/es/utils/useSyncExternalStore';
// import portData from "../../../../../../../../components/ports.json"



//Araç STEP 1
const AracStep1 = ({ handleClickNextStep, handleClickPrevStep, aracpartForm, setAracpartForm}) => {
    aracpartForm.vehicleOrContainer = "Kara Yolu";
    const currentUser = useSelector((state) => state.user);

    if(currentUser !== null){
        aracpartForm.client_id = currentUser.id;
    }

    const { t } = useTranslation("common");
    const { Option } = Select;
    // const [deger, setDeger] = useState(null);
    // Kutular dolu mu yoksa değilmi kontrol ediyor
    const [trueOrFalse, setTrueOrFalse] = useState(0)
    const [trueOrFalseDue, setTrueOrFalseDue] = useState(0)
    const [valuePort, setValuePort] = useState("")
    const [valuePortDue, setValuePortDue] = useState("")


    

    // değişkenlerin durumuna göre veri girilmesini zorunlu hale getirmek için gereken fonksiyon
    const requiredControl = () =>{

        if(valuePort["label"] !== undefined){
            console.log(valuePort["label"])
            aracpartForm.startFullAddress = valuePort["label"];
        }else{
            console.log(valuePort)
            aracpartForm.startFullAddress = "Port Of " + valuePort
        }

        if(valuePortDue["label"] !== undefined){
            console.log(valuePortDue["label"])
            aracpartForm.dueFullAddress = valuePortDue["label"];
        }else{
            console.log(valuePortDue)
            aracpartForm.dueFullAddress = "Port Of " + valuePortDue
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
    

// const [countryPortFilter1, setCountryPortFilter1] = useState([])
// const [countryPortFilter2, setCountryPortFilter2] = useState([])

// nakliye yolu seçildikten sonra veriyikaydetmek ve aksiyon almak için
// const [rota, setRota] = useState("");
// const shippingWayChoose = (e) =>{
//     setRota(e)
    
//     setTrueOrFalse(0)
//     setTrueOrFalseDue(0)
//     setValuePort("")
//     setValuePortDue("")
// }



// Switch-Case Taşıma Tipi İçin   
    const handleChange = (value) => {
        aracpartForm.startDeliveryMethod = value;
        console.log(`selected ${value}`);

        setAracpartForm({ ...aracpartForm })
        
        setTrueOrFalse(0)
        setTrueOrFalseDue(0)
        setValuePort("")
        setValuePortDue("")

    };

    //const user = useSelector((state) => state.user);

    const formValues = {
        // adStartingDate: adStartingDate,
        // commercialTitle: aracpartForm.startRecipient.commercialTitle,
        // firstName: aracpartForm.startRecipient.firstName,
        // lastName: aracpartForm.startRecipient.lastName,
        // phoneNumber: aracpartForm.startRecipient.phoneNumber,
        /*  district: aracpartForm.startingAddress.district,
         neighborhood: aracpartForm.startingAddress.neighborhood, */
        // fullAddress: aracpartForm.startFullAddress,
        /*  address: aracpartForm.startingAddress.province && aracpartForm.startingAddress.district && aracpartForm.startingAddress.neighborhood && aracpartForm.startFullAddress ? aracpartForm.startFullAddress + " " + aracpartForm.startingAddress.neighborhood + " - " + aracpartForm.startingAddress.district + "/" + aracpartForm.startingAddress.province : "",*/
        // zipCode: aracpartForm.startingAddress.zipCode,
        // portStart: aracpartForm.startingAddress.port,
        // portDue: aracpartForm.dueAddress.port,
        // selectedCountryCode: aracpartForm.startRecipient.selectedCountryCode ? aracpartForm.startRecipient.selectedCountryCode : "+90";
        // dueCity: aracpartForm.dueAddress.province,

        countryDue: aracpartForm.dueSelectCountryName,
        Dates: aracpartForm.startDate,
        DeliveryMethod: aracpartForm.startDeliveryMethod,
        FullDate: aracpartForm.fullDate,
        countryStart: aracpartForm.startSelectCountryName,
        fullAddress: aracpartForm.startFullAddress,
        fullAddressDue: aracpartForm.dueFullAddress,

     
    };

    // const saveAddressForm = {
    //     countryDue: aracpartForm.dueSelectCountryName,
    //     Dates: aracpartForm.startDate,
    //     DeliveryMethod: aracpartForm.startDeliveryMethod,
    //     FullDate: aracpartForm.fullDate,
    //     countryStart: aracpartForm.startSelectCountryName,
    //     regionType: 'INTERNATIONAL'
    // }

    // const [isDragged, setIsDragged] = useState(false)

    //data states
    //const [provinces, setProvinces] = useState([])
    // const [districts, setDistricts] = useState([])
    // const [neighborhoods, setNeighborhoods] = useState([])
    //const [myAddressess, setMyAddressess] = useState([])

    //modal states
    // const [savedAdressModal, setSavedAdressModal] = useState(false)
    // const [addressModal, setAddressModal] = useState(false)
    // const [saveAddressModal, setSaveAddressModal] = useState(false)

    // loading states
    // const [isAddressLoading, setIsAddressLoading] = useState(false)

    //get saved address
    // const fetchSavedAddresses = async () => {
    //     try {
    //         let res = await getMyAddressesRequest();
    //         if (res) {
    //             setMyAddressess(res.data.filter(v => v.regionType === 'INTERNATIONAL'))
    //         }
    //     } catch (error) {
    //         RequestError(error);
    //     }
    // }

    // const fetchAccountSettings = async () => {
    //     try {
    //         let res = await getAccountSettingsRequest();
    //         if (res) {
    //             if (res.data.createAdvertisementStartingAddressAutoFill) {
    //                 fetchMyDefaultAddress()
    //             }
    //         }
    //     } catch (error) {
    //         RequestError(error);
    //     }
    // }

    // useEffect(() => { fetchAccountSettings()}, [])

    // const [savedAddress, setSavedAddress] = useState()
    //get my default saved address
    // const fetchMyDefaultAddress = async () => {
    //     try {
    //         let res = await getMyDefaultAddressRequest();
    //         if (res) {
    //             setSavedAddress(res.data)
    //             if (res.data) {
    //                 onSelectSavedAddress(res.data)
    //             }
    //         }
    //     } catch (error) {
    //     }
    // }

    //open saved address modal
    // const clickOpenSavedAddressesModal = async () => {
    //     await fetchSavedAddresses();
    //     setSavedAdressModal(true)
    // }

    // const onSelectSavedAddress = (savedAddress) => {
    //     aracpartForm.startRecipient.commercialTitle = savedAddress.commercialTitle;
    //     aracpartForm.startRecipient.firstName = savedAddress.firstName;
    //     aracpartForm.startRecipient.lastName = savedAddress.lastName;
    //     aracpartForm.startRecipient.phoneNumber = savedAddress.phone.split(/[~]/g)[1];
    //     aracpartForm.startRecipient.selectedCountryCode = Countries.find(v => v.dial_code === savedAddress?.phone?.slice(0, 3))?.dial_code;
    //     aracpartForm.startingAddress.province = savedAddress.province;
    //     aracpartForm.startSelectCountryName = savedAddress.country;
    //     // aracpartForm.startingAddress.zipCode = savedAddress.zipCode;
    //     /* aracpartForm.startingAddress.district = savedAddress.district;
    //     aracpartForm.startingAddress.neighborhood = savedAddress.neighborhood; */
    //     aracpartForm.startFullAddress = savedAddress.fullAddress;
    //     /* aracpartForm.startingAddress.lat = savedAddress.lat;
    //     aracpartForm.startingAddress.lng = savedAddress.lng; */
    //     setAracpartForm({ ...aracpartForm });
    //     // setSavedAdressModal(false);
    // }
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
    // const fetchGeocode = async () => {
    //     try {
    //         let res = await createGeocodeFromAddressRequest({
    //             district: aracpartForm.startingAddress.district,
    //             neighborhood: aracpartForm.startingAddress.neighborhood,
    //             province: aracpartForm.startingAddress.province,
    //             fullAddress: aracpartForm.startFullAddress
    //         })
    //         if (res && !isDragged) {
    //             aracpartForm.startingAddress.lat = res.data.latitude;
    //             aracpartForm.startingAddress.lng = res.data.longitude;
    //             setAracpartForm({ ...aracpartForm })
    //         }
    //     } catch (error) {
    //         AntNotification({ type: "error", message: t("Bir sorun oluştu!"), description: t("Lütfen yeniden deneyin.") })
    //     }
    // }

    /*  useEffect(() => {
         if (aracpartForm.startingAddress.district || aracpartForm.startingAddress.neighborhood || aracpartForm.startingAddress.province) {
             fetchGeocode()
         }
     }, [aracpartForm.startingAddress.district, aracpartForm.startingAddress.neighborhood, aracpartForm.startingAddress.province, aracpartForm.startFullAddress]) */

    //get address by dragged marker
    // const onDragMarker = async ({ latitude, longitude }) => {
    //     setIsDragged(true)
    //     aracpartForm.startingAddress.lat = latitude;
    //     aracpartForm.startingAddress.lng = longitude;
    //     setAracpartForm({ ...aracpartForm })
    //     try {
    //         let res = await reverseGeocodeRequest({
    //             latitude: latitude,
    //             longitude: longitude,
    //         })
    //         if (res) {
    //             aracpartForm.startingAddress.province = res.data.province;
    //             aracpartForm.startingAddress.district = res.data.district;
    //             aracpartForm.startingAddress.neighborhood = res.data.neighborhood;
    //             aracpartForm.startFullAddress = res.data.fullAddress;
    //             setAracpartForm({ ...aracpartForm })
    //         }
    //     } catch (error) {
    //         AntNotification({ type: "error", message: t("Adres tanımlanamadı!"), description: t("Lütfen yeniden deneyin veya adres alanından seçim yaparak devam edin.") })
    //     }
    // }

    //if changed form set new value
    const [form] = Form.useForm();
    //useEffect(() => {form.setFieldsValue(formValues)}, [aracpartForm])

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
    // const setAddress = async () => {
    //     setIsAddressLoading(true)
    //     //fetch province
    //     try {
    //         let provinceData = await getProvinceRequest();
    //         if (provinceData) {
    //             setProvinces(provinceData.data)
    //             //fetch district
    //             /*  if (aracpartForm.startingAddress.province) {
    //                  setIsAddressLoading(true)
    //                  let findProvince = provinceData.data.find(v => v.provinceName === aracpartForm.startingAddress.province);
    //                  try {
    //                      let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
    //                      if (districtData) {
    //                          setDistricts(districtData.data)
    //                          //fetch neighborhood
    //                          if (aracpartForm.startingAddress.district) {
    //                              setIsAddressLoading(true)
    //                              let findDistrict = districtData.data.find(v => v.districtName === aracpartForm.startingAddress.district);
    //                              try {
    //                                  let res = await getNeighborhoodRequest(findDistrict.id);
    //                                  if (res) {
    //                                      setNeighborhoods(res.data)
    //                                  }
    //                                  setIsAddressLoading(false)
    //                              } catch (error) {
    //                                  RequestError(error);
    //                              }
    //                          }
    //                      }
    //                      setIsAddressLoading(false)
    //                  } catch (error) {
    //                      RequestError(error);
    //                  }
    //              } */
    //         }
    //         setIsAddressLoading(false)
    //     } catch (error) {
    //         RequestError(error);
    //     }
    // }

    /*  useEffect(() => {
         setAddress()
     }, [aracpartForm.startingAddress.province, aracpartForm.startingAddress.district]) */
    
    
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
                                Object.keys(saveAddressForm).some((k) => {
                                    return saveAddressForm[k] === null;
                                }) || Object.keys(saveAddressForm).some((k) => {
                                    return saveAddressForm[k] === '';
                                }) ||
                                <CustomButton
                                    onClick={() => user ? setSaveAddressModal(true) : AntNotification({ type: "warning", message: t("Bu özelliği kullanabilmek için giriş yapmalısınız!") })}
                                    color="light"
                                >
                                    {t("Bilgileri Kaydet")}
                                </CustomButton> */}
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
                                        
                                        aracpartForm.fullDate = values
                                        aracpartForm.startDate = values.map(item=>{
                                            return moment(item).format('DD-MM-YYYY  ')
                                        })
                                        aracpartForm.dueDate = aracpartForm.startDate[1]
                                        aracpartForm.startDate = aracpartForm.startDate[0]
                                        console.log(aracpartForm.startDate);
                                        console.log(aracpartForm.dueDate);
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
                                        disabled
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
                                            disabled:true,
                                            label: 'Limandan Limana',
                                            },
                                            {
                                            value: 'd2p',
                                            disabled: true,
                                            label: 'Kapıdan Limana ',
                                            },
                                            {
                                            value: 'p2d',
                                            disabled: true,
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
                                name="countryStart"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                            <Select
                               size="large"
                               placeholder={t("Yükleme Yapılacak Ülke")}
                               onChange={countryName => {
                                aracpartForm.startSelectCountryCode = countryName
                                // const portDataFliter = portData.PORTS.filter(PORT => PORT.code === countryName)
                                aracpartForm.startSelectCountryName = Countries.find(Countries => Countries.code === countryName)?.name
                                //setCountryPortFilter1(portDataFliter)
                                setAracpartForm({ ...aracpartForm })
                                //setIsDragged(false)
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
                                name="countryDue"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}                               
                            >   
                            <Select
                               size="large"
                               placeholder={t("Teslimat Yapılacak Ülke")}
                               onChange={countryName => {
                                // const portDataFliter = portData.PORTS.filter(PORT => PORT.code === countryName)
                                // setCountryPortFilter2(portDataFliter)
                                aracpartForm.dueSelectCountryCode = countryName
                                aracpartForm.dueSelectCountryName = Countries.find(Countries => Countries.code === countryName)?.name
                                setAracpartForm({ ...aracpartForm })
                                // setIsDragged(false)
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
                    {(aracpartForm.startDeliveryMethod === "d2d"  && aracpartForm.dueSelectCountryName !== null && aracpartForm.startSelectCountryName !== null) &&
                    <Row gutter={[24, 24]}>
                        <Col xs={24} xl={12} xxl={8}>
                            <Form.Item
                                label={t("Yükleme Noktası")}
                                name="city1"
                                rules={[{  required: (aracpartForm.startDeliveryMethod === "d2d")? (trueOrFalse === "" || trueOrFalse === 0)? true: (trueOrFalse === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
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
                                    
                                    country: aracpartForm.startSelectCountryCode,
                                    }
                                }}
                             />
                        
                         </Form.Item>
                        </Col>
                        <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                                label={t("Teslimat Noktası")}
                                name="city2"
                                rules={[{  required: (aracpartForm.startDeliveryMethod === "d2d")? (trueOrFalseDue === "")? true: (trueOrFalseDue === 1)? false: true : false, message: t('Lütfen bu alanı doldurun!') }]}
                            >   
                           
                           <GooglePlacesAutocomplete
                           
                                apiKey={process.env.REACT_APP_GOOGLE_API_KEY_MAP}
                                selectProps={{
                                    placeholder: 'Şehir veya Posta Kodu Giriniz.',
                                    onChange: setValuePortDue,
                                }}
                                autocompletionRequest={{
                                    componentRestrictions: {
                                    country: aracpartForm.dueSelectCountryCode,
                                    }
                                }}
                             />

                         </Form.Item>
                        </Col>
                    </Row>
                    }
                </div >
            </Form>

            {/* <Modal
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
            </Modal > */}

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

export default AracStep1;