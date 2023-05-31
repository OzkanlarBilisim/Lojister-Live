import { Button, Col, Divider, Row } from 'antd';
import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom';
import { getMyAdvertisementBids } from '../../api/controllers/client-advertisiment-bid-controller';
import { getTransportById } from '../../api/controllers/client-advertisiment-controller';
import DetailInfo from '../../components/DetailInfo';
import DetailAlert from '../../components/DetailAlert';
import DetailBids from '../../components/DetailBids';
import DetailSteps from '../../components/DetailSteps';
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline'
import { getDocumentsByTransportProcessIdRequest, getTransportProcessByTransportIdRequest } from '../../api/controllers/client-transport-process-controller';
import RequestError from '../../components/RequestError';
import SupportBanner from '../../components/SupportBanner';
import { useTranslation } from 'react-i18next';

const TransportDetail = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("İlan Detayları")

    const { transportId } = useParams();
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState(true);
    const [transport, setTransport] = useState({});
    const [bids, setBids] = useState([]);
    const [transportProcess, setTransportProcess] = useState({});
    const [transportDocuments, setTransportDocuments] = useState([]);

    //get transport
    const fetchTransport = async () => {
        setIsLoading(true)
        try {
            let res = await getTransportById(transportId);
            if (res) {
                setTransport(res.data);
                fetchBids(res.data.advertisementStatus);
                fetchTransportProcess();
            }
        } catch (error) {
            RequestError(error);
        }
    };

    //get bids
    const fetchBids = async (advertisementStatus) => {
        try {
            let res = await getMyAdvertisementBids(transportId);
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
            let res = await getTransportProcessByTransportIdRequest(transportId);
            if (res) {
                setTransportProcess(res.data);
                fetchTransportDocuments(res.data.id)
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
                        <DetailSteps
                            data={transport}
                            transportDocuments={transportDocuments}
                            status={transport?.advertisementProcessStatus}
                        />
                    </Col>
                    <Col style={{ width: "100%" }} sm={24} md={24} lg={24} xl={12}>
                        <DetailAlert
                            data={transport}
                            transport={transport}
                            updatePage={fetchTransport}
                            transportProcess={transportProcess}
                            transportDocuments={transportDocuments}
                            approvedBid={transportProcess?.acceptedClientAdvertisementBid}
                            status={transport?.advertisementProcessStatus}
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

export default TransportDetail;