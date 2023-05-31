import { Button, Col, Divider, Row } from 'antd';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import { getClientAdvertisimentBidByIdRequest } from '../../api/controllers/client-advertisiment-bid-controller';
import DetailInfo from '../../components/DetailInfo';
import DetailAlert from '../../components/DetailAlert';
import DetailBids from '../../components/DetailBids';
import DetailSteps from '../../components/DetailSteps';
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline';
import { getByClientAdvertisementBidId, getDocumentsByTransportProcessIdRequest } from '../../api/controllers/client-transport-process-controller';
import RequestError from '../../components/RequestError';
import { getTransportById } from '../../api/controllers/client-advertisiment-controller';
import SupportBanner from '../../components/SupportBanner';
import { useTranslation } from 'react-i18next';

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

    //get bid
    const fetchBid = async () => {
        setIsLoading(true);
        try {
            let res = await getClientAdvertisimentBidByIdRequest(offerId);
            if (res) {
                setBid(res.data);
                fetchTransportProcess(res.data.id);
                fetchTransport(res.data)
                if (res.data.bidStatus === "APPROVED" || res.data.bidStatus === "TRANSPORT") {
                    setStatus(res.data.clientAdvertisement.advertisementProcessStatus);
                } else {
                    setStatus(res.data.bidStatus);
                }
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const fetchTransport = async (bid) => {
        try {
            let res = await getTransportById(bid.clientAdvertisement.id)
            if (res) {
                setTransport(res.data)
                if (bid.bidStatus === "APPROVED" || bid.bidStatus === "TRANSPORT") {
                    setStatus(res.data.advertisementProcessStatus)
                } else {
                    setStatus(bid.bidStatus)
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
                        <DetailSteps
                            data={bid}
                            status={status}
                            isLoading={isLoading}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} md={24} lg={24} xl={12}>
                        <DetailAlert
                            data={bid}
                            transport={transport}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                            approvedBid={transportProcess?.acceptedClientAdvertisementBid}
                            updatePage={fetchBid}
                            status={status}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} lg={12}>
                        <DetailInfo
                            transport={transport}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} lg={12}>
                        <DetailBids
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