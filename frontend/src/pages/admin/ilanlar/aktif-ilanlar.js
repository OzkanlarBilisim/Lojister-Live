import { Button, Col, Collapse, Modal, Popconfirm, Row, Table } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react'
import { getActiveAdvertisementRequest, hideAdvertisementRequest } from '../../../api/controllers/client-advertisiment-controller';
import RequestError from '../../../components/RequestError';
import SuspenseFallbackInline from '../../../components/SuspenseFallbackInline';


const AdminAktifIlanlar = () => {
    const [ads, setAds] = useState([]);
    const [selectedAd, setSelectedAd] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    const fetchAds = async () => {
        setIsLoading(true);
        try {
            let res = await getActiveAdvertisementRequest();
            if (res) {
                setAds(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchAds();
    }, [])

    const openModal = (ad) => {
        setSelectedAd(ad);
        setIsVisible(true);
    }

    const closeModal = () => {
        setSelectedAd({});
        setIsVisible(false);
    }

    const confirm = async (id) => {
        try {
            let res = await hideAdvertisementRequest(selectedAd?.id);
            if (res) {
                fetchAds();
                closeModal();
            }
        } catch (error) {
            RequestError(error)
        }
    }

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: '1',
        },
        {
            title: 'İlan Sahibi',
            dataIndex: 'client',
            key: '2',
            render: (e) => e.firstName + " " + e.lastName,
        },
        {
            title: 'İlan Tarihi',
            dataIndex: 'createdDateTime',
            key: '3',
            render: (e) => moment(e).format("LLL"),
        },
        {
            title: '',
            dataIndex: 'id',
            key: '4',
            align: 'right',
            render: (e, row) => <Button onClick={() => openModal(row)}>İncele</Button>,
        },
    ];

    return (
        <>
            <Table
                columns={columns}
                dataSource={ads}
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
                {
                    selectedAd ?
                        <Row gutter={[24, 24]}>
                            <Col xs={24} md={12}>
                                <h3>Müşteri Bilgileri</h3>
                                <p><b>Müşteri id:</b> <span>{selectedAd?.client?.id}</span> </p>
                                <p><b>Adı:</b> <span>{selectedAd?.client?.firstName}</span> </p>
                                <p><b>Soyadı:</b> <span>{selectedAd?.client?.lastName}</span> </p>
                                <p><b>Telefon:</b> <span>{selectedAd?.client?.phone}</span> </p>
                                <p><b>Email:</b> <span>{selectedAd?.client?.email}</span> </p>
                            </Col>
                            <Col xs={24} md={12}>
                                <h3>Yük Bilgileri</h3>
                                <p><b>Başlangıç Adresi</b> <span>{selectedAd?.startingAddress?.province}</span> </p>
                                <p><b>Başlangıç Tarihi</b> <span>{moment(selectedAd?.adStartingDate).format("LL")}</span> </p>
                                <p><b>Varış Adresi</b> <span>{selectedAd?.dueAddress?.province}</span> </p>
                                <p><b>Varış Tarihi</b> <span>{moment(selectedAd?.adDueDate).format("LL")}</span> </p>
                            </Col>
                            <Popconfirm title="Emin misiniz?" onConfirm={confirm}>
                                <Button danger>İlanı gizle</Button>
                            </Popconfirm>
                        </Row> :
                        <SuspenseFallbackInline />
                }
            </Modal>
        </>
    )
}

export default AdminAktifIlanlar;