import React, { useEffect, useState } from 'react'
import { Button, Col, Divider, Row, Spin, Steps, Tabs } from 'antd';
import moment from 'moment';
import { getAdvertisementSimpleHideByIdRequest } from '../api/controllers/client-advertisiment-controller';
import CreateBid from './CreateBid';
import { useSelector } from 'react-redux';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';

const TransportInfoForMap = ({ selectedTransport, setSelectedTransport, handleClickShowHideAds }) => {
    const { t, i18n } = useTranslation("common");

    const [bidModalVisible, setBidModalVisible] = useState(false);
    const currentUser = useSelector((state) => state.user);

    const ContentLabel = ({ title, content }) => {
        return (
            <>
                <p className="card-label-title">{title}</p>
                <p className="card-label-content">{content}</p>
            </>
        )
    }

    const [transport, setTransport] = useState(null)

    const fetchTransportInfo = async () => {
        try {
            let res = await getAdvertisementSimpleHideByIdRequest(selectedTransport.id);
            if (res) {
                setTransport(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const closeClick = () => {
        setSelectedTransport(null);
        let menu = document.getElementById("map-transport");
        if (menu.className === "map-transport") {
            handleClickShowHideAds()
        }
    }

    useEffect(() => {
        fetchTransportInfo()
    }, [selectedTransport])

    return (
        <div className="transport-info-card-map" id="map-info-card">
            <CreateBid visible={bidModalVisible} setVisible={setBidModalVisible} transportId={transport?.id} transport={transport} />
            <button onClick={() => closeClick()} className="close-button">
                <i className="bi bi-x-lg"></i>
            </button>
            {
                transport ?
                    <Tabs defaultActiveKey="1">
                        <Tabs.TabPane tab={t("Genel Bilgiler")} key="1">
                            <div className="transport-simple-header">
                                <Steps current={1} size="small">
                                    <Steps.Step
                                        title={transport.startingAddress.province + ", " + transport.startingAddress.district}
                                        description={moment(transport.adStartingDate).format("LL")}
                                    />
                                    <Steps.Step
                                        title={transport.dueAddress?.province + ", " + transport.dueAddress.district}
                                        description={moment(transport.adDueDate).format("LL")}
                                    />
                                </Steps>
                                {
                                    currentUser?.role === "ROLE_CLIENT" ?
                                        <></> :
                                        <Button onClick={() => setBidModalVisible(true)} type="primary">{t("Hemen teklif ver")}</Button>
                                }
                            </div>
                            <Divider />

                            <Row gutter={[12, 12]}>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("İlan Numarası")}
                                        content={transport.clientAdvertisementCode} />
                                </Col>
                                <Col xs={24} md={12}>
                                    <ContentLabel
                                        title={t("Taşıma Tipi")}
                                        content={
                                            transport.clientAdvertisementType === "FTL" ?
                                                t("FTL - Tam Yük") :
                                                transport.clientAdvertisementType === "PARTIAL" ?
                                                    t("Parsiyel") :
                                                    transport.clientAdvertisementType === "CONTAINER" ?
                                                        t("Konteyner") :
                                                        t("Bilinmiyor")} />
                                </Col>
                            </Row>
                        </Tabs.TabPane>
                        <Tabs.TabPane tab={t("Taşınacak Yük Detayları")} key="2">
                            {
                                transport.clientAdvertisementType === "CONTAINER" ?
                                    <Row gutter={[12, 12]}>
                                        <Col xs={24} md={12}>
                                            <ContentLabel
                                                title={t("Konteyner Tipi")}
                                                content={transport?.containerType === "TWENTY" ?
                                                    t("20'lik") :
                                                    transport?.containerType === "FOURTY" ?
                                                        t("40'lık") :
                                                        transport?.containerType === "FOURTY_HC" ?
                                                            t("40'lık HC") :
                                                            transport?.containerType === "FOURTY_FIVE" ?
                                                                t("45'lik") :
                                                                "-"}
                                            />
                                        </Col>
                                        <Col xs={24} md={12}>
                                            <ContentLabel
                                                title={t("Toplam Ağırlık")}
                                                content={transport?.tonnage ? transport?.tonnage / 1000 + " " + t("ton") : "-"}
                                            />
                                        </Col>
                                    </Row> :
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
                            }
                        </Tabs.TabPane>
                        {
                            transport.clientAdvertisementType !== "CONTAINER" &&
                            <Tabs.TabPane tab={t("İstenilen Araç Bilgileri")} key="3">
                                <Row gutter={[12, 12]}>
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("Araç Tipi")}
                                            content={transport?.vehicleTypes.length > 0 ? transport?.vehicleTypes?.map((v, i) => v?.typeName + (i === transport?.vehicleTypes.length - 1 ? "" : ", ")) : t("Farketmez")} />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("Araç Sayısı")}
                                            content={transport?.vehicleCount ? transport?.vehicleCount : "-"} />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("Kasa Tipi")}
                                            content={transport?.trailerTypes.length > 0 ? transport?.trailerTypes?.map((v, i) => v?.typeName + (i === transport?.trailerTypes.length - 1 ? "" : ", ")) : t("Farketmez")} />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("Zemin Tipi")}
                                            content={transport?.trailerFloorTypes.length > 0 ? transport?.trailerFloorTypes?.map((v, i) => v?.typeName + (i === transport?.trailerFloorTypes.length - 1 ? "" : ", ")) : t("Farketmez")} />
                                    </Col>
                                    <Col xs={24} md={12}>
                                        <ContentLabel
                                            title={t("Kasa Özellikleri")}
                                            content={transport?.trailerFeatures.length > 0 ? transport?.trailerFeatures?.map((v, i) => v?.featureName + (i === transport?.trailerFeatures.length - 1 ? "" : ", ")) : t("Farketmez")} />
                                    </Col>
                                </Row>
                            </Tabs.TabPane>
                        }
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

export default TransportInfoForMap;