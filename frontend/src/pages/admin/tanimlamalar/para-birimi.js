import { Button, Table, Form, Input, Modal, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react'
import { createCurrencyUnitRequest, deleteCurrencyUnitRequest, getCurrencyUnitActivateRequest, getCurrencyUnitHideRequest, getCurrencyUnitsRequest } from '../../../api/controllers/currency-unit-controller';
import AntNotification from '../../../components/AntNotification';
import RequestError from '../../../components/RequestError'

const AdminParaBirimi = () => {
    const [formRef] = Form.useForm();
    const formDefault = {
        currencyAbbreviation: "",
        currencyName: "",
        currencySymbol: "",
    }
    const [types, setTypes] = useState([]);
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [form, setForm] = useState(formDefault);

    const fetchTypes = async () => {
        setIsLoading(true);
        try {
            let res = await getCurrencyUnitsRequest();
            if (res) {
                setTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    const closeModal = () => {
        setIsVisible(false);
        setForm(formDefault);
    }

    const handleSubmit = async () => {
        setIsSubmitting(true);
        try {
            let res = await createCurrencyUnitRequest(form);
            if (res) {
                closeModal();
                fetchTypes();
                AntNotification({ type: "success", message: "Tanım eklendi" });
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const onDelete = async (id) => {
        setIsSubmitting(true);
        try {
            let res = await deleteCurrencyUnitRequest(id);
            if (res) {
                fetchTypes();
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const onHide = async (id) => {
        setIsSubmitting(true);
        try {
            let res = await getCurrencyUnitHideRequest(id);
            if (res) {
                fetchTypes();
                AntNotification({ type: "success", message: "Tanım gizlendi" });
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const onActivate = async (id) => {
        setIsSubmitting(true);
        try {
            let res = await getCurrencyUnitActivateRequest(id);
            if (res) {
                fetchTypes();
                AntNotification({ type: "success", message: "Tanım aktifleştirildi" });
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: '1',
        },
        {
            title: 'Kısa Tanım',
            dataIndex: 'currencyAbbreviation',
            key: '2',
        },
        {
            title: 'Tam Tanım',
            dataIndex: 'currencyName',
            key: '4',
        },
        {
            title: 'Sembol',
            dataIndex: 'currencySymbol',
            key: '5',
        },
        {
            title: '',
            dataIndex: 'id',
            key: '6',
            align: 'right',
            render: (id, row) =>
                <div style={{ display: "flex", justifyContent: "flex-end", gap: ".5rem" }}>
                    {
                        row?.dynamicStatus === "ACTIVE" ?
                            <Button
                                onClick={() => onHide(id)}
                                loading={isSubmitting}
                            >
                                Gizle
                            </Button> :
                            <Button
                                onClick={() => onActivate(id)}
                                loading={isSubmitting}
                            >
                                Aktifleştir
                            </Button>
                    }
                    <Popconfirm
                        title="Emin misiniz?"
                        onConfirm={() => onDelete(id)}
                        okText="Evet"
                        placement="bottomRight"
                    >
                        <Button danger>Sil</Button>
                    </Popconfirm>
                </div >
        },
    ];

    useEffect(() => {
        formRef.setFieldsValue(form);
    }, [form])

    useEffect(() => {
        fetchTypes();
    }, [])

    return (
        <>
            <Button
                onClick={() => setIsVisible(true)}
                type="primary"
                style={{ marginBottom: "1rem" }}
            >Yeni Ekle</Button>

            <Table
                columns={columns}
                dataSource={types}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />

            <Modal
                title="Tipi inceleyin"
                centered
                visible={isVisible}
                onOk={() => handleSubmit()}
                okText="Kaydet"
                okButtonProps={{ loading: isSubmitting }}
                onCancel={() => closeModal()}
            >
                <Form
                    form={formRef}
                    layout="vertical"
                    onFinish={handleSubmit}
                >
                    <Form.Item
                        name="featureName"
                        label="Tanım Adı (TR)"
                    >
                        <Input
                            autoFocus
                            onChange={e => setForm({ ...form, featureName: e.target.value })}
                            placeholder="Tanım adını yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="engFeatureName"
                        label="Tanım Adı (EN)"
                    >
                        <Input
                            onChange={e => setForm({ ...form, engFeatureName: e.target.value })}
                            placeholder="Tanım adını ingilizce yazın"
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}

export default AdminParaBirimi;