import React, { useEffect, useState } from 'react'
import { Button, Col, Divider, Row, Spin, Steps, Tabs } from 'antd';
import moment from 'moment';
import { reverseGeocodeRequest } from '../api/controllers/addresses-controller';
import { downloadInsuranceFileByTransportCodeRequest } from '../api/controllers/insured-transport-process-file-controller';
import AntNotification from './AntNotification';
import ShowProfilePhoto from './ShowProfilePhoto';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';

const TransportInfoForTrackingMap = ({ transportProcess, closeClick, driverLastPosition }) => {
    const { t, i18n } = useTranslation("common");
    const [bidModalVisible, setBidModalVisible] = useState(false);

    const transport = transportProcess?.acceptedClientAdvertisementBid?.clientAdvertisement;
    const vehicle = transportProcess?.vehicle;
    const driver = transportProcess?.vehicle?.driver;
    const [trackingAddress, setTrackingAddress] = useState(null);
    const [isLoading, setIsLoading] = useState(true)

    const ContentLabel = ({ title, content }) => {
        return (
            <>
                <p className="card-label-title">{title}</p>
                <p className="card-label-content">{content}</p>
            </>
        )
    }

    const getReverseGeocode = async () => {
        setIsLoading(true);
        try {
            let res = await reverseGeocodeRequest({
                latitude: driverLastPosition.latitude,
                longitude: driverLastPosition.longitude,
            });
            if (res) {
                setTrackingAddress(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    const downloadInsurance = async () => {
        try {
            let res = await downloadInsuranceFileByTransportCodeRequest(transportProcess.transportCode)
            if (res) {
                AntNotification({ type: "success", message: t("Sigorta poliçesi indiriliyor...") })
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        if (driverLastPosition) {
            getReverseGeocode();
        } else {
            setTimeout(() => {
                setIsLoading(false)
            }, 2000);
        }
    }, [driverLastPosition])


    return (
        <div className="transport-info-card-map" id="map-info-card">
            <button onClick={() => closeClick()} className="close-button">
                <i className="bi bi-x-lg"></i>
            </button>
            {
                transport ?
                    <Tabs defaultActiveKey={"1"}>
                        <Tabs.TabPane tab={t("Genel Bilgiler")} key="1">
                            <div className="transport-simple-header">
                                <Steps current={
                                    transportProcess?.admissionDate && !transportProcess?.deliveryDate ? 1 :
                                        transportProcess?.admissionDate && transportProcess?.deliveryDate ? 2 :
                                            0} size="small">
                                    <Steps.Step
                                        title={<span style={{ fontWeight: 500 }}>{t("Teslim Alındı")}</span>}
                                        description={
                                            <div>
                                                <p>{transport.startingAddress.province ? transport.startingAddress.province + ", " + transport.startingAddress.district : t("Adres bilinmiyor!")}</p>
                                                <p>{transportProcess?.admissionLocalDateTime ? moment(transportProcess?.admissionLocalDateTime).format("LLL") : t("Henüz teslim alınmadı!")}</p>
                                            </div>
                                        }
                                    />
                                    <Steps.Step
                                        title={<span style={{ fontWeight: 500 }}> {t("Yolda")} </span>}
                                        description={
                                            isLoading ? <Spin /> :
                                                <div>
                                                    <p>{trackingAddress?.province ? trackingAddress?.province + ", " + trackingAddress?.district : t("Adres bilinmiyor!")}</p>
                                                    <p>{driverLastPosition?.logLocalDateTime ? moment(driverLastPosition?.logLocalDateTime).format("LLL") : t("Henüz teslim alınmadı!")}</p>
                                                </div>
                                        }
                                    />
                                    <Steps.Step
                                        title={<span style={{ fontWeight: 500 }}> {t("Teslim Edildi")} </span>}
                                        description={
                                            !transport?.advertisementStatus === "completed" ?
                                                t("Henüz teslim edilmedi!")
                                                :
                                                <div>
                                                    <p>{transport.dueAddress.province ? transport.dueAddress.province + ", " + transport.dueAddress.district : t("Adres bilinmiyor!")}</p>
                                                    <p>{transportProcess?.deliveryLocalDateTime ? moment(transportProcess?.deliveryLocalDateTime).format("LLL") : t("Henüz teslim edilmedi!")}</p>
                                                </div>
                                        }
                                    />
                                </Steps>
                            </div>
                            <Divider style={{ margin: ".5rem 0" }} />

                            <Row gutter={[12, 12]}>
                                <Col xs={24} md={8}>
                                    <ContentLabel
                                        title={t("Taşıma Numarası")}
                                        content={transportProcess.transportCode} />
                                </Col>
                                <Col xs={24} md={8}>
                                    <ContentLabel
                                        title={t("Taşıma Tipi")}
                                        content={transport.clientAdvertisementType === "FTL" ?
                                            t("FTL - Tam Yük") :
                                            transport.clientAdvertisementType === "PARTIAL" ?
                                                t("Parsiyel") :
                                                transport.clientAdvertisementType === "CONTAINER" ?
                                                    t("Konteyner") :
                                                    t("Bilinmiyor")} />
                                </Col>
                                <Col xs={24} md={8}>
                                    <ContentLabel
                                        title={t("Sigorta")}
                                        content={
                                            <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                                                <span>
                                                    {
                                                        transportProcess.insuranceType === "NARROW" ? t("Dar Teminat") :
                                                            transportProcess.insuranceType === "COMPREHENSIVE" ? t("Geniş Teminat") :
                                                                t("Sigorta yapılmadı")
                                                    }
                                                </span>
                                                {
                                                    transportProcess.isExistInsuredFile &&
                                                    <Button onClick={() => downloadInsurance()} type="primary" icon={<i className="bi bi-file-earmark-arrow-down"></i>}></Button>
                                                }
                                            </div>
                                        } />
                                </Col>
                            </Row>
                        </Tabs.TabPane>
                        <Tabs.TabPane tab={t("Taşınacak Yük Detayları")} key="2">
                            <Row gutter={[12, 12]}>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Yük Tipi")}
                                        content={transport?.cargoTypes.length > 0 ? transport?.cargoTypes?.map((v, i) => v?.typeName + (i === transport?.cargoTypes.length - 1 ? "" : ", ")) : t("Farketmez")}
                                    />
                                </Col>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Paketleme Tipi")}
                                        content={transport?.packagingType?.typeName ? transport?.packagingType?.typeName : "-"}
                                    />
                                </Col>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Yükleme Şekli")}
                                        content={transport?.loadType.length > 0 ? transport?.loadType?.map((v, i) => v?.typeName + (i === transport?.loadType.length - 1 ? "" : ", ")) : t("Farketmez")}
                                    />
                                </Col>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Toplam Ağırlık")}
                                        content={transport.clientAdvertisementType === "PARTIAL" ?
                                            (transport?.tonnage ? transport?.tonnage + " kg" : "-") :
                                            (transport?.tonnage ? transport?.tonnage / 1000 + " " + t("ton") : "-")}
                                    />
                                </Col>
                                {
                                    transport.clientAdvertisementType === "PARTIAL" &&
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("Ebatlar")}
                                            content={((transport?.width ? transport?.width : 0) + "/" + (transport?.length ? transport?.length : 0) + "/" + (transport?.height ? transport?.height : 0) + " cm") + " " + (transport?.desi ? transport?.desi + t("desi") : "")}
                                        />
                                    </Col>
                                }
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Mal Değeri")}
                                        content={transport?.goodsPrice ? transport?.goodsPrice + " " + transport?.currencyUnit?.currencySymbol : "-"}
                                    />
                                </Col>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Hammaliye")}
                                        content={transport?.isPorter ? t("İstiyorum") : t("İstemiyorum")}
                                    />
                                </Col>
                                {
                                    transport.clientAdvertisementType === "PARTIAL" &&
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("İstiflenebilir")}
                                            content={transport?.isStacking ? t("Evet") : t("Hayır")}
                                        />
                                    </Col>
                                }
                            </Row>
                        </Tabs.TabPane>
                        <Tabs.TabPane tab={t("Şoför & Araç Detayları")} key="3">
                            <Row gutter={[12, 12]}>
                                <Col span={24}>
                                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                        <div style={{ display: "flex", alignItems: "center", gap: ".5rem" }}>
                                            <ShowProfilePhoto profileId={driver?.id} />
                                            <p>{driver?.firstName + " " + driver?.lastName}</p>
                                        </div>
                                        <a href={`tel:${driver?.company?.phone}`}>
                                            <Button type="primary" icon={<i style={{ marginRight: ".5rem" }} className="bi bi-telephone-outbound"></i>}>{driver?.company?.phone}</Button>
                                        </a>
                                    </div>
                                </Col>
                                <Divider style={{ margin: ".5rem 0" }} />
                                <Col xs={24} md={12} xl={6}>
                                    <ContentLabel
                                        title={t("Araç Markası")}
                                        content={vehicle?.brand ? vehicle?.brand : t("Bilinmiyor")} />
                                </Col>
                                <Col xs={24} md={12} xl={6}>
                                    <ContentLabel
                                        title={t("Araç Modeli")}
                                        content={vehicle?.vehicleModel ? vehicle?.vehicleModel : t("Bilinmiyor")} />
                                </Col>
                                <Col xs={24} md={12} xl={6}>
                                    <ContentLabel
                                        title={t("Araç Plakası")}
                                        content={vehicle?.licencePlate ? vehicle?.licencePlate : t("Bilinmiyor")} />
                                </Col>
                                <Col xs={24} md={12} xl={6}>
                                    <ContentLabel
                                        title={t("Dorse Plakası")}
                                        content={vehicle?.trailerPlate ? vehicle?.trailerPlate : t("Bilinmiyor")} />
                                </Col>
                            </Row>
                        </Tabs.TabPane>
                        {
                            transport.explanation &&
                            <Tabs.TabPane tab={t("Açıklama")} key="4">
                                {transport.explanation}
                            </Tabs.TabPane>
                        }
                    </Tabs> :
                    <div style={{ width: "100%", height: "100%", display: "flex", justifyContent: "center", alignItems: "center" }}>
                        <Spin />
                    </div>
            }
        </div >
    )
}

export default TransportInfoForTrackingMap;