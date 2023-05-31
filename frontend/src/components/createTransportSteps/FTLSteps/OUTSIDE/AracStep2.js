import { Button, Col, Row, Form, Select, Input, Switch, Tooltip} from 'antd';
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
const AracStep2 = ({ handleClickNextStep, handleClickPrevStep, aracForm, setAracForm }) => {
    const { t} = useTranslation("common");
// Taşıma Tipleri İçin Handle Change
    const handleChange = (value) => {
        aracForm.deliveryType = value;
        console.log( value);
    }


        const formValues = {
        cargoTypeIdList: aracForm.cargoTypeIdList,
        packagingTypeId: aracForm.packagingTypeId,
        loadTypeIdList: aracForm.loadTypeIdList,
        tonnage: aracForm.tonnage,
        goodsPrice: aracForm.goodsPrice,
        DeliveryType:aracForm.deliveryType,
        hsCode:aracForm.hsCode,
        explanation: aracForm.explanation,
        unCode: aracForm.unCode,
        Kontrol: aracForm.kontrol,
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
        <Select defaultValue={aracForm.currencyUnitId} onChange={v => setAracForm({ ...aracForm, currencyUnitId: v })} size="large">
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
    // useEffect(() => {
    //     form.setFieldsValue(formValues)
    // }, [aracForm])

    return (

        <Form
            form={form}
            onFinish={handleClickNextStep}
            labelCol={{ span: 24 }}
            wrapperCol={{ span: 24 }}
            initialValues={formValues}
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
                                onChange={v => setAracForm({ ...aracForm, cargoTypeIdList: v ? v : null })}
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
                            defaultChecked={aracForm.kontrol}
                            onChange={e => setAracForm({ ...aracForm, kontrol: e })} />
                            
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
                                onChange={v => setAracForm({ ...aracForm, packagingTypeId: v })}
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
                                onChange={v => setAracForm({ ...aracForm, loadTypeIdList: v ? v : null })}
                            >
                                {
                                    loadTypes.map(val => (
                                        <Select.Option key={val.id} value={val.typeName}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={4}>
                        <Form.Item
                            label={t("Hs Kod")}
                            name="hsCode"
                        >
                            <NumberFormat
                                customInput={Input}
                                onChange={e => setAracForm({ ...aracForm, hsCode: e.target.value })}
                                placeholder={t("Hs Kod girin")}
                                size="large"
                                min={0}
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                        </Col>
                        {(aracForm.kontrol) && 
                            <Col xs={24} xl={12} xxl={8}>
                                <Form.Item
                                    label={t("Un Kod")}
                                    name="unCode"
                                    rules={[{ required: aracForm.kontrol, message: t('Lütfen bu alanı doldurun!') }]}
                                >
                                    <NumberFormat
                                        customInput={Input}
                                        onChange={e => setAracForm({ ...aracForm, unCode: e.target.value })}
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
                            name="DeliveryType"
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
                    <Col xs={24} xl={12} xxl={4}>
                        <Form.Item
                            label={t("Toplam Ağırlık")}
                            name="tonnage"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setAracForm({ ...aracForm, tonnage: value})}
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
                            
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setAracForm({ ...aracForm, goodsPrice: value })}
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
                                onChange={e => setAracForm({ ...aracForm, explanation: e.target.value })}
                                placeholder={t("Varsa eklemek istediğiniz notları giriniz.")}
                                size="large"
                                style={{ width: "100%" }}
                            />
                        </Form.Item>
                    </Col>
                </Row>   
            </div >
        </Form >
    );
};

export default AracStep2;