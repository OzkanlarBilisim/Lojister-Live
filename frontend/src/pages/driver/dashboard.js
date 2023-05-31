import React, { useEffect, useState } from 'react';
import { Col, Row } from 'antd';
import CustomTitle from '../../components/CustomTitle';
import TransportCard from '../../components/TransportCard';
import { Link } from 'react-router-dom';
import { getDriverDashboardRequest } from '../../api/controllers/driver-controller'
import { ParsedDriver } from '../../components/ParsedValues';
import SuspenseFallbackInline from "../../components/SuspenseFallbackInline"
import RequestError from '../../components/RequestError';
import SupportBanner from '../../components/SupportBanner';
import NoneContent from '../../components/NoneContent';
import { useTranslation } from 'react-i18next';

const DashboardStatisticCard = ({ title, count }) => {
    return (
        <div className="dashboardStatisticCard">
            <p style={{ margin: "0" }}>{title}</p>
            <span>
                {count}
            </span>
        </div >
    )
}

const DriverDashboard = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Panelim")

    const [isLoading, setIsLoading] = useState(true)
    const [dashboardData, setDashboardData] = useState({
        activeAdvertisementCount: 0,
        clientAdvertisements: [],
        finishedAdvertisementCount: 0,
        lastMonthCount: 0,
        transportAdvertisementCount: 0,
    })

    const fetchDriverDashboard = async () => {
        setIsLoading(true)
        try {
            let res = await getDriverDashboardRequest();
            if (res) {
                setDashboardData(res.data)
            }
            setIsLoading(false)
        } catch (error) {
            RequestError(error);
        }
    };

    useEffect(() => {
        fetchDriverDashboard()
    }, [])

    return (
        isLoading ?
            <SuspenseFallbackInline /> :
            <div className="layout-content-padding">
                <div>
                    <CustomTitle>{t("Göstergeler")}</CustomTitle>
                    <Row gutter={[24, 12]} style={{ flexWrap: "wrap" }}>
                        <Col xs={24} sm={12} md={12} lg={8} xl={6}>
                            <Link to="/driver/my-offers">
                                <DashboardStatisticCard title={t("Aktif tekliflerim")} count={dashboardData?.activeAdvertisementBidCount} />
                            </Link>
                        </Col>

                        <Col xs={24} sm={12} md={12} lg={8} xl={6}>
                            <Link to="/driver/my-offers">
                                <DashboardStatisticCard title={t("Taşıma durumunda olan tekliflerim")} count={dashboardData?.transportAdvertisementBidCount} />
                            </Link>
                        </Col>

                        <Col xs={24} sm={12} md={12} lg={8} xl={6}>
                            <Link to="/driver/reports">
                                <DashboardStatisticCard title={t("Son 1 aylık taşımalarım")} count={dashboardData?.lastMonthCount} />
                            </Link>
                        </Col>

                        <Col xs={24} sm={12} md={12} lg={8} xl={6}>
                            <Link to="/driver/my-offers?status=passive">
                                <DashboardStatisticCard title={t("Tamamlanmış taşımalarım")} count={dashboardData?.finishedTransportCount} />
                            </Link>
                        </Col>
                    </Row>
                </div>

                <div style={{ marginTop: "4rem" }}>
                    <CustomTitle>{t("İşlem Bekleyen Tekliflerim")}</CustomTitle>
                    { /* backend güncellenince aşağıdaki yorum satırına geç  */
                        dashboardData?.clientTransportProcess?.length > 0 ?
                            <Row gutter={[24, 24]}>
                                {
                                    dashboardData?.clientTransportProcess?.map(val => (
                                        <Col key={val.id} xs={24} md={12} lg={12} xl={8} xxl={6}>
                                            <Link to={`/driver/offer-detail/${val.bidId}`}>
                                                <TransportCard
                                                    data={val.clientAdvertisement}
                                                    warning={t(ParsedDriver(val?.transportProcessStatus).alertDescription)}
                                                />
                                            </Link>
                                        </Col>
                                    ))
                                }
                            </Row> :
                            <div style={{ backgroundColor: "var(--white)", borderRadius: "2rem", boxShadow: "var(--default-shadow)" }}>
                                <NoneContent
                                    title={t("İşlem bekleyen teklif bulunmuyor!")}
                                    description={<p>{t("Ancak")} <Link to="/driver/my-offers">{t("tekliflerim--")} </Link> {t("sayfasından tüm tekliflerinizi görüntüleyebilirsiniz.--")}</p>}
                                />
                            </div>
                    }
                    {/* {
                        dashboardData?.clientAdvertisementBidList.length > 0 ?
                            <Row gutter={[24, 24]}>
                                {
                                    dashboardData?.clientAdvertisementBidList?.map(val => (
                                        <Col key={val.id} xs={24} md={12} lg={12} xl={8} xxl={6}>
                                            <Link to={`/driver/offer-detail/${val.id}`}>
                                                <TransportCard
                                                    data={val.clientAdvertisement}
                                                    warning={ParsedDriver(val?.clientAdvertisement.advertisementProcessStatus).alertDescription}
                                                />
                                            </Link>
                                        </Col>
                                    ))
                                }
                            </Row> :
                            <div style={{ backgroundColor: "var(--white)", borderRadius: "2rem", boxShadow: "var(--default-shadow)" }}>
                                <NoneContent
                                    title="İşlem bekleyen teklif bulunmuyor!"
                                    description={<p>Ancak <Link to="/driver/my-offers">tekliflerim</Link> sayfasından tüm tekliflerinizi görüntüleyebilirsiniz.</p>}
                                />
                            </div>
                    } */}
                </div>
                <SupportBanner />
            </div>
    )
}

export default DriverDashboard;