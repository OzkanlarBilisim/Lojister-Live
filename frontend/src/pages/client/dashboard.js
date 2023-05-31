import React, { useEffect, useState } from 'react';
import { Col, Row } from 'antd';
import CustomTitle from '../../components/CustomTitle';
import TransportCard from '../../components/TransportCard';
import { Link } from 'react-router-dom';
import { getClientDashboardRequest } from '../../api/controllers/client-controller'
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline';
import RequestError from '../../components/RequestError';
import SupportBanner from '../../components/SupportBanner';
import NoneContent from '../../components/NoneContent';
import { useTranslation } from 'react-i18next';


export const DashboardStatisticCard = ({ title, count }) => {

    return (
        <div className="dashboardStatisticCard">
            <p style={{ margin: "0" }}>{title}</p>
            <span>
                {count}
            </span>
        </div >
    )
}

const ClientDashboard = () => {
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

    const fetchClientDashboard = async () => {
        setIsLoading(true)
        try {
            let res = await getClientDashboardRequest();

            if (res) {
                setDashboardData(res.data)
            }
            setIsLoading(false)
        } catch (error) {
            RequestError(error);
        }
    };

    useEffect(() => {
        fetchClientDashboard()
    }, []);

    const transportProcess = (status) => {
        switch (status) {
            case "BID_GIVEN":
                return {
                    description: t("Yeni teklif alındı!"),
                };
            case "BID_APPROVED":
                return {
                    description: t("Ödeme yapmanız bekleniyor!"),
                };
            case "WAITING_WAYBILL":
                return {
                    description: t("İrsaliye yüklemeniz bekleniyor."),
                };
            default:
                return {
                    description: t("Aksiyon bekliyor.")
                }
        }
    };

    return (
        isLoading ?
            <SuspenseFallbackInline /> :
            <div className="layout-content-padding">
                <div>
                    <CustomTitle> {t("Göstergeler")} </CustomTitle>
                    <Row gutter={[24, 12]} style={{ flexWrap: "wrap" }}>
                        <Col xs={24} sm={12} md={8} lg={6}>
                            <Link to="/client/my-transports">
                                <DashboardStatisticCard title={t("Aktif ilanlarım")} count={dashboardData?.activeAdvertisementCount} />
                            </Link>
                        </Col>

                        <Col xs={24} sm={12} md={8} lg={6}>
                            <Link to="/client/tracking">
                                <DashboardStatisticCard title={t("Taşıma durumunda olan ilanlarım")} count={dashboardData?.finishedAdvertisementCount} />
                            </Link>
                        </Col>

                        <Col xs={24} sm={12} md={8} lg={6}>
                            <Link to="/client/reports">
                                <DashboardStatisticCard title={t("Son 1 aylık ilanlarım")} count={dashboardData?.lastMonthCount} />
                            </Link>
                        </Col>

                        <Col xs={24} sm={12} md={8} lg={6}>
                            <Link to="/client/my-transports?status=passive">
                                <DashboardStatisticCard title={t("Tamamlanmış ilanlarım")} count={dashboardData?.transportAdvertisementCount} />
                            </Link>
                        </Col>
                    </Row>
                </div>

                <div style={{ marginTop: "4rem" }}>
                    <CustomTitle>{t("İşlem Bekleyen İlanlarım")}</CustomTitle>
                    {
                        dashboardData?.clientAdvertisements.length > 0 ?
                            <Row gutter={[24, 24]}>

                                {
                                    dashboardData?.clientAdvertisements.map(val => (
                                        <Col key={val.id} xs={24} md={12} lg={12} xl={8} xxl={6}>
                                            <Link to={`/client/transport-detail/${val.id}`}>
                                                <TransportCard
                                                    data={val}
                                                    warning={transportProcess(val.advertisementProcessStatus).description}
                                                />
                                            </Link>
                                        </Col>
                                    ))
                                }
                            </Row> :
                            <div style={{ backgroundColor: "var(--white)", borderRadius: "2rem", boxShadow: "var(--default-shadow)" }}>
                                <NoneContent
                                    title={t("İşlem bekleyen ilan bulunmuyor!")}
                                    description={<p>{t("Ancak")} <Link to="/client/my-transports">{t("ilanlarım--")} </Link> {t("sayfasından tüm ilanlarınızı görüntüleyebilirsiniz.--")} </p>}
                                />
                            </div>
                    }
                </div>
                <SupportBanner />
            </div>
    )
}

export default ClientDashboard;