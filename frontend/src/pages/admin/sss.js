import { Button, Table, Form, Input, Modal, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react'
import { createFaqRequest, deleteFaqByIdRequest, getAllFaq, updateFaqByIdRequest } from '../../api/controllers/frequently-asked-questions-controller';
import AntNotification from '../../components/AntNotification';
import RequestError from '../../components/RequestError';

const AdminSSS = () => {
    const [formRef] = Form.useForm();
    const [formRefForUpd] = Form.useForm();

    const formDefault = {
        eng_answer: "",
        eng_question: "",
        tr_answer: "",
        tr_question: ""
    }
    const [faqs, setFaqs] = useState([]);
    const [selectedFaq, setSelectedFaq] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isVisibleForUpd, setIsVisibleForUpd] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [form, setForm] = useState(formDefault);

    const fetchFaqs = async () => {
        setIsLoading(true);
        try {
            let res = await getAllFaq();
            if (res) {
                setFaqs(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    const handleSubmit = async () => {
        setIsSubmitting(true);
        try {
            let res = await createFaqRequest(form);
            if (res) {
                closeModal();
                fetchFaqs();
                AntNotification({ type: "success", message: "Sıkça sorulan soru eklendi" });
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const handleUpdate = async () => {
        setIsSubmitting(true);
        try {
            let res = await updateFaqByIdRequest(selectedFaq);
            if (res) {
                closeModalForUpd();
                AntNotification({ type: "success", message: "Sıkça sorulan soru güncellendi" });
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const onDelete = async (id) => {
        setIsSubmitting(true);
        try {
            let res = await deleteFaqByIdRequest(id);
            if (res) {
                fetchFaqs();
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    const openModalForUpd = async (faq) => {
        setIsVisibleForUpd(true);
        setSelectedFaq(faq);
        formRefForUpd.setFieldsValue(faq)
    }

    const closeModal = () => {
        setIsVisible(false);
        setForm(formDefault);
    }

    const closeModalForUpd = () => {
        setSelectedFaq({});
        setIsVisibleForUpd(false);
        fetchFaqs();
        formRefForUpd.resetFields();
    }

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: '1',
        },
        {
            title: 'Soru',
            dataIndex: 'tr_answer',
            key: '2',
        },
        {
            title: '',
            dataIndex: 'id',
            key: '5',
            align: 'right',
            render: (id, row) =>
                <div style={{ display: "flex", justifyContent: "flex-end", gap: ".5rem" }}>
                    {
                        <Button
                            onClick={() => openModalForUpd(row)}
                            loading={isSubmitting}
                        >
                            Düzenle
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
        fetchFaqs();
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
                dataSource={faqs}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />

            <Modal
                title="SSS ekleyin"
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
                        name="tr_answer"
                        label="Soru (TR)"
                    >
                        <Input.TextArea
                            autoFocus
                            onChange={e => setForm({ ...form, tr_answer: e.target.value })}
                            placeholder="Soruyu yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="tr_question"
                        label="Cevap (TR)"
                    >
                        <Input.TextArea
                            onChange={e => setForm({ ...form, tr_question: e.target.value })}
                            placeholder="Cevabı yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="eng_answer"
                        label="Soru (EN)"
                    >
                        <Input.TextArea
                            onChange={e => setForm({ ...form, eng_answer: e.target.value })}
                            placeholder="Soruyu yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="eng_question"
                        label="Cevap (EN)"
                    >
                        <Input.TextArea
                            onChange={e => setForm({ ...form, eng_question: e.target.value })}
                            placeholder="Cevabı yazın"
                        />
                    </Form.Item>
                </Form>
            </Modal>

            <Modal
                title="SSS düzenleyin"
                centered
                visible={isVisibleForUpd}
                onOk={() => handleUpdate()}
                okText="Kaydet"
                okButtonProps={{ loading: isSubmitting }}
                onCancel={() => closeModalForUpd()}
            >
                <Form
                    form={formRefForUpd}
                    layout="vertical"
                    onFinish={handleUpdate}
                >
                    <Form.Item
                        name="tr_answer"
                        label="Soru (TR)"
                    >
                        <Input.TextArea
                            autoFocus
                            onChange={e => setSelectedFaq({ ...selectedFaq, tr_answer: e.target.value })}
                            placeholder="Soruyu yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="tr_question"
                        label="Cevap (TR)"
                    >
                        <Input.TextArea
                            onChange={e => setSelectedFaq({ ...selectedFaq, tr_question: e.target.value })}
                            placeholder="Cevabı yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="eng_answer"
                        label="Soru (EN)"
                    >
                        <Input.TextArea
                            onChange={e => setSelectedFaq({ ...selectedFaq, eng_answer: e.target.value })}
                            placeholder="Soruyu yazın"
                        />
                    </Form.Item>
                    <Form.Item
                        name="eng_question"
                        label="Cevap (EN)"
                    >
                        <Input.TextArea
                            onChange={e => setSelectedFaq({ ...selectedFaq, eng_question: e.target.value })}
                            placeholder="Cevabı yazın"
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}

export default AdminSSS;