import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import { clientRegisterWebRequest, driverRegisterWebRequest } from '../api/controllers/account-controller';
import AntNotification from '../components/AntNotification';
import { ChoosePersonalType, Step1, Step2, Step3 } from '../components/RegisterSteps';
import { Form } from 'antd';
import RequestError from '../components/RequestError';
import { useTranslation } from 'react-i18next';

const Register = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Kayıt Ol")

    let params = (new URL(document.location)).searchParams;

    const navigate = useNavigate();
    const [registerStep, setRegisterStep] = useState(0);
    const [registerType, setRegisterType] = useState("")

    const [form, setForm] = useState({
        personType: "",
        firstName: "",
        lastName: "",
        password: "",
        phone: "",
        selectedCountryCode: "+90",
        userRegionType: "",
        email: "",
        citizenId: null,
        commercialTitle: "",
        taxNumber: "",
        taxAdministration: "",
        adres: "",
        address: {
            fullAddress: ""
        }
    })

    const handleChangeInput = (e) => {
        if (e.target.name === "adres") {
            form.address.fullAddress = e.target.value
            form.adres = e.target.value
            setForm({ ...form })
        } else {
            setForm({ ...form, [e.target.name]: e.target.value })
        }
    }

    const handleClickNextStep = () => {
        if (0 <= registerStep && registerStep < 3) {
            setRegisterStep(registerStep + 1)
        }
    }

    const handleClickPrevStep = () => {
        setRegisterStep(registerStep - 1)
    }

    const handleSubmit = async () => {
        form.personType = form.commercialTitle || form.taxNumber || form.taxAdministration || form.address.fullAddress ? "corporate" : "individual"
        form.phone = form.selectedCountryCode + "~" + form.phone
        setForm({ ...form })
        try {
            let res
            if (registerType === "client") {
                res = await clientRegisterWebRequest(form);
            } else if (registerType === "driver") {
                res = await driverRegisterWebRequest(form);
            }
            if (res) {
                AntNotification({ type: "success", message: t("Üyeliğiniz başarıyla kaydedildi. Giriş ekranına yönlendiriliyorsunuz...") })
                setTimeout(() => {
                    navigate('/login')
                }, 1000);
            }
        } catch (error) {
            RequestError(error);
            form.phone = form.phone.split(/[~]/g)[1]
            setForm({ ...form })
        }
    }

    useEffect(() => {
        let x = params.get('registerType');
        if (x && x === 'driver' || x === 'client') {
            setRegisterType(params.get('registerType'));
            setRegisterStep(1);
        }
    }, [])

    const [formRef] = Form.useForm();

    useEffect(() => {
        formRef.setFieldsValue(form);
    }, [registerStep])


    return (
        <>
            <header className="login-register-navbar">
                <nav>
                    <Link to="/">
                        <img src="/assets/img/logo.svg" alt="Lojister" />
                    </Link>
                </nav>
            </header>
            <div className='login-register-wrapper' style={{ backgroundImage: `url(/assets/img/register-bg${registerStep > 0 ? registerType : ""}.svg)` }}>
                <div className='login-register-container'>
                    <div className='form-area'>
                        {
                            registerStep === 1 && registerType ?
                                <Step1
                                    formRef={formRef}
                                    form={form}
                                    handleClickNextStep={handleClickNextStep}
                                    handleClickPrevStep={handleClickPrevStep}
                                    handleChangeInput={handleChangeInput}
                                /> : registerStep === 2 && registerType ?
                                    <Step2
                                        form={form}
                                        setForm={setForm}
                                        handleClickNextStep={handleClickNextStep}
                                        handleClickPrevStep={handleClickPrevStep}
                                        handleChangeInput={handleChangeInput}
                                    /> :
                                    registerStep === 3 && registerType ?
                                        <Step3
                                            formRef={formRef}
                                            form={form}
                                            setForm={setForm}
                                            handleSubmit={handleSubmit}
                                            handleClickPrevStep={handleClickPrevStep}
                                            handleChangeInput={handleChangeInput}
                                            registerType={registerType}
                                        /> :
                                        <ChoosePersonalType
                                            registerType={registerType}
                                            setRegisterType={setRegisterType}
                                            handleClickNextStep={handleClickNextStep}
                                            handleChangeInput={handleChangeInput}
                                            form={form}
                                            setForm={setForm}
                                        />
                        }
                    </div>
                </div>
            </div>
        </>
    );
};

export default Register;