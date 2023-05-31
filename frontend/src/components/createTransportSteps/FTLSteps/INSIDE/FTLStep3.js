import { Button, Col, Row, Form, Select, Input, InputNumber, Checkbox } from 'antd';
import React, { useEffect, useState } from 'react'
import { getCargoTypesRequest } from '../../../../api/controllers/cargo-type-controller';
import { getLoadTypesRequest } from '../../../../api/controllers/load-type-controller';
import { getPackagingTypesRequest } from '../../../../api/controllers/packaging-type-controller';
import { getCurrencyUnitsRequest } from '../../../../api/controllers/currency-unit-controller';
import { CustomButton } from '../../../CustomButton';
import RequestError from '../../../RequestError';
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';

//FTL STEP 3
const FTLStep3 = ({ handleClickNextStep, handleClickPrevStep, ftlForm, setFtlForm }) => {
    const { t, i18n } = useTranslation("common");

    const formValues = {
        cargoTypeIdList: ftlForm.cargoTypeIdList[0],
        packagingTypeId: ftlForm.packagingTypeId,
        loadTypeIdList: ftlForm.loadTypeIdList,
        tonnage: ftlForm.tonnage ? ftlForm.tonnage / 1000 : null,
        goodsPrice: ftlForm.goodsPrice,
        explanation: ftlForm.explanation,
        isPorter: ftlForm.isPorter,
    };

    const [cargoTypes, setCargoTypes] = useState([]);
    const [packagingTypes, setPackagingTypes] = useState([]);
    const [loadTypes, setLoadTypes] = useState([])
    const [currencyUnits, setCurrencyUnits] = useState([])

    const fetchCargoTypes = async () => {
        try {
            let res = await getCargoTypesRequest();
            if (res) {
                setCargoTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchPackagingTypes = async () => {
        try {
            let res = await getPackagingTypesRequest();
            if (res) {
                setPackagingTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchLoadTypes = async () => {
        try {
            let res = await getLoadTypesRequest();
            if (res) {
                setLoadTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchCurrencyUnits = async () => {
        try {
            let res = await getCurrencyUnitsRequest();
            if (res) {
                setCurrencyUnits(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const priceAfter = (
        <Select defaultValue={ftlForm.currencyUnitId} onChange={v => setFtlForm({ ...ftlForm, currencyUnitId: v })} size="large">
            {
                currencyUnits.map(v =>
                    <Select.Option key={v.id} value={v.id}>{v.currencySymbol}</Select.Option>
                )
            }
        </Select>
    );

    useEffect(() => {
        fetchCargoTypes();
        fetchPackagingTypes();
        fetchLoadTypes();
        fetchCurrencyUnits();
    }, []);

    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [ftlForm])

    return (

        <Form
            form={form}
            onFinish={handleClickNextStep}
            labelCol={{ span: 24 }}
            wrapperCol={{ span: 24 }}
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
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Yük Tipi")}
                            name="cargoTypeIdList"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Select
                                size="large"
                                placeholder={t("Yük tipini seçin")}
                                allowClear
                                onChange={v => setFtlForm({ ...ftlForm, cargoTypeIdList: v ? [v] : [] })}
                            >
                                {
                                    cargoTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Paketleme Tipi")}
                            name="packagingTypeId"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Select
                                size="large"
                                placeholder={t("Paketleme tipini seçin")}
                                allowClear
                                onChange={v => setFtlForm({ ...ftlForm, packagingTypeId: v })}
                            >
                                {
                                    packagingTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Yükleme Şekli")}
                            name="loadTypeIdList"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Select
                                size="large"
                                placeholder={t("Yükleme şeklini seçin")}
                                allowClear
                                maxTagCount="responsive"
                                onChange={v => setFtlForm({ ...ftlForm, loadTypeIdList: v ? [v] : [] })}
                            >
                                {
                                    loadTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
                                    ))
                                }
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
                                onValueChange={({ value }) => setFtlForm({ ...ftlForm, tonnage: value * 1000 })}
                                placeholder={t("Toplam ağırlığı girin")}
                                size="large"
                                addonAfter={t("TON")}
                                min={0}
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={4}>
                        <Form.Item
                            label={t("Mal Değeri")}
                            name="goodsPrice"
                            extra={<span style={{ opacity: .5 }}>{t("*Sigorta yapılırken baz alınacaktır.")}</span>}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setFtlForm({ ...ftlForm, goodsPrice: value })}
                                placeholder={t("Mal değerini girin")}
                                size="large"
                                addonAfter={priceAfter}
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
                                onChange={e => setFtlForm({ ...ftlForm, explanation: e.target.value })}
                                placeholder={t("Varsa eklemek istediğiniz notları giriniz.")}
                                size="large"
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24}>
                        <Form.Item
                            style={{ margin: 0 }}
                            labelCol={20}
                            wrapperCol={4}
                            name="isPorter"
                        >
                            <Checkbox checked={ftlForm.isPorter} onChange={e => setFtlForm({ ...ftlForm, isPorter: e.target.checked })}>{t("Hammaliye istiyorum.")}</Checkbox>
                        </Form.Item>
                    </Col>
                </Row>
            </div >
        </Form >
    );
};

export default FTLStep3;