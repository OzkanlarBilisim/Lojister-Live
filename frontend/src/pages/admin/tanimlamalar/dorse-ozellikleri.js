import { Button, Table, Form, Input, Modal, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react'
import { createTrailerFeatureRequest, deleteTrailerFeatureRequest, getTrailerFeatureActivateRequest, getTrailerFeatureHideRequest, getTrailerFeaturesRequest } from '../../../api/controllers/trailer-feature-controller';
import AntNotification from '../../../components/AntNotification';
import RequestError from '../../../components/RequestError'

const AdminDorseOzellikleri = () => {
    const [formRef] = Form.useForm();
    const formDefault = {
        engFeatureName: "",
        featureName: ""
    }
    const [types, setTypes] = useState([]);
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [form, setForm] = useState(formDefault);

    const fetchTypes = async () => {
        setIsLoading(true);
        try {
            let res = await getTrailerFeaturesRequest();
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
            let res = await createTrailerFeatureRequest(form);
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
            let res = await deleteTrailerFeatureRequest(id);
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
            let res = await getTrailerFeatureHideRequest(id);
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
            let res = await getTrailerFeatureActivateRequest(id);
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
            title: 'Tanım (TR)',
            dataIndex: 'featureName',
            key: '2',
        },
        {
            title: 'Tanım (EN)',
            dataIndex: 'engFeatureName',
            key: '4',
        },
        {
            title: '',
            dataIndex: 'id',
            key: '5',
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

export default AdminDorseOzellikleri;