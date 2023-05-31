import { Alert, Button, Checkbox, Col, Divider, Dropdown, Popconfirm, Radio, Rate, Row, Select, Space, Tooltip, Menu, Modal, Upload, message } from 'antd';
import React, { useEffect, useRef, useState } from 'react'
import { Steps } from 'antd';
import { CustomButton } from './CustomButton';
import { CustomModal } from './CustomModal';
import UploadItem from './UploadItem';
import TextArea from 'antd/lib/input/TextArea';
import { useSelector } from 'react-redux';
import moment from 'moment';
import { ParsedDriver, ParsedClient } from "./ParsedValues"
import AntNotification from './AntNotification';
import { getMyDriversAll } from '../api/controllers/driver-controller';
import { changeDriverRequest, getMyVehiclesAll } from '../api/controllers/vehicle-controller';
import { assignVehicleRequest, endConfirmationTransporNoTokentRequest, endingTransportRequest, startConfirmationTransporNoTokentRequest, startingTransportRequest, updateStatusForCargoDeliveredRequest, currentPaymentapi } from '../api/controllers/client-transport-process-controller';
import { BASE_URL } from '../api/ApiProvider';
import { halkBankPaymentSuccessRequest } from '../api/controllers/halkbank-payment-controller';
import { createWaybillUploadRequest } from '../api/controllers/way-bill-controller';
import { createRatingRequestAbroud } from '../api/controllers/rating-client-advertisement-controller';
import Sozlesmeler from './Sozlesmeler';
import RequestError from './RequestError';
import CreateBid from './CreateBid';
import { useTranslation } from 'react-i18next';
import { getDolar,getEuro } from "../api/controllers/TCMB-controller";
import {Insurance} from "./Insurance";
import CreditCard from "./CreditCard";
import { current } from '@reduxjs/toolkit';

const DetailAlert = ({ data, approvedBid, transport, transportProcess, transportDocuments, updatePage, status }) => {
    const { t, i18n } = useTranslation("common");
    const [insuranceType, setInsuranceType] = useState("UNINSURED");
 
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
        insuranceType:  insuranceType,
        abroadOrDomestic: "Domastic",
        transportID: transport.id
      });
      
      
    const insuranceData = Insurance(transport?.goodsPrice, "TR", "TR", transport?.currencyUnit?.currencySymbol);

    
    const token = localStorage.getItem('token')
    const currentUser = useSelector((state) => state.user);
    const { role } = currentUser;
    const getClientParsedValue = ParsedClient({ transport, transportDocuments, status });
    const getDriverParsedValue = ParsedDriver({ transport, transportDocuments, status });
    const [paymentOptions, setPaymentOptions] = useState(!currentUser?.current&& "pos"); 
    const [paymentIsLoading, setPaymentIsLoading] = useState(false);

    const [modalPayment, setModalPayment] = useState(data?.advertisementProcessStatus === "BID_APPROVED" ? true : false)
    const [bidModalVisible, setBidModalVisible] = useState(false)

    /* EVALUATE STATES */
    const [modalEvaluate, setModalEvaluate] = useState(false)
    const [evaluate, setEvaluate] = useState({
        advertAbroud: null,
        advertDomastic: {
            id: transport.id
        },
        comment: "",
        rating: null,
        abroudOrDomastic: "DOMASTIC"
    })

    const { Step } = Steps;

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
            advertAbroud: null,
            advertDomastic: {
                id: transport.id
            },
            comment: "",
            rating: null,
            abroudOrDomastic: "DOMASTIC"
        })
    }

    /* PAYMENT STATES */
    const [selectedBank, setSelectedBank] = useState("")
    const [paymentPrice, setPaymentPrice] = useState(approvedBid?.bidWithVat)
    const [contract, setContract] = useState(false)
    const [insurance, setInsurance] = useState(0);
    const [insufficientBalance, setInsufficientBalance] = useState(false);

    const onChangePayment = (e) => {
        creditCardFormValue.insuranceType = e.target.value;
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
                                    <div className='price'>{insuranceData?.insurancePrice}₺</div>
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
                            <Radio value={"COMPREHENSIVE"} disabled={transport?.goodsPrice === null || insuranceData?.comprehensiveprice === 0}>
                                <div className='extensive'>
                                    <div>{t("Geniş teminat kapsamında istiyorum")}</div>
                                    <div className='price'>{insuranceData?.comprehensiveprice}₺</div>
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
                {
                    paymentOptions === "pos"&&
                    <CreditCard abroudorDomestic="Domastic" transportId={transport.id} setWitchStep={setWitchStep} setContract={setContract} contract={contract} transportProcess={transportProcess}  creditCardFormValue={creditCardFormValue} />
                }
                {
                    paymentOptions !== "pos"&&
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

        if (transportProcess.transportCode && selectedBank === "halk") {
            return (
                <>
                    {/* <button onClick={() => paymentSuccess()}></button> ödemeyi atlama butonu */}
                    <iframe id='#output-frame-id' style={{ minHeight: "400px", height: "100%", width: "100%", border: "none", borderRadius: "1.5rem" }} src={`${BASE_URL}/payment/halkbank/3d?transportCode=${transportProcess.transportCode}&insuranceType=${insuranceType}`} title="halk"></iframe >
                </>
            )
        } else if (transportProcess.transportCode && selectedBank === "teb") {
            return (
                <iframe id='#output-frame-id' style={{ minHeight: "400px", height: "100%", width: "100%", border: "none", borderRadius: "1.5rem" }} src={`${BASE_URL}/payment/3d?transportCode=${transportProcess.transportCode}&insuranceType=${insuranceType}`} title="teb"></iframe>
            )
        }
    }

    /* Button loading */

    const [isLoading, setIsLoading] = useState(false)

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
            let ref = await assignVehicleRequest({ clientTransportProcessId: transportProcess.id, vehicleId: selectedVehicle.value });
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
            let res = await halkBankPaymentSuccessRequest(transportProcess.transportCode)
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const startingTransport = async () => {
        try {
            let res = await startingTransportRequest(transportProcess.id);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const startConfirmationTransport = async () => {
        try {
            let res = await startConfirmationTransporNoTokentRequest(transportProcess.transportCode);
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
            let res = await endingTransportRequest(transportProcess.id);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }
    const currentPayment = async () =>{
        setPaymentIsLoading(true);

        try {
            data = {
                abroudOrDomastic: "Domastic",
                transportId: transport.id,
                insuranceType: insuranceType,
                insuranceCurency: "₺",
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
    const endConfirmationTransport = async () => {
        try {
            let res = await endConfirmationTransporNoTokentRequest(transportProcess.transportCode);
            if (res) {
                updatePage()
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const [onDeliverModal, setOnDeliverModal] = useState(false);
    const [filesForSend, setFilesForSend] = useState([])
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


    const DriverActionButtons = () => {

        if (getDriverParsedValue?.buttonShown && getDriverParsedValue?.buttonText) {
            switch (true) {
                case status === "ASSIGNED_VEHICLE": //Taşımayı başlat
                    return (
                        <Popconfirm placement="bottomRight" title={t("Taşıma sürecini başlatmak istediğinize emin misiniz?")} onConfirm={() => startingTransport()}
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

                case status === "UPLOADED_DOCUMENT": //Belge yüklendi --vgm, föy
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

                case status === "CARGO_DELIVERED": //Konteyner iade ediliyor
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

                case status === "CARGO_ON_THE_WAY" && transport?.tradeType === "EXPORT": //Teslim et CONTAINER export
                    return (
                        <Upload
                            accept="image/*"
                            action={`${BASE_URL}/clientTransportProcess/${transportProcess?.id}/driverDocument?documentType=VGM`}
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

                case status === "CARGO_ON_THE_WAY" && transport?.tradeType === "IMPORT": //Teslim et CONTAINER import
                    return (
                        <Popconfirm placement="bottomRight" title={t("Yükü teslim etmek istediğinize emin misiniz?")} onConfirm={async () => {
                            try {
                                let res = await updateStatusForCargoDeliveredRequest(transportProcess.transportCode);
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

                case status === "CARGO_ON_THE_WAY": //Müşteriden onay al
                    return (
                        <Dropdown
                            overlay={
                                <Menu
                                    items={[
                                        {
                                            label: <Popconfirm placement="bottomRight" title={t("Taşıma sürecini tamamlamak istediğinize emin misiniz?")} onConfirm={() => endingTransport()}
                                                okText={t("Evet")}
                                                cancelText={t("İptal")}
                                            >
                                                <span>{t("Müşteriden onay iste")}</span>
                                            </Popconfirm>,
                                            key: '0',
                                        },
                                        {
                                            label: <span onClick={() => setOnDeliverModal(true)}>{t("İrsaliye yükle")}</span>,
                                            key: '1',
                                        },
                                    ]}
                                />
                            }
                            trigger={['click']}>
                            <CustomButton
                                style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                                disabled={isLoading}>
                                {t(getDriverParsedValue?.buttonText)}
                            </CustomButton>
                        </Dropdown>
                    );

                default:
                    return (
                        <CustomButton
                            disabled={isLoading}
                            onClick={() =>
                                status === "WAYBILL_DENIED" ? //İrsaliye yükle tekrar
                                    setOnDeliverModal(true) :
                                    status === "PAYMENT_SUCCESSFUL" ? //Araç ata
                                        assignVehicleDriver() :
                                        status === "DENIED" && //Yeniden teklif ver
                                        setBidModalVisible(true)
                            }
                            style={{ backgroundColor: `var(--${getDriverParsedValue?.color})` }}
                        >
                            {t(getDriverParsedValue?.buttonText)}
                        </CustomButton>
                    );
            }
        }
    }

    const ClientActionButtons = () => {

        if (getClientParsedValue?.buttonShown && getClientParsedValue?.buttonText) {
            switch (true) {
                case status === "ASSIGNED_VEHICLE" && transport?.clientAdvertisementType === "CONTAINER" && transport?.tradeType === "EXPORT": //Föy yükle
                    return (
                        <Upload
                            accept="image/*"
                            action={`${BASE_URL}/clientTransportProcess/${transportProcess?.id}/clientDocument?documentType=SUMMARY`}
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

                case status === "STARTING_TRANSPORT": //Yüklemeyi onayla
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

                case status === "ENDING_TRANSPORT": //Teslimatı onayla
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
                                status === "BID_APPROVED" ? //Ödeme yap
                                    setModalPayment(true) :
                                    status === "CARGO_ON_THE_WAY" ? //Takip et
                                        followTransport() :
                                        status === "COMPLETED" && //Süreci değerlendir
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
                        <DriverActionButtons />
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
                        <ClientActionButtons />
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
                        keyboard = {true}
                        visible={modalPayment}
                        onCancel={paymentCancel}
                        footer={
                            witchStep === 1 ?
                                <Button
                                    type="primary"
                                    onClick={() => setWitchStep(2)}>
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
                                                loading={paymentIsLoading}
                                                disabled={paymentOptions !== "current" || !contract}
                                                type="primary"
                                                onClick={() => currentPayment()}>
                                                {t("Ödemeyi yap")}
                                            </Button>
                                        </div>
                                        :
                                        null
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
                                            <Steps responsive={false} size='small'  >
                                                <Step
                                                    icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle"></i>}
                                                    title={approvedBid?.clientAdvertisement?.regionAdvertisementType === 'INTERNATIONAL' ? approvedBid?.clientAdvertisement?.startingAddress?.province + "-" + approvedBid?.clientAdvertisement?.startingAddress?.country : approvedBid?.clientAdvertisement?.regionAdvertisementType === 'DOMESTIC' ? approvedBid?.clientAdvertisement?.startingAddress?.province + ", " + approvedBid?.clientAdvertisement?.startingAddress?.district : ""}
                                                    description={moment(approvedBid?.clientAdvertisement?.adStartingDate).format("LL")} />
                                                <Step icon={<i style={{ color: "var(--dark)" }} className="bi bi-circle-fill"></i>}
                                                    title={approvedBid?.clientAdvertisement?.regionAdvertisementType === 'INTERNATIONAL' ? approvedBid?.clientAdvertisement?.dueAddress?.province + "-" + approvedBid?.clientAdvertisement?.dueAddress?.country : approvedBid?.clientAdvertisement?.regionAdvertisementType === 'DOMESTIC' ? approvedBid?.clientAdvertisement?.dueAddress?.province + ", " + approvedBid?.clientAdvertisement?.dueAddress?.district : ""}
                                                    description={moment(approvedBid?.clientAdvertisement?.adDueDate).format("LL")} />
                                            </Steps>
                                        </div>
                                    </Col>
                                    <Col xs={24} sm={8}>
                                        <div className='alert'>
                                            <div className='top'>
                                                <div>{t("Ödenecek Tutar (KDV Dahil)")}</div>
                                                <Tooltip placement="bottom" title={'info'}>
                                                    <span style={{ cursor: "pointer" }}>
                                                        <i className="bi bi-info-circle-fill"></i>
                                                    </span>
                                                </Tooltip>
                                            </div>
                                            {insuranceType === "NARROW"&&
                                                <div className='price'>{approvedBid?.bidWithVat + insuranceData?.insurancePrice}₺</div>
                                            }
                                            {insuranceType == "COMPREHENSIVE"&&
                                                <div className='price'>{approvedBid?.bidWithVat + insuranceData?.comprehensiveprice}₺</div>
                                            }
                                            {insuranceType == "UNINSURED"&&
                                                <div className='price'>{approvedBid?.bidWithVat}₺</div>
                                            }
                                        </div>
                                    </Col>
                                </Row>
                                <Divider />

                                {witchStep === 1 ? <FirstStep /> : witchStep === 2 ? <SecondStep /> : <ThirdStep />}

                            </div >
                        }
                    />
                </>
            );
        default:
            break;
    }
};

export default React.memo(DetailAlert);