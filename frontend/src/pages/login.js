import { Alert, Button, Checkbox, Divider, Form, Input, Modal, Select } from 'antd';
import React, { useEffect, useState } from 'react'
import NumberFormat from 'react-number-format';
import { Link, useNavigate } from 'react-router-dom';
import { changeLanguageRequest, getMeRequest, loginRequest, resetPasswordMailRequest, resetPasswordRequest } from '../api/controllers/account-controller';
import { CustomButton } from '../components/CustomButton';
import AntNotification from '../components/AntNotification';
import SuspenseFallback from '../components/SuspenseFallback';
import { useDispatch, useSelector } from 'react-redux';
import RequestError from '../components/RequestError';
import { setUser } from '../redux/actions/userActions';
import { useTranslation } from 'react-i18next';
import { Countries } from '../components/Countries';
import moment from 'moment';

const Login = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Giriş Yap")
    const { Option } = Select;

    let params = (new URL(document.location)).searchParams;
    const savedTransport = useSelector((state) => state.savedTransport);
    const dispatch = useDispatch();

    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true)
    const [forgotPassModal, setForgotPassModal] = useState(false)
    const [resetPassModal, setResetPassModal] = useState(false)
    const [form, setForm] = useState({
        password: "",
        phoneNumber: "",
        selectedCountryCode: "+90",
        rememberMe: true
    });
    const [forgotPassForm, setForgotPassForm] = useState({
        email: ""
    })
    const [resetPassForm, setResetPassForm] = useState({
        verificationToken: "",
        nwPassword: "",
        passwordConfirm: ""
    })

    const handleSubmit = async () => {
        form.phoneNumber = form.selectedCountryCode + "~" + form.phoneNumber
        setForm({ ...form })

        try {
            let res = await loginRequest(form);
            if (res) {
                changeLanguageHandler(localStorage.getItem('currentLanguage'))
                AntNotification({ type: "success", message: t("Başarıyla giriş yapıldı.") });
                if (res.data.role === "ROLE_CLIENT" && savedTransport || res.data.role === "ROLE_CLIENT_EMPLOYEE" && savedTransport) {
                    navigate("/client/create-transport")
                } else if (res.data.role === "ROLE_CLIENT" && !savedTransport || res.data.role === "ROLE_CLIENT_EMPLOYEE" && !savedTransport) {
                    navigate("/client")
                } else if (res.data.role === "ROLE_DRIVER" || res.data.role === "ROLE_DRIVER_EMPLOYEE") {
                    return navigate("/driver")
                } else if (res.data.role === "ROLE_ADMIN") {
                    return navigate("/admin/tum-ilanlar")
                }
            }
        } catch (error) {
            form.phoneNumber = form.phoneNumber.split(/[~]/g)[1]
            setForm({ ...form })
            RequestError(error);
        }
    }

    const changeLanguageHandler = async (lang) => {
        try {
            let res = await changeLanguageRequest(lang)
            if (res) {
                localStorage.setItem('currentLanguage', lang)
                /*               */
                i18n.changeLanguage(lang)
                moment.locale(
                    localStorage.getItem('currentLanguage') === 'TURKISH' ? 'tr' :
                        localStorage.getItem('currentLanguage') === 'ENGLISH' ? 'en-nz' :
                            'tr'
                )
                localStorage.setItem('currentLanguageCode', lang === 'ENGLISH' ? 'en' : 'TURKISH' ? 'tr' : 'en')
                /* window.location.reload() */
            }
        } catch (error) {
            RequestError(error);
        }
    }


    const handleSubmitForgotPass = async () => {
        try {
            let res = await resetPasswordMailRequest(forgotPassForm);
            if (res) {
                closeForgotPassModal()
                AntNotification({ type: "success", message: t("Şifre sıfırlama isteğiniz başarıyla alındı."), description: t("Lütfen mailinizi kontrol edin.") });
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const closeForgotPassModal = () => {
        setForgotPassForm({
            email: ""
        });
        setForgotPassModal(false)
    }

    const handleSubmitResetPass = async () => {
        try {
            let res = await resetPasswordRequest(resetPassForm);
            if (res) {
                closeResetPassModal()
                AntNotification({ type: "success", message: t("Şifreniz başarıyla değiştirildi."), description: t("Yeni şifreniz ile giriş yapabilirsiniz") });
                navigate("/login")
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const closeResetPassModal = () => {
        setResetPassForm({
            verificationToken: "",
            password: "",
            passwordConfirm: ""
        });
        setResetPassModal(false)
    }

    const checkAuthentication = async () => {
        try {
            let res = await getMeRequest();
            if (res && res.status === 200) {

                switch (res.data.role) {
                    case "ROLE_CLIENT":
                        return navigate("/client")
                    case "ROLE_DRIVER":
                        return navigate("/driver")
                    case "ROLE_ADMIN":
                        return navigate("/admin/tum-ilanlar")
                    default:
                        return;
                }
            }
        } catch (error) {
            if (error?.response?.status === 401) {
                dispatch(setUser(null));
            }
        }
        setIsLoading(false)
    }
    useEffect(() => {
        let x = params.get('resetCode');
        if (x) {
            setResetPassModal(true);
            setResetPassForm({ ...resetPassForm, verificationToken: x })
        }
        checkAuthentication()
    }, [])


    return (
        isLoading ? <SuspenseFallback /> :
            <>
                <header className="login-register-navbar">
                    <nav>
                        <Link to="/">
                            <img src="/assets/img/logo.svg" alt="Lojister" />
                        </Link>
                    </nav>
                </header>
                <div className='login-register-wrapper' style={{ backgroundImage: "url(/assets/img/login-bg.svg)" }}>
                    <div className="login-register-container">
                        <div className='form-area'>
                            <h2 className='form-title'>{t("Giriş Yapın")}</h2>
                            <p className='form-desc'>{t("Uygun maliyet ve güvenli lojistik için giriş yapın!")}</p>
                            <Divider />
                            <Form
                                layout="vertical"
                                labelCol={{ span: 24 }}
                                onFinish={handleSubmit}
                                scrollToFirstError={true}
                            >
                                <Form.Item
                                    label={t("Telefon Numaranız")}
                                    name="phoneNumber"
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
                                        placeholder={t("Telefon numaranızı yazın.")}
                                        customInput={Input}
                                        onValueChange={({ value }) => {
                                            form.phoneNumber = `${value}`
                                            setForm({ ...form })
                                        }}
                                    />
                                </Form.Item>

                                <Form.Item
                                    label={t("Şifreniz")}
                                    name="password"
                                    rules={[{ required: true, message: t('Şifrenizi yazmalısınız!') }]}
                                >
                                    <Input.Password
                                        placeholder={t('Şifrenizi yazın.')}
                                        value={form.password}
                                        size="large"
                                        onChange={(e) => {
                                            setForm({ ...form, password: e.target.value })
                                        }}
                                    />
                                </Form.Item>

                                <Form.Item>
                                    <CustomButton htmlType="submit" style={{ width: "100%" }}>
                                        {t("Giriş Yap")}
                                    </CustomButton>
                                </Form.Item>

                                <Form.Item name="rememberMe">
                                    <Checkbox
                                        checked={form.rememberMe}
                                        onChange={(e) => setForm({ ...form, rememberMe: e.target.checked })}
                                    >{t("Beni hatırla")}</Checkbox>
                                </Form.Item>
                            </Form>
                            <Divider />
                            <Link to="/register">
                                <CustomButton style={{ width: "100%" }} type='outlined'>{t("Üye değilim, olmak istiyorum.")}</CustomButton>
                            </Link>
                            <Button onClick={() => setForgotPassModal(true)} style={{ width: "100%", marginTop: ".5rem" }} type='text'>{t("Şifremi unuttum, değiştirmek istiyorum.")}</Button>
                        </div>
                    </div>
                </div>

                <Modal
                    title={t("Şifremi unuttum!")}
                    visible={forgotPassModal}
                    centered
                    onOk={() => handleSubmitForgotPass()}
                    onCancel={() => closeForgotPassModal()}
                    okText={t('Doğrulama Maili Gönder')}
                    cancelText={t('İptal Et')}
                >
                    <Alert type="info" message={t("Tarafınıza gönderilecek mail onayından sonra şifrenizi değiştirebilirsiniz.")} showIcon />

                    <Divider />

                    <Form
                        layout="vertical"
                        labelCol={{ span: 24 }}
                        onFinish={handleSubmitForgotPass}
                    >
                        <Form.Item
                            labelCol={{ span: 24 }}
                            label={t("Mail Adresiniz")}
                            name="password"
                        >
                            <Input
                                placeholder={t('Mail adresinizi yazın.')}
                                value={forgotPassForm.email}
                                size="large"
                                type="email"
                                onChange={(e) => {
                                    setForgotPassForm({ ...forgotPassForm, email: e.target.value })
                                }}
                            />
                        </Form.Item>
                    </Form>
                </Modal>

                <Modal
                    title={t("Şifremi değiştir!")}
                    visible={resetPassModal}
                    centered
                    onOk={() => handleSubmitResetPass()}
                    onCancel={() => closeResetPassModal()}
                    okText={t('Kaydet')}
                    cancelText={t('İptal Et')}
                >
                    <Form
                        layout="vertical"
                        labelCol={{ span: 24 }}
                        onFinish={handleSubmitResetPass}
                    >
                        <Form.Item
                            label={t("Şifreniz")}
                            name="nwPassword"
                            rules={[
                                { required: true, message: t('Şifrenizi yazmalısınız!') },
                                { min: 8, message: t('Şifreniz en az 8 haneli olmalıdır.') },
                                { max: 32, message: t('Şifreniz en fazla 32 haneli olmalıdır.') },
                            ]}
                        >
                            <Input.Password
                                name="password"
                                size="large"
                                value={resetPassForm.nwPassword}
                                onChange={(e) => setResetPassForm({ ...resetPassForm, nwPassword: e.target.value })}
                                placeholder={t('Şifrenizi yazın.')}
                            />
                        </Form.Item>

                        <Form.Item
                            label={t("Şifreniz (Tekrar)")}
                            name="passwordConfirm"
                            rules={[
                                { required: true, message: t('Şifrenizi tekrar yazmalısınız!') },
                            ]}
                            validateStatus={resetPassForm.nwPassword !== resetPassForm.passwordConfirm && resetPassForm.passwordConfirm ? "error" : ""}
                            help={resetPassForm.nwPassword !== resetPassForm.passwordConfirm && resetPassForm.passwordConfirm ? t("Şifreleriniz uyuşmuyor!") : ""}
                        >
                            <Input.Password
                                name="passwordConfirm"
                                size="large"
                                value={resetPassForm.passwordConfirm}
                                onChange={(e) => setResetPassForm({ ...resetPassForm, passwordConfirm: e.target.value })}
                                placeholder={t('Şifrenizi tekrar yazın.')}
                            />
                        </Form.Item>
                    </Form>
                </Modal>
            </>
    );
};

export default Login;