import { Table, Button, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react'
// import AntNotification from '../../components/AntNotification';
import RequestError from '../../components/RequestError';
import { CustomButton } from '../../components/CustomButton';
import { useTranslation } from 'react-i18next';
import moment from 'moment';
import {getAdvertPaymetController, getReceipt, approveOrDeleteReceipt} from '../../api/controllers/payment-controller-receipt';
import {getTodayForeignCurrencyUsd} from '../../api/controllers/foreign-currency-api';
import {getAllAdvertisementYurtdisiDetay } from '../../api/controllers/client-advertisiment-controller';

import { CustomModal } from '../../components/CustomModal';
import DetailInfoAbroad from '../../components/DetailInfoAbroad';

const PaymentControle = () => {
    const [isReceipt ,setIsReceipt] = useState(false);
    const [controleList, setControleList] = useState();
    const [showModel, setShowModel] = useState(false);
    const [showModel2, setShowModel2] = useState(false);
    const [advertDetail, setAdvertDetail] = useState();
    const [receiptID, setReceiptID] = useState();
    const [documentUrl, setDocumentUrl] = useState();
    const [usd, setUsd] = useState(); 

    const updatePage = () =>{
      getAdvertPaymetControl();
      setShowModel2(false);
    }
    const getAdvertPaymetControl = async () =>{
      try{
        let res = await getAdvertPaymetController(isReceipt);
        if(res){
          setControleList(res.data);
        }
      }catch(Error){
        RequestError(Error);
      }
    }
    const getTodayForeignCurrencyx = async () =>{
      try{
        let res = await getTodayForeignCurrencyUsd();
        if(res){
          setUsd(res.data);
        }
      }catch(Error){
        RequestError(Error);
      }
    }

    const detail = async (id) => {
      setShowModel(true);
      try {
          let res = await getAllAdvertisementYurtdisiDetay(id);
          if (res) {
              setAdvertDetail(res.data);
          }
      } catch (error) {
          RequestError(error);
      }
  }
    const approveOrDeleteReceiptx = async (deleteOrAproved) => {
      try {
          let res = await approveOrDeleteReceipt(receiptID,deleteOrAproved);
          if (res) {
            updatePage();
          }
      } catch (error) {
          RequestError(error);
      }
  }
    const getReceiptx = async (id) => {
      setReceiptID(id);
      setShowModel2(true);
      try {
          let res = await getReceipt(id);
          if (res) {
            const blob = new Blob([res?.data], {type: 'application/pdf'});
            const url = URL.createObjectURL(blob);
            setDocumentUrl(url);
            //console.log(URL.createObjectURL(res?.data));
          }
      } catch (error) {
          RequestError(error);
      }
  }
  const DetailsOption = ({advertID, id}) =>{
    return(
      <div style={{display: "flex"}}>
        <Button onClick={() => detail(advertID)}>Detaylar</Button>
        {isReceipt&&
          <Button onClick={() => getReceiptx(id)}>Dekont</Button>
        }
      </div>
    )
  }

  useEffect(() => {
    getAdvertPaymetControl();
  }, [isReceipt]);
  
  useEffect(() => {
    getTodayForeignCurrencyx();
  }, []);

    const { t } = useTranslation("common");
    const columns = [
        {
          title: 'Ad Soyad',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: 'Tel No',
          dataIndex: 'tel',
          key: 'tel',
        },
        {
          title: 'Mail Adresi',
          dataIndex: 'mail',
          key: 'mail',
        },
        {
          title: 'İlan Yayın',
          dataIndex: 'date',
          key: 'date',
        },
        {
          title: 'Ödenecek Turtar',
          dataIndex: 'money',
          key: 'money',
        },
        {
          title: 'Daha Fazla',
          dataIndex: 'button',
          key: 'button',
        },
      ];
      const dataSource = controleList?.map(val => {
        let price;
        if(!isReceipt){
          price = val?.price * usd;
          price = price.toFixed(2);
        }else{
          price = val?.price * 1;
          price = price.toFixed(2);
        }

        
        return { 
          key: '1',
          name: val?.companyName,
          tel: <a href={'tel:'+val?.tel}>{val?.tel}</a>,
          mail: <a href={"mailto:"+val?.mail}>{val?.mail}</a>,
          date: moment(val?.date).format("YYYY-MM-DD"),
          money: price +'₺',
          button: <DetailsOption advertID={val?.advertID} id={val?.id}/>,
            };
      });
      
      const pdfFooter = () =>{
        return(
          <>
            <div style={{display: "flex", justifyContent:  "end"}}>
              <Popconfirm popupVisible={true} placement="bottomRight" title={t("Dekontu silmek İstiyorum?")} onConfirm={() => approveOrDeleteReceiptx(false)}
                okText={t("Evet")}
                cancelText={t("İptal")}
              >
                <Button danger>Dekontu Sil</Button>
              </Popconfirm>
              <Popconfirm popupVisible={true} placement="bottomRight" title={t("Ödemenin alındığını onaylayorum?")} onConfirm={() => approveOrDeleteReceiptx(true)}
                okText={t("Evet")}
                cancelText={t("Hayır")}
              >
                <Button type="primary">Ödemeyi Onayla</Button>
              </Popconfirm>
            </div>
          </>
        )
      }
      const pdf = () => {
        return(
          <>
            <br/>
            <iframe
              src={documentUrl}
              width="100%"
              height="500"
              title="PDF Preview"
            />
            <br/>
          </>
        )
      }
      const content = () =>{
        return(
          <>
          <br/>
            <DetailInfoAbroad
                transport={advertDetail}
                transportId={advertDetail?.id}
                AdvertiseAgain={false}
            />  
            <br/>
          </>      
        )
      }  

    return (
        <>
            <div style={{ display: "flex" , marginBottom: "20px"}}>
                <CustomButton
                    onClick={() => setIsReceipt(false)}
                    color={!isReceipt ? "primary" : "light"}
                >
                    {t("Dekonsuz İşlemler")}
                </CustomButton>
                <CustomButton
                    onClick={() => setIsReceipt(true)}
                    color={isReceipt ? "primary" : "light"}
                >
                    {t("Dekont Yüklenen")}
                </CustomButton>
            </div>

            {
                !isReceipt&&
                <div style={{width: "fit-contante"}}>
                    <Table scroll={{ x: true }} dataSource={dataSource} columns={columns} />;
                </div>
            }
            {
                isReceipt&&
                <div style={{width: "fit-contante"}}>
                    <Table scroll={{ x: true }} dataSource={dataSource} columns={columns} />;
                </div>
            }
            <CustomModal
                footer={null}
                visible={showModel}
                onCancel={() => {setShowModel(false)}}
                onOk={null}
                content={content()}
                width="60%"
            />
            <CustomModal
                footer={pdfFooter()}
                visible={showModel2}
                onCancel={() => {setShowModel2(false)}}
                onOk={null}
                content={pdf()}
                width="60%"
            />
        </>
    )
}

export default PaymentControle;