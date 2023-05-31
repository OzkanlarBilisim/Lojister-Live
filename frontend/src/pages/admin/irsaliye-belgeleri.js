import { Alert, Button, Col, Modal, Row, Table } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react'
import { createWaybillStatusRequest, getWaitingWaybillRequest, getWaybillByIdRequest } from '../../api/controllers/way-bill-controller';
import RequestError from '../../components/RequestError';

const AdminIrsaliyeBelgeleri = () => {
    const [waybills, setWaybills] = useState([]);
    const [transportProcessId, setTransportProcessId] = useState(null);
    const [transport, setTransport] = useState({});
    const [vehicle, setVehicle] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    const fetchWaybills = async () => {
        setIsLoading(true);
        try {
            let res = await getWaitingWaybillRequest();
            if (res) {
                setWaybills(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchWaybills();
    }, [])

    const openModal = (clientTransportProcess) => {
        setIsVisible(true);
        setTransport(clientTransportProcess?.acceptedClientAdvertisementBid?.clientAdvertisement);
        setVehicle(clientTransportProcess?.vehicle)
    }

    const closeModal = () => {
        setIsVisible(false);
        setTransport({});
        setVehicle({});
        setTransportProcessId(null)
    }

    const openDocument = async (documentId) => {
        setIsLoading(true);
        try {
            let res = await getWaybillByIdRequest(documentId);
            if (res) {
                let file_window = window.open()
                let fileURL = `data:${res.data.type};base64,${res.data.data}`
                file_window.document.write(`<img src="${fileURL}" alt="" />`)
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    const handleUpdateStatus = async (data) => {
        try {
            let res = await createWaybillStatusRequest(data);
            if (res) {
                closeModal();
                fetchWaybills();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: '1',
        },
        {
            title: 'İlan',
            align: 'center',
            dataIndex: 'clientTransportProcess',
            key: '2',
            render: e => <Button onClick={() => openModal(e)}>İlanı görüntüle</Button>
        },
        {
            title: 'İrsaliye Belgesi',
            align: 'center',
            dataIndex: 'id',
            key: '3',
            render: e => <Button onClick={() => openDocument(e)}>Belgeyi görüntüle</Button>
        },
        {
            title: '',
            align: 'center',
            dataIndex: 'clientTransportProcess',
            key: '4',
            render: e =>
                <div style={{ display: "flex", gap: ".5rem", justifyContent: "center" }}>
                    <Button onClick={() => handleUpdateStatus({ transportProcessId: e?.id, waybillStatus: "APPROVED" })} type="primary">Onayla</Button>
                    <Button onClick={() => handleUpdateStatus({ transportProcessId: e?.id, waybillStatus: "DENIED" })} danger>Reddet</Button>
                </div>
        },
    ];

    return (
        <>
            <Table
                columns={columns}
                dataSource={waybills}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />
            <Modal
                width={800}
                title="İlanı inceleyin"
                centered
                visible={isVisible}
                footer={null}
                onCancel={() => closeModal()}
            >
                <Row gutter={[24, 24]}>
                    <Col xs={24} md={12}>
                        <Row gutter={[12, 12]}>
                            <Col xs={24} md={12}>
                                <div className='detail-title'>İlan Kodu</div>
                                <div>{transport?.clientAdvertisementCode ? transport?.clientAdvertisementCode : "-"}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='detail-title'>İlan Tarihi</div>
                                <div>{transport?.createdDateTime ? moment(transport?.createdDateTime).format("LLL") : "-"}</div>
                            </Col>
                            {
                                transport?.clientAdvertisementType === "CONTAINER" &&
                                <Col xs={24} md={12}>
                                    <div className='detail-title'>Ticaret Türü</div>
                                    <div>{transport?.tradeType === "EXPORT" ? "İhracat" : transport?.tradeType === "IMPORT" ? "İthalat" : "-"}</div>
                                </Col>
                            }
                        </Row>
                    </Col>
                    <Col xs={24} md={12}>
                        <Row gutter={[12, 12]}>
                            <Alert style={{ width: "100%" }} type="info" message={<p style={{ fontWeight: 500 }}>Yükleme Bilgileri <span style={{ fontWeight: 300 }}>| {moment(transport?.adStartingDate).format("LL")} {transport?.adStartingTime?.substring(0, 5)}</span></p>} />
                            <Col xs={24} md={12}>
                                <div className='detail-title'>Şirket Ünvanı</div>
                                <div>{transport?.startRecipient?.commercialTitle ? transport?.startRecipient?.commercialTitle : "-"}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='detail-title'>Yetkili Adı Soyadı</div>
                                <div className='detail-address'>
                                    <div>{transport?.startRecipient?.firstName}</div>
                                    <div className='detail-space'></div>
                                    <div>{transport?.startRecipient?.lastName}</div>
                                </div>
                            </Col>
                            <Col xs={24}>
                                <div className='detail-title'>Adres</div>
                                <p>{transport?.startingAddress?.fullAddress + " " + transport?.startingAddress?.neighborhood + " - " + transport?.startingAddress?.district + "/" + transport?.startingAddress?.province}</p>
                            </Col>
                        </Row>
                    </Col>
                    <Col xs={24} md={12}>
                        <Row gutter={[12, 12]}>
                            <Alert style={{ width: "100%" }} type="info" message={<p style={{ fontWeight: 500 }}>İndirme Bilgileri <span style={{ fontWeight: 300 }}>| {moment(transport?.adDueDate).format("LL")} {transport?.adDueTime?.substring(0, 5)}</span></p>} />
                            <Col xs={24} md={12}>
                                <div className='detail-title'>Şirket Ünvanı</div>
                                <div>{transport?.dueRecipient?.commercialTitle ? transport?.dueRecipient?.commercialTitle : "-"}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='detail-title'>Yetkili Adı Soyadı</div>
                                <div className='detail-address'>
                                    <div>{transport?.dueRecipient?.firstName}</div>
                                    <div className='detail-space'></div>
                                    <div>{transport?.dueRecipient?.lastName}</div>
                                </div>
                            </Col>
                            <Col xs={24}>
                                <div className='detail-title'>Adres</div>
                                <p>{transport?.dueAddress?.fullAddress + " " + transport?.dueAddress?.neighborhood + " - " + transport?.dueAddress?.district + "/" + transport?.dueAddress?.province}</p>
                            </Col>
                        </Row>
                    </Col>
                </Row>
            </Modal >
        </>
    )
}

export default AdminIrsaliyeBelgeleri;