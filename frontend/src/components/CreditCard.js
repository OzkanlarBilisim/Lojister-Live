import { Input, Select, Form, Row, Col, Button, Checkbox, Modal,Card } from 'antd';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import Sozlesmeler from './Sozlesmeler';
import AntNotification from './AntNotification';
import RequestError from './RequestError';
import { AdvertPayController,CardInfos ,sendSelectedCard} from '../api/controllers/payment-controller';
import { useEffect } from 'react';



const CreditCard = ({creditCardFormValue,  transportProcess, setWitchStep, abroudorDomestic,transportId}) => {

  const { t } = useTranslation("common");
  const dayList = Array.from({ length: 12 }, (_, index) => String(index + 1).padStart(2, '0'));
  const [cardSave, setCardSave] = useState(creditCardFormValue.registery);
  const [isLoading, setIsLoading] = useState(false);
  const [contract , setContract] = useState(false);
  const [showcardInfos, setShowCardInfos] = useState([]);
  const [selectedCardId, setSelectedCardId]= useState(null);
  const [selectedButton, setSelectedButton] = useState('Kart Bilgisi Gir');

  let yearList = [];
  let currentYear = new Date().getFullYear();
  for (let i = currentYear; i < currentYear + 10; i++) {
    yearList.push(i);
  }
  currentYear = String(currentYear);

  const SelectDay = (value) => {
    creditCardFormValue.expDates.expMonth = value;
  };

  const SelectYear = (value) => {
    creditCardFormValue.expDates.expYear = value;
  };

  const CardNumber = (e) => {
    let cardNumber = e.replace(/\D/g, "");
    cardNumber = cardNumber.replace(/(\d{4})/g, "$1 ");
    cardNumber = cardNumber.substring(0, 19);
    document.getElementById("cardNumber").value = cardNumber;
    creditCardFormValue.cardNumber = cardNumber;
  };

  const IsNumber = (e, id) => {
    let number = e.replace(/\D/g, "");
    document.getElementById(id).value = number;
    creditCardFormValue.cvcNumber = number;
  };

  const AddName = (e) => {
    creditCardFormValue.cardHolderFullName = e;
  };

  const AddCardName = (e) => {
    creditCardFormValue.cardName = e;
  };
  const handleCardClick = (paymentId) => {
    setSelectedCardId(paymentId);
 
 
  };

 

  useEffect(() => {
    async function fetchCardInfos() {
      try {
        const response = await CardInfos();
        if (response && response.data) {
          setShowCardInfos(response.data);
          console.log (response.data)
        } else {
          throw new Error('Kart bilgileri alınamadı.');
        }
      } catch (error) {
        console.error('Hata:', error);
      
      }
    }

    fetchCardInfos();
  }, []); 
  const paySabmit = async () => {
    setIsLoading(true)
    try {
        let res = await AdvertPayController(creditCardFormValue);

        if (res) {
          
            let domain = window.location.hostname;
            if(domain === "localhost"){
              AntNotification({ type: "success", message: t("3D ödeme sayfasına yönlendiriliyorsunuz.") })
              window.location = res?.data;
            }else{       
              if(res?.data?.ResultCode === "Success"){
              AntNotification({ type: "success", message: t("3D ödeme sayfasına yönlendiriliyorsunuz.") })
              window.location = res?.data?.Data;
              }else{
                let message = null;
                switch(res?.data?.ResultCode){
                  case "PaymentDealer.CheckCardInfo.InvalidCardInfo":
                    message = "Kart bilgilerinde hata var.";
                    break;
                  case "PaymentDealer.CheckDealerPaymentLimits.DailyCardLimitExceeded":
                    message = "Gün içinde bu kart kullanılarak daha fazla işlem yapılamaz.";
                    break;
                  default:
                    message = "Beklenmeyen bir hata oluştu";
                    break;
                }         
                AntNotification({ type: "error", message: t(message) })
              }    
            }   
        }
    } catch (error) {
        RequestError(error);
    }
    setIsLoading(false)
  }
  const registeredCardPayment = async (selectedCardId) => {
    const selectedCard = {
      abroudorDomestic: abroudorDomestic, 
      advertId: transportId, 
      paymentId: selectedCardId,
    };
    try {
      let res = await sendSelectedCard(selectedCard);
      if (res) {  
        let domain = window.location.hostname;
        if(domain === "localhost"){
          AntNotification({ type: "success", message: t("3D ödeme sayfasına yönlendiriliyorsunuz.") })
          window.location = res?.data;
        }else{       
              if(res?.data?.ResultCode === "Success"){
              AntNotification({ type: "success", message: t("3D ödeme sayfasına yönlendiriliyorsunuz.") })
              window.location = res?.data?.Data;
              }else{
                let message = null;
                switch(res?.data?.ResultCode){
                  case "PaymentDealer.CheckCardInfo.InvalidCardInfo":
                    message = "Kart bilgilerinde hata var.";
                    break;
                  case "PaymentDealer.CheckDealerPaymentLimits.DailyCardLimitExceeded":
                    message = "Gün içinde bu kart kullanılarak daha fazla işlem yapılamaz.";
                    break;
                  default:
                    message = "Beklenmeyen bir hata oluştu";
                    break;
                }         
                AntNotification({ type: "error", message: t(message) })
              }    
            }   
        }
    } catch (error) {
        RequestError(error);
    }
    setIsLoading(false)
  }

  useEffect(() => {
    creditCardFormValue.expDates.expYear = currentYear % 100;
    creditCardFormValue.expDates.expMonth = "01";
  }, []);

    const [showComponent, setShowComponent] = useState(showcardInfos.length === 0);
  
    const handleKartBilgisiGir = () => {
      setSelectedButton('Kart Bilgisi Gir');
      setShowComponent(true);
    };
  
    const handleKayitliKartlarim = () => {
      setSelectedButton('Kayıtlı Kartlarım');
      setShowComponent(false);
    };
    const UnRegisteredCard = () =>{
      return(
        <>
          <Form layout="vertical" onFinish={paySabmit} autoComplete="off" initialValues={creditCardFormValue}>
            <Row gutter={[24, 24]}>
              <Col xs={24} sm={12}>
                <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "24px" }}>
                  <Button type={"primary"} onClick={handleKartBilgisiGir}>
                    Kart Bilgisi Gir
                  </Button>
                  <Button type={"default"} disabled={showcardInfos.length === 0} onClick={handleKayitliKartlarim}>
                    Kayıtlı Kartlarım
                  </Button>
                </div>
                <Form.Item
                  label="Kart Sahibinin Adı Soyadı:"
                  name="cardHolderFullName"
                  rules={[{ required: true, message: "Kart Bilgisi Zorunludur" }]}
                >
                  <Input onInput={(e) => AddName(e.target.value)} placeholder="Adı Soyadı" />
                </Form.Item>
                <Form.Item
                  label="Kart Numaranızı Girin"
                  name="cardNumber"
                  rules={[{ required: true, message: "Kart Bilgisi Zorunludur" }]}
                >
                  <Input onInput={(e) => CardNumber(e.target.value)} id="cardNumber" placeholder="xxxx xxxx xxxx xxxx" maxLength={19} />
                </Form.Item>
                <Form.Item
                  label="Son Kullanma Tarihi:"
                  name="expDates"
                
                >
                  <Select
                    style={{ width: 120, marginRight: 5 }}
                    name="expMonth"
                    defaultValue={creditCardFormValue.expDates.expMonth}
                    onChange={SelectDay}
                    options={dayList.map((day) => ({
                      label: day,
                      value: day,
                    }))}
                  />
                  <Select
                    style={{ width: 120, marginLeft: 5 }}
                    name="expYear"
                    defaultValue={creditCardFormValue.expDates.expYear}
                    onChange={SelectYear}
                    options={yearList.map((year) => ({
                      label: year % 100,
                      value: year,
                    }))}
                  />
                </Form.Item>
                <Form.Item
                  label="CVV Kodu:"
                  name="cvcNumber"
                  rules={[{ required: true, message: "Kart Bilgisi Zorunludur" }]}
                >
                  <Input
                    style={{ width: "55px", textAlign: "center" }}
                    placeholder="XXX"
                    id="cvv"
                    onInput={(e) => IsNumber(e.target.value, "cvv")}
                    maxLength={3}
                  />
                </Form.Item>
              <input type="submit" id="cardInfoSubmit" style={{display: "none"}}/>
              </Col>
              <Col xs={24} sm={12}>
                <>
                  <Form.Item label="Kart Kayıt:" name="registery">
                    <Checkbox
                      checked={cardSave}
                      onChange={(e) => {
                        creditCardFormValue.registery = e.target.checked;
                        setCardSave(e.target.checked);
                      }}
                    >
                      Kart Bilgilerimi Kaydet
                    </Checkbox>
                  </Form.Item>
                  {cardSave && (
                    <Form.Item
                      label="Kart Adı:"
                      name="cardName"
                      rules={[{ required: true, message: "Bilgi Zorunludur" }]}
                    >
                      <Input onInput={(e) => AddCardName(e.target.value)} placeholder="Kartınıza bir isim verin" />
                    </Form.Item>
                  )}
                  <div className="Hlooz">
                      <div className="chevron">
                        <img src="/assets/img/chevron-double-right.svg" alt="" />
                      </div>
                      <div className="text">{t("Sözleşmeleri onaylayın")}</div>
                  </div>
                  <div className="agreements">
                    <Checkbox onChange={(e) => setContract(e.target.checked)} checked={contract}>
                    <b
                      onClick={() =>
                        Modal.info({
                          title: t("Sözleşmeler"),
                          icon: false,
                          content: <Sozlesmeler res={transportProcess} />,
                          okText: t("Okudum"),
                        })
                      }
                    >
                      {t("Ön Bilgilendirme Koşulları")}
                    </b>
                    {localStorage.getItem("currentLanguage") === "TURKISH" ? "nı" : ","}{" "}
                    <b
                      onClick={() =>
                        Modal.info({
                          title: t("Sözleşmeler"),
                          icon: false,
                          content: <Sozlesmeler res={transportProcess} />,
                          okText: t("Okudum"),
                        })
                      }
                    >
                      {t("Mesafeli Satış Sözleşmesi")}
                    </b>
                    {localStorage.getItem("currentLanguage") === "TURKISH" ? "'ni ve" : " and"}{" "}
                    <b
                      onClick={() =>
                        Modal.info({
                          title: t("Sözleşmeler"),
                          icon: false,
                          content: <Sozlesmeler res={transportProcess} />,
                          okText: t("Okudum"),
                        })
                      }
                    >
                      {t("Cayma Hakkı Bildirimi")}
                    </b>
                    {localStorage.getItem("currentLanguage") === "TURKISH" ? "'ni" : ","}{" "}
                    {t("okudum ve onaylıyorum.")}
                  </Checkbox>
                </div>
                <br />
                <div className="actions">
                  <Button onClick={() => setWitchStep(1)}>Geri</Button>
                  <Form.Item>
                    <Button
                      loading={isLoading}
                      disabled={!contract}
                      type="primary"
                      htmlType="submit"
                      onClick={() => {document.getElementById("cardInfoSubmit").click()}}
                    >
                      Ödemeyi Yap
                    </Button>
                  </Form.Item>
                </div>
              </>
          </Col>
        </Row>
      </Form>
    </>
      )
    }
    const RegisteredCard = () =>{
      return(
        <>
          <Row gutter={[24, 24]}>
            <Col xs={24} sm={12}>
              <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "24px" }}>
                <Button type={"default"} onClick={handleKartBilgisiGir}>
                  Kart Bilgisi Gir
                </Button>
                <Button type={"primary"} onClick={handleKayitliKartlarim}>
                  Kayıtlı Kartlarım
                </Button>
              </div>
              {showcardInfos.map((card) => (
                <Card
                  key={card.paymentId}
                  hoverable
                  className={selectedCardId === card.paymentId ? 'selected' : ''}
                  onClick={() => handleCardClick(card.paymentId)}
                  style={{
                    border: selectedCardId === card.paymentId ? '2px solid #3B97D2' : '2px solid #E9E9E9',
                    marginBottom: '2px'
                  }}
                  cover={
                    <div
                      style={{
                        backgroundImage: `url(${
                          card.cardType === 'MasterCard' ? '/assets/img/mc_symbol.svg' :
                            card.cardType === 'Visa' ? '/assets/img/visa_symbol.png' :
                              card.cardType === 'AmericanExpress' ? '/assets/img/amex_symbol.svg' :
                                card.cardType === 'Discover' ? '/assets/img/Discover.jpg' : ''
                        })`,
                        backgroundSize: '20% auto',
                        backgroundRepeat: 'no-repeat',
                        backgroundPosition: 'center',
                        position: 'absolute',
                        bottom: '0',
                        left: '80%',
                        transform: 'translateX(-50%)',
                        width: '100%',
                        height: '100px',
                      }}
                    />
                  }
                >
                  <p>{card.cardName}</p>
                  <p>{card.maskedCardNumber}</p>
                </Card>
              ))}
            </Col>
            <Col xs={24} sm={12}>
              <div className="Hlooz">
                <img src="/assets/img/chevron-double-right.svg" alt="" />
                <div className="text">{t("Sözleşmeleri onaylayın")}</div>
              </div>
              <div className="aggreements">
                <Checkbox onChange={(e) => setContract(e.target.checked)} checked={contract}>
                  <b
                    onClick={() =>
                      Modal.info({
                        title: t("Sözleşmeler"),
                        icon: false,
                        content: <Sozlesmeler res={transportProcess} />,
                        okText: t("Okudum"),
                      })
                    }
                  >
                    {t("Ön Bilgilendirme Koşulları")}
                  </b>
                  {localStorage.getItem("currentLanguage") === "TURKISH" ? "nı" : ","}{" "}
                  <b
                    onClick={() =>
                      Modal.info({
                        title: t("Sözleşmeler"),
                        icon: false,
                        content: <Sozlesmeler res={transportProcess} />,
                        okText: t("Okudum"),
                      })
                    }
                  >
                    {t("Mesafeli Satış Sözleşmesi")}
                  </b>
                  {localStorage.getItem("currentLanguage") === "TURKISH" ? "'ni ve" : " and"}{" "}
                  <b
                    onClick={() =>
                      Modal.info({
                        title: t("Sözleşmeler"),
                        icon: false,
                        content: <Sozlesmeler res={transportProcess} />,
                        okText: t("Okudum"),
                      })
                    }
                  >
                    {t("Cayma Hakkı Bildirimi")}
                  </b>
                  {localStorage.getItem("currentLanguage") === "TURKISH" ? "'ni" : ","}{" "}
                  {t("okudum ve onaylıyorum.")}
                </Checkbox>
              </div>
              <br />
              <div style={{ display: "flex", justifyContent: "end" }}>
                <Button onClick={() => setWitchStep(1)}>Geri</Button>
                <Form.Item>
                  <Button
                    loading={isLoading}
                    disabled={!contract || selectedCardId === null}
                    type="primary"
                    htmlType="submit"
                    onClick={() => registeredCardPayment(selectedCardId)}
                  >
                    Ödemeyi Yap
                  </Button>
                </Form.Item>
              </div>
            </Col>
          </Row>
        </>
      );
    }
   
    return (
      <>
          {showComponent && 
            <UnRegisteredCard/>
          }
          {!showComponent&&
            <RegisteredCard/>
          }
      </>
    );
    

      
    
}
export default CreditCard;