import { Button, Col, Row, Form, Select, Input } from 'antd';
import React, { useEffect, useState } from 'react'
import { getVehicleTypesRequest } from '../../../../api/controllers/vehicle-type-controller';
import { getTrailerFloorTypesRequest } from '../../../../api/controllers/trailer-floor-type-controller';
import { getTrailerFeaturesRequest } from '../../../../api/controllers/trailer-feature-controller';
import { CustomButton } from '../../../CustomButton';
import { getTrailerTypesRequest } from '../../../../api/controllers/trailer-type-controller';
import RequestError from '../../../RequestError';
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';



//FTL STEP 4
const AracStep3 = ({ handleClickNextStep, handleClickPrevStep, aracForm, setAracForm }) => {
    const { t } = useTranslation("common");

    const formValues = {
        trailerFeatureIdList: aracForm.trailerFeatureIdList,
        trailerTypeIdList: aracForm.trailerTypeIdList,
        trailerFloorTypeIdList: aracForm.trailerFloorTypeIdList,
        vehicleCount: aracForm.vehicleCount,
        vehicleTypeIdList: aracForm.vehicleTypeIdList,

    };
    const [vehicleTypes, setVehicleTypes] = useState([]);
    const [trailerTypes, setTrailerTypes] = useState([]);
    const [trailerFloorTypes, setTrailerFloorTypes] = useState([]);
    const [trailerFeatures, setTrailerFeatures] = useState([]);

    const fetchVehicleTypes = async () => {
        try {
            let res = await getVehicleTypesRequest();
            if (res) {
                setVehicleTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchTrailerTypes = async () => {
        try {
            let res = await getTrailerTypesRequest();
            if (res) {
                setTrailerTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchTrailerFloorTypes = async () => {
        try {
            let res = await getTrailerFloorTypesRequest();
            if (res) {
                setTrailerFloorTypes(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const fetchTrailerFeatures = async () => {
        try {
            let res = await getTrailerFeaturesRequest();
            if (res) {
                setTrailerFeatures(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    };
    const trailerFeatureNameSelect = (v)=> {
            for(var i=0; i< v.length; i++){
                if(i == 0){
                    aracForm.trailerFeatureName = v[i]
                }else{
                    aracForm.trailerFeatureName += ", "+v[i]
                }
                
            }
            
            setAracForm({ ...aracForm, trailerFeatureIdList: v })

            console.log(aracForm.trailerFeatureIdList)
            console.log(aracForm.trailerFeatureName)
    }

    useEffect(() => {
        fetchVehicleTypes();
        fetchTrailerTypes();
        fetchTrailerFloorTypes();
        fetchTrailerFeatures();
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
            <Button
                        onClick={() => handleClickPrevStep()}
                        shape="circle"
                        type="primary"
                        icon={<i className="bi bi-arrow-left"></i>}>
            </Button>
           
                    <CustomButton
                        color="primary"
                        htmlType="submit"
                    >
                        {t("Sonraki Adıma Geç")}
                    </CustomButton>
                </div>
                    
                <Row gutter={[24, 24]}>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Araç Tipi")}
                            name="vehicleTypeIdList"
                        >
                            <Select
                                size="large"
                                placeholder={t("Farketmez")}
                                allowClear
                                onChange={v => setAracForm({ ...aracForm, vehicleTypeIdList: v })}
                            >
                                {
                                    vehicleTypes.map(val => (
                                        <Select.Option key={val.typeName} value={val.typeName}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Araç Sayısı")}
                            name="vehicleCount"
                            rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                        >
                            <NumberFormat
                                customInput={Input}
                                onValueChange={({ value }) => setAracForm({ ...aracForm, vehicleCount: value })}
                                placeholder={t("Araç sayısını girin")}
                                size="large"
                                style={{ width: "100%" }}
                                min={1} />
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Kasa Tipi")}
                            name="trailerTypeIdList"
                        >
                            <Select
                                size="large"
                                placeholder={t("Farketmez")}
                                allowClear
                                onChange={v => setAracForm({ ...aracForm, trailerTypeIdList: v })}
                            >
                                {
                                    trailerTypes.map(val => (
                                        <Select.Option key={val.id} value={val.typeName}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Zemin Tipi")}
                            name="trailerFloorTypeIdList"
                        >
                            <Select
                                size="large"
                                placeholder={t("Farketmez")}
                                allowClear
                                onChange={v => setAracForm({ ...aracForm, trailerFloorTypeIdList: v })}
                            >
                                {
                                    trailerFloorTypes.map(val => (
                                        <Select.Option key={val.id} value={val.typeName}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                    <Col xs={24} xl={12} xxl={8}>
                        <Form.Item
                            label={t("Kasa Özellikleri")}
                            name="trailerFeatureIdList"
                        >
                            <Select
                                size="large"
                                mode="multiple"
                                placeholder={t("Farketmez")}
                                allowClear
                                maxTagCount="responsive" 
                                onChange={trailerFeatureNameSelect}
                            >
                                {
                                    trailerFeatures.map(val => (
                                        <Select.Option key={val.id} value={val.featureName}>{val.featureName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                </Row>
             
            </div >
                                
        </Form >
    );
};

export default AracStep3;