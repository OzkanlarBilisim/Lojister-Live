import { Button, Col, Modal, Row, Table, Tabs, Form, Input, DatePicker, TimePicker, Radio } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react'
import { getPaymentDetailByTransportProcessIdRequest, getWaitingPaymentsRequest, payTheDriverRequest } from '../../../api/controllers/transport-payment-controller';
import SuspenseFallbackInline from '../../../components/SuspenseFallbackInline';
import RequestError from '../../../components/RequestError';

const AdminBekleyenOdemeler = () => {
    const [form] = Form.useForm();
    const payFormDefault = {
        isPayment: true,
        paymentDate: "",
        paymentDescription: "",
        selectedTime: null,
        paymentTime: {
            hour: null,
            minute: null,
            nano: null,
            second: null
        },
        paymentTransactionNumber: "",
        transportPaymentId: null,
    }
    const [payments, setPayments] = useState([]);
    const [selectedPayment, setSelectedPayment] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isVisibleForPay, setIsVisibleForPay] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [payForm, setPayForm] = useState(payFormDefault)

    useEffect(() => {
        const fetchPayments = async () => {
            setIsLoading(true);
            try {
                let res = await getWaitingPaymentsRequest();
                if (res) {
                    setPayments(res.data);
                }
            } catch (error) {
                RequestError(error);
            }
            setIsLoading(false);
        }
        fetchPayments();
    }, [])

    const openModal = async ({ transportCode, transportPaymentId, paymentDate, paymentTime, paymentDescription, paymentTransactionNumber }) => {
        setIsVisible(true);
        try {
            let res = await getPaymentDetailByTransportProcessIdRequest(transportCode);
            if (res) {
                setSelectedPayment({ ...res.data, transportPaymentId, paymentDate, paymentTime, paymentDescription, paymentTransactionNumber });
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const closeModal = () => {
        setIsVisible(false);
        setSelectedPayment({});
    }

    const openModalForPay = async (payment) => {
        setIsVisibleForPay(true);
        setSelectedPayment(payment);
        setPayForm({ ...payForm, transportPaymentId: payment.id })
    }

    const closeModalForPay = () => {
        setIsVisibleForPay(false);
        setSelectedPayment({});
        setPayForm(payFormDefault);
        form.setFieldsValue(payFormDefault);
    }

    const handleSubmitPayForm = async () => {
        payForm.paymentTime.hour = moment(payForm.selectedTime).format("HH");
        payForm.paymentTime.minute = moment(payForm.selectedTime).format("mm");
        payForm.paymentTime.second = moment(payForm.selectedTime).format("ss");
        setPayForm({ ...payForm })
        try {
            let res = await payTheDriverRequest(payForm);
            if (res) {
                closeModalForPay();
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
            render: (e, row) =>
                <div style={{ display: "flex", gap: "1rem", justifyContent: "flex-end" }}>
                    <Button onClick={() => openModalForPay(row)}>Ödeme Yap</Button>
                    <Button onClick={() => openModal({ transportCode: e, paymentDate: row.paymentDate, paymentDescription: row.paymentDescription, paymentTransactionNumber: row.paymentTransactionNumber })} > İncele</Button >
                </div>,
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

            {/* İncele */}
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

            {/* Ödeme Yap */}
            <Modal
                title="Ödeme yapın"
                centered
                visible={isVisibleForPay}
                onOk={() => handleSubmitPayForm()}
                onCancel={() => closeModalForPay()}
            >
                {
                    selectedPayment ?
                        <Form
                            form={form}
                            layout="vertical"
                            onFinish={handleSubmitPayForm}
                        >
                            <Row gutter={[24, 24]}>

                                <Col xs={24} md={12}>
                                    <h3>Makbuz Bilgileri</h3>
                                    <br />
                                    <Form.Item
                                        name="paymentTransactionNumber"
                                        label="İşlem Numarası"
                                    >
                                        <Input onChange={(e) => setPayForm({ ...payForm, paymentTransactionNumber: e.target.value })} />
                                    </Form.Item>
                                    <Form.Item
                                        name="paymentDescription"
                                        label="Ödeme Açıklaması"
                                    >
                                        <Input onChange={(e) => setPayForm({ ...payForm, paymentDescription: e.target.value })} />
                                    </Form.Item>
                                    <Form.Item
                                        name="paymentDate"
                                        label="Ödeme Tarihi"
                                    >
                                        <DatePicker style={{ width: "100%" }} onChange={(e) => setPayForm({ ...payForm, paymentDate: moment(e).format("YYYY-MM-DD") })} />
                                    </Form.Item>
                                    <Form.Item
                                        name="selectedTime"
                                        label="Ödeme Saati"
                                    >
                                        <TimePicker style={{ width: "100%" }} onChange={e => setPayForm({ ...payForm, selectedTime: e })} />
                                    </Form.Item>
                                    <Form.Item
                                        name="isPayment"
                                        label="Ödeme Durumu"
                                    >
                                        <Radio.Group>
                                            <Radio value="true"> Yapıldı </Radio>
                                            <Radio value="false"> Yapılmadı </Radio>
                                        </Radio.Group>
                                    </Form.Item>
                                </Col>

                                <Col xs={24} md={12}>
                                    <h3>Hesap Bilgileri</h3>
                                    <br />
                                    <Label
                                        title="Banka Adı"
                                        text={selectedPayment?.bankName}
                                    />
                                    <Label
                                        title="Banka Şubesi"
                                        text={selectedPayment?.branch}
                                    />
                                    <Label
                                        title="Hesap Numarası"
                                        text={selectedPayment?.accountNumber}
                                    />
                                    <Label
                                        title="IBAN"
                                        text={selectedPayment?.iban}
                                    />
                                </Col>
                            </Row>
                        </Form> :
                        <SuspenseFallbackInline />
                }
            </Modal>
        </>
    )
}

export default AdminBekleyenOdemeler;