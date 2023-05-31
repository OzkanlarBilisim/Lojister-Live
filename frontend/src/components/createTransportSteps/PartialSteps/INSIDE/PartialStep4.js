import { Button, Col, Row, Form, Select } from 'antd';
import React, { useEffect, useState } from 'react'
import { getVehicleTypesRequest } from '../../../../api/controllers/vehicle-type-controller';
import { getTrailerFloorTypesRequest } from '../../../../api/controllers/trailer-floor-type-controller';
import { getTrailerFeaturesRequest } from '../../../../api/controllers/trailer-feature-controller';
import { CustomButton } from '../../../CustomButton';
import { getTrailerTypesRequest } from '../../../../api/controllers/trailer-type-controller';
import RequestError from '../../../RequestError';
import { useTranslation } from 'react-i18next';


//Partial STEP 4
const PartialStep4 = ({ handleClickNextStep, handleClickPrevStep, partialForm, setPartialForm }) => {
    const { t, i18n } = useTranslation("common");/*  */

    const formValues = {
        trailerFeatureIdList: partialForm.trailerFeatureIdList,
        trailerTypeIdList: partialForm.trailerTypeIdList,
        trailerFloorTypeIdList: partialForm.trailerFloorTypeIdList,
        vehicleCount: partialForm.vehicleCount,
        vehicleTypeIdList: partialForm.vehicleTypeIdList,
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

    useEffect(() => {
        fetchVehicleTypes();
        fetchTrailerTypes();
        fetchTrailerFloorTypes();
        fetchTrailerFeatures();
    }, []);


    //if changed form set new value
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue(formValues)
    }, [partialForm])

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
                                onChange={v => setPartialForm({ ...partialForm, vehicleTypeIdList: [v] })}
                            >
                                {
                                    vehicleTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
                                    ))
                                }
                            </Select>
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
                                onChange={v => setPartialForm({ ...partialForm, trailerTypeIdList: [v] })}
                            >
                                {
                                    trailerTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
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
                                onChange={v => setPartialForm({ ...partialForm, trailerFloorTypeIdList: [v] })}
                            >
                                {
                                    trailerFloorTypes.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.typeName}</Select.Option>
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
                                onChange={v => setPartialForm({ ...partialForm, trailerFeatureIdList: v })}
                            >
                                {
                                    trailerFeatures.map(val => (
                                        <Select.Option key={val.id} value={val.id}>{val.featureName}</Select.Option>
                                    ))
                                }
                            </Select>
                        </Form.Item>
                    </Col>
                </Row>
            </div >

        </Form>
    );
};

export default PartialStep4;