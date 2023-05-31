import { Button, Col, Divider, Modal, Row, Table, Input } from 'antd';
import React, { useEffect, useState } from 'react'
import { createVehicleStatusRequest, getVehicleReviewStatusRequest } from '../../../api/controllers/vehicle-controller';
import { getVehicleDocumentBase64Request } from '../../../api/controllers/vehicle-document-file-controller';
import RequestError from '../../../components/RequestError';
import SuspenseFallbackInline from '../../../components/SuspenseFallbackInline';

const AdminOnayBekleyenAraclar = () => {
    const [vehicles, setVehicles] = useState([]);
    const [selectedVehicle, setSelectedVehicle] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [statusDescription, setStatusDescription] = useState("");

    const fetchVehicles = async () => {
        setIsLoading(true);
        try {
            let res = await getVehicleReviewStatusRequest();
            if (res) {
                setVehicles(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchVehicles();
    }, [])

    const openModal = async (vehicle) => {
        setIsVisible(true);
        setSelectedVehicle(vehicle);
    }

    const closeModal = () => {
        setSelectedVehicle({});
        setIsVisible(false);
    }

    const columns = [
        {
            title: 'Firma',
            dataIndex: 'vehicleDto',
            key: '1',
            render: e => e.company.commercialTitle
        },
        {
            title: 'Araç',
            dataIndex: 'vehicleDto',
            key: '2',
            render: e => e.licencePlate
        },
        {
            title: 'Durum',
            dataIndex: 'vehicleDto',
            key: '4',
            render: e => e.vehicleStatus
        },
        {
            title: '',
            dataIndex: 'vehicleDto',
            key: '5',
            align: 'right',
            render: (e, row) => <Button onClick={() => openModal(row)} >İncele</Button >,
        },
    ];

    const openDocument = async (documentId) => {
        setIsLoading(true);
        try {
            let res = await getVehicleDocumentBase64Request(documentId);
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
            let res = await createVehicleStatusRequest({ vehicleId: selectedVehicle.vehicleDto.id, statusDescription, value: status });
            if (res) {
                closeModal();
                fetchVehicles();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    return (
        <>
            <Table
                columns={columns}
                dataSource={vehicles}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />

            <Modal
                width={800}
                title="Aracı inceleyin"
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
                    selectedVehicle ?
                        <Row gutter={[24, 24]}>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Marka</p>
                                <p>{selectedVehicle?.vehicleDto?.brand}</p>
                            </Col>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Model</p>
                                <p>{selectedVehicle?.vehicleDto?.vehicleModel}</p>
                            </Col>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Tip</p>
                                <p>{selectedVehicle?.vehicleDto?.vehicleType?.typeName}</p>
                            </Col>
                            <Col xs={24} md={12}>
                                <p style={{ opacity: .7 }}>Şirket</p>
                                <p>{selectedVehicle?.vehicleDto?.company?.commercialTitle}</p>
                            </Col>
                            <Divider style={{ margin: 0 }} />
                            <Col span={24}>
                                <h3>Belgeler</h3>
                                <br />
                                <div style={{ display: "flex", gap: "1rem", flexWrap: "wrap" }}>
                                    {
                                        selectedVehicle?.documentMinimalDtoList?.map(v =>
                                            <Button loading={isLoading} onClick={() => openDocument(v?.documentId)}>{v?.vehicleDocumentType?.typeName}</Button>
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

export default AdminOnayBekleyenAraclar;