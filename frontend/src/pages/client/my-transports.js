import { Table, Tooltip, Button, Input, Space } from 'antd';
import React, { useEffect, useState, useRef } from 'react'
import "react-step-progress-bar/styles.css";
import { ProgressBar, Step } from "react-step-progress-bar";
import { getMyAdvertisementForClientRequest, getAllAdvertisementYurtdisiClientList } from '../../api/controllers/client-advertisiment-controller';
import moment from 'moment';
import { CustomButton } from '../../components/CustomButton';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ParsedClient } from '../../components/ParsedValues';
import { ParsedClientAbroud } from '../../components/ParsedValuesAbroad';
import RequestError from '../../components/RequestError';
import SupportBanner from '../../components/SupportBanner';
import NoneContent from '../../components/NoneContent';
import { useTranslation } from 'react-i18next';
import Highlighter from 'react-highlight-words';
import { SearchOutlined } from '@ant-design/icons';
import { useSelector } from 'react-redux';

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
const TransportStatusForTableAbroud = (status) => {
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
                {t(ParsedClientAbroud(status)?.title)}
            </p>
            <p
                style={{
                    margin: 0,
                    opacity: .5,
                    lineHeight: "14px"
                }}>
                {t(ParsedClientAbroud(status)?.description)}
            </p>
            <ProgressBar
                percent={ParsedClientAbroud(status)?.percent}
                filledBackground="transparent"
            >
                <Step transition="scale">
                    {({ accomplished }) => (
                        ParsedClientAbroud(status)?.color === "red" ?
                            <i style={{ color: `var(--${ParsedClientAbroud(status)?.color})` }} className="bi bi-x-circle-fill"></i> :
                            status === "WAITING" ?
                                <i style={{ color: `var(--${ParsedClientAbroud(status)?.color})` }} className="bi bi-clock-history"></i> :
                                < div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedClientAbroud(status)?.color})` : "var(--dark-light)",
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
                            backgroundColor: accomplished ? `var(--${ParsedClientAbroud(status)?.color})` : "var(--dark-light)",
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
                                backgroundColor: accomplished ? `var(--${ParsedClientAbroud(status)?.color})` : "var(--dark-light)",
                            }} />
                    )}
                </Step>
                <Step transition="scale">
                {({ accomplished }) => (
                            status.status === "TRANSPORT" ?
                                <img height="16px" src="/assets/img/typesForTable/cargo.svg" alt="" /> :
                                <div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedClientAbroud(status)?.color})` : "var(--dark-light)",
                                }} />
                    )}
                </Step>
                <Step transition="scale">
                    {({ accomplished }) => (
                        accomplished ? <i style={{ color: `var(--${ParsedClientAbroud(status)?.color})` }} className="bi bi-check-circle-fill"></i> :
                            <div style={{
                                width: 12,
                                height: 12,
                                borderRadius: "50%",
                                border: "1px solid #fff",
                                backgroundColor: accomplished ? `var(--${ParsedClientAbroud(status)?.color})` : "var(--dark-light)",
                            }} />
                    )}
                </Step>
            </ProgressBar>
        </div>
    )
}

const MyAds = () => {
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const searchInput = useRef(null);
    const [transports, setTransports] = useState([]);
    const [adsLoading, setAdsLoading] = useState(true);
    const currentUser = useSelector((state) => state.user);

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

    
    const getAds = async () => {
        setAdsLoading(true)
        try {
            let res = await getAllAdvertisementYurtdisiClientList(currentUser?.id);
            if (res) {
                setTransports(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
        setAdsLoading(false)
    }
    const dataAbroud = transports.map(val => {
        return { 
                key: val.id,
                transportType: val.clientAdvertisementType+" / "+val.vehicleOrContainer,
                advertisementProcessStatus: <TransportStatusForTableAbroud status={val.advertisementStatus} />,
                loadingDate: val.startDate+" / "+val.dueDate,
                loadingZone: val.startFullAddress,
                destinationZone: val.dueFullAddress
            };
    });




    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("İlanlarım")

    const [searchParams, setSearchParams] = useSearchParams();
    let param = searchParams.get("status");

    const [domasticOrAbroud, setDomasticOrAbroud] = useState("DOMASTIC")
    const [isActive, setIsActive] = useState(param === "passive" ? false : true)
    const [isLoading, setIsLoading] = useState(true)
    const [myTransports, setMyTransports] = useState([])
    const [pageable, setPageable] = useState({
        page: 1,
        pageSize: 10,
        totalSize: null,
        sort: "",
    })

    const navigate = useNavigate();

    const fetchMyAds = async () => {
        setIsLoading(true)
        try {
            let res = await getMyAdvertisementForClientRequest({ isActive, pageable });
            if (res) {
                setMyTransports(res.data.content);
                setPageable({ ...pageable, totalSize: res.data.totalElements })
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false)
    }

    useEffect(() => {
        getAds();
        fetchMyAds();
        setPageable({ ...pageable, page: 1 })
    }, [isActive])

    const columns = [
        {
            title: t('Taşıma Tipi'),
            dataIndex: 'clientAdvertisementType',
            width: 0,
            sorter: true,
            render: (val, row) => {
                return (
                    <TransportTypeForTable ClientAdvertisementType={val} isActive={isActive} status={row.advertisementProcessStatus} />
                )
            }
        },
        {
            title: t('Taşıma Durumu'),
            dataIndex: 'advertisementProcessStatus',
            width: 250,
            sorter: true,
            render: (val) => {
                return (
                    <TransportStatusForTable status={val} />
                )
            }
        },
        {
            title: t('Taşıma Numarası'),
            dataIndex: 'clientAdvertisementCode',
            sorter: true,
            render: (val) => {
                return (
                    val
                )
            }
        },
        {
            title: t('Yükleme Noktası'),
            dataIndex: 'startingAddress',
            sorter: true,
            render: (val) => {
                return (
                    val?.district ? val?.province + ", " + val?.district : val?.province + ", " + val?.country
                )
            }
        },
        {
            title: t('Teslimat Noktası'),
            dataIndex: 'dueAddress',
            sorter: true,
            render: (val) => {
                return (
                    val?.district ? val?.province + ", " + val?.district : val?.province + ", " + val?.country
                )
            }
        },
        {
            title: t('Yükleme Zamanı'),
            dataIndex: 'adStartingDate',
            sorter: true,
            render: (val) => {
                return (
                    moment(val).format('LL')
                )
            }
        },
    ];
    const abroudColumns = [
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
            title: t('Taşıma Durumu'),
            dataIndex: 'advertisementProcessStatus',
            width: 250,
            sorter: true,
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

    const onChange = async (pagination, filters, sorter) => {
        pageable.page = pagination.current;
        pageable.pageSize = pagination.pageSize;
        let sorterField;
        if (sorter.order) {
            if (sorter.field === "clientAdvertisementType") {
                sorterField = "clientAdvertisementType"
            } else if (sorter.field === "advertisementProcessStatus") {
                sorterField = "advertisementProcessStatus"
            } else if (sorter.field === "clientAdvertisementCode") {
                sorterField = "clientAdvertisementCode"
            } else if (sorter.field === "startingAddress") {
                sorterField = "startingAddress.province"
            } else if (sorter.field === "dueAddress") {
                sorterField = "dueAddress.province"
            } else if (sorter.field === "adStartingDate") {
                sorterField = "adStartingDate"
            } else {
                pageable.sort = ""
            }
        } else {
            pageable.sort = ""
        };
        if (sorterField) {
            pageable.sort = `${sorterField + ","}${sorter.order === "ascend" ? "asc" : sorter.order === "descend" ? "desc" : ""}`
        };
        setPageable({ ...pageable });
        fetchMyAds();
    }

    return (
        <div className="layout-content-padding">
            <div style={{ display: "flex" }}>
                <CustomButton
                    disabled={isLoading}
                    onClick={() => setIsActive(true)}
                    color={isActive ? "primary" : "light"}
                >
                    {t("Aktif ilanlarım")}
                </CustomButton>
                <CustomButton
                    disabled={isLoading}
                    onClick={() => setIsActive(false)}
                    color={!isActive ? "primary" : "light"}
                >
                    {t("Geçmiş ilanlarım")}
                </CustomButton>
            </div>
            
            {
                domasticOrAbroud === "DOMASTIC"&&
                <div style={{width: "100%", display: "flex", justifyContent: "center"}}>
                    <Button type="primary" onClick={() => {setDomasticOrAbroud("ABROUD")}}>{t("Yurt Dışı İlanlarını Görüntüle")}</Button>
                </div>
            }
            {
                domasticOrAbroud === "ABROUD"&&
                <div style={{width: "100%", display: "flex", justifyContent: "center"}}>
                    <Button type="primary" onClick={() => {setDomasticOrAbroud("DOMASTIC")}}>{t("Yurt İçi İlanlarını Görüntüle")}</Button>
                </div>
            }
            {
                domasticOrAbroud === "DOMASTIC"&&
                    <Table
                    rowKey={(val) => val.id}
                    loading={isLoading}
                    columns={columns}
                    dataSource={myTransports}
                    onChange={onChange}
                    scroll={{ x: "auto" }}
                    className="myAdsTable"
                    pagination={{
                        total: pageable.totalSize,
                        pageSize: pageable.pageSize,
                        current: pageable.page,
                    }}
                    onRow={e => ({
                        onClick: () => {
                            navigate(`/client/transport-detail/${e.id} `)
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
            }
            {
                domasticOrAbroud === "ABROUD"&&
                    <Table
                    loading={adsLoading}
                    columns={abroudColumns}
                    dataSource={dataAbroud}
                    onChange={onChange}
                    scroll={{ x: "auto" }}
                    className="myAdsTable"
                    pagination={{
                        total: pageable.totalSize,
                        pageSize: pageable.pageSize,
                        current: pageable.page,
                    }}
                    onRow={e => ({
                        onClick: () => {
                            navigate(`/client/transport-detail-abroud/${e.key} `)
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
            }
            <SupportBanner />
        </div>
    )
}

export default MyAds;