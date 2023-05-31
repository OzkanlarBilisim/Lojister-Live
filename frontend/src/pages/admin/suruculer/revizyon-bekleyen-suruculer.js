import { Button, Col, Divider, Modal, Row, Table, Input } from 'antd';
import React, { useEffect, useState } from 'react'
import { getDriverRevisionStatusRequest, updateDriverStatusRequest } from '../../../api/controllers/driver-controller';
import { getDriverDocumentBase64Request } from '../../../api/controllers/driver-document-file-controller';
import RequestError from '../../../components/RequestError';
import SuspenseFallbackInline from '../../../components/SuspenseFallbackInline';

const AdminRevizyonBekleyenSuruculer = () => {
    const [drivers, setDrivers] = useState([]);
    const [selectedDriver, setSelectedDriver] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [statusDescription, setStatusDescription] = useState("");

    const fetchDrivers = async () => {
        setIsLoading(true);
        try {
            let res = await getDriverRevisionStatusRequest();
            if (res) {
                setDrivers(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchDrivers();
    }, [])

    const openModal = async () => {
        setIsVisible(true);
    }

    const closeModal = () => {
        setSelectedDriver({});
        setIsVisible(false);
    }

    const columns = [
        {
            title: 'Firma',
            dataIndex: 'driverDto',
            key: '1',
            render: e => e.company.commercialTitle
        },
        {
            title: 'Şoför',
            dataIndex: 'driverDto',
            key: '2',
            render: e => e.firstName + " " + e.lastName
        },
        {
            title: 'Telefon',
            dataIndex: 'driverDto',
            key: '3',
            render: e => e.phone
        },
        {
            title: 'Durum',
            dataIndex: 'driverDto',
            key: '4',
            render: e => e.status
        },
        {
            title: '',
            dataIndex: 'driverDto',
            key: '5',
            align: 'right',
            render: (e, row) => <Button onClick={() => openModal()} >İncele</Button >,
        },
    ];

    const openDocument = async (documentId) => {
        setIsLoading(true);
        try {
            let res = await getDriverDocumentBase64Request(documentId);
            if (res) {
                let file_window = window.open()
                let fileURL = `data:${res.data.type};base64,${res.data.data}`
                file_window.document.write(`<iframe src="${fileURL}" frameborder="0" style="border:0; top:0px; left:0px; bottom:0px; right:0px; width:100%; height:100%;" allowfullscreen></iframe>`)
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    const handleUpdateStatus = async (status) => {
        try {
            let res = await updateDriverStatusRequest({ driverId: selectedDriver.driverDto.id, statusDescription, value: status });
            if (res) {
                closeModal();
                fetchDrivers();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    return (
        <>
            <Table
                columns={columns}
                dataSource={drivers}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />

            <Modal
                width={800}
                title="Sürücüyü inceleyin"
                centered
                visible={isVisible}
                footer={
                    <div>
                        <Button onClick={() => handleUpdateStatus(false)} danger>Reddet</Button>
                        <Button onClick={() => handleUpdateStatus(true)} type="primary">Onayla</Button>
                    </div>
                }
                onCancel={() => closeModal()}
            >
                {
                    selectedDriver ?
                        <Row gutter={[24, 24]}>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>İsim</p>
                                <p>{selectedDriver?.driverDto?.firstName + " " + selectedDriver?.driverDto?.lastName}</p>
                            </Col>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Telefon</p>
                                <p>{selectedDriver?.driverDto?.phone}</p>
                            </Col>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Email</p>
                                <p>{selectedDriver?.driverDto?.email}</p>
                            </Col>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Şirket</p>
                                <p>{selectedDriver?.driverDto?.company.commercialTitle}</p>
                            </Col>
                            <Divider />
                            <Col span={24}>
                                <h3>Belgeler</h3>
                                <br />
                                <div style={{ display: "flex", gap: "1rem", flexWrap: "wrap" }}>
                                    {
                                        selectedDriver?.documentMinimalDtoList?.map(v =>
                                            <Button loading={isLoading} onClick={() => openDocument(v?.documentId)}>{v?.driverDocumentType?.typeName}</Button>
                                        )
                                    }
                                </div>
                            </Col>
                            <Divider style={{ margin: 0 }} />
                            <Col span={24}>
                                <p><b>Notlar</b></p>
                                <br />
                                <Input.TextArea
                                    placeholder="Eklemek istedikleriniz"
                                    value={statusDescription}
                                    onChange={e => setStatusDescription(e.target.value)}
                                />
                            </Col>
                        </Row> :
                        <SuspenseFallbackInline />
                }
            </Modal>
        </>
    )
}

export default AdminRevizyonBekleyenSuruculer;