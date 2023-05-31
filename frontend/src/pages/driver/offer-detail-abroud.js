import { Button, Col, Divider, Row } from 'antd';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { getClientAdvertisimentBidByIdRequest } from '../../api/controllers/client-advertisiment-bid-controller';
import DetailInfoAbroud from '../../components/DetailInfoAbroad';
import DetailAlertAbroud from '../../components/DetailAlertAbroad';
import DetailBidsAbroud from '../../components/DetailBidsAbroud';
import DetailStepsAbroud from '../../components/DetailStepsAbroud';
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline';
import { getByClientAdvertisementBidId, getDocumentsByTransportProcessIdRequest, getFindMyBid } from '../../api/controllers/client-transport-process-controller';
import RequestError from '../../components/RequestError';
import { getTransportByIdAbroud } from '../../api/controllers/client-advertisiment-controller';
import SupportBanner from '../../components/SupportBanner';
import { useTranslation } from 'react-i18next';
import {useSelector } from 'react-redux';


const TransportDetail = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Teklif Detayları")

    const { offerId } = useParams();
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState(true);
    const [bid, setBid] = useState([]);
    const [transport, setTransport] = useState({});
    const [transportProcess, setTransportProcess] = useState({});
    const [transportDocuments, setTransportDocuments] = useState([]);
    const [status, setStatus] = useState("");
    const currentUser = useSelector((state) => state.user);

    //get bid
    const fetchBid = async () => {
        setIsLoading(true);
        try {
            let res = await getFindMyBid(offerId, currentUser.companyId);
            if (res) {;
                setBid(res.data);
                fetchTransportProcess(res.data.id);
                fetchTransport(res.data)
                if (res.data.status === "APPROVED" || res.data.status === "TRANSPORT") {
                    setStatus(res.data.clientAdvertisement.advertisementProcessStatus);
                } else {
                    setStatus(res.data.status);
                }
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const fetchTransport = async () => {
        try {
            let res = await getTransportByIdAbroud(offerId)
            if (res) {
                setTransport(res.data);

                if (res.data.advertisementStatus === "APPROVED" || res.data.advertisementStatus === "WAITING" || res.data.advertisementStatus === "DENIED" ) {
                    setStatus(res.data.advertisementStatus);
                } else {
                    setStatus(res.data.advertisementStatus2)
                }
               

            }
        } catch (error) {
            RequestError(error);
        }
    }

    //get transport process
    const fetchTransportProcess = async (id) => {
        try {
            let res = await getByClientAdvertisementBidId(id);
            if (res) {
                setTransportProcess(res.data);
                fetchTransportDocuments(res.data.id);
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
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchBid();
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
                            status={status}
                            isLoading={isLoading}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} md={24} lg={24} xl={12}>
                        <DetailAlertAbroud
                            data={transport}
                            transport={transport}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                            approvedBid={transportProcess?.acceptedClientAdvertisementBid}
                            updatePage={fetchBid}
                            status={status}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} lg={12}>
                        <DetailInfoAbroud
                            transport={transport}
                            transportId={transport.id}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} lg={12}>
                        <DetailBidsAbroud
                            user={'driver'}
                            data={bid}
                        />
                    </Col>
                </Row>
                <SupportBanner />
            </div>
    )
}

export default TransportDetail;