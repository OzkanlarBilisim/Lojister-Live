import { Button, Col, Divider, Row, Steps, Alert } from "antd";
import moment from "moment";
import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { getAdvertisementSimpleHideByIdRequest, getAllAdvertisementYurtdisiDetay, getAllAdvertisementYurtdisiDetayFirstBid } from "../api/controllers/client-advertisiment-controller";
import CreateBidAbroud from "./CreateBidAbroud";
import CustomTitle from "./CustomTitle";
import RequestError from "./RequestError";
import SuspenseFallbackInline from "./SuspenseFallbackInline";

const TransportDetailAbroad = ({ data, mySwiper, setShowDetail, ref }) => {
    const { t, i18n } = useTranslation("common");

    const currentUser = useSelector((state) => state.user);
    const {advertID} = useParams();

    const [transport, setTransport] = useState({})
    const [bidModalVisible, setBidModalVisible] = useState(false);
    const [isloading, setIsloading] = useState(false)
    const [firstBid, setFirstBid] = useState(true)

    const ContentLabel = ({ title, content }) => {
        return (
            <>
                <p className="card-label-title">{title}</p>
                <p className="card-label-content">{content}</p>
            </>
        )
    }

    const fetchTransportInfo = async () => {
        setIsloading(true)
        try {
            let res = await getAllAdvertisementYurtdisiDetay(advertID);
            if (res) {
                setTransport(res.data)
                setIsloading(false)
            }
        } catch (error) {
            RequestError(error);
        }
    }
    const fetchTransportInfoFirstBid = async () => {
        setIsloading(true)
        try {
            let res = await getAllAdvertisementYurtdisiDetayFirstBid(currentUser?.companyId, advertID);
            if (res) {
                console.log(res);
                if(res.data.length > 0){
                    setFirstBid(false);
                }
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        if (advertID) {
            fetchTransportInfo()
            if(currentUser !== null){
                fetchTransportInfoFirstBid()
            }
        }
    }, [data])

    return (

        <div ref={ref} className="TransportDetailAbroad" style={{}}>
            {
                isloading ?
                    <div style={{ height: "450px" }}>

                        <SuspenseFallbackInline />
                    </div>
                    :
                    <>
                        < CreateBidAbroud visible={bidModalVisible} setVisible={setBidModalVisible} transportId={transport?.id} setFirstBid={setFirstBid} transport={transport} />

                        <div style={{ display: "flex" }}>
                            <Button
                                /* disabled={isLoading} */
                                onClick={() => setShowDetail(false)}
                                type="primary"
                                icon={<i style={{ marginRight: ".5rem" }} className="bi bi-arrow-left-circle-fill"></i>}
                            >{t("Geri dön")}</Button>
                        </div>

                        <CustomTitle style={{ marginTop: "16px" }}>{t("Genel Bilgiler")}</CustomTitle>

                        <div className="transport-simple-abroad" >
                            <Steps current={1} size="small" style={{ }}>
                                <Steps.Step
                                    title={transport.startSelectCountryName}
                                    description={transport.startDate +" "+ transport.dueDate}
                                />
                                <Steps.Step
                                    title={transport.dueSelectCountryName}
                                />
                            </Steps>
                            {
                                currentUser?.role === "ROLE_CLIENT" ?
                                    <></> :
                                    firstBid&&
                                    <Button onClick={() => setBidModalVisible(true)} type="primary">{t("Hemen teklif ver")}</Button>
                            }
                        </div>

                        <Divider />

                        <Row gutter={[12, 12]} >
                            <Col xs={12} md={12} lg={8} xl={6}>
                                <ContentLabel
                                    title={t("Taşıma Tipi")}
                                    content={
                                        t(transport.clientAdvertisementType + " / " + transport.vehicleOrContainer)
                                    } />
                            </Col>
                            <Col xs={12} md={12} lg={8} xl={6}>
                                <ContentLabel
                                    title={t("Taşıma Şekli")}
                                    
                                    content={
                                        transport.startDeliveryMethod === "d2d" ?
                                            t("Kapıdan Kapıya"):
                                        transport.startDeliveryMethod === "p2p" ?
                                            t("Limandan Limana") :
                                        transport.startDeliveryMethod === "p2d" ?
                                            t("Limandan Kapıya") :
                                            t("Kapıdan Limana")
                                    } />
                            </Col>
                            <Col xs={12} md={12} lg={8} xl={6}>
                                <ContentLabel
                                    title={t("Yükleme Tam Adresi")}
                                    content={
                                        t(transport.dueFullAddress)
                                    } />
                            </Col>
                            <Col xs={12} md={12} lg={8} xl={6}>
                                <ContentLabel
                                    title={t("Teslima Tam Adresi")}
                                    content={
                                        t(transport.startFullAddress)
                                    } />
                            </Col>
                        </Row>

                        <CustomTitle style={{ marginTop: "16px" }}>{t("Yük Bilgileri")}</CustomTitle>
                        {
                            transport.clientAdvertisementType === "CONTAINER" ?
                                <Row gutter={[12, 12]}>
                                    <Col xs={12} md={12} lg={8} xl={6}>
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
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Toplam Ağırlık")}
                                            content={transport?.tonnage ? transport?.tonnage / 1000 + " " + t("ton") : "-"}
                                        />
                                    </Col>
                                </Row> :
                                <Row gutter={[12, 12]}>
                                    {
                                        transport.unCode !== null &&
                                                
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <Alert message="İmolu Ürün" type="warning" showIcon closable />
                                        </Col>
                                    }
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Yük Tipi")}
                                            content={transport.cargoTypeIdList !== null ? t(transport.cargoTypeIdList) : t("Farketmez")}
                                        />
                                    </Col>
                                    {
                                        transport.clientAdvertisementType === "FTL" &&
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Paketleme Tipi")}
                                                content={transport.packagingTypeId !== null ? t(transport.packagingTypeId) : t("Farketmez")}
                                            />
                                        </Col>
                                    }
                                    {
                                        transport.clientAdvertisementType === "FTL" &&
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Yükleme Şekli")}
                                                content={transport.loadTypeIdList !== null ? t(transport.loadTypeIdList) : t("Farketmez")}
                                            />
                                        </Col>
                                    }
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Hs kod")}
                                            content={transport.hsCode !== null ? t(transport.hsCode) : t("-")}
                                        />
                                    </Col>
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Un kod")}
                                            content={transport.unCode !== null ? t(transport.hsCode) : t("-")}
                                        />
                                    </Col>
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Teslimat Şekli")}
                                            content={transport.deliveryType !== null ? t(transport.deliveryType) : t("-")}
                                        />
                                    </Col>
                                    
                                    {
                                        transport.clientAdvertisementType === "PARSİYEL" &&
                                            transport.vehicleOrContainer !== "Kara Yolu" &&
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("En x Boy x Yükseklik x Adet")}
                                                content={transport.width+" "+transport.length+" "+transport.height+" "+transport.piece}
                                            />
                                        </Col>
                                    }
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Toplam Ağırlık")}
                                            content={transport.clientAdvertisementType === "PARTİYEL" ?
                                                (transport?.tonnage ? transport?.tonnage + " kg" : "-") :
                                                (transport?.tonnage ? transport?.tonnage  + " " + t("ton") : "-")}
                                        />
                                    </Col>
                                    <Col xs={12} md={12} lg={8} xl={6}>
                                        <ContentLabel
                                            title={t("Açıklama")}
                                            content={transport?.explanation !== null ? t(transport?.explanation) : t("-")}
                                        />
                                    </Col>
                                </Row>
                        }
                        {
                            transport.clientAdvertisementType === "FTL" &&
                                transport.vehicleOrContainer === "Kara Yolu" &&
                                    <CustomTitle style={{ marginTop: "16px" }}>{t("İstenilen Araç Bilgileri")}</CustomTitle>
                        }
                        {
                            transport.clientAdvertisementType === "FTL" &&
                                transport.vehicleOrContainer === "Kara Yolu" &&
                                    <Row gutter={[12, 12]}>
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Araç Tipi")}
                                                content={transport?.vehicleTypes !== null ? t(transport.vehicleTypeIdList) : t("Farketmez")} />
                                        </Col>
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Araç Sayısı")}
                                                content={transport?.vehicleCount !== null ? t(transport.vehicleCount) : t("Farketmez")} />
                                        </Col>
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Kasa Tipi")}
                                                content={transport?.trailerTypeIdList !== null ? t(transport.trailerTypeIdList) : t("Farketmez")} />
                                        </Col>
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Zemin Tipi")}
                                                content={transport?.trailerFloorTypeIdList !== null ? t(transport.trailerFloorTypeIdList) : t("Farketmez")} />
                                        </Col>
                                        <Col xs={12} md={12} lg={8} xl={6}>
                                            <ContentLabel
                                                title={t("Kasa Özellikleri")}
                                                content={transport?.trailerFeatureName !== null ? t(transport.trailerFeatureName) : t("Farketmez")} />
                                        </Col>
                                    </Row>
                        }
                    </>
            }
        </div >

    )
}

export default TransportDetailAbroad