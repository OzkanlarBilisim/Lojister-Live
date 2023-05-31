import { Button, Col, Modal, Row, Table, Tabs } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react'
import { getCompletedPaymentsRequest, getPaymentDetailByTransportProcessIdRequest } from '../../../api/controllers/transport-payment-controller';
import SuspenseFallbackInline from '../../../components/SuspenseFallbackInline';

const AdminTamamlananOdemeler = () => {
    const [payments, setPayments] = useState([]);
    const [selectedPayment, setSelectedPayment] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchPayments = async () => {
            setIsLoading(true);
            try {
                let res = await getCompletedPaymentsRequest();
                if (res) {
                    setPayments(res.data);
                }
            } catch (error) {
                console.log('error', error)
            }
            setIsLoading(false);
        }
        fetchPayments();
    }, [])

    const openModal = async ({ transportCode, paymentDate, paymentTime, paymentDescription, paymentTransactionNumber }) => {
        setIsVisible(true);
        try {
            let res = await getPaymentDetailByTransportProcessIdRequest(transportCode);
            if (res) {
                setSelectedPayment({ ...res.data, paymentDate, paymentTime, paymentDescription, paymentTransactionNumber });
            }
        } catch (error) {
            console.log('error', error)
        }
    }

    const closeModal = () => {
        setSelectedPayment({});
        setIsVisible(false);
    }

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: '1',
        },
        {
            title: 'Taşıma Kodu',
            dataIndex: 'transportCode',
            key: '2',
        },
        {
            title: 'Fiyat',
            dataIndex: 'price',
            key: '3',
            render: e => e + '₺'
        },
        {
            title: 'Durum',
            dataIndex: 'paymentStatus',
            key: '4',
        },
        {
            title: '',
            dataIndex: 'transportCode',
            key: '5',
            align: 'right',
            render: (e, row) => <Button onClick={() => openModal({ transportCode: e, paymentDate: row.paymentDate, paymentDescription: row.paymentDescription, paymentTransactionNumber: row.paymentTransactionNumber })} > İncele</Button >,
        },
    ];

    const Label = ({ title, text }) => (
        <div style={{ marginBottom: "1rem" }}>
            <p style={{ fontSize: "12px", opacity: ".7", fontWeight: "500" }}>{title}</p>
            <p>{text ? text : "-"}</p>
        </div>
    )
    return (
        <>
            <Table
                columns={columns}
                dataSource={payments}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />

            <Modal
                title="Ödemeyi inceleyin"
                centered
                visible={isVisible}
                footer={null}
                onCancel={() => closeModal()}
            >
                {
                    selectedPayment ?
                        <Tabs defaultActiveKey="1" >
                            <Tabs.TabPane tab="Makbuz Bilgileri" key="1">
                                <Label
                                    title="İşlem Numarası"
                                    text={selectedPayment?.paymentTransactionNumber}
                                />
                                <Label
                                    title="Ödeme Açıklaması"
                                    text={selectedPayment?.paymentDescription}
                                />
                                <Label
                                    title="Ödeme Tarih ve Saati"
                                    text={(selectedPayment?.paymentDate ? moment(selectedPayment?.paymentDate).format("LL") + " " : "") + (selectedPayment?.paymentTime ? selectedPayment?.paymentTime : "")}
                                />
                            </Tabs.TabPane>
                            <Tabs.TabPane tab="İlan Bilgileri" key="2">
                                <Row gutter={[24, 24]}>
                                    <Col xs={24} md={12}>
                                        <Label
                                            title="İlan Tarihi"
                                            text={moment(selectedPayment?.advertisementStartDate).format("LLL")}
                                        />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <Label
                                            title="Teklif Tarihi"
                                            text={moment(selectedPayment?.bidStartDate).format("LLL")}
                                        />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <h3>Gönderici</h3>
                                        <Label
                                            title="Yükleme Adresi"
                                            text={selectedPayment?.startAddress}
                                        />
                                        <Label
                                            title="Yükleme Tarihi"
                                            text={(selectedPayment?.admissionDate ? moment(selectedPayment?.admissionDate).format("LL") : "") + " " + (selectedPayment?.admissionTime ? selectedPayment?.admissionTime : "")}
                                        />
                                        <Label
                                            title="Teslim Eden Kişi"
                                            text={selectedPayment?.startRecipientFirstname + " " + selectedPayment?.startRecipientLastname}
                                        />
                                        <Label
                                            title="Teslim Eden Kişi Telefon"
                                            text={selectedPayment?.startRecipientNumber}
                                        />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <h3>Alıcı</h3>
                                        <Label
                                            title="Teslimat Adresi"
                                            text={selectedPayment?.finishAddress}
                                        />
                                        <Label
                                            title="Teslimat Tarihi"
                                            text={(selectedPayment?.deliveryDate ? moment(selectedPayment?.deliveryDate).format("LL") : "") + " " + (selectedPayment?.deliveryTime ? selectedPayment?.deliveryTime : "")}
                                        />
                                        <Label
                                            title="Teslim Alan Kişi"
                                            text={selectedPayment?.dueRecipientFirstname + " " + selectedPayment?.dueRecipientLastname}
                                        />
                                        <Label
                                            title="Teslim Alan Kişi Telefon"
                                            text={selectedPayment?.dueRecipientNumber}
                                        />
                                        <Label
                                            title="Alıcı Açıklaması"
                                            text={selectedPayment?.clientExplanation}
                                        />
                                    </Col>
                                </Row>
                            </Tabs.TabPane>
                            <Tabs.TabPane tab="İlan Sahibi" key="3">
                                <Label
                                    title="Firma Adı"
                                    text={selectedPayment?.companyName}
                                />
                                <Label
                                    title="Firma Telefon"
                                    text={selectedPayment?.bossNumber}
                                />
                                <Label
                                    title="İsim"
                                    text={selectedPayment?.clientFirstname + " " + selectedPayment?.clientLastname}
                                />
                                <Label
                                    title="Telefon"
                                    text={selectedPayment?.clientNumber}
                                />
                            </Tabs.TabPane>
                            <Tabs.TabPane tab="Taşıyıcı Bilgileri" key="4">
                                <Label
                                    title="Firma Adı"
                                    text={selectedPayment?.bossFirstname + " " + selectedPayment?.bossLastname}
                                />
                                <Label
                                    title="İsim"
                                    text={selectedPayment?.driverFirstname + " " + selectedPayment?.driverLastname}
                                />
                                <Label
                                    title="Telefon"
                                    text={selectedPayment?.driverNumber}
                                />
                                <Label
                                    title="Araç Plakası"
                                    text={selectedPayment?.licencePlate}
                                />
                            </Tabs.TabPane>
                        </Tabs> :
                        <SuspenseFallbackInline />
                }
            </Modal>
        </>
    )
}

export default AdminTamamlananOdemeler;