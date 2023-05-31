import { Button, Col, Row, Form, Select, Input, Switch, Tooltip, InputNumber, Checkbox, Alert } from 'antd';
import React, { useEffect, useState } from 'react'
import { getCargoTypesRequest } from '../../../../../api/controllers/cargo-type-controller';
import { getLoadTypesRequest } from '../../../../../api/controllers/load-type-controller';
import { getPackagingTypesRequest } from '../../../../../api/controllers/packaging-type-controller';
import { getCurrencyUnitsRequest } from '../../../../../api/controllers/currency-unit-controller';
import { CustomButton } from '../../../../CustomButton';
import RequestError from '../../../../RequestError';
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';

//FTL STEP 3
const ContainerOutsideStep2 = ({ handleClickNextStep, handleClickPrevStep, containeroutsidepartForm, setContaineroutsidepartForm }) => {
    const { t, i18n } = useTranslation("common");
    const handleChange = (value) => {
        containeroutsidepartForm.deliveryType = value;
        console.log( value);
    }
    const formValues = {
        cargoTypeIdList: containeroutsidepartForm.cargoTypeIdList,
        packagingTypeId: containeroutsidepartForm.packagingTypeId,
        loadTypeIdList: containeroutsidepartForm.loadTypeIdList,
        width: containeroutsidepartForm.width,
        height: containeroutsidepartForm.height,
        length: containeroutsidepartForm.length,
        desi: containeroutsidepartForm.desi,
        piece: containeroutsidepartForm.piece,
        tonnage: containeroutsidepartForm.tonnage ? containeroutsidepartForm.tonnage : null,
        goodsPrice: containeroutsidepartForm.goodsPrice,
        explanation: containeroutsidepartForm.explanation,
        isPorter: containeroutsidepartForm.isPorter,
        isStacking: containeroutsidepartForm.isStacking,
        isHighway: containeroutsidepartForm.isHighway,
        isSeaway: containeroutsidepartForm.isSeaway,
        hsCode: containeroutsidepartForm.hsCode,
        deliveryType: containeroutsidepartForm.deliveryType,
        unCode: containeroutsidepartForm.unCode,
        Kontrol: containeroutsidepartForm.kontrol,

    };

    const [cargoTypes, setCargoTypes] = useState([]);
    const [packagingTypes, setPackagingTypes] = useState([]);
    const [loadTypes, setLoadTypes] = useState([]);
    const [currencyUnits, setCurrencyUnits] = useState([]);
    

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
        <Select defaultValue={containeroutsidepartForm.currencyUnitId} onChange={v => setContaineroutsidepartForm({ ...containeroutsidepartForm, currencyUnitId: v })} size="large">
            {
                currencyUnits.map(v =>
                    <Select.Option key={v.id} value={v.currencySymbol}>{v.currencySymbol}</Select.Option>
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
    }, [containeroutsidepartForm])


    useEffect(() => {
        // desi hesaplama
        setContaineroutsidepartForm({ ...containeroutsidepartForm, desi: Number(Number(containeroutsidepartForm.volume) / 3000).toFixed(4) })
    }, [containeroutsidepartForm.volume])

    useEffect(() => {

        // ldm hesaplama
        var x = (Number(containeroutsidepartForm.length) * Number(containeroutsidepartForm.width) * Number(containeroutsidepartForm.piece)) / 2.4 / 10000;
        if (containeroutsidepartForm.isStacking) {
            x = x / 2;
        }
        var res = x;
        res = Math.round(res * 100) / 100;
        if (isNaN(res)) {
            res = "0";
        }
        setContaineroutsidepartForm({ ...containeroutsidepartForm, ldm: res.toFixed(4) })
        // hacim hesaplama
        setContaineroutsidepartForm({ ...containeroutsidepartForm, volume: Number(Number(containeroutsidepartForm.width) * Number(containeroutsidepartForm.length) * Number(containeroutsidepartForm.height) * Number(containeroutsidepartForm.piece)).toFixed(4) })

    }, [containeroutsidepartForm.width, containeroutsidepartForm.length, containeroutsidepartForm.height, containeroutsidepartForm.piece, containeroutsidepartForm.isStacking])

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
                    <Col xs={18} xl={9} xxl={6}>
                        <Form.Item
                            label={t("Yük Tipi")}
                            name="cargoTypeIdList"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <Select
                                size="large"
                                placeholder={t("Yük tipini seçin")}
                                allowClear
                                onChange={v => setContaineroutsidepartForm({ ...containeroutsidepartForm, cargoTypeIdList: v })}
                            >
                                {
                                    cargoTypes.map(val => (
                                        <Select.Option key={val.id} value={val.typeName}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={6} xl={3} xxl={2}>

                        <Form.Item
                            label={t("IMO'lu Ü  rün")}
                            name="hsCode/unCode"
                        >     
                        <div className='hs/un'style={{display:"flex"}}>
                            <Switch checkedChildren="Un Kod" unCheckedChildren="UN Kod" 
                             defaultChecked={containeroutsidepartForm.kontrol}
                             onChange={e => setContaineroutsidepartForm({ ...containeroutsidepartForm, kontrol: e })} />
                            &nbsp; &nbsp; 
                            <Tooltip placement="right" title=
                                        {t("* MSDS Bilgisi istenen yükler için lütfen UN kodu seçiniz.")}
                                  >
                                        <span>
                                            <i className="bi bi-info-circle-fill"></i>
                                        </span>
                                    </Tooltip>
                            
                        </div>
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
                                onChange={v => setContaineroutsidepartForm({ ...containeroutsidepartForm, packagingTypeId: v })}
                            >
                                {
                                    packagingTypes.map(val => (
                                        <Select.Option key={val.id} value={val.typeName}>{val.typeName}</Select.Option>
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
                                onChange={v => setContaineroutsidepartForm({ ...containeroutsidepartForm, loadTypeIdList: v })}
                            >
                                {
                                    loadTypes.map(val => (
                                        <Select.Option key={val.id} value={val.typeName}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Hs Kod")}
                            name="hsCode"
                        >
                            <NumberFormat
                                customInput={Input}
                                onChange={e => setContaineroutsidepartForm({ ...containeroutsidepartForm, hsCode: e.target.value })}
                                placeholder={t("Hs Kod girin")}
                                size="large"
                                min={0}
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
              {(containeroutsidepartForm.kontrol) && 
              <Col xs={24} xl={12} xxl={8}>
              <Form.Item
                            label={t("Un Kod")}
                            name="unCode"
                            rules={[{ required: containeroutsidepartForm.kontrol, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <NumberFormat
                                customInput={Input}
                                onChange={e => setContaineroutsidepartForm({ ...containeroutsidepartForm, unCode: e.target.value })}
                                placeholder={t("UN Kod girin")}
                                size="large"
                                min={0}
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                </Col>
                }
                
                        <Col xs={24} xl={12} xxl={4}>
                        <Form.Item
                            label={t("Teslimat Şekli")}
                            name="deliveryType"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            
                            <Select 
                                        size="large"
                                        placeholder={t("Teslimat Şeklini seçin")}
                                        onChange={handleChange}
                                        allowClear
                                        options={[
                                            {
                                            value: 'Fas',
                                            label: 'FAS',
                                            },
                                            {
                                            value: 'Fob',
                                            label: 'FOB',
                                            },
                                            {
                                            value: 'Cfr',
                                            label: 'CFR',
                                            },
                                            {
                                            value: 'Cif',
                                            label: 'CIF',
                                            },
                                            {
                                            value: 'Exw',
                                            label: 'EXW',
                                            },
                                            {
                                            value: 'Fca',
                                            label: 'FCA',
                                            },
                                            {
                                            value: 'Cpt',
                                            label: 'CIF ',
                                            },
                                            {
                                            value: 'Cip',
                                            label: 'CIP',
                                             },
                                             {
                                            value: 'Dat',
                                            label: 'DAT',
                                            },
                                            {
                                            value: 'Dap',
                                            label: 'DAP',
                                            },
                                            {
                                            value: 'Ddp',
                                            label: 'DDP',
                                            },
                                        ]}
                                    /> 
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
                                onValueChange={({ value }) => setContaineroutsidepartForm({ ...containeroutsidepartForm, width: value })}
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
                                onValueChange={({ value }) => setContaineroutsidepartForm({ ...containeroutsidepartForm, length: value })}
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
                                onValueChange={({ value }) => setContaineroutsidepartForm({ ...containeroutsidepartForm, height: value })}
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
                                onValueChange={({ value }) => setContaineroutsidepartForm({ ...containeroutsidepartForm, piece: value })}
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
                                onValueChange={({ value }) => setContaineroutsidepartForm({ ...containeroutsidepartForm, tonnage: value })}
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
                                onValueChange={({ value }) => setContaineroutsidepartForm({ ...containeroutsidepartForm, goodsPrice: value })}
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
                                onChange={e => setContaineroutsidepartForm({ ...containeroutsidepartForm, explanation: e.target.value })}
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
                        </Form.Item>
                    </Col>
                </Row>
            </div >
        </Form>
    );
};

export default ContainerOutsideStep2;