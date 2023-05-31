import { Button, Col, Divider, Row } from 'antd';
import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom';
import { getMyAdvertisementBidsAbroud } from '../../api/controllers/client-advertisiment-bid-controller';
import { getAllAdvertisementYurtdisiDetay } from '../../api/controllers/client-advertisiment-controller';
import DetailInfoAbroad from '../../components/DetailInfoAbroad';
import DetailAlertAbroad from '../../components/DetailAlertAbroad';
import DetailBidsAbroud from '../../components/DetailBidsAbroud';
import DetailStepsAbroud from '../../components/DetailStepsAbroud';
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline'
import { getDocumentsByTransportProcessIdRequest, getAbroudBidPrice } from '../../api/controllers/client-transport-process-controller';
import RequestError from '../../components/RequestError';
import SupportBanner from '../../components/SupportBanner';
import { useTranslation } from 'react-i18next';
import { BASE_URL_FRONTEND } from '../../api/ApiProvider';

const TransportDetailAbroud = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("İlan Detayları")

    const { transportId } = useParams();
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState(true);
    const [transport, setTransport] = useState({});
    const [bids, setBids] = useState([]);
    const [transportProcess, setTransportProcess] = useState({});
    const [transportDocuments, setTransportDocuments] = useState([]);
    const currentUser = useSelector((state) => state.user);


    const permission = (advert) =>{
        if(currentUser?.role === "ROLE_CLIENT"){
            if(parseInt(advert?.client_id) !== currentUser?.id){
                window.location = BASE_URL_FRONTEND
            }
        }else{
            window.location = BASE_URL_FRONTEND
        }
    }


    //get transport
    const fetchTransport = async () => {
        setIsLoading(true)
        try {
            let res = await getAllAdvertisementYurtdisiDetay(transportId);
            if (res) {
                setTransport(res.data);
                console.log(res.data);
                fetchBids(res.data.advertisementStatus);
                fetchTransportProcess();
                permission(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    //get bids
    const fetchBids = async (advertisementStatus) => {
        try {
            let res = await getMyAdvertisementBidsAbroud(transportId);
            if (res) {
                setBids(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        if (advertisementStatus !== "TRANSPORT") {
            setIsLoading(false);
        }
    }

    //get transport process
    const fetchTransportProcess = async () => {
        try {
            let res = await getAbroudBidPrice(transportId);
            if (res) {
                if(res.data.length > 0){
                    for(var i=0; i<res.data.length; i++){
                        switch(res.data[i].status){
                            case "DENIED": 
                              break;
                            default:
                                setTransportProcess(res.data[i]);
                                fetchTransportDocuments(res.data[i].id);
                                break;
                        }
                    }
                }
            }
        } catch (error) {
            RequestError(error);
        }
    }

    //get transport documents
    const fetchTransportDocuments = async (id) => {
        try {
            let res = await getDocumentsByTransportProcessIdRequest(id);
            if (res) {
                setTransportDocuments(res.data);
                console.log(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchTransport();
    }, [])

    return (
        isLoading ?
            <SuspenseFallbackInline /> :
            <div className="layout-content-padding">
                <div style={{ display: "flex" }}>
                    <Button
                        disabled={isLoading}
                        onClick={() => navigate(-1)}
                        type="primary"
                        icon={<i style={{ marginRight: ".5rem" }} className="bi bi-arrow-left-circle-fill"></i>}
                    >{t("Geri dön")}</Button>
                </div>

                <Divider />

                <Row gutter={[24, 24]} >
                    <Col style={{ width: "100%" }} sm={24} md={24} lg={24} xl={12} >
                        <DetailStepsAbroud
                            data={transport}
                            transportDocuments={transportDocuments}
                            status={transport?.advertisementStatus}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} md={24} lg={24} xl={12}>
                        <DetailAlertAbroad
                            data={transport}
                            transport={transport}
                            updatePage={fetchTransport}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                            approvedBid={transportProcess?.bid}
                            status={transport?.advertisementStatus}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} lg={12}>
                        <DetailInfoAbroad
                            transport={transport}
                            transportId={transportId}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} lg={12}>
                        <DetailBidsAbroud
                            transportId={transportId}
                            user='client'
                            data={bids}
                            updatePage={fetchTransport}
                        />
                    </Col>
                </Row>
                <SupportBanner />
            </div>
    )
}

export default TransportDetailAbroud;