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

//Partial STEP 3
const PartialStep3 = ({ handleClickNextStep, handleClickPrevStep, partialForm, setPartialForm }) => {
    const { t, i18n } = useTranslation("common");

    const formValues = {
        cargoTypeIdList: partialForm.cargoTypeIdList[0],
        packagingTypeId: partialForm.packagingTypeId,
        loadTypeIdList: partialForm.loadTypeIdList,
        width: partialForm.width,
        height: partialForm.height,
        length: partialForm.length,
        desi: partialForm.desi,
        piece: partialForm.piece,
        tonnage: partialForm.tonnage ? partialForm.tonnage : null,
        goodsPrice: partialForm.goodsPrice,
        explanation: partialForm.explanation,
        isPorter: partialForm.isPorter,
        isStacking: partialForm.isStacking,
        isHighway: partialForm.isHighway,
        isSeaway: partialForm.isSeaway,
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
        <Select defaultValue={partialForm.currencyUnitId} onChange={v => setPartialForm({ ...partialForm, currencyUnitId: v })} size="large">
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
    }, [partialForm])


    useEffect(() => {
        // desi hesaplama
        setPartialForm({ ...partialForm, desi: Number(Number(partialForm.volume) / 3000).toFixed(4) })
    }, [partialForm.volume])

    useEffect(() => {

        // ldm hesaplama
        var x = (Number(partialForm.length) * Number(partialForm.width) * Number(partialForm.piece)) / 2.4 / 10000;
        if (partialForm.isStacking) {
            x = x / 2;
        }
        var res = x;
        res = Math.round(res * 100) / 100;
        if (isNaN(res)) {
            res = "0";
        }
        setPartialForm({ ...partialForm, ldm: res.toFixed(4) })
        // hacim hesaplama
        setPartialForm({ ...partialForm, volume: Number(Number(partialForm.width) * Number(partialForm.length) * Number(partialForm.height) * Number(partialForm.piece)).toFixed(4) })

    }, [partialForm.width, partialForm.length, partialForm.height, partialForm.piece, partialForm.isStacking])

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
                <Row gutter={[24, 12]}>
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
                                onChange={v => setPartialForm({ ...partialForm, cargoTypeIdList: v ? [v] : [] })}
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
                                onChange={v => setPartialForm({ ...partialForm, packagingTypeId: v })}
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
                                onChange={v => setPartialForm({ ...partialForm, loadTypeIdList: v ? [v] : [] })}
                            >
                                {
                                    loadTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={12} xl={3} xxl={3}>
                        <Form.Item
                            label={t("En")}
                            name="width"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                             <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setPartialForm({ ...partialForm, width: value })}
                                placeholder={t("En girin")}
                                size="large"
                                maxLength={10}
                                addonAfter="cm"
                                style={{ width: "100%" }}
                                min={0}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={12} xl={3} xxl={3}>
                        <Form.Item
                            label={t("Boy")}
                            name="length"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                             <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setPartialForm({ ...partialForm, length: value })}
                                placeholder={t("Boy girin")}
                                size="large"
                                maxLength={10}
                                addonAfter="cm"
                                style={{ width: "100%" }}
                                min={0}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={12} xl={3} xxl={3}>
                        <Form.Item
                            label={t("Yükseklik")}
                            name="height"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                             <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setPartialForm({ ...partialForm, height: value })}
                                placeholder={t("Yükseklik girin")}
                                size="large"
                                maxLength={10}
                                addonAfter="cm"
                                style={{ width: "100%" }}
                                min={0}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={12} xl={3} xxl={3}>
                        <Form.Item
                            label={t("Desi")}
                            name="desi"
                        >
                             <NumberFormat
                                customInput={Input}
                                disabled
                                placeholder={t("Hesaplanmadı")}
                                size="large"
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={4} xxl={3}>
                        <Form.Item
                            label={t("Adet")}
                            name="piece"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                             <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setPartialForm({ ...partialForm, piece: value })}
                                placeholder={t("Adet girin")}
                                size="large"
                                style={{ width: "100%" }}
                                min={0} />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={4} xxl={4}>
                        <Form.Item
                            label={t("Toplam Ağırlık")}
                            name="tonnage"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                             <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setPartialForm({ ...partialForm, tonnage: value })}
                                placeholder={t("Toplam ağırlığı girin")}
                                size="large"
                                addonAfter="kg"
                                style={{ width: "100%" }}
                                min={1} />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={4} xxl={5}>
                        <Form.Item
                            label={t("Mal Değeri")}
                            name="goodsPrice"
                            extra={<span style={{ opacity: .5 }}> {t("*Sigorta yapılırken baz alınacaktır.")}</span>}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setPartialForm({ ...partialForm, goodsPrice: value })}
                                placeholder={t("Mal değerini girin")}
                                size="large"
                                addonAfter={priceAfter}
                                style={{ width: "100%" }}
                                min={0}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={24}>
                        <Form.Item
                            label={t("Açıklama")}
                            name="explanation"
                        >
                            <Input.TextArea
                                onChange={e => setPartialForm({ ...partialForm, explanation: e.target.value })}
                                placeholder={t("Varsa eklemek istediğiniz notları giriniz.")}
                                size="large"
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                    <Col xs={12}>
                        <Form.Item
                            style={{ margin: 0 }}
                            labelCol={20}
                            wrapperCol={4}
                            name="advertisementTransportType"
                        >
                            <Checkbox checked={partialForm.advertisementTransportType === 'HIGHWAY' || partialForm.advertisementTransportType === 'SEAWAY_AND_HIGHWAY'} onChange={e => setPartialForm({ ...partialForm, advertisementTransportType: e.target.checked && partialForm.advertisementTransportType === 'SEAWAY' ? 'SEAWAY_AND_HIGHWAY' : !e.target.checked && partialForm.advertisementTransportType === 'SEAWAY_AND_HIGHWAY' ? 'SEAWAY' : e.target.checked ? 'HIGHWAY' : '' })}>{t("Kara yolu ile taşınabilir.")}</Checkbox>
                        </Form.Item>
                    </Col>
                    <Col xs={12}>
                        <Form.Item
                            style={{ margin: 0 }}
                            name="isStacking"
                        >
                            <Checkbox checked={partialForm.isStacking} onChange={e => setPartialForm({ ...partialForm, isStacking: e.target.checked })}> {t("İstiflenebilir.")}</Checkbox>
                        </Form.Item>
                    </Col>
                    <Col xs={12}>
                        <Form.Item
                            style={{ margin: 0 }}
                            labelCol={20}
                            wrapperCol={4}
                            name="advertisementTransportType"
                        >
                            <Checkbox checked={partialForm.advertisementTransportType === 'SEAWAY' || partialForm.advertisementTransportType === 'SEAWAY_AND_HIGHWAY'} onChange={e => setPartialForm({ ...partialForm, advertisementTransportType: e.target.checked && partialForm.advertisementTransportType === 'HIGHWAY' ? 'SEAWAY_AND_HIGHWAY' : !e.target.checked && partialForm.advertisementTransportType === 'SEAWAY_AND_HIGHWAY' ? 'HIGHWAY' : e.target.checked ? 'SEAWAY' : '' })}>{t("Deniz yolu ile taşınabilir.")}</Checkbox>
                        </Form.Item>
                    </Col>
                    <Col xs={12}>
                        <Form.Item
                            style={{ margin: 0 }}
                            name="isPorter"
                        >
                            <Checkbox checked={partialForm.isPorter} onChange={e => setPartialForm({ ...partialForm, isPorter: e.target.checked })}> {t("Hammaliye istiyorum.")}</Checkbox>
                        </Form.Item>
                    </Col>
                </Row>
            </div >
        </Form>
    );
};

export default PartialStep3;