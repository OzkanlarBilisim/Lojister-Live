import React, { useState } from 'react'
import { Button, Col, Row, Tabs, List, Alert } from 'antd';
import moment from 'moment';
import AntNotification from './AntNotification';
import { downloadInsuranceFileByTransportCodeRequest } from '../api/controllers/insured-transport-process-file-controller'
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';

const DetailInfo = ({ transport, transportProcess, transportDocuments }) => {
    const { t, i18n } = useTranslation("common");

    const token = localStorage.getItem("token");
    const { TabPane } = Tabs;

    const downloadInsurance = async () => {
        try {
            let res = await downloadInsuranceFileByTransportCodeRequest(transportProcess.transportCode)
            if (res) {
                AntNotification({ type: "success", message: t("Sigorta poliçesi indiriliyor") })
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const GeneralInformation = () => {
        return (
            <div className='general-information'>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("İlan Kodu")} </div>
                            <div>{transport?.clientAdvertisementCode ? transport?.clientAdvertisementCode : "-"}</div>
                        </Col>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("İlan Tarihi")} </div>
                            <div>{transport?.createdDateTime ? moment(transport?.createdDateTime).format("LLL") : "-"}</div>
                        </Col>
                        {
                            transport?.clientAdvertisementType === "CONTAINER" &&
                            <Col xs={24} md={12}>
                                <div className='detail-title'> {t("Ticaret Türü")} </div>
                                <div>{transport?.tradeType === "EXPORT" ? t("İhracat") : transport?.tradeType === "IMPORT" ? t("İthalat") : "-"}</div>
                            </Col>
                        }
                    </Row>
                </div>
                <br />
                <div className='title'>
                    <div style={{ fontWeight: 500 }}> {t("Yükleme Bilgileri")}  <span style={{ fontWeight: 300 }}>| {moment(transport?.adStartingDate).format("LL")} {transport?.adStartingTime?.substring(0, 5)}</span></div>
                    <img style={{ width: "40px", transform: "rotateY(180deg)" }} src={`../../assets/img/types/active${transport?.clientAdvertisementType}.svg`} alt="" />
                </div>
                <div className='detail'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("Şirket Ünvanı")} </div>
                            <div>{transport?.startRecipient?.commercialTitle ? transport?.startRecipient?.commercialTitle : "-"}</div>
                        </Col>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("Yetkili Adı Soyadı")} </div>
                            <div className='detail-address'>
                                <div>{transport?.startRecipient?.firstName}</div>
                                <div className='detail-space'></div>
                                <div>{transport?.startRecipient?.lastName}</div>
                            </div>
                        </Col>
                        <Col xs={24}>
                            <div className='detail-title'> {t("Adres")} </div>
                            <p>{transport?.startingAddress?.district ? transport?.startingAddress?.fullAddress + " " + transport?.startingAddress?.neighborhood + " - " + transport?.startingAddress?.district + "/" + transport?.startingAddress?.province : transport?.startingAddress?.country + " - " + transport?.startingAddress?.province}</p>
                        </Col>
                    </Row>
                </div>
                <div className='title'>
                    <div style={{ fontWeight: 500 }}> {t("İndirme Bilgileri")}  <span style={{ fontWeight: 300 }}>| {moment(transport?.adDueDate).format("LL")} {transport?.adDueTime?.substring(0, 5)}</span></div>
                    <img style={{ width: "40px", }} src={`../../assets/img/types/active${transport?.clientAdvertisementType}.svg`} alt="" />
                </div>
                <div className='detail2'>
                    <Row gutter={[12, 12]}>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("Şirket Ünvanı")} </div>
                            <div>{transport?.dueRecipient?.commercialTitle ? transport?.dueRecipient?.commercialTitle : "-"}</div>
                        </Col>
                        <Col xs={24} md={12}>
                            <div className='detail-title'> {t("Yetkili Adı Soyadı")} </div>
                            <div className='detail-address'>
                                <div>{transport?.dueRecipient?.firstName}</div>
                                <div className='detail-space'></div>
                                <div>{transport?.dueRecipient?.lastName}</div>
                            </div>
                        </Col>
                        <Col xs={24}>
                            <div className='detail-title'> {t("Adres")} </div>
                            <p>{transport?.dueAddress?.district ? transport?.dueAddress?.fullAddress + " " + transport?.dueAddress?.neighborhood + " - " + transport?.dueAddress?.district + "/" + transport?.dueAddress?.province : transport?.dueAddress?.country + " - " + transport?.dueAddress?.province}</p>
                        </Col>
                    </Row>
                </div>
            </div>
        )
    }

    const DetailOfLoad = () => {
        switch (true) {
            case transport.clientAdvertisementType !== "CONTAINER":
                return (
                    <div className='detail-of-load' >
                        <Row gutter={[12, 12]}>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Yük Tipi")} </div>
                                <div>{transport?.cargoTypes.length > 0 ? transport?.cargoTypes?.map((v, i) => v?.typeName + (i === transport?.cargoTypes.length - 1 ? "" : ", ")) : t("Farketmez")}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Paketleme Tipi")} </div>
                                <div>{transport?.packagingType?.typeName ? transport?.packagingType?.typeName : "-"}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Yükleme Şekli")} </div>
                                <div>{transport?.loadType.length > 0 ? transport?.loadType?.map((v, i) => v?.typeName + (i === transport?.loadType.length - 1 ? "" : ", ")) : t("Farketmez")}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Toplam Ağırlık")} </div>
                                {
                                    transport.clientAdvertisementType === "PARTIAL" ?
                                        <div>{transport?.tonnage ? transport?.tonnage + " kg" : "-"}</div> :
                                        <div>{transport?.tonnage ? transport?.tonnage / 1000 + " " + t("ton") : "-"}</div>
                                }
                            </Col>
                            {
                                transport.clientAdvertisementType === "PARTIAL" &&
                                <Col xs={24} md={12}>
                                    <div className='title'> {t("Ebatlar")} </div>
                                    <div>{(transport?.width ? transport?.width : 0) + "/" + (transport?.length ? transport?.length : 0) + "/" + (transport?.height ? transport?.height : 0) + " cm"} - {
                                        transport?.desi ? transport?.desi + t("desi") : ""
                                    }</div>
                                </Col>
                            }
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Mal Değeri")} </div>
                                <div>{transport?.goodsPrice ? transport?.goodsPrice + " " + transport?.currencyUnit?.currencySymbol : "-"}</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Hammaliye")} </div>
                                <div>  {transport?.isPorter ? t("İstiyorum") : t("İstemiyorum")}</div>
                            </Col>
                            {
                                transport.clientAdvertisementType === "PARTIAL" &&
                                <Col xs={24} md={12}>
                                    <div className='title'> {t("İstiflenebilir")} </div>
                                    <div>  {transport?.isStacking ? t("Evet") : t("Hayır")}</div>
                                </Col>
                            }
                        </Row>
                    </div>
                );

            case transport.clientAdvertisementType === "CONTAINER":
                return (
                    <div className='detail-of-load' >
                        <Row gutter={[12, 12]}>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Konteyner Tipi")} </div>
                                <div>{
                                    transport?.containerType === "TWENTY" ?
                                        t("20'lik") :
                                        transport?.containerType === "FOURTY" ?
                                            t("40'lık") :
                                            transport?.containerType === "FOURTY_HC" ?
                                                t("40'lık HC") :
                                                transport?.containerType === "FOURTY_FIVE" ?
                                                    t("45'lik") :
                                                    "-"
                                }</div>
                            </Col>
                            <Col xs={24} md={12}>
                                <div className='title'> {t("Toplam Ağırlık")} </div>
                                <div>{transport?.tonnage ? transport?.tonnage / 1000 + " " + t("ton") : "-"}</div>
                            </Col>
                        </Row>
                    </div>
                );

            default:
                break;
        }

    }

    const DetailOfVehicle = () => {
        return (
            <div className='detail-of-load' >
                <Row gutter={[12, 12]}>
                    <Col xs={24} md={12}>
                        <div className='title'> {t("Araç Tipi")} </div>
                        <div>{transport?.vehicleTypes.length > 0 ? transport?.vehicleTypes?.map((v, i) => v?.typeName + (i === transport?.vehicleTypes.length - 1 ? "" : ", ")) : t("Farketmez")}</div>
                    </Col>
                    <Col xs={24} md={12}>
                        <div className='title'> {t("Araç Sayısı")} </div>
                        <div>{transport?.vehicleCount ? transport?.vehicleCount : "-"}</div>
                    </Col>
                    <Col xs={24} md={12}>
                        <div className='title'> {t("Kasa Tipi")} </div>
                        <div>{transport?.trailerTypes.length > 0 ? transport?.trailerTypes?.map((v, i) => v?.typeName + (i === transport?.trailerTypes.length - 1 ? "" : ", ")) : t("Farketmez")}</div>
                    </Col>
                    <Col xs={24} md={12}>
                        <div className='title'> {t("Zemin Tipi")} </div>
                        <div>{transport?.trailerFloorTypes.length > 0 ? transport?.trailerFloorTypes?.map((v, i) => v?.typeName + (i === transport?.trailerFloorTypes.length - 1 ? "" : ", ")) : t("Farketmez")}</div>
                    </Col>
                    <Col xs={24} md={12}>
                        <div className='title'> {t("Kasa Özellikleri")} </div>
                        <div>{transport?.trailerFeatures.length > 0 ? transport?.trailerFeatures?.map((v, i) => v?.featureName + (i === transport?.trailerFeatures.length - 1 ? "" : ", ")) : t("Farketmez")}</div>
                    </Col>
                </Row>
            </div>
        )
    }

    const DetailOfExplanation = () => {
        return (
            <div className='detail-of-load' >
                <p>
                    {transport?.explanation}
                </p>
            </div>
        )
    }

    const Documents = () => {
        const [opening, setOpening] = useState(false);
        return (
            <List
                size="small"
                locale={{ emptyText: t("Belge bulunamadı!") }}
            >
                {
                    !transport?.clientTransportProcess?.isExistInsuredFile && transportDocuments.length === 0 &&
                    <Alert message={t("Görüntülenecek belge bulunamadı!")} type="info" showIcon />
                }
                {
                    transport?.clientTransportProcess?.isExistInsuredFile &&
                    <List.Item>
                        <List.Item.Meta
                            title={t("Sigorta Poliçesi")}
                        />
                        <Button
                            type="primary"
                            size="small"
                            onClick={() => downloadInsurance()}
                        >
                            {t("İndir")}
                        </Button>
                    </List.Item>
                }
                {
                    transportDocuments.map(item =>
                        <List.Item key={item.id}>
                            <List.Item.Meta
                                title={item.documentType === "SUMMARY" ? t("Föy Belgesi") : item.documentType === "VGM" ? t("VGM Belgesi") : t("Bilinmeyen belge")}
                            />
                            <Button
                                size="small"
                                type="primary"
                                loading={opening}
                                onClick={async () => {
                                    setOpening(true);
                                    const res = await fetch(item.url, {
                                        method: 'GET',
                                        headers: {
                                            "Content-Security-Policy": "default-src https:",

                                            "Authorization": "Bearer " + token
                                        }
                                    });
                                    setOpening(false);
                                    const blob = await res.blob();
                                    const urlObject = URL.createObjectURL(blob);
                                    let file_window = window.open();
                                    file_window.document.write(`<img  src="${urlObject}" alt="" />`);
                                }}
                            > {t("Göster")} </Button>
                        </List.Item>
                    )
                }
            </List>
        )
    }

    return (
        <>
            <div className='general-knowledges'>
                <Tabs defaultActiveKey="1">
                    <TabPane tab={t("Genel Bilgiler")} key="1">
                        <GeneralInformation />
                    </TabPane>
                    <TabPane tab={t("Yük Detayları")} key="2">
                        <DetailOfLoad />
                    </TabPane>
                    {
                        transport?.clientAdvertisementType !== "CONTAINER" &&
                        <TabPane tab={t("Araç Detayları")} key="3">
                            <DetailOfVehicle />
                        </TabPane>
                    }
                    {
                        transport?.explanation &&
                        <TabPane tab={t("Açıklama")} key="4">
                            <DetailOfExplanation />
                        </TabPane>
                    }
                    <TabPane tab={t("Belgeler")} key="5">
                        <Documents />
                    </TabPane>
                </Tabs>
            </div>
        </>
    );
};

export default React.memo(DetailInfo);