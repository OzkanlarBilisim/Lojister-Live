import React, { useState, useRef, useEffect, componentDidMount} from 'react';
import { useSelector } from 'react-redux';
import {Button, Col, Row, Tabs, List, Alert,Modal,Select,Divider, Input, Space,Form, message,Upload, Image} from 'antd';
import moment from 'moment';
import AntNotification from './AntNotification';
import AntNotificationWarning from './AntNotificationWarning';
import { PlusOutlined , LoadingOutlined} from '@ant-design/icons';
import { downloadInsuranceFileByTransportCodeRequest, DriverSendRequest, reguestDocument,reguestDocumentListClient, reguestDocumentListDriver, fileUploadAbroud, requestTransportInfo} from '../api/controllers/insured-transport-process-file-controller';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';
import { type } from '@testing-library/user-event/dist/type';
import axios from 'axios';
import {BASE_URL} from '../api/ApiProvider';


let index = 0;


const DetailInfoAbroad = ({ transport, transportId, transportProcess, transportDocuments, AdvertiseAgain}) => {
    useEffect(() => {
        RequestDocument();
    }, [])
    const currentUser = useSelector((state) => state.user);
    const [visible4, setVisible4] = useState(false);
    const [visible2, setVisible2] = useState(false);
    const [visible3, setVisible3] = useState(false);
    const [visible, setVisible] = useState(false);
    const [request,setRequest] = useState([]);
    const [transpotInfo, setTransportInfo] = useState();
    const [defaultValueForDocument,setDefaultValueForDocument] = useState([]);
    const [documentsRequestList, SetDocumentsRequestList] = useState([]);
    const [myDocumentsRequestList, SetMyDocumentsRequestList] = useState([]);
    const [documentUrl, setDocumentUrl] = useState('');
    const [pdf, setPdf] = useState(0);
    
    const transportInfoOpen = async () =>{
        setVisible4(true);
        try {
            let res = await requestTransportInfo(transportId);
            if (res) {
                setTransportInfo(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }
    const RequestDocument = async () =>{
        try {
            let res = currentUser.role === "ROLE_DRIVER" ? await reguestDocument(transportId,"CLIENT"): await reguestDocument(transportId, "DRIVER");
            if (res) {
                if(res?.data?.documents !== null){
                    setRequest(res?.data?.documents.split(","));
                }
            }
        } catch (error) {
            RequestError(error);
        }
        
        try {
            let res = currentUser.role === "ROLE_CLIENT" ? await reguestDocument(transportId,"CLIENT"): await reguestDocument(transportId, "DRIVER");
            if (res) {
                if(res?.data?.documents !== null){
                    setDefaultValueForDocument(res?.data?.documents.split(","));
                }
            }
        } catch (error) {
            RequestError(error);
        }

        try {
            let res = currentUser.role === "ROLE_DRIVER" ? await reguestDocumentListClient(transportId): await reguestDocumentListDriver(transportId);
            if (res) {
                if(res.data !== null){
                    SetDocumentsRequestList(res.data);
                }
            }
        } catch (error) {
            RequestError(error);
        }
        try {
            let res = currentUser.role === "ROLE_CLIENT" ? await reguestDocumentListClient(transportId): await reguestDocumentListDriver(transportId);
            if (res) {
                if(res.data !== null){
                    SetMyDocumentsRequestList(res.data);
                }
            }
        } catch (error) {
            RequestError(error);
        }
    } 
    
    var deger2;

    const { t, i18n } = useTranslation("common");

    const token = localStorage.getItem("token");
    const { TabPane } = Tabs;

    const downloadInsurance = async () => {
        try {
            let res = await downloadInsuranceFileByTransportCodeRequest(transportProcess.transportCode)
            if (res) {
                AntNotification({ type: "success", message: t("Sigorta poliçesi indiriliyor") })
            }
        } catch (error) {
            RequestError(error);
        }
    }
   
    

    const startDeliveryMethod = (method) =>{
        switch(method){
            case "d2d":
                return t("Kapıdan Kapıya");
            case "d2p":
                return t("Kapıdan Limana");
            case "p2p":
                return t("Limandan Limana");
            case "p2d":
                return t("Limandan Kapıya");
            default:
                return t("Bilinmeyen bir taşıma durumu lütfen bizimle iletişime geçelim")
        }
    }
    const trailerFeatureNameSelect = (v)=> {
        for(var i=0; i< v.length; i++){
            
            if(i == 0){
                deger2 = v[i]
            }else{
                deger2 += ","+v[i]
            }
            
        }
      
        console.log(deger2);
    }

    

    const showModal2 = () => {
        setVisible2(true);
    }
    const handleCancel2 = () => {
        setVisible2(false);
    }
    const handleCancel3 = () => {
        setVisible3(false);
    }
    const showModal = () => {
        setVisible(true);
    }
    const handleCancel = () => {
        setVisible(false);
        
    }
    const showModalDocument = (id,type) => {
        setVisible3(true);
        axios.get(`/files/`+id, {
            responseType: 'blob',
          })
          .then(response => {
            if(type === "application/pdf"){
                setPdf(1);
            }else{
                setPdf(0);
            }
            
            setDocumentUrl(URL.createObjectURL(response.data));

          });
      };
    const requestDocumentDriver = async () =>{
        if(currentUser?.role === "ROLE_DRIVER"){
            var data = {
                clientID: 0,
                driverID: currentUser.companyId,
                documents: deger2,
                transportId: transportId,
                wanting: "DRIVER"
            }

            console.log(data);
        }else{
            var data = {
                clientID: currentUser.id,
                driverID: 0,
                documents: deger2,
                transportId: transportId,
                wanting: "CLIENT"
            }
        }
        
        try {
            let res = await DriverSendRequest(data)
            if (res) {
                AntNotification({ type: "success", message: t("Talepleriniz Gönderildi") })
                setVisible(false)
            }
        } catch (error) {
            RequestError(error);
        }
        console.log(data);
        handleCancel();
    }


    const DocumentsAbroadRequest = () => {     
        const defaultItems = currentUser?.role === "ROLE_DRIVER"? ['MSDS','DG Form','Yükleme Talimatı','Gümrük Beyannamesi','Sevkiyat Listesi', 'Gümrük Belgesi'] : [];

        const [items, setItems] = useState(defaultItems);
        const [items2, setItems2] = useState([]);
        const [name, setName] = useState('');
        const inputRef = useRef(null);

        const onNameChange = (event) => {
            setName(event.target.value);
            
            setItems2([...items2, event.target.value || `New item ${index++}`]);

            console.log(items2);
            
        };

        const addItem = (e) => {
            e.preventDefault();
            setItems([...items, name || `New item ${index++}`]);
            setName('');
            setTimeout(() => {
            inputRef.current?.focus();
            }, 0);
            
        };
            const [loading, setLoading] = useState(false);
            const [imageUrl, setImageUrl] = useState("");


        return (
          <div className='buttonyeri'>
           <>
          <Button type="primary"  onClick={showModal}> {t("Belge Talep Et")}</Button >
       
         
      

      <Modal
      title={t("Belge Talep Et")}
      visible={visible}
      centered
     
      keyboard={true}
      onCancel={handleCancel}
      okText={t("Belge Talep Et")}
      onOk={() => requestDocumentDriver()}
      cancelText={t("İptal Et")}
      width={700}
      
  >
       <><Row><Col xs={24} xl={12} xxl={8}>  
    
     <Form.Item
     label={"İstenilen Belgeler"}
     name="RequiredDocuments"
       
     >    
      <Select
    style={{
      width: 300,
    
    }}
    onChange={trailerFeatureNameSelect}
    defaultValue={defaultValueForDocument}
    placeholder="Talep Etmek İstediğiniz Belgeleri Seçiniz"
    mode="multiple"
    dropdownRender={(menu) => (
      <>
        {menu}
        <Divider
          style={{
            margin: '8px 0',
          }} 
        />
        <Space
           
          style={{
            padding: '0 8px 4px',
          }}   
        >    
          <Input
            placeholder="Belge İsmi Girin"
            ref={inputRef}
            value={name}
            onChange={onNameChange}
          />
          <Button type="text" icon={<PlusOutlined />} onClick={addItem}  >
          
            Belge Ekle
          </Button>
        </Space>
      </>
    )}
    options={items.map((item) => ({
      label: item,
      value: item,
    }))}
  
  /></Form.Item></Col></Row></>
  
        </Modal>
        </>
        
        </div>
        
        
        );
  };
    const MyDocumentsAbroadRequest = () => {     
        const defaultItems = currentUser?.role === "ROLE_DRIVER"? ['MSDS','DG Form','Yükleme Talimatı','Gümrük Beyannamesi','Sevkiyat Listesi', 'Gümrük Belgesi', 'Imo Formu'] : ['Konşimento'];

        const [items, setItems] = useState(defaultItems);
        const [items2, setItems2] = useState([]);
        const [name, setName] = useState('');
        const inputRef = useRef(null);
        var request_File_id = 0;

        const onNameChange = (event) => {
            setName(event.target.value);
            
            setItems2([...items2, event.target.value || `New item ${index++}`]);

            console.log(items2);
            
        };

        const addItem = (e) => {
            e.preventDefault();
            setItems([...items, name || `New item ${index++}`]);
            setName('');
            setTimeout(() => {
            inputRef.current?.focus();
            }, 0);
            
        };
            const [loading, setLoading] = useState(false);
            const [imageUrl, setImageUrl] = useState("");
          
        const getBase64 =  (img, callback) => {
            const reader = new FileReader();
            reader.addEventListener("load", () => callback(reader.result));
            reader.readAsDataURL(img);
          };
          const handleChange = info => {
            if (info.file.status === "uploading") {
              setLoading(true);
              return;
            }
            if (info.file.status === "done") {
                getBase64(info.file.originFileObj, imageUrl =>
                  setImageUrl(imageUrl),
                  setLoading(false)
                );
              }
            };
        const handleUpload = async (file, fileName, id) => {
            var Permitted_File = ["image/jpeg","image/webp", "image/png", "application/pdf"];
            var Permitted_File_True = Permitted_File.includes(file.type);
            
            if(Permitted_File_True){
                const formData = new FormData();

                formData.append("file", file);
                formData.append("name", currentUser.role+"-ho-2090-"+transportId+"-ho-2090-"+fileName);
                formData.append("role", currentUser.role);
                formData.append("fileName", fileName);
                formData.append("advert_ID", transportId);
            
                // accept='"image/jpeg","image/webp", "image/png", "application/pdf"'
                try {
                    let res = await fileUploadAbroud(formData)
                    if (res) {
                        if(res.data === ""){
                            AntNotificationWarning({ type: "warning", message: t("Bu belge tekrar eklenemez") })
                        }else{
                            AntNotification({ type: "success", message: t("Dosya başarıyla yüklendi") })
                            document.getElementById("upload_"+id).style.display = "none";
                            document.getElementById("upload_show_"+id).style.display = "block";
                            if(file.type === "application/pdf"){
                                document.getElementById("upload_show_image_"+id).src = '/assets/img/pdf_file.png';
                            }else{
                                document.getElementById("upload_show_image_"+id).src = URL.createObjectURL(file);
                            }
                        }                    
                    }
                } catch (error) {
                    RequestError(error);
                }
            }else{
                AntNotificationWarning({ type: "error", message: t("Sadece jpg, png, jpeg, webp ve pdf dosyaları yüklenebilir!") })
            }
            
        };

        return (
          <div className='buttonyeri'>
          {
            request?.length > 0 &&
            <Button type="primary"  onClick={showModal2}> {t("Talep Edilen Belgeler")}</Button >
          }
       
         <Modal
            title={t("İstenilen Belgeler")}
            visible={visible2}
            centered
            
            keyboard={true}
            onCancel={handleCancel2}
            okText={t("Belge Talep Et")}
            cancelText={t("İptal Et")}
            width={700}>
                <div style={{display: "flex", flexWrap: "wrap", justifyContent: "space-evenly"}}>

                    {request?.map((fileName) => (
                            <div style={{width: "104px", marginLeft: "5px", marginRight: "5px", marginTop: "10px", textAlign: "center"}}>
                                <div style={{display: "none", width: "104px", height: "104px", marginBottom: "8px", border: "1px dashed #d9d9d9", borderRadius: "8px"}} id={"upload_show_"+request.indexOf(fileName)}>
                                    <img  style={{width: "104px", height: "104px", objectFit: "cover", borderRadius: "8px"}} id={"upload_show_image_"+request.indexOf(fileName)} src=""/> 
                                </div>  
                                <div id={"upload_"+request?.indexOf(fileName)}>
                                    <Upload 
                                        listType="picture-card"
                                        showUploadList={false}
                                        maxCount={1}
                                        action={BASE_URL+"/files/"}
                                        name={fileName}
                                        beforeUpload={file => {
                                        handleUpload(file,fileName,request?.indexOf(fileName));
                                        return false;
                                        }}
                                    >
                                        {<h2>+</h2>}
                                    </Upload>
                                </div> 
                                {fileName}
                            </div>
                    ))}
                </div>
            </Modal>
            <Modal
            title={t("Belge Talep Et")}
            visible={visible}
            centered
            
            keyboard={true}
            onCancel={handleCancel}
            okText={t("Belge Talep Et")}
            onOk={() => requestDocumentDriver()}
            cancelText={t("İptal Et")}
            width={700}
            
            >
                <><Row><Col xs={24} xl={12} xxl={8}>  
    
                    <Form.Item
                    label={"İstenilen Belgeler"}
                    name="RequiredDocuments"
                    
                    >    
                        <Select
                        style={{
                        width: 300,
                        
                        }}
                        onChange={trailerFeatureNameSelect}
                        value={items2}
                        placeholder="Talep Etmek İstediğiniz Belgeleri Seçiniz"
                        mode="multiple"
                        dropdownRender={(menu) => (
                        <>
                            {menu}
                            <Divider
                            style={{
                                margin: '8px 0',
                            }} 
                            />
                            <Space
                            
                            style={{
                                padding: '0 8px 4px',
                            }}   
                            >    
                            <Input
                                placeholder="Belge İsmi Girin"
                                ref={inputRef}
                                value={name}
                                onChange={onNameChange}
                            />
                            <Button type="text" icon={<PlusOutlined />} onClick={addItem}  >
                            
                                Belge Ekle
                            </Button>
                            </Space>
                        </>
                        )}
                        options={items.map((item) => ({
                        label: item,
                        value: item,
                        }))}
                    
                    /></Form.Item></Col></Row></>
            </Modal>
        </div>        
    );
  };

    const GeneralInformation = () => {
        return (
            <div className='general-information'>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("İlan Numarası")} </div>
                            <div>{transport?.id}</div>
                        </Col>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("İlan Tarihi")} </div>
                            <div>{transport?.dateNow ? moment(transport?.dateNow).format("LLL") : "-"}</div>
                        </Col>
                        {
                            transport?.clientAdvertisementType === "CONTAINER" &&
                            <Col xs={24} md={12}>
                                <div className='detail-title'> {t("Ticaret Türü")} </div>
                                <div>{transport?.tradeType === "EXPORT" ? t("İhracat") : transport?.tradeType === "IMPORT" ? t("İthalat") : "-"}</div>
                            </Col>
                        }
                    </Row>
                </div>
                <br />
                {
                    !AdvertiseAgain &&
                    <div className='title'>
                        <div style={{ fontWeight: 500 }}> {t("Yükleme Aralığı:")}  <span style={{ fontWeight: 300 }}> {transport?.startDate+" / "+transport?.dueDate}</span></div>
                        <img style={{ width: "40px", transform: "rotateY(180deg)" }} src={`../../assets/img/types/active${transport?.clientAdvertisementType}.svg`} alt="" />
                    </div>
                }
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("Taşıma Tipi")} </div>
                            <div>{transport?.clientAdvertisementType+" / "+transport?.vehicleOrContainer}</div>
                        </Col>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("Taşıma Şekli")} </div>
                            <div>{startDeliveryMethod(transport?.startDeliveryMethod)}</div>
                        </Col>
                    </Row>
                </div>
                <br />
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={12}>
                            <Button onClick={() => transportInfoOpen()}>Taşıma Detayları</Button>
                        </Col>
                    </Row>
                </div>
            </div>
        )
    }

    const DetailOfLoadFtlArac = () => {
        return (
            <div className='general-information'>
                <div className='detail2'>
                    {
                        transport?.unCode &&
                            <Row>
                                <Col xs={24} md={24}>
                                    <Alert
                                      message="Imolu Ürün"
                                      banner
                                      closable
                                    />
                                </Col>
                            </Row>
                    }
                    {
                        transport?.unCode &&
                            <br/>
                    }
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Yük Tipi")} </div>
                            <div>{t(transport?.cargoTypeIdList)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Paketleme Tipi")} </div>
                            <div>{transport?.packagingTypeId}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Yükleme Şekli")} </div>
                            <div>{transport?.loadTypeIdList}</div>
                        </Col>
                    </Row>
                </div>
                <br/>
                <div className='detail'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Hs Code")} </div>
                            <div>{t(transport?.hsCode)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Un Code")} </div>
                            <div>{transport?.unCode}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Taşıma Şekli")} </div>
                            <div>{transport?.deliveryType}</div>
                        </Col>
                    </Row>
                    <br/>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Toplam Ağırlık")} </div>
                            <div>{t(transport?.tonnage + " " + t("TON"))}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Mal Değeri")} </div>
                            <div>{ transport?.goodsPrice && transport?.goodsPrice+" "+transport?.currencyUnitId}</div>
                        </Col>
                    </Row>
                </div>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={24}>
                            <div className='detail-title'> {t("Açıklama")} </div>
                            <div>{transport?.explanation}</div>
                        </Col>
                    </Row>
                </div>
            </div>
        )
    }
    const DetailOfLoadFtlKonteynerParsiyelArac = () => {
        return (
            <div className='general-information'>
                <div className='detail2'>
                    {
                        transport?.unCode &&
                            <Row>
                                <Col xs={24} md={24}>
                                    <Alert
                                      message="Imolu Ürün"
                                      banner
                                      closable
                                    />
                                </Col>
                            </Row>
                    }
                    {
                        transport?.unCode &&
                            <br/>
                    }
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Yük Tipi")} </div>
                            <div>{t(transport?.cargoTypeIdList)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Konteyner Tipi")} </div>
                            <div>{transport?.containerType}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Taşıma Şekli")} </div>
                            <div>{transport?.deliveryType}</div>
                        </Col>
                    </Row>
                </div>
                <br/>
                <div className='detail'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Hs Code")} </div>
                            <div>{t(transport?.hsCode)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Un Code")} </div>
                            <div>{transport?.unCode}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Toplam Ağırlık")} </div>
                            <div>{t(transport?.tonnage + " " + t("TON"))}</div>
                        </Col>
                    </Row>
                    <br/>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Mal Değeri")} </div>
                            <div>{ transport?.goodsPrice && transport?.goodsPrice+" "+transport?.currencyUnitId}</div>
                        </Col>
                    </Row>
                </div>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={24}>
                            <div className='detail-title'> {t("Açıklama")} </div>
                            <div>{transport?.explanation}</div>
                        </Col>
                    </Row>
                </div>
            </div>
        )
    }
    const DetailOfLoadParsiyelKonteyner = () => {
        return (
            <div className='general-information'>
                <div className='detail2'>
                    {
                        transport?.unCode &&
                            <Row>
                                <Col xs={24} md={24}>
                                    <Alert
                                      message="Imolu Ürün"
                                      banner
                                      closable
                                    />
                                </Col>
                            </Row>
                    }
                    {
                        transport?.unCode &&
                            <br/>
                    }
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Yük Tipi")} </div>
                            <div>{t(transport?.cargoTypeIdList)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Paketleme Tipi")} </div>
                            <div>{transport?.packagingTypeId}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Yükleme Şekli")} </div>
                            <div>{transport?.loadTypeIdList}</div>
                        </Col>
                    </Row>
                </div>
                <br/>
                <div className='detail'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Hs Code")} </div>
                            <div>{t(transport?.hsCode)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Un Code")} </div>
                            <div>{transport?.unCode}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Taşıma Şekli")} </div>
                            <div>{transport?.deliveryType}</div>
                        </Col>
                    </Row>
                    <br/>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Toplam Ağırlık")} </div>
                            <div>{t(transport?.tonnage + " " + t("TON"))}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Mal Değeri")} </div>
                            <div>{ transport?.goodsPrice && transport?.goodsPrice+" "+transport?.currencyUnitId}</div>
                        </Col>
                    </Row>
                    <br/>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={24}>
                            <div className='detail-title'> {t("Açıklama")} </div>
                            <div>{transport?.explanation}</div>
                        </Col>
                    </Row>
                </div>
                <div className='detail'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("En")} </div>
                            <div>{t(transport?.width+" cm")}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Boy")} </div>
                            <div>{transport?.length+" cm"}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Yükseklik")} </div>
                            <div>{transport?.height+" cm"}</div>
                        </Col>
                    </Row>
                    <br/>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Adet")} </div>
                            <div>{t(transport?.piece)}</div>
                        </Col>
                    </Row>
                </div>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={24}>
                            <div className='detail-title'> {t("Açıklama")} </div>
                            <div>{transport?.explanation}</div>
                        </Col>
                    </Row>
                </div>
            </div>
        )
    }

    const DetailOfVehicle = () => {
        return (
            <div className='general-information'>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Araç Tipi")} </div>
                            <div>{t(transport?.vehicleTypeIdList)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Araç Sayısı")} </div>
                            <div>{transport?.vehicleCount}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Kasa Tipi")} </div>
                            <div>{transport?.trailerTypeIdList}</div>
                        </Col>
                    </Row>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Zemin Tipi")} </div>
                            <div>{t(transport?.trailerFloorTypeIdList)}</div>
                        </Col>
                        <Col xs={24} md={8}>
                            <div className='detail-title'> {t("Kasa Özellikleri")} </div>
                            <div>{transport?.trailerFeatureName}</div>
                        </Col>
                    </Row>
                </div>
            </div>
        )
    }

    const Documents = () => {
        if(documentsRequestList.length > 0){
            return(
                documentsRequestList.map((data) => (
                    <List.Item
                        style={{display: "flex"}}
                    >
                        <List.Item.Meta
                            title={t(data.fileName)}
                        />
                        <Button
                            type="primary"
                            size="small"
                            onClick={() => showModalDocument(data.id,data.filetype)}
                        >
                            {t("Görüntüle")}
                        </Button>
                    </List.Item>
                ))
            );
           
        }else{
            return (
                <Alert message={t("Görüntülenecek belge bulunamadı!")} type="info" showIcon />
            )
        }
    }
    const MyDocuments = () => {
        if(myDocumentsRequestList.length > 0){
            return(
                myDocumentsRequestList.map((data) => (
                    <List.Item
                        style={{display: "flex"}}
                    >
                        <List.Item.Meta
                            title={t(data.fileName)}
                        />
                        <Button
                            type="primary"
                            size="small"
                            onClick={() => showModalDocument(data.id,data.filetype)}
                        >
                            {t("Görüntüle")}
                        </Button>
                    </List.Item>
                ))
            );
           
        }else{
            return (
                <Alert message={t("Görüntülenecek belge bulunamadı!")} type="info" showIcon />
            )
        }
    }

    return (
        <>

        <Modal
            title={t("Belge Talep Et")}
            visible={visible3}
            centered
           
            keyboard={true}
            onCancel={handleCancel3}
            width={700}
        >
            {
                pdf === 0 &&
                <div style={{display: "flex", justifyContent: "center"}}>
                    <Image width={500} src={documentUrl} />
                </div>
            }
            {
                pdf === 1 &&
                
                <iframe
                src={documentUrl}
                width="100%"
                height="500"
                title="PDF Preview"
                />
            }
           
        </Modal>

            <div className='general-knowledges'>
                <Tabs defaultActiveKey="1">
                    <TabPane tab={t("Genel Bilgiler")} key="1">
                        <GeneralInformation />
                    </TabPane>
                    <TabPane tab={t("Yük Detayları")} key="2">
                        {
                            transport?.clientAdvertisementType === "FTL" &&
                            transport?.vehicleOrContainer === "Kara Yolu" &&
                             <DetailOfLoadFtlArac />
                        }
                        {
                            transport?.clientAdvertisementType === "FTL" &&
                            transport?.vehicleOrContainer !== "Kara Yolu" &&
                             <DetailOfLoadFtlKonteynerParsiyelArac />
                        }
                        {
                            transport?.clientAdvertisementType === "PARSİYEL" &&
                            transport?.vehicleOrContainer === "Kara Yolu" &&
                             <DetailOfLoadFtlKonteynerParsiyelArac />
                        }
                        {
                            transport?.clientAdvertisementType === "PARSİYEL" &&
                            transport?.vehicleOrContainer !== "Kara Yolu" &&
                             <DetailOfLoadParsiyelKonteyner />
                        }
                    </TabPane>
                    {
                        transport?.clientAdvertisementType === "FTL" &&
                            transport?.vehicleOrContainer === "Kara Yolu" &&
                        <TabPane tab={t("Araç Detayları")} key="3">
                            <DetailOfVehicle />
                        </TabPane>
                    }
                    
                    {
                        !AdvertiseAgain &&
                        <>
                            <TabPane tab={t("İstenen Belgeler")} key="5">
                                <Documents />
                                {documentsRequestList.length === 0 &&
                                <div><br/><br/><br/><br/><br/><br/><br/></div>
                                }
                                <br/>
                                <br/>
                                {transport?.advertisementStatus === "APPROVED" &&
                                    <DocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "PAYMENT_SUCCESSFUL" &&
                                    <DocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "WAITING_FOR_TRANSPORT" &&
                                    <DocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "TRANSPORT" &&
                                    <DocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "COMPLETED" &&
                                    <DocumentsAbroadRequest />
                                }
                            </TabPane>
                            <TabPane tab={t("Yüklenen Belgeler")} key="6">
                                <MyDocuments />
                                {documentsRequestList.length === 0 &&
                                <div><br/><br/><br/><br/><br/><br/><br/></div>
                                }
                                <br/>
                                <br/>
                                {transport?.advertisementStatus === "APPROVED" &&
                                    <MyDocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "PAYMENT_SUCCESSFUL" &&
                                    <MyDocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "WAITING_FOR_TRANSPORT" &&
                                    <MyDocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "TRANSPORT" &&
                                    <MyDocumentsAbroadRequest />
                                }
                                {transport?.advertisementStatus === "COMPLETED" &&
                                    <MyDocumentsAbroadRequest />
                                }
                            </TabPane>
                        </>
                    }
                </Tabs>
            </div>
            <Modal
                visible={visible4}
                onCancel={() => setVisible4(false)}
                footer={null}
            >
                {
                    transpotInfo?.id !== null?
                        <>
                            {transpotInfo?.plate&&           <h3>{t("Araç Plakası: ") + transpotInfo.plate}</h3>}
                            {transpotInfo?.trailerPlate&&    <h3>{t("Dorse Plakası: ") + transpotInfo.trailerPlate}</h3>}
                            {transpotInfo?.name&&            <h3>{t("Şoför Adı Soyadı: ") + transpotInfo.name}</h3>}
                            {transpotInfo?.tc&&              <h3>{t("Şoför Tc: ") + transpotInfo.tc}</h3>}
                            {transpotInfo?.tel&&             <h3>{t("Şoför Tel: ") + transpotInfo.tel}</h3>}
                            {transpotInfo?.awb&&             <h3>{t("Awb No: ") + transpotInfo.awb}</h3>}
                            {transpotInfo?.airlane&&         <h3>{t("Airlane: ") + transpotInfo.airlane}</h3>}
                            {transpotInfo?.orginAirport&&    <h3>{t("Orgin Ariport: ") + transpotInfo.orginAirport}</h3>}
                            {transpotInfo?.destination&&     <h3>{t("Destination: ") + transpotInfo.destination}</h3>}
                            {transpotInfo?.ambar&&           <h3>{t("Ambar: ") + transpotInfo.ambar}</h3>}
                            {transpotInfo?.bayrak&&          <h3>{t("Bayrak: ") + transpotInfo.bayrak}</h3>}
                            {transpotInfo?.opsiyon&&         <h3>{t("Opsiyon: ") + transpotInfo.opsiyon}</h3>}
                            {transpotInfo?.iskele&&          <h3>{"2." + t("İskele: ") + transpotInfo.iskele}</h3>}
                            {transpotInfo?.booking&&         <h3>{t("Booking No: ") + transpotInfo.booking}</h3>}
                            {transpotInfo?.seal&&            <h3>{t("Mühür No: ") + transpotInfo.seal}</h3>}
                            {transpotInfo?.cotainer&&        <h3>{t("Konteyner No: ") + transpotInfo.cotainer}</h3>}
                        </>
                        
                        :
                        <p>{t("Henüz taşıma bilgisi girilmedi")}</p>
                }
            </Modal>
        </>
    );
};

export default React.memo(DetailInfoAbroad);