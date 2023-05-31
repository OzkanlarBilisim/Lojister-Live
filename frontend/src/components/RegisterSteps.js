import { Alert, Button, Divider, Form, Input, InputNumber, message, Modal, Radio, Select } from 'antd';
import React, { useEffect, useState } from 'react'
import NumberFormat from 'react-number-format';
import { Link } from 'react-router-dom';
import { CustomButton } from './CustomButton';
import firebase from 'firebase/compat/app';
import 'firebase/compat/auth';
import AntNotification from './AntNotification';
import ReactCodeInput from 'react-verification-code-input';
import { emailVerificationRequest, phoneVerificationRequest } from '../api/controllers/account-controller';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';
import { Countries } from './Countries';

export const ChoosePersonalType = ({ registerType, setRegisterType, handleClickNextStep, form, setForm }) => {
    const { t, i18n } = useTranslation("common");/*  */ /* t() */
    const { Option } = Select;

    return (
        <>
            <h2 className='form-title'>{t("Üye Olun")}</h2>
            <p className='form-desc'>{t("Uygun maliyet ve güvenli lojistik için üye olun!")}</p>
            <Divider />

            <Radio.Group className="register-radio-group" value={registerType} onChange={e => {
                setForm({ ...form, userRegionType: "INLAND" })
                setRegisterType(e.target.value)
            }}>
                <Radio.Button value="client">
                    <div className="register-radio">
                        <div className="register-radio-image">
                            <img src='/assets/img/register-choose-client.svg' alt='' />
                        </div>
                        {t("Yük Gönderiyorum")}
                    </div>
                </Radio.Button>
                {
                    registerType === 'client' &&
                    <Radio.Group  className="register-radio-group-region" value={form.userRegionType} onChange={e => setForm({ ...form, userRegionType: e.target.value })}>
                        <Radio.Button style={{display: 'none'}} value="INLAND" Select>
                            <div className="register-radio">
                                {t("Yurt İçi")}
                            </div>
                        </Radio.Button>
                        <Radio.Button style={{display: 'none'}} value="ABROAD">
                            <div className="register-radio">
                                {t("Yurt Dışı")}
                            </div>
                        </Radio.Button>
                    </Radio.Group>
                }
                <Radio.Button value="driver">
                    <div className="register-radio">
                        <div className="register-radio-image">
                            <img src='/assets/img/register-choose-transporter.svg' alt='' />
                        </div>
                        {t("Yük Taşıyorum")}
                    </div>
                </Radio.Button>
            </Radio.Group>

            <CustomButton
                disabled={registerType === 'client' && form.userRegionType === '' || registerType === ''}
                onClick={() => handleClickNextStep()} htmlType="submit" style={{ width: "100%" }}>
                {t("Devam Et")}
            </CustomButton>

            <Divider />
            <Link to="/login">
                <CustomButton style={{ width: "100%" }} type='outlined'>{t("Üyeyim, giriş yapmak istiyorum.")}</CustomButton>
            </Link>
        </>
    );
}

export const Step1 = ({ formRef, form, handleClickNextStep, handleClickPrevStep, handleChangeInput }) => {
    const { t, i18n } = useTranslation("common");

    const onSubmit = async () => {
        try {
            let res = await emailVerificationRequest(form.email)
            if (res) {
                handleClickNextStep();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    return (
        <>
            <h2 className='form-title'>{t("Üye Olun")}</h2>
            <p className='form-desc'>{t("Şimdi sizi tanıyalım.")}</p>
            <Divider />

            <Form
                form={formRef}
                name="basic"
                layout="vertical"
                labelCol={{ span: 24 }}
                onFinish={onSubmit}
                scrollToFirstError={true}
            >
                <Form.Item
                    label={t("İsminiz")}
                    name="firstName"
                    rules={[{ required: true, message: t('İsminizi yazmalısınız!') }]}
                >
                    <Input
                        size="large"
                        autoFocus
                        name="firstName"
                        value={form.firstName}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('İsminizi yazın.')}
                    />
                </Form.Item>

                <Form.Item
                    label={t("Soyisminiz")}
                    name="lastName"
                    rules={[{ required: true, message: t('Soyisminizi yazmalısınız!') }]}
                >
                    <Input
                        size="large"
                        name="lastName"
                        value={form.lastName}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('Soyisminizi yazın.')}
                    />
                </Form.Item>

                <Form.Item
                    label={t("Mail Adresiniz")}
                    name="email"
                    rules={[{ required: true, message: t('Mail adresinizi yazmalısınız!') }]}
                >
                    <Input
                        size="large"
                        type="email"
                        name="email"
                        value={form.email}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('Mail adresinizi yazın.')}
                    />
                </Form.Item>

                <div style={{ display: "flex", gap: "16px", paddingTop: "24px" }}>
                    <Button onClick={() => handleClickPrevStep()} type="outlined" style={{ flex: 1 }}>
                        {t("Geri Dön")}
                    </Button>
                    <Button type="primary" htmlType="submit" style={{ flex: 1 }}>
                        {t("Devam Et")}
                    </Button>
                </div>

            </Form >
        </>
    );
};

export const Step2 = ({ formRef, form, setForm, handleClickNextStep, handleClickPrevStep, handleChangeInput }) => {
    const { t, i18n } = useTranslation("common");

    const [phoneVerificationModal, setPhoneVerificationModal] = useState(false);
    const [confirmCode, setConfirmCode] = useState(false);
    const [checkPass, setCheckPass] = useState("");
    const [isSubmit, setIsSubmit] = useState(false)

    useEffect(() => {
        firebase.auth().languageCode = 'tr';

        if (window.recaptchaVerifier) {
            window.recaptchaVerifier.clear()
        }

        window.recaptchaVerifier = new firebase.auth.RecaptchaVerifier('captcha_container', {
            'size': 'invisible',
            'callback': (response) => {
                window.recaptchaVerifier.verify()
            }
        });

    }, [])

    const onSubmit = async () => {
        setIsSubmit(true);
        try {
            let res = await phoneVerificationRequest(form.selectedCountryCode + "~" + form.phone)
            if (res) {
                checkPassFunc();
            }
        } catch (error) {
            RequestError(error);
        }
        setIsSubmit(false);
    }

    const checkPassFunc = () => {
        if (form.password === checkPass) {
            openAuthModal()
        }
    }

    const openAuthModal = async () => {
        setIsSubmit(true);
        AntNotification({ type: "info", message: t("Numaranız kontrol ediliyor! Lütfen bekleyin.") });
        firebase.auth().signInWithPhoneNumber(form.selectedCountryCode + form.phone, window.recaptchaVerifier)
            .then((confirmationResult) => {
                window.confirmationResult = confirmationResult;
                setPhoneVerificationModal(true);
                AntNotification({ type: "success", message: t("Telefonunuza kod gönderildi.") });
            })
            .catch((err) => {
                AntNotification({ type: "error", message: t("Hata meydana geldi! Lütfen tekrar deneyin.") })
                /*                 console.log('girdi');
                                console.log('err', err) */
            })
        setIsSubmit(false);
    }

    const onAuth = async (code) => {
        setConfirmCode(true)
        window.confirmationResult.confirm(code).then(async (result) => {
            setPhoneVerificationModal(false);
            AntNotification({ type: "success", message: t("Telefonunuz başarıyla doğrulandı.") });
            handleClickNextStep();

        }).catch((error) => {
            AntNotification({ type: "error", message: t("Doğrulama başarısız! Lütfen tekrar deneyin.") })
        });
        setConfirmCode(false)
    }

    return (
        <>
            <h2 className='form-title'>{t("Üye Olun")}</h2>
            <p className='form-desc'>{t("Giriş yaparken kullanacağınız bilgileri belirleyin.")}</p>
            <Divider />

            <Form
                form={formRef}
                name="basic"
                layout="vertical"
                labelCol={{ span: 24 }}
                onFinish={onSubmit}
            >
                <Form.Item
                    label={t("Telefon Numaranız")}
                    validateStatus={form.phone === "" ? "error" : form.phone !== "" ? "success" : ""}
                    name="phone"
                    rules={[
                        { required: true, message: t('Telefon numaranızı yazmalısınız!') },
                    ]}
                >
                    <NumberFormat
                        addonBefore={<Select
                            style={{ width: 100 }}
                            name='selectedCountryCode'
                            showSearch
                            size='large'
                            value={form.selectedCountryCode}
                            onChange={(value) => {
                                form.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                setForm({ ...form })
                            }}
                            filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                        >
                            {
                                Countries.map((v, i) =>
                                    <Option key={i} value={v.dial_code}>{v.code + " " + v.dial_code}</Option>
                                )
                            }
                        </Select>}
                        size="large"
                        format="### ### ## ## ## ## ## ## ##"
                        placeholder={t("Yükleme yetkilisinin telefonunu girin")}
                        customInput={Input}
                        onValueChange={({ value }) => {
                            form.phone = `${value}`
                            setForm({ ...form })
                        }}
                    />
                </Form.Item>

                <Form.Item
                    label={t("Şifreniz")}
                    name="password"
                    rules={[
                        { required: true, message: t('Şifrenizi yazmalısınız!') },
                        { min: 8, message: t('Şifreniz en az 8 haneli olmalıdır.') },
                        { max: 32, message: t('Şifreniz en fazla 32 haneli olmalıdır.') },
                    ]}
                >
                    <Input.Password
                        name="password"
                        size="large"
                        value={form.password}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('Şifrenizi yazın.')}
                    />
                </Form.Item>

                <Form.Item
                    label={t("Şifreniz (Tekrar)")}
                    name="checkPass"
                    rules={[
                        { required: true, message: t('Şifrenizi tekrar yazmalısınız!') },
                    ]}
                    validateStatus={form.password !== checkPass && checkPass ? "error" : ""}
                    help={form.password !== checkPass && checkPass ? t("Şifreleriniz uyuşmuyor!") : ""}
                >
                    <Input.Password
                        name="checkPass"
                        size="large"
                        value={checkPass}
                        onChange={(e) => setCheckPass(e.target.value)}
                        placeholder={t('Şifrenizi tekrar yazın.')}
                    />
                </Form.Item>

                <div style={{ display: "flex", gap: "16px", paddingTop: "24px" }}>
                    <Button onClick={() => handleClickPrevStep()} style={{ flex: 1 }}>
                        {t("Geri Dön")}
                    </Button>
                    <Button loading={isSubmit} disabled={!form.password || !form.phone || !checkPass} type="primary" htmlType="submit" style={{ flex: 1 }}>
                        {t("Devam Et")}
                    </Button>
                </div>
            </Form>

            <Modal
                visible={phoneVerificationModal}
                title={t("Telefonunuzu doğrulayın")}
                centered
                onCancel={() => setPhoneVerificationModal(false)}
                cancelText={t("İptal")}
                footer={/* 
                    <Button
                        type="primary"
                        disabled={phoneVerificationCode.length !== 6}
                        onClick={() => onAuth()}>Gönder</Button>
               */  null}
            >
                <div style={{
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    alignItems: "center",
                    gap: "1rem"
                }}>
                    <p>{t("Aşağıdaki alana telefonuza gelen doğrulama kodunu girin")}</p>
                    <ReactCodeInput
                        autoFocus={true}
                        type="number"
                        onComplete={e => onAuth(e)}
                    />
                    <Alert type="info" message={t("Doğrulama kodunuz kontrol ediliyor...")} />
                </div>
            </Modal>

            <div id="captcha_container"></div>
        </>
    );
};

export const Step3 = ({ registerType, formRef, form, setForm, handleSubmit, handleClickPrevStep, handleChangeInput }) => {
    const { t, i18n } = useTranslation("common");

    return (
        <>
            <h2 className='form-title'>{t("Üye Olun")}</h2>
            <p className='form-desc'>{t("Şimdi de sizi ticari olarak tanıyalım. Şirket değilseniz T.C. kimlik numaranızı yazmanız yeterlidir.")}</p>
            <Divider />

            <Form
                form={formRef}
                name="basic"
                layout="vertical"
                labelCol={{ span: 24 }}
                onFinish={handleSubmit}
            >
                <Form.Item
                    label={t("Ünvanınız / Adınız Soyadı")}
                    name="commercialTitle"
                    rules={[{ required: true, message: t('Ünvanınızı yazmalısınız!') }]}
                >
                    <Input
                        size="large"
                        autoFocus
                        name="commercialTitle"
                        value={form.commercialTitle}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('Ünvanınızı yazın.')}
                    />
                </Form.Item>

                {
                    registerType === "client" &&
                    <Form.Item
                        label={t("Vergi Numaranız / T.C. Kimlik Numaranız")}
                        name="taxNumber"
                        rules={[{ required: true, message: t('Vergi numaranızı / T.C. kimlik numaranızı yazmalısınız!') }, form.userRegionType === 'INLAND' && { min: 10, message: t('Lütfen eksiksiz doldurun!') }]}
                    >
                        <NumberFormat
                            name="taxNumber"
                            size="large"
                            placeholder={t('Vergi numaranızı / T.C. kimlik numaranızı yazın.')}
                            customInput={Input}
                            maxLength={form.userRegionType === 'ABROAD' ? 63 : 11}
                            value={form.commercialTitle ? form.taxNumber : form.citizenId}
                            onValueChange={({ value }) =>
                                form.commercialTitle ?
                                    setForm({ ...form, taxNumber: value }) :
                                    setForm({ ...form, citizenId: value })
                            }
                        />
                    </Form.Item>
                }

                {
                    registerType === "driver" &&
                    <Form.Item
                        label={t("Vergi Numaranız")}
                        name="taxNumber"
                        rules={[{ required: true, message: t('Vergi numaranızı yazmalısınız!') }, form.userRegionType === 'INLAND' && { min: 10, message: t('Lütfen eksiksiz doldurun!') }]}
                    >
                        <NumberFormat
                            name="taxNumber"
                            size="large"
                            placeholder={t('Vergi numaranızı yazın.')}
                            customInput={Input}
                            maxLength={form.userRegionType === 'ABROAD' ? 63 : 11}
                            value={form.taxNumber}
                            onValueChange={({ value }) => setForm({ ...form, taxNumber: value })}
                        />
                    </Form.Item>
                }

                <Form.Item
                    label={t("Vergi Daireniz")}
                    name="taxAdministration"
                    rules={[{ required: registerType === "driver", message: t('Vergi dairenizi yazmalısınız!') }]}
                >
                    <Input
                        size="large"
                        name="taxAdministration"
                        value={form.taxAdministration}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('Vergi dairenizi yazın.')}
                    />
                </Form.Item>

                <Form.Item
                    label={t("Adresiniz")}
                    name="adres"
                    rules={[{ required: registerType === "driver", message: t('Adresinizi yazmalısınız!') }]}
                >
                    <Input.TextArea
                        rows={2}
                        name="adres"
                        value={form.adres}
                        onChange={(e) => handleChangeInput(e)}
                        placeholder={t('Adresinizi yazın.')}
                    />
                </Form.Item>

                <div style={{ display: "flex", gap: "16px", paddingTop: "24px" }}>
                    <Button onClick={() => handleClickPrevStep()} type="outlined" style={{ flex: 1 }}>
                        {t("Geri Dön")}
                    </Button>
                    <Button type="primary" htmlType="submit" style={{ flex: 1 }}>
                        {t("Gönder")}
                    </Button>
                </div>

            </Form>
        </>
    );
};