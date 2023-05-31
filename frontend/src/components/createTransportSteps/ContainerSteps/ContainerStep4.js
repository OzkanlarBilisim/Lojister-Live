import { Button, Col, Row, Form, Select, Input, InputNumber } from 'antd';
import React, { useEffect } from 'react'
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';
import { CustomButton } from '../../CustomButton';

//CONTAINER STEP 4
const ContainerStep4 = ({ handleClickNextStep, handleClickPrevStep, containerForm, setContainerForm }) => {
    const { t, i18n } = useTranslation("common");

    const formValues = {
        containerType: containerForm.containerType,
        tonnage: containerForm.tonnage ? containerForm.tonnage / 1000 : null,
        explanation: containerForm.explanation
    };

    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [containerForm])

    return (

        <Form
            form={form}
            onFinish={handleClickNextStep}
            layout="vertical"
            labelCol={{ span: 24 }}
            scrollToFirstError={true}
        >
            <div className="create-ad-wrapper">
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>

                    <div>
                        <Button
                            onClick={() => handleClickPrevStep()}
                            shape="circle"
                            type="primary"
                            icon={<i className="bi bi-arrow-left"></i>}>
                        </Button>
                    </div>
                    <div>
                        <CustomButton
                            color="primary"
                            htmlType="submit"
                        >
                            {t("Sonraki Adıma Geç")}
                        </CustomButton>
                    </div>
                </div>
                <Row gutter={[24, 24]}>
                    <Col xs={24} xl={12} xxl={4}>
                        <Form.Item
                            label={t("Konteyner Tipi")}
                            name="containerType"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Select
                                size="large"
                                placeholder={t("Konteyner tipini seçin")}
                                allowClear
                                onChange={v => setContainerForm({ ...containerForm, containerType: v })}
                            >
                                <Select.Option value="TWENTY">{t("20'lik")}</Select.Option>
                                <Select.Option value="FOURTY">{t("40'lık")}</Select.Option>
                                <Select.Option value="FOURTY_HC">{t("40'lık HC")}</Select.Option>
                                <Select.Option value="FOURTY_FIVE">{t("45'lik")}</Select.Option>
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={4}>
                        <Form.Item
                            label={t("Toplam Ağırlık")}
                            name="tonnage"
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setContainerForm({ ...containerForm, tonnage: value * 1000 })}
                                placeholder={t("Toplam ağırlığı girin")}
                                size="large"
                                addonAfter={t("TON")}
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Açıklama")}
                            name="explanation"
                        >
                            <Input.TextArea
                                onChange={e => setContainerForm({ ...containerForm, explanation: e.target.value })}
                                placeholder={t("Varsa eklemek istediğiniz notları giriniz.")}
                                size="large"
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                </Row>
            </div >
        </Form>
    );
};

export default ContainerStep4;