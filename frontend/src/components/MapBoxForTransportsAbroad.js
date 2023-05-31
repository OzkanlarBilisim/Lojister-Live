import React, { useCallback, useEffect, useRef, useState } from 'react';
import Map, { Layer, Marker, NavigationControl, Source } from 'react-map-gl';
import { Button, Divider, Modal, Popover, Select, Tag, Radio, message, Tooltip, Table, Input, Space} from 'antd';
import routeStart from "../assets/img/routeStart.svg";
import routeFinish from "../assets/img/routeFinish.svg"
import markerSvg from "../assets/img/marker.svg"
import { getAdvertisementRouteRequest,getAllAdvertisementYurtdisi } from '../api/controllers/client-advertisiment-controller';
import { getDistrictByProvinceIdRequest, getNeighborhoodRequest, getProvinceRequest } from '../api/controllers/addresses-controller';
import { CustomButton } from './CustomButton';
import TransportCard from './TransportCard';
import AntNotification from './AntNotification';
import SuspenseFallbackInline from './SuspenseFallbackInline';
import TransportInfoForMap from './TransportInfoForMap';
import HomepageCreateAd from './createTransportSteps/HomepageCreateAd';
import { useSelector } from 'react-redux';
import RequestError from './RequestError';
import NoneContent from './NoneContent';
import { useTranslation } from 'react-i18next';
import { ParsedClient } from '../components/ParsedValues';
import { ProgressBar, Step } from "react-step-progress-bar";
import moment from 'moment';
import { useSearchParams } from 'react-router-dom';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import TransportDetailAbroad from './TransportDetailAbroad';
import { useSwiper } from 'swiper/react';
import { CSSTransition } from 'react-transition-group';
import Highlighter from 'react-highlight-words';
import {BASE_URL_FRONTEND} from '../api/ApiProvider'


import { SearchOutlined } from '@ant-design/icons';

const TransportTypeForTable = ({ ClientAdvertisementType, status }) => {
    return (
        <Tooltip
            title={ClientAdvertisementType}
        >
            <div
                style={{
                    borderRadius: ".5rem",
                    padding: "1rem",
                    backgroundColor: ParsedClient(status)?.bg,
                    width: "fit-content"
                }}>
                <img
                    src={"/assets/img/types/" + ParsedClient(status)?.color + ClientAdvertisementType + ".svg"} alt="" />
            </div>
        </Tooltip>
    )
}

const TransportStatusForTable = ({ status }) => {
    const { t, i18n } = useTranslation("common");

    return (
        <div
            style={{
                display: "grid",
                gap: ".5rem"
            }}>
            <p
                style={{
                    margin: 0,
                    lineHeight: "14px"
                }}>
                {t(ParsedClient(status)?.title)}
            </p>
            <p
                style={{
                    margin: 0,
                    opacity: .5,
                    lineHeight: "14px"
                }}>
                {t(ParsedClient(status)?.description)}
            </p>
            <ProgressBar
                percent={ParsedClient(status)?.percent}
                filledBackground="transparent"
            >
                <Step transition="scale">
                    {({ accomplished }) => (
                        ParsedClient(status)?.color === "red" ?
                            <i style={{ color: `var(--${ParsedClient(status)?.color})` }} className="bi bi-x-circle-fill"></i> :
                            status === "WAITING" ?
                                <i style={{ color: `var(--${ParsedClient(status)?.color})` }} className="bi bi-clock-history"></i> :
                                < div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedClient(status)?.color})` : "var(--dark-light)",
                                }} />
                    )}
                </Step>
                <Step transition="scale">
                    {({ accomplished }) => (
                        <div style={{
                            width: 12,
                            height: 12,
                            borderRadius: "50%",
                            border: "1px solid #fff",
                            backgroundColor: accomplished ? `var(--${ParsedClient(status)?.color})` : "var(--dark-light)",
                        }} />
                    )}
                </Step>
                <Step transition="scale">
                    {({ accomplished }) => (
                        status === "CARGO_ON_THE_WAY" ?
                            <img height="16px" src="/assets/img/typesForTable/cargo.svg" alt="" /> :
                            <div style={{
                                width: 12,
                                height: 12,
                                borderRadius: "50%",
                                border: "1px solid #fff",
                                backgroundColor: accomplished ? `var(--${ParsedClient(status)?.color})` : "var(--dark-light)",
                            }} />
                    )}
                </Step>
                <Step transition="scale">
                    {({ accomplished }) => (
                        <div style={{
                            width: 12,
                            height: 12,
                            borderRadius: "50%",
                            border: "1px solid #fff",
                            backgroundColor: accomplished ? `var(--${ParsedClient(status)?.color})` : "var(--dark-light)",
                        }} />
                    )}
                </Step>
                <Step transition="scale">
                    {({ accomplished }) => (
                        accomplished ? <i style={{ color: `var(--${ParsedClient(status)?.color})` }} className="bi bi-check-circle-fill"></i> :
                            <div style={{
                                width: 12,
                                height: 12,
                                borderRadius: "50%",
                                border: "1px solid #fff",
                                backgroundColor: accomplished ? `var(--${ParsedClient(status)?.color})` : "var(--dark-light)",
                            }} />
                    )}
                </Step>
            </ProgressBar>
        </div>
    )
}


const MapBoxForTransportsAbroad = ({ setTransportRegion }) => {




  const [searchText, setSearchText] = useState('');
  const [searchedColumn, setSearchedColumn] = useState('');
  const searchInput = useRef(null);
  const handleSearch = (selectedKeys, confirm, dataIndex) => {
    confirm();
    setSearchText(selectedKeys[0]);
    setSearchedColumn(dataIndex);
  };
  const handleReset = (clearFilters) => {
    clearFilters();
    setSearchText('');
  };
  const getColumnSearchProps = (dataIndex) => ({
    filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters, close }) => (
      <div
        style={{
          padding: 8,
        }}
        onKeyDown={(e) => e.stopPropagation()}
      >
        <Input
          ref={searchInput}
          placeholder={`Adresler Arasında Ara`}
          value={selectedKeys[0]}
          onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
          onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
          style={{
            marginBottom: 8,
            display: 'block',
          }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{
              width: 90,
            }}
          >
            Ara
          </Button>
          <Button
            onClick={() => clearFilters && handleReset(clearFilters)}
            size="small"
            style={{
              width: 90,
            }}
          >
            Temizle
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              confirm({
                closeDropdown: false,
              });
              setSearchText(selectedKeys[0]);
              setSearchedColumn(dataIndex);
            }}
          >
            Filtere
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              close();
            }}
          >
            Kapat
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered) => (
      <SearchOutlined
        style={{
          color: filtered ? '#1890ff' : undefined,
        }}
      />
    ),
    onFilter: (value, record) =>
      record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: (visible) => {
      if (visible) {
        setTimeout(() => searchInput.current?.select(), 100);
      }
    },
    render: (text) =>
      searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{
            backgroundColor: '#ffc069',
            padding: 0,
          }}
          searchWords={[searchText]}
          autoEscape
          textToHighlight={text ? text.toString() : ''}
        />
      ) : (
        text
      ),
  });









    const { t, i18n } = useTranslation("common");

    const [searchParams, setSearchParams] = useSearchParams();
    let param = searchParams.get("status");

    const [isActive, setIsActive] = useState(param === "passive" ? false : true)
    const [isLoading, setIsLoading] = useState(true)
    const [myTransports, setMyTransports] = useState([])
    const [pageable, setPageable] = useState({
        page: 1,
        pageSize: 10,
        totalSize: null,
        sort: "",
    })

    const defaultFilterValue = {
        startingAddressProvince: null,
        startingAddressDistrict: null,
        startingAddressNeighborhood: null,
        dueAddressProvince: null,
        dueAddressDistrict: null,
        dueAddressNeighborhood: null,
        clientAdvertisementTypeList: [],
        regionAdvertisementTypeList: ['INTERNATIONAL']
    };


    const columns = [
        {
          title: 'Taşıma Tipi',
          dataIndex: 'transportType',
          filters: [
            {
              text: 'FTL',
              value: 'FTL',
              children: [
                {
                  text: 'Kara Yolu',
                  value: 'FTL / Kara Yolu',
                },
                {
                  text: 'Hava Yolu',
                  value: 'FTL / Hava Yolu',
                },
                {
                  text: 'Deniz Yolu',
                  value: 'FTL / Deniz Yolu',
                },
              ],
            },
            {
              text: 'PARSİYEL',
              value: 'PARSİYEL',
              children: [
                {
                    text: 'Kara Yolu',
                    value: 'PARSİYEL / Kara Yolu',
                  },
                  {
                    text: 'Hava Yolu',
                    value: 'PARSİYEL / Hava Yolu',
                  },
                  {
                    text: 'Deniz Yolu',
                    value: 'PARSİYEL / Deniz Yolu',
                  },
              ],
            },
          ],
          // specify the condition of filtering result
          // here is that finding the name started with `value`
          onFilter: (value, record) => record.transportType.indexOf(value) === 0,
        },
        {
          title: 'Taşıma Şekli',
          dataIndex: 'modeOfTransport',
          filters: [
            {
                text: 'Fas',
                value: 'Fas',
            },
            {
                text: 'Fob',
                value: 'Fob',
            },
            {
                text: 'Crf',
                value: 'Crf',
            },
            {
                text: 'Cıf',
                value: 'Cıf',
            },
            {
                text: 'Exw',
                value: 'Exw',
            },
            {
                text: 'Fca',
                value: 'Fca',
            },
            {
                text: 'Cip',
                value: 'Cip',
            },
            {
                text: 'Dat',
                value: 'Dat',
            },
            {
                text: 'Dap',
                value: 'Dap',
            },
          ],
          // specify the condition of filtering result
          // here is that finding the name started with `value`
          onFilter: (value, record) => record.modeOfTransport.indexOf(value) === 0,
        },
        {
          title: 'Yükleme Zamanı',
          dataIndex: 'loadingDate',
          defaultSortOrder: 'descend',
        },
        {
            title: 'Yükleme Adresi',
            dataIndex: 'loadingZone',
            key: '4',
            ...getColumnSearchProps('loadingZone'),
        },

        {
            title: 'Teslimat Adresi',
            dataIndex: 'destinationZone',
            key: 'address',
            ...getColumnSearchProps('destinationZone'),
            sortDirections: ['descend', 'ascend'],
          },
      ];
    

    //all states
    const [markerValues, setMarkerValues] = useState("starting")
    const [createAdModal, setCreateAdModal] = useState(false)
    const [isFiltered, setIsFiltered] = useState(false)
    const [adsLoading, setAdsLoading] = useState(true)
    const [transports, setTransports] = useState([])
    const [selectedTransport, setSelectedTransport] = useState(null)
    const [filterValues, setFilterValues] = useState(defaultFilterValue);
    const [sortValues, setSortValues] = useState("createdDateTime")
    const [filterPopover, setFilterPopover] = useState(false);
    const [sortPopover, setSortPopover] = useState(false)
    const [coordinates, setCoordinates] = useState([]);

    const data = transports.map(val => {
        return { 
                key: val.id,
                transportType: val.clientAdvertisementType+" / "+val.vehicleOrContainer,
                modeOfTransport: val.deliveryType,
                loadingDate: val.startDate+" / "+val.dueDate,
                loadingZone: val.startFullAddress,
                destinationZone: val.dueFullAddress
            };
      });
    //get all ads
    const getAds = async () => {
        setAdsLoading(true)
        try {
            let res = await getAllAdvertisementYurtdisi();
            if (res) {
                console.log(res.data);
                setTransports(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
        setAdsLoading(false)
    }


    const onChange = async (pagination, filters, sorter) => {
        pageable.page = pagination.current;
        pageable.pageSize = pagination.pageSize;
        let sorterField;
        if (sorter.order) {
            if (sorter.field === "clientAdvertisementType") {
                sorterField = "clientAdvertisementType"
            } else if (sorter.field === "clientAdvertisementCode") {
                sorterField = "clientAdvertisementCode"
            } else if (sorter.field === "startingAddress") {
                sorterField = "startingAddress.province"
            } else if (sorter.field === "dueAddress") {
                sorterField = "dueAddress.province"
            } else if (sorter.field === "adStartingDate") {
                sorterField = "adStartingDate"
            } else if (sorter.field === "adDueDate") {
                sorterField = "adDueDate"
            } else {
                pageable.sort = ""
            }
        } else {
            pageable.sort = ""
        };
        if (sorterField) {
            sorterField = `${sorterField + ","}${sorter.order === "ascend" ? "asc" : sorter.order === "descend" ? "desc" : ""}`
        };
        setSortValues(sorterField)
        setPageable({ ...pageable });
        getAds();
    }


    useEffect(() => {
        getAds()

    }, [])

    const [mySwiper, setMySwiper] = useState({});
    const [adDetailAbroad, setAdDetailAbroad] = useState({})


    const [showDetail, setShowDetail] = useState(false)

    const nodeRef = useRef(null);

    console.log('nodeRef', nodeRef)

    return (
        <div className='home-page-abroad-container'>
            {!showDetail ?
            
                <Table
                    rowKey={(val) => val.id}
                    loading={adsLoading}
                    columns={columns}
                    dataSource={data}
                    onChange={onChange}
                    scroll={{ x: "auto"}}
                    className="myAdsTable"
                    pagination={{
                        total: pageable.totalSize,
                        pageSize: pageable.pageSize,
                        current: pageable.page,
                    }}
                    onRow={e => ({
                        onClick: () => {
                            setShowDetail(true)
                            // setAdDetailAbroad(e)
                            window.location = BASE_URL_FRONTEND+"adsAbroadId/"+e.key;
                        }
                    })}
                    locale={{
                        emptyText:
                            <NoneContent
                                title={t("İlan bulunamadı!")}
                                description={t(`Hesabınızda ilan bulunamadı. Hemen sizde Lojister’da ilan yayınlayın güvenli ve avantajlı hizmetin keyfini çıkarın.`)}
                            />
                    }}
                />
                :
                <TransportDetailAbroad ref={nodeRef} setShowDetail={setShowDetail} data={adDetailAbroad} mySwiper={mySwiper} />
            }
     {/*        <CSSTransition
                in={showDetail}
                nodeRef={nodeRef}
                timeout={3000}
                classNames="alert"
                unmountOnExit
                onEnter={() => setShowDetail(true)}
                onExited={() => setShowDetail(false)}
            >
              
            </CSSTransition>
 */}









            {/* <Swiper
                allowTouchMove={false}
                spaceBetween={50}
                slidesPerView={1}
                onInit={(ev) => {
                    setMySwiper(ev)
                }}
            >
                <SwiperSlide>
                    <Table
                        rowKey={(val) => val.id}
                        loading={adsLoading}
                        columns={columns}
                        dataSource={transports}
                        onChange={onChange}
                        scroll={{ x: "auto", y: 600, }}
                        className="myAdsTable"
                        pagination={{
                            total: pageable.totalSize,
                            pageSize: pageable.pageSize,
                            current: pageable.page,
                        }}
                        onRow={e => ({
                            onClick: () => {
                                mySwiper.slideNext();
                                setAdDetailAbroad(e)
                            }
                        })}
                        locale={{
                            emptyText:
                                <NoneContent
                                    title={t("İlan bulunamadı!")}
                                    description={t(`Hesabınızda ilan bulunamadı. Hemen sizde Lojister’da ilan yayınlayın güvenli ve avantajlı hizmetin keyfini çıkarın.`)}
                                />
                        }}
                    />
                </SwiperSlide>
                <SwiperSlide>
                    <TransportDetailAbroad data={adDetailAbroad} mySwiper={mySwiper} />
                </SwiperSlide>
            </Swiper> */}

            {
                <div className="choose-marker-value2">
                    <Button size='large' type='primary' onClick={() => setTransportRegion('inland')}>{t("Yurt İçi İlanları Görüntüle")}</Button>
                </div>
            }
        </div>
    )

}

export default MapBoxForTransportsAbroad;