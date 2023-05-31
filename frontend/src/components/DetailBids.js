import { Button, Col, Divider, Form, List, Modal, Pagination, Rate, Row, Select, Tooltip } from 'antd';
import React, { useEffect, useState } from 'react'
import { CustomModal } from './CustomModal';
import moment from 'moment';
import { updateBidStatusRequest } from '../api/controllers/client-advertisiment-bid-controller';
import { getByCompanyIdAndRatingList, getRatingScoresByCompanyId } from '../api/controllers/rating-client-advertisement-controller';
import ShowProfilePhoto from './ShowProfilePhoto';
import SuspenseFallbackInline from './SuspenseFallbackInline';
import RequestError from './RequestError';
import NoneContent from './NoneContent';
import { useTranslation } from 'react-i18next';

const DetailBids = ({ data, user, updatePage, }) => {
    const { t, i18n } = useTranslation("common");


    const CardClient = ({ cardData }) => {
        const [isModalVisible, setIsModalVisible] = useState(false);
        const [witchModal, setWitchModal] = useState({})
        const [isLoading, setIsLoading] = useState(false)

        const showModal = (e) => {
            setWitchModal(e)
            setIsModalVisible(true);
        };

        const handleOk = async () => {
            setIsLoading(true)
            if (witchModal.status === "approve") {
                try {
                    let res = await updateBidStatusRequest({ id: witchModal.id, status: true })
                    if (res) {
                        updatePage()
                    }

                } catch (error) {
                    RequestError(error);
                }
            } else if (witchModal.status === "decline") {
                try {
                    let res = await updateBidStatusRequest({ id: witchModal.id, status: false })
                    if (res) {
                        updatePage()
                    }

                } catch (error) {
                    RequestError(error);
                }
            } else {
            }
            setIsModalVisible(false);
            setIsLoading(false)
        };

        const handleCancel = () => {
            setIsModalVisible(false);
        };

        const [rating, setRating] = useState({
            companyId: null,
            ratingList: [1, 2, 3, 4, 5],
            page: 0,
            size: 10,
            sort: "createdDateTime,desc",
        })
        const [ratingScore, setRatingScore] = useState(null)
        const [isCustomModalVisible, setIsCustomModalVisible] = useState(false)

        const customOk = () => {
            setIsCustomModalVisible(false)
        }

        const customCancel = () => {
            setIsCustomModalVisible(false);
            setRatingScore(null);
            setRating({
                companyId: null,
                ratingList: [1, 2, 3, 4, 5],
                page: 0,
                size: 10,
                sort: "createdDateTime,desc",
            });
        }

        const handleChangeFilter = (value) => {
            if (value === 'all') {
                rating.ratingList = [1, 2, 3, 4, 5]
            } else if (value === '4') {
                rating.ratingList = [4, 5]
            } else if (value === '3') {
                rating.ratingList = [3, 4, 5]
            } else if (value === '2') {
                rating.ratingList = [2, 3, 4, 5]
            }
            setRating({ ...rating })
        };

        const [commentData, setCommentData] = useState()
        const openCommentModal = async (id) => {
            await setIsLoading(true);
            await setIsCustomModalVisible(true);
            await fetchRatingScore(id);
            await setRating({ ...rating, companyId: id });
        }

        const fetchRatingScore = async (id) => {
            try {
                let res = await getRatingScoresByCompanyId(id);
                if (res) {
                    setRatingScore(res.data);
                }
            } catch (error) {
                RequestError(error);
            }
        }

        const fetchRating = async () => {
            setIsLoading(true)
            try {
                let res = await getByCompanyIdAndRatingList(rating)
                if (res) {
                    console.log(res.data);
                    setCommentData(res.data)
                }
            } catch (error) {
                RequestError(error);
            }
            setIsLoading(false)
        }

        const onPaginationChange = (page) => {
            setRating({ ...rating, page: page - 1 })
        }

        const { Option } = Select;

        const Content = ({ comment }) => {
            return (
                <div className='card'>
                    <div className='top'>
                        <ShowProfilePhoto profileId={comment?.clientID} />
                        <div>
                            <p>{
                                comment?.clientName +" "+ comment?.clientSurname
                            }</p>
                            <div className='star-date' >
                                <Rate
                                    allowHalf
                                    defaultValue={comment.rating}
                                    disabled
                                    className='rate'
                                />
                                <div>{moment(comment.createdDateTime).format("LLL")}</div>
                            </div>
                        </div>
                    </div>
                    <Divider className='divider' />
                    <div>{comment.comment}</div>
                </div>
            )
        }

        useEffect(() => {
            if (rating.companyId) {
                fetchRating()
            }
        }, [rating])

        return (
            <>
                {
                    cardData?.content?.length < 1 ?
                        <NoneContent vertical title={t("Henüz teklif alınmadı!")} />
                        :
                        cardData?.content?.map((v, i) => (
                            <div key={i} className={v.bidStatus === 'DENIED' || v.bidStatus === 'AD_CLOSED' || v.bidStatus === 'TIMEOUT' ? 'container-bid-transparent' : v.bidStatus === 'APPROVED' || v.bidStatus === 'TRANSPORT' || v.bidStatus === 'COMPLETED' ? 'container-bid-approved' : 'container-bid'}>
                                <div className='card'>
                                    <div className='left'>
                                        <div className='price'>
                                            {v.bid ? <p><b>{v.bid} {v?.clientAdvertisement?.regionAdvertisementType === 'INTERNATIONAL' ? "$" : v?.clientAdvertisement?.regionAdvertisementType === 'DOMESTIC' ? "₺"  : ""}</b> {v?.clientAdvertisement?.regionAdvertisementType === 'INTERNATIONAL' ? "" : v?.clientAdvertisement?.regionAdvertisementType === 'DOMESTIC' ? "+" + t("KDV") : ""}</p> : "?"}
                                        </div>
                                        <Divider type='vertical' />
                                        <div className='company' onClick={() => { openCommentModal(v?.driverBidder?.company?.id) }}>
                                            <div className='text'>
                                                {v?.driverBidder?.company?.commercialTitle ?
                                                    v?.driverBidder?.company?.commercialTitle.split(' ').map(v =>
                                                        String(v)[0] + String(v).substr(1).replace(/[a-zA-Z0-9.,üÜıİşŞçÇğĞ]/g, "*") + " "
                                                    ) :
                                                    t("Bilinmiyor")}
                                            </div>
                                            <div className='star'>
                                                <img className='icon' src='../../assets/img/star.svg' alt="" />
                                                <div className='text'> {v?.driverBidder?.company?.rating ? v?.driverBidder?.company?.rating.toFixed(1) <= 5?  v?.driverBidder?.company?.rating.toFixed(1): "?" : "?"}</div>
                                            </div>
                                        </div>
                                    </div>
                                    {v.bidStatus === 'DENIED' || v.bidStatus === 'AD_CLOSED' ?
                                        <div>
                                            {t("Reddedildi")}
                                        </div>
                                        :
                                        v.bidStatus === 'APPROVED' || v.bidStatus === 'TRANSPORT' || v.bidStatus === 'COMPLETED' ?
                                            <div className='approved-text'>
                                                {t("Kabul Edildi")}
                                            </div>
                                            : v.bidStatus === 'TIMEOUT' ?
                                                t("Bu teklifin süresi geçti!") :
                                                <div className='right'>
                                                    <Button onClick={() => showModal({ id: v?.id, status: "decline" })} className='decline' type="primary" shape="circle" icon={<i style={{ fontSize: 20 }} className="bi bi-x"></i>} />
                                                    <Button onClick={() => showModal({ id: v?.id, status: "approve" })} className='approve' type="primary" shape="circle" icon={<i style={{ fontSize: 20 }} className="bi bi-check2"></i>} />
                                                </div>
                                    }
                                </div>
                                <div className='bid-info'>
                                    {v.bidStatus === 'WAITING' &&
                                        < div className='bid-time'>
                                            
                                            {v.expiration !== null&& <><i className="bi bi-alarm" style={{ height: "fit-content" }} ></i> <span>{t("Bu teklif")} <b>{moment(v.expiration).fromNow()}</b> {t("sona erecektir!")}</span></>}
                                        </div>
                                    }
                                    {v.explanation ?
                                        <Tooltip placement="bottom" title={v.explanation}>
                                            <div className={v.bidStatus === 'APPROVED' || v.bidStatus === 'TRANSPORT' || v.bidStatus === 'COMPLETED' ? 'bid-explanation-white' : 'bid-explanation'} >
                                                <i className="bi bi-chat-square-text"></i>
                                                <div style={{ marginLeft: "12px", textOverflow: "ellipsis", overflow: "hidden", whiteSpace: "nowrap" }}>{v?.explanation}</div>
                                            </div>
                                        </Tooltip>
                                        : <></>}
                                </div>
                            </div>
                        ))
                }

                <Modal
                    title={<img className='image' src='../../assets/img/warning.svg' alt="" />}
                    visible={isModalVisible}
                    centered
                    onOk={handleOk}
                    okButtonProps={{ loading: isLoading }}
                    onCancel={handleCancel}
                    okText={t("Evet")}
                    cancelText={t("İptal Et")}
                >
                    {witchModal.status === "approve" ? <p>{t("Teklifi kabul etmek istediğinize emin misiniz?")}</p> : witchModal.status === "decline" ? <p>{t("Teklifi reddetmek istediğinize emin misiniz?")} </p> : ""}
                </Modal>

                <CustomModal
                    keyboard = {true}
                    visible={isCustomModalVisible}
                    onCancel={customCancel}
                    onOk={customOk}
                    footer={null}
                    /*  okText={""} */
                    /*  cancelText={""} */
                    /*  style={{}} */
                    width={800}
                    wrapClassName='modal-comments'
                    title={
                        <div>{t("Taşıyıcı Hakkında Yapılan Yorumlar")} </div>
                    }
                    content={
                        <div>
                            <div className='comments-top'>
                                <Row gutter={[24, 24]}>
                                    <Col xs={24} sm={24} md={8}>
                                        <div className='average-score'>
                                            <div>{t("Ortalama Puan")} </div>
                                            <div className='number'>
                                                <div>{ratingScore?.averageRating ? ratingScore?.averageRating.toFixed(1) <= 5? ratingScore?.averageRating.toFixed(1): "?" : "?"}</div>
                                                <div className='slash'>/</div>
                                                <div>5</div>
                                            </div>
                                        </div>
                                    </Col>
                                    <Col xs={24} sm={24} md={16} className='second'>
                                        <Form
                                            layout='horizontal'
                                            labelCol={12}
                                        >
                                            <Row gutter={[24, 24]}>
                                                <Col span={12}>
                                                    <Form.Item
                                                        label={t("Sırala")}
                                                    >
                                                        <Select
                                                            style={{
                                                                width: "100%",
                                                            }}
                                                            value={rating.sort}
                                                            onChange={e => setRating({ ...rating, sort: e })}
                                                        >
                                                            <Option value="createdDateTime,desc">{t("Önce en yeni yorum")} </Option>
                                                            <Option value="createdDateTime,asc">{t("Önce en eski yorum")} </Option>
                                                            <Option value="rating,desc">{t("Önce en yüksek puan")} </Option>
                                                            <Option value="rating,asc">{t("Önce en düşük puan")} </Option>
                                                        </Select>
                                                    </Form.Item>
                                                </Col>
                                                <Col span={12}>
                                                    <Form.Item
                                                        label={t("Filtrele")}
                                                    >
                                                        <Select
                                                            defaultValue="all"
                                                            style={{
                                                                width: "100%",
                                                            }}
                                                            onChange={handleChangeFilter}
                                                        >
                                                            <Option value="all">{t("Hepsi")} </Option>
                                                            <Option value="4">{t("4 puan ve üzeri")} </Option>
                                                            <Option value="3">{t("3 puan ve üzeri")} </Option>
                                                            <Option value="2">{t("2 puan ve üzeri")} </Option>
                                                        </Select>
                                                    </Form.Item>
                                                </Col>

                                            </Row>

                                        </Form>

                                    </Col>
                                </Row>
                            </div>

                            <Divider />
                            {
                                isLoading ?
                                    <SuspenseFallbackInline /> :
                                    <>
                                        <List
                                            loading={isLoading}
                                            dataSource={commentData}
                                            renderItem={item => (
                                                <Content comment={item} />
                                            )}
                                        />
                                        <div style={{ marginTop: "1rem", display: "flex", justifyContent: "flex-end" }}>
                                            <Pagination
                                                hideOnSinglePage={true}
                                                onChange={onPaginationChange}
                                                defaultCurrent={1}
                                                size={commentData?.size}
                                                total={commentData?.totalElements} />
                                        </div>
                                    </>
                            }
                        </div>
                    }
                />
            </>
        )
    }

    const CardDriver = ({ cardData }) => {
        return (
            <Row className='my-offer' gutter={[16, 16]}>
                <Col span={12}>
                    <div className='title'>
                        {t("Teklif Miktarı")}
                    </div>
                    <div className='text'>
                        {cardData?.bid} {cardData?.clientAdvertisement?.regionAdvertisementType === 'INTERNATIONAL' ? "$" : cardData?.clientAdvertisement?.regionAdvertisementType === 'DOMESTIC' ? "₺" + t("KDV") : ""}
                    </div >
                </Col>
                <Col span={12}>
                    <div className='title'>
                        {t("Teklif Zamanı")}
                    </div>
                    <div className='text'>
                        {moment(cardData?.createdDateTime).format('L')}
                    </div>
                </Col>
                <Col span={24}>
                    <div className='title'>
                        {t("Teklif Açıklaması")}
                    </div>
                    <div className='text'>
                        {cardData?.explanation ? cardData?.explanation : t("Açıklama eklenmemiştir.")}
                    </div>
                </Col>
            </Row>
        )
    }

    return (
        <>
            <div className='offers'>
                <div className='title'>
                    <img className='icon' src='../../assets/img/chevron-double-right.svg' alt="" />
                    <div className='text'>{user === 'client' ? t("Gelen Teklifler") : user === 'driver' ? t("Verdiğim Teklif") : <></>}</div>
                </div>

                <Divider style={{ margin: "16px 0 16px 0" }} />
                {
                    user === 'client' ?
                        <CardClient cardData={data} />
                        : user === 'driver' ?
                            <CardDriver cardData={data} />
                            : <></>
                }
            </div>
        </>
    );
};


export default DetailBids;