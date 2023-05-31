import { Alert, Button, Checkbox, Col, Divider, Dropdown, Input, Form, Popconfirm, Radio, Rate, Row, Select, Space, Tooltip, Menu, Modal, Upload, message,Typography  } from 'antd';
import React, { useEffect, useState } from 'react'
import { Steps } from 'antd';
import { CustomButton } from './CustomButton';
import { CustomModal } from './CustomModal';
import UploadItem from './UploadItem';
import TextArea from 'antd/lib/input/TextArea';
import { useSelector } from 'react-redux';
import moment from 'moment';
import { ParsedDriverAbroud, ParsedClientAbroud } from "./ParsedValuesAbroad"
import AntNotification from './AntNotification';
import { getMyDriversAll } from '../api/controllers/driver-controller';
import { changeDriverRequest, getMyVehiclesAll } from '../api/controllers/vehicle-controller';
import { assignVehicleRequest, endConfirmationTransporNoTokentRequest, endingTransportRequest, startConfirmationTransporNoTokentRequest, startingTransportRequest, updateStatusForCargoDeliveredRequest, submitShipsmentInfoDataSend, currentPaymentapi, afterPayApi, getAbroudInsuranceApi } from '../api/controllers/client-transport-process-controller';
import { BASE_URL } from '../api/ApiProvider';
import { halkBankPaymentSuccessRequest } from '../api/controllers/halkbank-payment-controller';
import { createWaybillUploadRequest } from '../api/controllers/way-bill-controller';
import { createRatingRequestAbroud, advertStatusStepController} from '../api/controllers/rating-client-advertisement-controller';
import Sozlesmeler from './Sozlesmeler';
import RequestError from './RequestError';
import CreateBid from './CreateBid';
import { useTranslation } from 'react-i18next';
import { InboxOutlined } from '@ant-design/icons';
import {PaymentControllReceipt} from '../api/controllers/payment-controller-receipt';
import {getTodayForeignCurrencyEuro, getTodayForeignCurrencyUsd} from '../api/controllers/foreign-currency-api';
import {Insurance} from "./Insurance";
import CreditCard from "./CreditCard";
import axios from 'axios';

const DetailAlertAbroud = ({ data, approvedBid, transport, transportProcess, transportDocuments, updatePage, status }) => {
    const insuranceData = Insurance(transport?.goodsPrice, transport?.startSelectCountryCode, transport?.dueSelectCountryCode, transport?.currencyUnit?.currencySymbol);
    const [insuranceType, setInsuranceType] = useState("UNINSURED");
    const getAbroudInsurance = async () => {
        if(transport.advertisementStatus === "TEMPORARY_METHOD"){
            try {
                let res = await getAbroudInsuranceApi(transport.id);
                setInsuranceType(res?.data);
            }catch (error) {
                RequestError(error);
            }
        }
    } 
    useEffect(() => {
        getAbroudInsurance();
    }, []);
    const [creditCardFormValue] = useState({
        cardHolderFullName: null,
        cardNumber: null,
        expDates : {
            expMonth: null,
            expYear: null,
        },
        cardName: null,
        registery: false,
        cvcNumber: null,
        insuranceType: null,
        abroadOrDomestic: "Abroud",
        transportID: transport.id
      });
      
    const { t, i18n } = useTranslation("common");
    const defaultShipsmentInfoData = {
        adAbroud: {
            id: transport.id
        },
        plate: null,
        trailerPlate: null,
        name: null,
        tc: null,
        tel: null,
        awb: null,
        airlane: null,
        orginAirport: null,
        destination: null,
        ambar: null,
        bayrak: null,
        opsiyon: null,
        iskele: null,
        booking: null,
        seal: null,
        cotainer: null,
    }

    const token = localStorage.getItem('token')
    const currentUser = useSelector((state) => state.user);


    const { role } = currentUser;
    const getClientParsedValue = ParsedClientAbroud({ transport, transportDocuments, status });
    const getDriverParsedValue = ParsedDriverAbroud({ transport, transportDocuments, status });
    const [onDeliverModal, setOnDeliverModal] = useState(false);
    const [filesForSend, setFilesForSend] = useState([])
    const [temporary_method, TEMPORARY_METHOD] = useState(false);
    const [usd, setUsd] = useState(); 
    const [euro, setEuro] = useState();
    const [insurance, setInsurance] = useState(0);
    const [paymentIsLoading, setPaymentIsLoading] = useState(false);

    
    const [paymentOptions, setPaymentOptions] = useState(!currentUser?.current&& "pos");
    const [insufficientBalance, setInsufficientBalance] = useState(false);

    const [modalPayment1, setModalPayment1] = useState(data?.advertisementStatus === "APPROVED" ? true : false)
    const [modalPayment, setModalPayment] = useState(data?.advertisementStatus === "TEMPORARY_METHOD" ? true : false)
    const [shipsmentInfo, setShipsmentInfo] = useState(false);
    const [bidModalVisible, setBidModalVisible] = useState(false)
    const [shipsmentInfoData, setShipsmentInfoData] = useState(defaultShipsmentInfoData);
    
    /* EVALUATE STATES */
    const [modalEvaluate, setModalEvaluate] = useState(false)

    const formValues = {
        plate:          shipsmentInfoData.plate,
        trailerPlate:   shipsmentInfoData.trailerPlate,
        name:           shipsmentInfoData.name,
        tc:             shipsmentInfoData.tc,
        tel:            shipsmentInfoData.tel,
        awb:            shipsmentInfoData.awb,
        airlane:        shipsmentInfoData.airlane,
        orginAirport:   shipsmentInfoData.orginAirport,
        destination:    shipsmentInfoData.destination,
        ambar:          shipsmentInfoData.ambar,
        bayrak:         shipsmentInfoData.bayrak,
        opsiyon:        shipsmentInfoData.opsiyon,
        iskele:         shipsmentInfoData.iskele,
        booking:        shipsmentInfoData.booking,
        seal:           shipsmentInfoData.seal,
        cotainer:       shipsmentInfoData.cotainer,
    }

    const [evaluate, setEvaluate] = useState({
        advertAbroud: {
            id: transport.id
        },
        advertDomastic: null,
        comment: "",
        rating: null,
        abroudOrDomastic: "ABROUD"
    })

    const { Step } = Steps;

       
      const currentPayment = async () =>{
        setPaymentIsLoading(true);

        try {
            data = {
                abroudOrDomastic: "Abroud",
                transportId: transport.id,
                insuranceType: insuranceType,
                insuranceCurency: "$",
            }
            let res = await currentPaymentapi(data);
            if (res) {
                setPaymentIsLoading(false);
                window.location.reload();
            }
        } catch (error) {
            console.log(error);
            setInsufficientBalance(true);
            setPaymentIsLoading(false);
        }
    }
    const onDeliver = async () => {
        let file = filesForSend[transportProcess.id];
        const formData = new FormData();
        formData.append('result', file);
        try {
            let res = await createWaybillUploadRequest(transportProcess.id, formData);
            if (res) {
                setOnDeliverModal(false)
                AntNotification({ type: "success", message: t("İrsaliye başarıyla gönderildi.") })
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const openMessage = (type) => {
        const key = 'updatable';

        if (type === "uploading") {
            message.loading({ content: t("Belge yükleniyor..."), key });
        } else if (type === "done") {
            message.success({ content: t("Belge yüklendi!"), key, duration: 2 });
            updatePage();
        }
    };


    const evaluateOk = async () => {
        setIsLoading(true)
        try {
            let res = await createRatingRequestAbroud(evaluate);

            console.log(res);
            if (res) {
                AntNotification({ type: "success", message: t("Değerlendirmeniz başarıyla kaydedildi.") })
                evaluateCancel()
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false)
    }

    const evaluateCancel = () => {
        setModalEvaluate(false)
        setEvaluate({
            advertAbroud: {
                id: transport.id
            },
            advertDomastic: null,
            comment: "",
            rating: null,
            abroudOrDomastic: "ABROUD"
        })
    }
    const afterPay = async () =>{
        setPaymentIsLoading(true);
        try {
            let res = await afterPayApi(transport.id, insuranceType);
            console.log(res);
            if (res) {
                updatePage()
            }
        } catch (error) {
            setPaymentIsLoading(false)
            RequestError(error);
        }
    }
    const paymentSelect = () =>{
        if(currentUser?.current){
            setWitchStep(2);
        }else{
            afterPay();
        }
    }

/*Uplaod ve copyable */
const { Dragger } = Upload;
const props = {
    name: 'file',
    multiple: false,
    beforeUpload: async (file) => {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('advertID', transport.id)
  
      try {
        const response = await PaymentControllReceipt(formData) ;
  
        if (response.status === 200) {
          message.success(`${file.name} dosyası başarıyla yüklendi.`);
          updatePage();
        } else {
          message.error(`Dosya yükleme işlemi başarısız oldu.`);
        }
      } catch (error) {
        message.error(`Dosya yükleme işlemi sırasında bir hata oluştu: ${error.message}`);
      }
  
      return false;
    },
  };
  const { Paragraph } = Typography;

    /* PAYMENT STATES */
    const [selectedBank, setSelectedBank] = useState("")
    const [contract, setContract] = useState(false)

    const onChangePayment = (e) => {
        setInsuranceType(e.target.value);
    };


    const paymentCancel = () => {
        if(witchStep === 3){
            updatePage();
        }
        setModalPayment(false)
        setSelectedBank("")
        setContract(false)
        setWitchStep(1)
        setInsuranceType("UNINSURED")
    }
    const paymentCancel1 = () => {
        setModalPayment1(false)
        setSelectedBank("")
        setContract(false)
        setWitchStep(1)
        setInsuranceType("UNINSURED")
    }

    const [witchStep, setWitchStep] = useState(1)
    
    const FirstStep = () => {
        return (
            <Row className='bottom' gutter={24}>
                <Col xs={24} sm={16}>
                    <div className='title'>
                        {t("Taşımanızı daha güvenli hale getirin!")}
                    </div>
                    <div>
                        {t("Aşağıda size uygun olan sigorta tekliflerinden birini seçerek yükünüzü güvence altına alın.")}
                    </div>
                    <Divider />
                    <Radio.Group onChange={onChangePayment} value={insuranceType}>
                        <Space direction="vertical">
                            <Radio value={"UNINSURED"}>{t("Sigorta istemiyorum.")} </Radio>
                            
                                <Radio value={"NARROW"} disabled={transport?.goodsPrice === null || insuranceData?.insurancePrice === 0}>
                                <div className='narrow'>
                                    <div>{t("Dar teminat kapsamında istiyorum")}</div>
                                    <div className='price'>{insuranceData?.insurancePrice}$</div>
                                    <Tooltip placement="right" title={<ul>
                                        <b>{t("ÜRÜN KAMYON KLOZU - DAR TEMİNAT")}</b>
                                        <li>{t("Grev-Lokavt- Savaş Dahil")}</li>
                                        <li>{t("Tüm yükleme boşaltmalar Hariç")}</li>
                                        <li>{t("Tüm Türkiye tek fiyat")}</li>
                                        <li>{t("Onaylı emtea Grupları")}</li>
                                        <li>{t("Muafiyet uygulanmayacaktır")}</li>
                                        <li>{t("Sigorta ettiren Lojistik firmasına rücu edilmeyecektir")}</li>
                                        <li>{t("Fiili taşıyıcı rücu maks. 5.000₺")}</li>
                                        <li>{t("Araç Başı maks. Limit 1.000.000₺")}</li>
                                    </ul>}>
                                        <span>
                                            <i className="bi bi-info-circle-fill"></i>
                                        </span>
                                    </Tooltip>
                                </div>
                                </Radio>
                                <Radio value={"COMPREHENSIVE"} disabled={transport?.goodsPrice === null || insuranceData?.insurancePrice === 0}>
                                    <div className='extensive'>
                                        <div>{t("Geniş teminat kapsamında istiyorum")}</div>
                                        <div className='price'>{insuranceData?.comprehensiveprice}$</div>
                                        <Tooltip placement="right" title={t("Çok yakında!")}>
                                            <span>
                                                <i className="bi bi-info-circle-fill"></i>
                                            </span>
                                        </Tooltip>
                                    </div>
                                </Radio>
                               
                        </Space>
                    </Radio.Group>
                </Col>
                <Col xs={24} sm={8}>
                    <div className='image'>
                        <img src='/assets/img/undraw-security.svg' alt="" />
                    </div>
                </Col>
            </Row>
        )
    }

    const SecondStep = () => {

        return (
            <div className='bottom-second'>
                {
                    currentUser?.current&&
                        <>
                            <div className='top'>
                                <img src='/assets/img/chevron-double-right.svg' alt="" />
                                <div>{t("Ödeme yöntemini seçin")}</div>
                            </div>
                        </>

                }
                {
                    currentUser?.current&&
                        <>
                            <div className='banks'>
                                <Row gutter={[24, 24]}>
                                    <Col xs={24} sm={12}>
                                        <div className={paymentOptions === "current" ? 'halk-bank-selected' : 'halk-bank'}>
                                            <img style={{width: "80px"}} onClick={() => {
                                                setPaymentOptions("current")
                                            }}  src={paymentOptions === "current"? '/assets/img/currentPaymentActive.svg': '/assets/img/currentPayment.svg'} alt="" />
                                            <p>Cari Ödeme</p>
                                        </div>
                                    </Col>
                                    <Col xs={24} sm={12}>
                                    <div className={paymentOptions === "pos" ? 'teb-bank-selected' : 'teb-bank'}>
                                    <img onClick={() => {
                                            setPaymentOptions("pos")
                                        }} style={{width: "80px"}} src={paymentOptions === "pos"? '/assets/img/creditCardActive.svg': '/assets/img/creditCard.svg'} alt="" />
                                            <p>Kredi Kartı</p>
                                        </div>
                                        
                                    </Col>
                                </Row>
                            </div>
                        </>

                }
                {paymentOptions === "current"&&
                    <>
                        <div className='top'>
                            <img src='/assets/img/chevron-double-right.svg' alt="" />
                            <div>{t("Sözleşmeleri onaylayın")}</div>
                        </div>
                        <div className='aggreements'>
                            <Checkbox onChange={(e) => setContract(e.target.checked)} checked={contract} >
                                <b onClick={() => Modal.info({
                                    title: t("Sözleşmeler"),
                                    icon: false,
                                    content: <Sozlesmeler res={transportProcess} />,
                                    okText: t('Okudum'),
                                })}>{t("Ön Bilgilendirme Koşulları")}</b>{localStorage.getItem('currentLanguage') === 'TURKISH' ? "nı" : ","} <b onClick={() => Modal.info({
                                    title: t("Sözleşmeler"),
                                    icon: false,
                                    content: <Sozlesmeler res={transportProcess} />,
                                    okText: t('Okudum'),
                                })}>{t("Mesafeli Satış Sözleşmesi")}</b>{localStorage.getItem('currentLanguage') === 'TURKISH' ? "'ni ve" : " and"} <b onClick={() => Modal.info({
                                    title: t("Sözleşmeler"),
                                    icon: false,
                                    content: <Sozlesmeler res={transportProcess} />,
                                    okText: t('Okudum'),
                                })}>{t("Cayma Hakkı Bildirimi")}</b>{localStorage.getItem('currentLanguage') === 'TURKISH' ? "'ni" : ","} {t("okudum ve onaylıyorum.")}
                            </Checkbox>
                        </div>
                    </>
                }
                {
                    insufficientBalance&&
                        <Alert
                        message="Yetersiz Bakiye"
                        description="Lütfen bizimle iletişime geçip bakiye arttırma talebinde bulunun."
                        type="error"
                        showIcon
                        />
                }
                
            </div >
        )
    }

    const ThirdStep = () => {
        console.log(transport.id)

        if (transport.id && selectedBank === "halk") {
            return (
                <>
                    {/* <button onClick={() => paymentSuccess()}></button> ödemeyi atlama butonu */}
                    <iframe id='#output-frame-id' style={{ minHeight: "400px", height: "100%", width: "100%", border: "none", borderRadius: "1.5rem" }} src={`${BASE_URL}/payment/halkbank/abroud/3d?transportCode=${transport.id}&insuranceType=${insuranceType}`} title="halk"></iframe >
                </>
            )
        } else if (transport.id && selectedBank === "teb") {
            return (
                <iframe id='#output-frame-id' style={{ minHeight: "400px", height: "100%", width: "100%", border: "none", borderRadius: "1.5rem" }} src={`${BASE_URL}/payment/abroud/3d?transportCode=${transport.id}&insuranceType=${insuranceType}`} title="teb"></iframe>
            )
        }
    }

    /* Button loading */

    const [isLoading, setIsLoading] = useState(false)
    const [isLoading2, setIsLoading2] = useState(false)

    const followTransport = () => {
        setIsLoading(true)
        setTimeout(() => {
            setIsLoading(false)
        }, 1500);
        AntNotification({ type: "info", message: t("Çok yakında!"), description: t("Bu özellik şaunda aktif değil. Çok yakında sizlerle beraber olacak") })

    }

    /* ASSIGN VEHICLE */

    const [assingVehicleDriverModal, setAssingVehicleDriverModal] = useState(false)
    const [myDrivers, setMyDrivers] = useState([])
    const [selectedDriver, setSelectedDriver] = useState()
    const [myVehicles, setMyVehicles] = useState([])
    const [selectedVehicle, setSelectedVehicle] = useState()
    const [assingVehicleDriverStep, setAssingVehicleDriverStep] = useState(1)

    const assignVehicleDriver = async () => {
        try {
            let res = await getMyVehiclesAll()
            if (res) {
                if (res.data.length > 0) {
                    setMyVehicles(res.data)
                    setAssingVehicleDriverModal(true)
                } else {
                    AntNotification({ type: "error", message: t("Mevcut aracınız bulunmamaktadır. Öncelikle araç ekleyiniz") })

                }

            }

        } catch (error) {

        }
    }

    const [isButtonLoading, setIsButtonLoading] = useState(false)
    const assignVehicleDriverOk = async () => {
        setIsButtonLoading(true)
        try {
            let ref = await assignVehicleRequest({ clientTransportProcessId: transport.id, vehicleId: selectedVehicle.value });
            if (ref) {
                if (ref.data.summaryDriverData) {
                    AntNotification({ type: "success", message: t("Atama başarıyla gerçekleştirildi.") })
                    setAssingVehicleDriverModal(false)
                    updatePage()
                } else {
                    try {
                        let res = await getMyDriversAll()
                        if (res) {
                            setMyDrivers(res.data)
                            setAssingVehicleDriverStep(2)
                        }
                    } catch (error) {
                        RequestError(error);
                    }
                }
            }
        } catch (error) {
            RequestError(error)
        }
        setIsButtonLoading(false)
    }

    const assignVehicleDriverCancel = () => {
        setSelectedDriver(null)
        setSelectedVehicle(null)
        setAssingVehicleDriverModal(false)
        setAssingVehicleDriverStep(1)
    }

    const assignVehicleDriverOk2 = async () => {
        setIsButtonLoading(true)
        try {
            let res = await changeDriverRequest({ vehicleId: selectedVehicle.value, driverId: selectedDriver.value, transportProcessId: transport.clientTransportProcess.id })
            if (res) {
                setAssingVehicleDriverModal(false)
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
        setIsButtonLoading(false)
    }
    const submitShipsmentInfo = async () =>{
        setIsLoading2(true);
        let res = await submitShipsmentInfoDataSend(shipsmentInfoData)
        if(res.status === 200){
            setShipsmentInfo(false)
            setIsLoading2(false);
            updatePage()
        }else{
            RequestError(res.status)
        }
    }


    const ChooseVehicle = () => {
        return (
            <>
                <div>{t("Taşımaya atamak istediğiniz araç")}</div>
                <Select
                    allowClear
                    placeholder={t("Araç seçin")}
                    showSearch
                    filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                    value={selectedVehicle}
                    onChange={(e, newValue) => {
                        setSelectedVehicle(newValue)
                    }}>
                    {
                        myVehicles.map(v =>
                            <Select.Option key={v.id} >{v.licencePlate}</Select.Option>
                        )
                    }
                </Select>
            </>
        )
    }

    const ChooseDriver = () => {
        return (
            <>
                <div>{t("Taşımaya atamak istediğiniz şoför")}</div>
                <Select
                    allowClear
                    placeholder={t("Şoför seçin")}
                    showSearch
                    filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                    value={selectedDriver}
                    onChange={(e, newValue) => {
                        setSelectedDriver(newValue)
                    }}>
                    {
                        myDrivers.map(v =>
                            <Select.Option key={v.id} >{v.firstName + " " + v.lastName}</Select.Option>
                        )
                    }
                </Select>
            </>
        )
    }

    const paymentSuccess = async () => {
        try {
            let res = await halkBankPaymentSuccessRequest(transport.id)
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const startingTransport = async () => {
        try {
            let res = await startingTransportRequest(transport.id);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const startConfirmationTransport = async () => {
        try {
            let res = await startConfirmationTransporNoTokentRequest(transport.transportCode);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
            updatePage()
        }
    }

    const endingTransport = async () => {
        try {
            let res = await endingTransportRequest(transport.id);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const endConfirmationTransport = async () => {
        try {
            let res = await endConfirmationTransporNoTokentRequest(transport.transportCode);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const advertStatusStep = async () => {
        try{
            let res = await advertStatusStepController(transport.id);
            if (res) {
                updatePage()
            }
        }catch (error) {
            RequestError(error);
        }
    }

    const shipsmentInfoModalFooter = () =>{
        return(
            <>
                <Button
                    onClick={() => setShipsmentInfo(false)}
                    loading={isLoading2}
                >
                    {t("İptal Et")}
                </Button>
                <Button
                    type="primary"
                    onClick={() => document.getElementById("submitFormShipsement").click()}
                    loading={isLoading2}
                >
                    {t("Bilgileri Kaydet")}
                </Button>
            </>
        );
    }
    const DriverActionButtons = ({status}) => {

        if (getDriverParsedValue?.buttonShown && getDriverParsedValue?.buttonText) {
            switch (status) {
                case "PAYMENT_SUCCESSFUL": //Taşımayı Bilgisi gir
                return (
                    <CustomButton
                    disabled={isLoading}
                    style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                    onClick={() => setShipsmentInfo(true)}
                    >
                        {t(getDriverParsedValue?.buttonText)}
                    </CustomButton>
                );
                case "SHIPSMENTINFO": //Taşımayı başlat
                return (
                    <Popconfirm placement="bottomRight" title={t("Yükü teslim aldığınızı onaylıyor musunuz?")} onConfirm={() => advertStatusStep()}
                        okText={t("Evet")}
                        cancelText={t("İptal")}
                    >
                        <CustomButton
                            disabled={isLoading}
                            style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                        >
                            {t(getDriverParsedValue?.buttonText)}
                        </CustomButton>
                    </Popconfirm>
                );
                case "TRANSPORT": //Taşımayı başlat
                return (
                    <Popconfirm placement="bottomRight" title={t("Yükü teslim ettiğinizi onaylıyor musunuz?")} onConfirm={() => advertStatusStep()}
                        okText={t("Evet")}
                        cancelText={t("İptal")}
                    >
                        <CustomButton
                            disabled={isLoading}
                            style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                        >
                            {t(getDriverParsedValue?.buttonText)}
                        </CustomButton>
                    </Popconfirm>
                );
                case "UPLOADED_DOCUMENT": //Belge yüklendi --vgm, föy
                    return (
                        <Popconfirm placement="bottomRight" title={t("Taşıma sürecini tamamlamak istediğinize emin misiniz?")} onConfirm={() => transportDocuments?.some(v => v.documentType === "VGM" ? endingTransport() : startingTransport())}
                            okText={t("Evet")}
                            cancelText={t("İptal")}
                        >
                            <CustomButton
                                disabled={isLoading}
                                style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                            >
                                {t(getDriverParsedValue?.buttonText)}
                            </CustomButton>
                        </Popconfirm>
                    );

                case "CARGO_DELIVERED": //Konteyner iade ediliyor
                    return (
                        <Popconfirm placement="bottomRight" title={t("Taşıma sürecini tamamlamak istediğinize emin misiniz?")} onConfirm={() => endingTransport()}
                            okText={t("Evet")}
                            cancelText={t("İptal")}
                        >
                            <CustomButton
                                disabled={isLoading}
                                style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                            >
                                {t(getDriverParsedValue?.buttonText)}
                            </CustomButton>
                        </Popconfirm>
                    );

                case "CARGO_ON_THE_WAY" &&  "EXPORT": //Teslim et CONTAINER export
                    return (
                        <Upload
                            accept="image/*"
                            action={`${BASE_URL}/clientTransportProcess/${transport?.id}/driverDocument?documentType=VGM`}
                            headers={{
                                Authorization: `Bearer ${token}`,
                            }}
                            showUploadList={false}
                            onChange={x => openMessage(x.file.status)}                                            >
                            <CustomButton
                                style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                                disabled={isLoading}>
                                {t(getDriverParsedValue?.buttonText)}
                            </CustomButton>
                        </Upload >
                    );

                case  "CARGO_ON_THE_WAY" &&  "IMPORT": //Teslim et CONTAINER import
                    return (
                        <Popconfirm placement="bottomRight" title={t("Yükü teslim etmek istediğinize emin misiniz?")} onConfirm={async () => {
                            try {
                                let res = await updateStatusForCargoDeliveredRequest(transport.transportCode);
                                if (res) {
                                    updatePage();
                                }
                            } catch (error) {
                                RequestError();
                            }
                        }}
                            okText={t("Evet")}
                            cancelText={t("İptal")}
                        >
                            <CustomButton
                                disabled={isLoading}
                                style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                            >
                                {t(getDriverParsedValue?.buttonText)}
                            </CustomButton>
                        </Popconfirm>
                    );

                
            
            }
        }
    }

    const ClientActionButtons = ({status}) => {
        if (getClientParsedValue?.buttonShown && getClientParsedValue?.buttonText) {
            switch (status) {
                case "WAITING_FOR_TRANSPORT": //Taşımayı başlat
                return (
                    <Popconfirm placement="bottomRight" title={t("Yükü teslim ettiğiniz onaylıyor musunuz?")} onConfirm={() => advertStatusStep()}
                        okText={t("Evet")}
                        cancelText={t("İptal")}
                    >
                        <CustomButton
                            disabled={isLoading}
                            style={{ backgroundColor: `var(--${getClientParsedValue?.color})` }}
                        >
                            {t(getClientParsedValue?.buttonText)}
                        </CustomButton>
                    </Popconfirm>
                );          
                case "ASSIGNED_VEHICLE" &&  "CONTAINER" &&  "EXPORT": //Föy yükle
                    return (
                        <Upload
                            accept="image/*"
                            action={`${BASE_URL}/clientTransportProcess/${transport?.id}/clientDocument?documentType=SUMMARY`}
                            headers={{
                                Authorization: `Bearer ${token}`,
                            }}
                            showUploadList={false}
                            onChange={x => openMessage(x.file.status)}
                        >
                            <CustomButton
                                style={{ backgroundColor: `var(--${getClientParsedValue?.color})` }}
                                disabled={isLoading}>
                                {t(getClientParsedValue?.buttonText)}
                            </CustomButton>
                        </Upload>
                    );

                case "STARTING_TRANSPORT": //Yüklemeyi onayla
                    return (
                        <Popconfirm
                            placement="bottomRight"
                            title={t("Yükün teslim alındığını onaylıyor musunuz?")}
                            okText={t("Evet")}
                            cancelText={t("Hayır")}
                            onConfirm={() => startConfirmationTransport()}>
                            <CustomButton
                                style={{ backgroundColor: `var(--${getClientParsedValue?.color})` }}>{t(getClientParsedValue?.buttonText)}</CustomButton>
                        </Popconfirm>
                    );

                case "ENDING_TRANSPORT": //Teslimatı onayla
                    return (
                        <Popconfirm
                            placement="bottomRight"
                            title={t("Yükün teslim edildiğini onaylıyor musunuz?")}
                            okText={t("Evet")}
                            cancelText={t("Hayır")}
                            onConfirm={() => endConfirmationTransport()}>
                            <CustomButton
                                style={{ backgroundColor: `var(--${getClientParsedValue?.color})` }}>{t(getClientParsedValue?.buttonText)}</CustomButton>
                        </Popconfirm >
                    );

                default:
                    return (
                        <CustomButton
                            disabled={isLoading}
                            onClick={() => 
                                data?.advertisementStatus === "TEMPORARY_METHOD" ? //Ödeme yap
                                    setModalPayment(true) :
                                data?.advertisementStatus === "APPROVED" ? //Ödeme yap
                                     setModalPayment1(true) :
                                    data?.advertisementStatus === "CARGO_ON_THE_WAY" ? //Takip et
                                        followTransport() :
                                        data?.advertisementStatus === "RATING" && //Süreci değerlendir
                                            setModalEvaluate(true)

                            }
                            style={{ backgroundColor: `var(--${getClientParsedValue?.color})` }
                            }
                        >
                            {t(getClientParsedValue?.buttonText)}
                        </CustomButton >
                    );
            }
        }
    }

    switch (role) {
        case "ROLE_DRIVER":
            return (
                <>
                    <CreateBid visible={bidModalVisible} setVisible={setBidModalVisible} transportId={transport?.id} revision />
                    <div className={`alert-${getDriverParsedValue?.color}`}>
                        <div className='leftSide'>
                            <img
                                style={{ paddingRight: ".5rem" }}
                                className={
                                    getDriverParsedValue?.color === 'blue' ?
                                        "transport-card-clientAdvertisementType" : ""}
                                src={
                                    getDriverParsedValue?.color === 'orange' ?
                                        "/assets/img/warning.svg" :
                                        getDriverParsedValue?.color === 'red' ?
                                            "/assets/img/error.svg" :
                                            getDriverParsedValue?.color === 'green' ?
                                                "/assets/img/success.svg" :
                                                `/assets/img/types/${getDriverParsedValue?.color}${transport?.clientAdvertisementType}.svg`}
                                alt=""
                            />
                            <div className={`text-${getDriverParsedValue?.color}`}>{t(getDriverParsedValue?.alertDescription)}</div>
                        </div>
                        <DriverActionButtons status={status} />
                    </div>

                    <CustomModal
                        keyboard = {true}
                        visible={assingVehicleDriverModal}
                        onCancel={assignVehicleDriverCancel}
                        footer={
                            assingVehicleDriverStep === 1 ?
                                <Button loading={isButtonLoading} disabled={!selectedVehicle} type="primary" onClick={() => assignVehicleDriverOk()}>{t("Devam et")}</Button> :
                                <Button loading={isButtonLoading} disabled={!selectedDriver} type="primary" onClick={() => assignVehicleDriverOk2()}>{t("Devam et")}</Button>
                        }
                        /* footer={true} */
                        okText={t("Devam Et")}
                        cancelText={t("İptal Et")}
                        /* style={{}} */
                        width={450}
                        wrapClassName='modal-evaluate'
                        title={
                            <div>{t("Araç ve Şoför Seçin")}</div>
                        }
                        content={
                            <div className='assign-vehicle'>
                                <Row gutter={[0, 10]}>
                                    <Col xs={24} sm={11} className='left'>
                                        <img className='image' src={assingVehicleDriverStep === 1 ? '../../assets/img/assign-vehicle.svg' : '../../assets/img/assign-driver.svg'} alt="" />
                                    </Col>
                                    <Col xs={24} sm={13} className='right'>
                                        {assingVehicleDriverStep === 1 ? <ChooseVehicle /> : assingVehicleDriverStep === 2 ? <ChooseDriver /> : <></>}
                                    </Col>
                                </Row>
                            </div>
                        }
                    />
                    <CustomModal
                        keyboard = {true}
                        visible={onDeliverModal}
                        onCancel={() => setOnDeliverModal(false)}
                        onOk={onDeliver}
                        wrapClassName='modal-info'
                        content={
                            <div>
                                <Alert style={{ marginBottom: "1rem" }} type="info" message={t("Lütfen teslimat için irsaliye fotoğrafını yükleyin!")} />
                                <UploadItem
                                    multiple={false}
                                    id={transportProcess?.id}
                                    filesForSend={filesForSend}
                                    setFilesForSend={setFilesForSend}
                                />
                            </div>
                        }
                    />
                    
                    <CustomModal
                        keyboard = {true}
                        visible={shipsmentInfo}
                        footer={shipsmentInfoModalFooter()}
                        wrapClassName='modal-info'
                        content={
                            <div style={{maxWidth: "300px",}}>
                                <Form
                                labelCol={{ span: 24 }}
                                wrapperCol={{ span: 24 }}
                                initialValues={formValues}
                                onFinish={submitShipsmentInfo}
                                autoComplete="off"
                                >
                                    {
                                        transport.startDeliveryMethod != "p2p"&&
                                        <>
                                            <Form.Item
                                            label="Araç Plakası"
                                            name="plate"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen Araç Plakası Girin',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.plate = e.target.value;}} placeholder="Araç Plakası" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Dorse Plakası"
                                            name="trailerPlate"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen Dorse Plakası Girin',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.trailerPlate = e.target.value;}} placeholder="Dorse Plakası" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Şoför Adı Soyadı"
                                            name="name"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen Konteyner Şoför Adı Soyadını Girin',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.name = e.target.value;}} placeholder="Şoför Adı Soyadı" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Şoför Tc"
                                            name="tc"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen Konteyner Şoför Tc Girin',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.tc = e.target.value;}} placeholder="Şoför Tc" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Şoför Tel"
                                            name="tel"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen Konteyner Şoför telefonunu Girin',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.tel = e.target.value;}} placeholder="Şoför Tel" />
                                            </Form.Item>
                                        </>
                                    }
                                    {
                                       transport.vehicleOrContainer === "Hava Yolu" &&
                                       <>
                                            <Form.Item
                                            label="Awb No"
                                            name="awb"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen awb no girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.awb = e.target.value;}} placeholder="Awb No" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Airlane"
                                            name="airlane"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen airlane adı girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.airlane = e.target.value;}} placeholder="Airlane Adı" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Orgin Ariport"
                                            name="orginAirport"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen orgin ariport adı girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.orginAirport = e.target.value;}} placeholder="Orgin Ariport Adı" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Destination"
                                            name="destination"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen destination bilgisi girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.destination = e.target.value;}} placeholder="Destination" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Ambar"
                                            name="ambar"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen ambar bilgisi girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.ambar = e.target.value;}} placeholder="Ambar" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Opsiyon"
                                            name="opsiyon"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen opsiyon bilgisi girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.opsiyon = e.target.value;}} placeholder="Opsiyon" />
                                            </Form.Item>
                                            <Form.Item
                                            label="Bayrak"
                                            name="bayrak"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen bayrak bilgisi girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.bayrak = e.target.value;}} placeholder="Bayrak" />
                                            </Form.Item>
                                            <Form.Item
                                            label="2.İskele"
                                            name="iskele"
                                            rules={[
                                                {
                                                required: true,
                                                message: 'Lütfen 2.İskele bilgisi girin!',
                                                },
                                            ]}
                                            >
                                                <Input onChange={(e) => {shipsmentInfoData.iskele = e.target.value;}} placeholder="2.İskele" />
                                            </Form.Item>
                                       </>
                                    }
                                    {
                                       transport.vehicleOrContainer === "Deniz Yolu" &&
                                       <>
                                        <Form.Item
                                        label="Booking No"
                                        name="booking"
                                        rules={[
                                            {
                                            required: true,
                                            message: 'Lütfen Booking Bilgisi Girin',
                                            },
                                        ]}
                                        >
                                            <Input onChange={(e) => {shipsmentInfoData.booking = e.target.value;}} placeholder="Booking No" />
                                        </Form.Item>
                                        <Form.Item
                                        label="Mühür No"
                                        name="seal"
                                        rules={[
                                            {
                                            required: true,
                                            message: 'Lütfen Mühür Bilgisi Girin',
                                            },
                                        ]}
                                        >
                                            <Input onChange={(e) => {shipsmentInfoData.seal = e.target.value;}} placeholder="Booking No" />
                                        </Form.Item>
                                        <Form.Item
                                        label="Konteyner No"
                                        name="cotainer"
                                        rules={[
                                            {
                                            required: true,
                                            message: 'Lütfen Konteyner No Plakası Girin',
                                            },
                                        ]}
                                        >
                                            <Input onChange={(e) => {shipsmentInfoData.cotainer = e.target.value;}} placeholder="Konteyner No" />
                                        </Form.Item>
                                       </>

                                    }

                                    <Form.Item
                                        style={{display: "none"}}
                                    >
                                        <Button
                                            type="primary"
                                            htmlType='submit'
                                            id='submitFormShipsement'
                                            loading={isLoading2}
                                        >
                                            {t("Bilgileri Kaydet")}
                                        </Button>
                                    </Form.Item>
                                </Form>
                            </div>
                        }
                    />
                </>
            );


        case "ROLE_CLIENT":
            return (
                <>
                    {/*  {
                        status === "BID_APPROVED" &&
                        <Button onClick={() => paymentSuccess()}>Ödemeyi atla</Button>
                    } */}
                    <div className={`alert-${getClientParsedValue?.color}`}>
                        <div className='leftSide'>
                            <img
                                style={{ paddingRight: ".5rem" }}
                                className={
                                    getClientParsedValue?.color === 'blue' ? "transport-card-clientAdvertisementType" :
                                        ""}
                                src={
                                    getClientParsedValue?.color === 'orange' ? "/assets/img/warning.svg" :
                                        getClientParsedValue?.color === 'red' ? "/assets/img/error.svg" :
                                            getClientParsedValue?.color === 'green' ? "/assets/img/success.svg" : `/assets/img/types/${getClientParsedValue?.color}${data?.clientAdvertisementType}.svg`} alt="" />
                            <div className={`text-${getClientParsedValue?.color}`}>{t(getClientParsedValue?.alertDescription)}</div>
                        </div>
                        <ClientActionButtons status={status} />
                    </div >

                    {/* EVALUATE MODAL */}
                    <CustomModal
                        keyboard = {true}
                        visible={modalEvaluate}
                        onCancel={evaluateCancel}
                        footer={
                            <Button loading={isLoading} disabled={!evaluate.rating} onClick={() => evaluateOk()} type="primary" >{t("Değerlendir")}</Button >
                        }
                        width={450}
                        wrapClassName='modal-evaluate'
                        title={
                            <div>{t("Süreci Değerlendir")}</div>
                        }
                        content={
                            <div className='content' >
                                <div className='evaluate'>
                                    <p>
                                        {t("Değerlendirmeniz")}
                                    </p>
                                    <div >
                                        <Rate
                                            value={evaluate.rating}
                                            className='rate'
                                            onChange={(e) => setEvaluate({ ...evaluate, rating: e })}
                                        />
                                    </div>
                                </div>

                                <div className='comment'>
                                    <p style={{ marginBottom: ".5rem" }}>
                                        {t("Lojister deneyiminizi nasıl buldunuz?")}
                                    </p>
                                    <div>
                                        <TextArea value={evaluate.comment} onChange={(e) => setEvaluate({ ...evaluate, comment: e.target.value })} rows={4} placeholder={t("Deneyiminizi yazın")} maxLength={255} />
                                    </div>
                                </div>
                            </div >
                        }
                    />

                    {/* PAYMENT MODAL */}
                    <CustomModal
                        // visible={modalPayment}
                        visible={false}
                        keyboard = {false}
                        onCancel={paymentCancel}
                        footer={
                            witchStep === 1 ?
                                <Button
                                    loading={paymentIsLoading}
                                    type="primary"
                                    onClick={() => paymentSelect()}>
                                    {t("Devam et")}
                                </Button> :
                                witchStep === 2 ?
                                paymentOptions === "current"?
                                <div style={{ display: "flex", gap: ".5rem", justifyContent: "flex-end" }}>
                                    <Button
                                        type="text"
                                        onClick={() => setWitchStep(1)}>
                                        {t("Geri")}
                                    </Button>
                                    <Button
                                        disabled={paymentOptions !== "current" || !contract}
                                        loading={paymentIsLoading}
                                        type="primary"
                                        onClick={() => currentPayment()}>
                                        {t("Ödemeyi yap")}
                                    </Button>
                                </div>
                                :
                                <div style={{ display: "flex", gap: ".5rem", justifyContent: "flex-end" }}>
                                    <Button
                                        type="text"
                                        onClick={() => setWitchStep(1)}>
                                        {t("Geri")}
                                    </Button>
                                    <Button
                                        disabled={!selectedBank || !contract }
                                        loading={paymentIsLoading}
                                        type="primary"
                                        onClick={() => setWitchStep(3)}>
                                        {t("Ödemeyi yap")}
                                    </Button>
                                </div> 
                                :
                            null
                        }
                        width={800}
                        wrapClassName='modal-evaluate'
                        title={
                            < div >{t("Ödeme Yap")} </div >
                        }
                        content={
                            < div className='payment' >
                                <Row className='top' gutter={24}>
                                    <Col xs={24} sm={16}>
                                        <div className='steps'>

                                        <Steps
                                            size='small'
                                            current={0}
                                            direction="vertical"
                                            >
                                                <Step
                                                    icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle"></i>}
                                                    title={data?.startFullAddress}
                                                    description={data?.startDate+ " / " +data?.dueDate}/>
                                                <Step icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle-fill"></i>}
                                                    title={data?.dueFullAddress} />
                                        </Steps>
                                        </div>
                                    </Col>
                                    <Col xs={24} sm={8}>
                                        <div className='alert'>
                                            <div className='top'>
                                                <div>{t("Ödenecek Tutar")}</div>
                                                <Tooltip placement="bottom" title={'info'}>
                                                    <span style={{ cursor: "pointer" }}>
                                                        <i className="bi bi-info-circle-fill"></i>
                                                    </span>
                                                </Tooltip>
                                            </div>
                                            {insuranceType === "NARROW"&&
                                                <div className='price'>{approvedBid + insuranceData?.insurancePrice}$</div>
                                            }
                                            {insuranceType == "COMPREHENSIVE"&&
                                                <div className='price'>{approvedBid + insuranceData?.comprehensiveprice}$</div>
                                            }
                                            {insuranceType == "UNINSURED"&&
                                                <div className='price'>{approvedBid}$</div>
                                            }
                                        </div>
                                    </Col>
                                </Row>
                                <Divider />

                                {witchStep === 1 ? <FirstStep /> : witchStep === 2 ? <SecondStep /> : <ThirdStep />}

                            </div >
                        }
                    />
                    <CustomModal
                        visible={modalPayment1}
                        keyboard = {true}
                        onCancel={paymentCancel1}
                        footer={
                            witchStep === 1 ?
                                <Button
                                    type="primary"
                                    loading={paymentIsLoading}
                                    onClick={() => paymentSelect()}>
                                    {t("Devam et")}
                                </Button> :
                                witchStep === 2 ?
                                paymentOptions === "current"?
                                <div style={{ display: "flex", gap: ".5rem", justifyContent: "flex-end" }}>
                                    <Button
                                        type="text"
                                        onClick={() => setWitchStep(1)}>
                                        {t("Geri")}
                                    </Button>
                                    <Button
                                        disabled={paymentOptions !== "current" || !contract}
                                        loading={paymentIsLoading}
                                        type="primary"
                                        onClick={() => currentPayment()}>
                                        {t("Ödemeyi yap")}
                                    </Button>
                                </div>
                                :
                                <div style={{ display: "flex", gap: ".5rem", justifyContent: "flex-end" }}>
                                    <Button
                                        type="text"
                                        onClick={() => setWitchStep(1)}>
                                        {t("Geri")}
                                    </Button>
                                    <Button
                                        loading={paymentIsLoading}
                                        disabled={paymentOptions !== "pos"}
                                        type="primary"
                                        onClick={() => afterPay()}>
                                        {t("Devam Et")}
                                    </Button>
                                </div> 
                                :
                                null
                        }
                        width={800}
                        wrapClassName='modal-evaluate'
                        title={
                            < div >{t("Ödeme Yap")} </div >
                        }
                        content={
                            <div className='payment' >
                                <Row className='top' gutter={24}>
                                    <Col xs={24} sm={16}>
                                        <div className='steps'>

                                        <Steps
                                            size='small'
                                            current={0}
                                            direction="vertical"
                                            >
                                                <Step
                                                    icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle"></i>}
                                                    title={data?.startFullAddress}
                                                    description={data?.startDate+ " / " +data?.dueDate}/>
                                                <Step icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle-fill"></i>}
                                                    title={data?.dueFullAddress} />
                                        </Steps>
                                        </div>
                                    </Col>
                                    <Col xs={24} sm={8}>
                                        <div className='alert'>
                                            <div className='top'>
                                                <div>{t("Ödenecek Tutar")}</div>
                                                <Tooltip placement="bottom" title={'info'}>
                                                    <span style={{ cursor: "pointer" }}>
                                                        <i className="bi bi-info-circle-fill"></i>
                                                    </span>
                                                </Tooltip>
                                            </div>
                                            {insuranceType === "NARROW"&&
                                                <>
                                                   <div className='price'>{parseFloat(approvedBid) + insuranceData?.insurancePrice}$</div>
                                                </>
                                            }
                                            {insuranceType == "COMPREHENSIVE"&&
                                                <>
                                                    <div className='price'>{parseFloat(approvedBid) + insuranceData?.comprehensiveprice}$</div>
                                                </>
                                            }
                                            {insuranceType == "UNINSURED"&&
                                                <div className='price'>{approvedBid}$</div>
                                            }
                                        </div>
                                    </Col>
                                </Row>
                                <Divider />

                                {witchStep === 1 ? <FirstStep /> : witchStep === 2 ? <SecondStep /> : <ThirdStep />}

                            </div >
                        }
                    />
                    
                    <CustomModal                       
                        visible={modalPayment}
                        keyboard = {true}
                        onCancel={paymentCancel}
                        width={800}
                        wrapClassName='modal-evaluate'
                        title={
                            < div >{t("Ödeme Yap")} </div >
                        }
                        footer={null}
                        content={
                            <div className='payment' >
                                <Row className='top' gutter={24}>
                                    <Col xs={24} sm={16}>
                                        <div className='steps'>

                                        <Steps
                                            size='small'
                                            current={0}
                                            direction="vertical"
                                            >
                                                <Step
                                                    icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle"></i>}
                                                    title={data?.startFullAddress}
                                                    description={data?.startDate+ " / " +data?.dueDate}/>
                                                <Step icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle-fill"></i>}
                                                    title={data?.dueFullAddress} />
                                        </Steps>
                                        </div>
                                    </Col>
                                    <Col xs={24} sm={8}>
                                        <div className='alert'>
                                            <div className='top'>
                                                <div>{t("Ödenecek Tutar")}</div>
                                                <Tooltip placement="bottom" title={'info'}>
                                                    <span style={{ cursor: "pointer" }}>
                                                        <i className="bi bi-info-circle-fill"></i>
                                                    </span>
                                                </Tooltip>
                                            </div>
                                            {insuranceType === "NARROW"&&
                                                <>
                                                <div className='price'>{parseFloat(approvedBid) + insuranceData?.insurancePrice}$</div>
                                                </>
                                            }
                                            {insuranceType == "COMPREHENSIVE"&&
                                                <>
                                                    <div className='price'>{parseFloat(approvedBid) + insuranceData?.comprehensiveprice}$</div>
                                                </>
                                            }
                                            {insuranceType == "UNINSURED"&&
                                                <div className='price'>{approvedBid}$</div>
                                            }
                                        </div>
                                    </Col>
                                </Row>
                            <Divider />

                            <CreditCard abroudorDomestic="Abroud" transportId={transport.id} setWitchStep={setWitchStep} setContract={setContract} contract={contract} transportProcess={transportProcess}  creditCardFormValue={creditCardFormValue} />

                        </div >
                        }
                    />
                </>
            );
        default:
            break;
    }
};

export default React.memo(DetailAlertAbroud);