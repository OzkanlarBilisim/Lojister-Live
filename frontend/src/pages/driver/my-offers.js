import { Menu, Table, Tooltip, Button, Input, Space} from 'antd'
import { useSelector } from 'react-redux';
import React, { useEffect, useState, useRef} from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { ProgressBar, Step } from 'react-step-progress-bar'
import { CustomButton } from '../../components/CustomButton'
import { getMyBidsRequest, getAllAdvertisementYurtdisiDriverBidList } from '../../api/controllers/client-advertisiment-bid-controller'
import { ParsedOffer } from '../../components/ParsedValues'
import { ParsedDriverAbroud } from '../../components/ParsedValuesAbroad'
import moment from 'moment'
import RequestError from '../../components/RequestError'
import NoneContent from '../../components/NoneContent'
import Highlighter from 'react-highlight-words';
import { SearchOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next'

export default function MyOffers() {
    
    const currentUser = useSelector((state) => state.user);
    const searchInput = useRef(null);
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const [transports, setTransports] = useState([]);
    const [adsLoading, setAdsLoading] = useState(true);

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };
    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText('');
    };

    const [abroudBidTable, setAbroudBidTable] = useState(false);
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Tekliflerim")

    const TransportTypeForTable = ({ ClientAdvertisementType, status }) => {
        return (
            <Tooltip
                title={ClientAdvertisementType}
            >
                <div
                    style={{
                        borderRadius: ".5rem",
                        padding: "1rem",
                        width: "fit-content",
                        backgroundColor: ParsedOffer(status)?.bg,
                    }}>
                    <img src={"/assets/img/types/" + (ParsedOffer(status)?.color === 'blue' ? "blue" : ParsedOffer(status)?.color === 'orange' ? "orange" : ParsedOffer(status)?.color === 'red' ? "red" : ParsedOffer(status)?.color === 'green' ? "green" : "") + ClientAdvertisementType + ".svg"} alt="" />
                </div>
            </Tooltip>
        )
    }
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


    const TransportStatusForTable = ({ status }) => {
        

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
                    {t(ParsedOffer(status)?.title)}
                </p>

                <p
                    style={{
                        margin: 0,
                        opacity: .5,
                        lineHeight: "14px"
                    }}>
                    {t(ParsedOffer(status)?.description)}
                </p>
                <ProgressBar
                    percent={ParsedOffer(status)?.percent}
                    filledBackground="transparent"
                >
                    <Step transition="scale">
                        {({ accomplished }) => (
                            ParsedOffer(status).color === "red" ?
                                <i style={{ color: `var(--${ParsedOffer(status).color})` }} className="bi bi-x-circle-fill"></i> :
                                status === "WAITING" ?
                                    <i style={{ color: `var(--${ParsedOffer(status).color})` }} className="bi bi-clock-history"></i> :
                                    < div style={{
                                        width: 12,
                                        height: 12,
                                        borderRadius: "50%",
                                        border: "1px solid #fff",
                                        backgroundColor: accomplished ? `var(--${ParsedOffer(status).color})` : "var(--dark-light)",
                                    }} />
                        )}
                    </Step>
                    <Step transition="scale">
                        {({ accomplished }) => (
                            accomplished && ParsedOffer(status)?.color === "orange" && ParsedOffer(status)?.percent === 25 ?
                                <i style={{ color: `var(--${ParsedOffer(status).color})` }} className="bi bi-clock-history"></i> :
                                <div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedOffer(status).color})` : "var(--dark-light)",
                                }} />
                        )}
                    </Step>
                    <Step transition="scale">
                        {({ accomplished }) => (
                            status === "CARGO_ON_THE_WAY" ?
                                <img style={{ transform: "rotateY(180deg)" }} height="16px" src="/assets/img/typesForTable/orangeCARGO.svg" alt="" /> :
                                <div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedOffer(status).color})` : "var(--dark-light)",
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
                                backgroundColor: accomplished ? `var(--${ParsedOffer(status).color})` : "var(--dark-light)",
                            }} />
                        )}
                    </Step>
                    <Step transition="scale">
                        {({ accomplished }) => (
                            accomplished ? <i style={{ color: `var(--${ParsedOffer(status).color})` }} className="bi bi-check-circle-fill"></i> :
                                <div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedOffer(status).color})` : "var(--dark-light)",
                                }} />
                        )}
                    </Step>
                </ProgressBar>
            </div>
        )
    }
    const TransportStatusForTableAbroud = ({ status }) => {
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
                    {t(ParsedDriverAbroud(status)?.title)}
                </p>
                <p
                    style={{
                        margin: 0,
                        opacity: .5,
                        lineHeight: "14px"
                    }}>
                    {t(ParsedDriverAbroud(status)?.description)}
                </p>
                <ProgressBar
                    percent={ParsedDriverAbroud(status)?.percent}
                    filledBackground="transparent"
                >
                    <Step transition="scale">
                        {({ accomplished }) => (
                            ParsedDriverAbroud(status)?.color === "red" ?
                                <i style={{ color: `var(--${ParsedDriverAbroud(status)?.color})` }} className="bi bi-x-circle-fill"></i> :
                                status === "WAITING" ?
                                    <i style={{ color: `var(--${ParsedDriverAbroud(status)?.color})` }} className="bi bi-clock-history"></i> :
                                    < div style={{
                                        width: 12,
                                        height: 12,
                                        borderRadius: "50%",
                                        border: "1px solid #fff",
                                        backgroundColor: accomplished ? `var(--${ParsedDriverAbroud(status)?.color})` : "var(--dark-light)",
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
                                backgroundColor: accomplished ? `var(--${ParsedDriverAbroud(status)?.color})` : "var(--dark-light)",
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
                                    backgroundColor: accomplished ? `var(--${ParsedDriverAbroud(status)?.color})` : "var(--dark-light)",
                                }} />
                        )}
                    </Step>
                    <Step transition="scale">
                    {({ accomplished }) => (
                                status === "TRANSPORT" ?
                                    <img height="16px" src="/assets/img/typesForTable/CARGO_ORANGE.svg" alt="" /> :
                                    <div style={{
                                        width: 12,
                                        height: 12,
                                        borderRadius: "50%",
                                        border: "1px solid #fff",
                                        backgroundColor: accomplished ? `var(--${ParsedDriverAbroud(status)?.color})` : "var(--dark-light)",
                                    }} />
                        )}
                    </Step>
                    <Step transition="scale">
                        {({ accomplished }) => (
                            accomplished ? <i style={{ color: `var(--${ParsedDriverAbroud(status)?.color})` }} className="bi bi-check-circle-fill"></i> :
                                <div style={{
                                    width: 12,
                                    height: 12,
                                    borderRadius: "50%",
                                    border: "1px solid #fff",
                                    backgroundColor: accomplished ? `var(--${ParsedDriverAbroud(status)?.color})` : "var(--dark-light)",
                                }} />
                        )}
                    </Step>
                </ProgressBar>
            </div>
        )
    }
    const [searchParams, setSearchParams] = useSearchParams();
    let param = searchParams.get("status");

    const [isActive, setIsActive] = useState(param === "passive" ? false : true)
    const [isLoading, setIsLoading] = useState(true)
    const [myOffers, setMyOffers] = useState()
    const [pageable, setPageable] = useState({
        page: 1,
        pageSize: 10,
        totalSize: null,
        sort: "",
    })
    const navigate = useNavigate();

    const fetchMyOffers = async () => {
        setIsLoading(true)
        try {
            let res = await getMyBidsRequest({ isActive, pageable });
            if (res) {
                setMyOffers(res.data.content)
                setPageable({ ...pageable, totalSize: res.data.totalElements })
            }
            setIsLoading(false)
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        getAds();
        fetchMyOffers();
        setPageable({ ...pageable, page: 1 })
    }, [isActive]);


    const columns = [
        {
            title: t('Taşıma Tipi'),
            dataIndex: "summaryAdvertisementData",
            width: 0,
            sorter: true,
            render: (val, row) => {
                return (
                    <TransportTypeForTable ClientAdvertisementType={val.advertisementType} status={row.bidStatus === "AD_CLOSED" || row.bidStatus === "DENIED" || row.bidStatus === "TIMEOUT" ? row.bidStatus : row.advertisementProcessStatus} isActive={isActive} />
                )
            }
        },
        {
            title: t('Taşıma Durumu'),
            dataIndex: "advertisementProcessStatus",
            width: 250,
            sorter: true,
            render: (val, row) => {
                return (
                    <TransportStatusForTable ClientAdvertisementType={row.summaryAdvertisementData.advertisementType} status={row.bidStatus === "AD_CLOSED" || row.bidStatus === "DENIED" || row.bidStatus === "TIMEOUT" ? row.bidStatus : val} bidStatus={row.bidStatus} />
                )
            }
        },
        {
            title: t('İlan Numarası'),
            dataIndex: "summaryAdvertisementData",
            sorter: true,
            render: (val) => {
                return (
                    val?.clientAdvertisementCode
                )
            }
        },
        {
            title: t('Yükleme Noktası'),
            dataIndex: "summaryAdvertisementData",
            sorter: true,
            render: (val) => {
                return (
                    <div>
                        <div>{val?.startingAddress}</div>
                        <div>{moment(val?.adStartingDate).format("L")}</div>
                    </div>
                )
            }
        },
        {
            title: t('Teslimat Noktası'),
            dataIndex: "summaryAdvertisementData",
            sorter: true,
            render: (val) => {
                return (
                    <div>
                        <div>{val?.dueAddress}</div>
                        <div>{moment(val?.adDueDate).format("L")}</div>
                    </div>
                )
            }
        },
        {
            title: t('Verilen Teklif Tutarı'),
            dataIndex: "bid",
            sorter: true,
            render: (val) => {
                return (
                    <div style={{ display: "flex", flexDirection: "row", gap: "3px" }}>
                        <p>{val.toFixed(2)}₺</p>
                    </div>
                )
            }
        },
    ];

    const onChange = (pagination, filters, sorter) => {
        pageable.page = pagination.current;
        pageable.pageSize = pagination.pageSize;
        let sorterField;
        if (sorter.order) {
            if (sorter.column.title === t("Taşıma Tipi")) {
                sorterField = "summaryAdvertisementData.advertisementType"
            } else if (sorter.column.title === t("Taşıma Durumu")) {
                sorterField = "clientAdvertisement.advertisementProcessStatus"
            } else if (sorter.column.title === t("İlan Numarası")) {
                sorterField = "summaryAdvertisementData.clientAdvertisementCode"
            } else if (sorter.column.title === t("Yükleme Noktası")) {
                sorterField = "summaryAdvertisementData.startingAddress"
            } else if (sorter.column.title === t("Teslimat Noktası")) {
                sorterField = "summaryAdvertisementData.dueAddress"
            } else if (sorter.column.title === t("Verilan Teklif Tutarı")) {
                sorterField = "bid"
            } else {
                pageable.sort = ""
            }
        } else {
            pageable.sort = ""
        };
        if (sorterField) {
            pageable.sort = `${sorterField + ","}${sorter.order === "ascend" ? "asc" : sorter.order === "descend" ? "desc" : ""}`
        };
        setPageable({ ...pageable })
        fetchMyOffers()
    }
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
            dataIndex: 'bidStatus',
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
    const getAds = async () => {

        setAdsLoading(false)
        try {
            let res = await getAllAdvertisementYurtdisiDriverBidList(currentUser?.companyId);
            if (res) {
                console.log(res.data);
                console.log(res.data.length);
                setTransports(res.data);
                setPageable({ ...pageable, totalSize: res.data.length })
            }
            setAdsLoading(false)
        } catch (error) {
            RequestError(error);
        }


    }
    console.log(transports);
    const dataAbroud = transports.map(val => {
        return { 
                key: val.advertisementId,
                transportType: val.transportType,
                bidStatus: <TransportStatusForTableAbroud status={val.bidStatus} />,
                loadingDate: val.loadingDate,
                loadingZone: val.loadingZone,
                destinationZone: val.destinationZone
            };
      });

    return (
        <div className="layout-content-padding">
            <div style={{ display: "flex" }}>
                <CustomButton
                    disabled={isLoading}
                    onClick={() => setIsActive(true)}
                    color={isActive ? "primary" : "light"}
                >
                    {t("Aktif tekliflerim")}
                </CustomButton>
                <CustomButton
                    disabled={isLoading}
                    onClick={() => setIsActive(false)}
                    color={!isActive ? "primary" : "light"}
                >
                    {t("Geçmiş tekliflerim")}
                </CustomButton>
            </div>
            <div>
                {
                    abroudBidTable &&
                    <div style={{width: "100%", display: "flex", justifyContent: "center"}}>
                    <Button onClick={() => setAbroudBidTable(false)} type="primary">{t("Yurt İçi Tekliflerim")}</Button>
                    </div>
                }
                {
                    !abroudBidTable &&
                    <div style={{width: "100%", display: "flex", justifyContent: "center"}}>
                    <Button onClick={() => setAbroudBidTable(true)} type="primary">{t("Yurt Dışı Tekliflerim")}</Button>
                    </div>
                }
                {
                    !abroudBidTable &&
                        <Table
                        rowKey={(val) => val.id}
                        loading={isLoading}
                        columns={columns}
                        dataSource={myOffers}
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
                                navigate(`/driver/offer-detail/${e.id}`)
                            }
                        })}
                        locale={{
                            emptyText:
                                <NoneContent
                                    title={t("Teklif bulunamadı!")}
                                    description={t(`Hesabınızda teklif bulunamadı. Hemen sizde Lojister’da ki ilanlara teklif verin güvenli ve avantajlı hizmetin keyfini çıkarın.`)}
                                />
                        }}
                    />
                }
                {
                    abroudBidTable &&
                        <Table
                            rowKey={(val) => console.log(val)}
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
                                    navigate(`/driver/offer-detail-abroud/${e.key} `)
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
            </div>
        </div>
    )
}
