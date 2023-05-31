import { Button, Divider, Form, Input } from 'antd';
import React, { useEffect, useState } from 'react'
import NumberFormat from 'react-number-format';
import { createInsuranceCompanyRequest } from '../../api/controllers/insurance-company-controller';
import AntNotification from '../../components/AntNotification';
import RequestError from '../../components/RequestError';

const AdminKullaniciOlusturma = () => {
    const [formRef] = Form.useForm();
    const formDefault = {
        email: "",
        firstName: "",
        lastName: "",
        password: "",
        phone: ""
    };
    const [form, setForm] = useState(formDefault);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async () => {
        setIsSubmitting(true);
        try {
            let res = await createInsuranceCompanyRequest(form);
            if (res) {
                setForm(formDefault);
                formRef.setFieldsValue(formDefault);
                AntNotification({ type: "success", message: "Tanım eklendi" });
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmitting(false);
    }

    return (
        <>
            <h3>Kullanıcı Oluştur</h3>
            <Divider />
            <Form
                form={formRef}
                layout="vertical"
                style={{ maxWidth: 400 }}
                onFinish={handleSubmit}
            >
                <Form.Item
                    rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                    name="email"
                    label="Email"
                >
                    <Input
                        placeholder="Email girin"
                        onClick={e => setForm({ ...form, email: e.target.value })}
                    />
                </Form.Item>
                <Form.Item
                    rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                    name="firstName"
                    label="İsim"
                >
                    <Input
                        placeholder="İsim girin"
                        onClick={e => setForm({ ...form, firstName: e.target.value })}
                    />
                </Form.Item>
                <Form.Item
                    rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                    name="lastName"
                    label="Soyisim"
                >
                    <Input
                        placeholder="Soyisim girin"
                        onClick={e => setForm({ ...form, lastName: e.target.value })}
                    />
                </Form.Item>
                <Form.Item
                    rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                    name="password"
                    label="Şifre"
                >
                    <Input
                        placeholder="Şifre girin"
                        onClick={e => setForm({ ...form, password: e.target.value })}
                    />
                </Form.Item>
                <Form.Item
                    rules={[{ required: true, message: 'Lütfen bu alanı doldurun!' }]}
                    name="phone"
                    label="Telefon"
                >
                    <NumberFormat
                        placeholder="Telefon girin"
                        size="large"
                        minLength={13}
                        format="+90 ### ### ## ##"
                        customInput={Input}
                        onValueChange={({ value }) => {
                            setForm({ ...form, phone: `+90${value}` })
                        }}
                    />
                </Form.Item>
                <Form.Item>
                    <Button loading={isSubmitting} type="primary" style={{ width: "100%" }} htmlType="submit">Ekle</Button>
                </Form.Item>
            </Form>
        </>
    )
}

export default AdminKullaniciOlusturma;