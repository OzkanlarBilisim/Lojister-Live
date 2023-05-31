import React, { useEffect, useState } from 'react'
import { Table, Tooltip, Button, Tag } from 'antd';
import { useNavigate } from 'react-router';
import { getMyVehiclesRequest } from '../../api/controllers/vehicle-controller';
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';

function Vehicles() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Araçlarım")

    const [isLoading, setIsLoading] = useState(true)
    const [vehicles, setVehicles] = useState([])
    const [pageable, setPageable] = useState({
        page: 1,
        pageSize: 10,
        totalSize: null,
        sort: "createdDateTime,desc",
    })
    const navigate = useNavigate();

    const fetchVehicles = async () => {
        setIsLoading(true)
        try {
            let res = await getMyVehiclesRequest(pageable);
            if (res) {
                setVehicles(res.data.content);
                setPageable({ ...pageable, totalSize: res.data.totalElements })
            }
            setIsLoading(false)
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        fetchVehicles();
    }, [])


    const parsedStatus = (status) => {
        switch (status) {
            case "ACCEPTED":
                return {
                    title: "Onaylandı",
                    color: "var(--green)",
                    bgColor: "var(--green-light)",
                    icon: <i className="bi bi-check-circle-fill"></i>,
                }
            case "DENIED":
                return {
                    title: "Reddedildi",
                    color: "var(--red)",
                    bgColor: "var(--red-light)",
                    icon: <i className="bi bi-x-circle-fill"></i>,
                }
            case "REGISTERED":
            case "REVIEW":
            case "REVIEW_SENT":
            case "REVISION":
                return {
                    title: "Değerlendiriliyor",
                    color: "var(--orange)",
                    bgColor: "var(--orange-light)",
                    icon: <i className="bi bi-clock-history"></i>,
                }

            default:
                return {
                    title: "Bilinmiyor",
                    color: "var(--dark)",
                    bgColor: "var(--dark-light)",
                    icon: <i className="bi bi-question-circle-fill"></i>,
                }
        }
    }

    const columns = [
        {
            title: t('Araç Tipi'),
            dataIndex: 'vehicleTypeName',
            width: 50,
            sorter: true,
            render: (vehicleTypeName) => (
                <Tooltip title={vehicleTypeName}>
                    <div style={{
                        padding: "1rem",
                        backgroundColor: "var(--primary-light)",
                        borderRadius: ".5rem",
                        display: "grid",
                        justifyContent: "center",
                        alignContent: "center",
                    }}>
                        {
                            vehicleTypeName ?
                                <img src={`/assets/img/vehicle-types/${vehicleTypeName}.svg`} /> :
                                <p>{t("Belirtilmemiş")}</p>
                        }
                    </div>
                </Tooltip>
            )
        },
        {
            title: t('Plaka'),
            dataIndex: 'licencePlate',
            sorter: true,
        },
        {
            title: t('Marka'),
            dataIndex: 'brand',
            sorter: true,
        },
        {
            title: t('Model'),
            dataIndex: 'vehicleModel',
            sorter: true,
        },
        {
            title: t('Durum'),
            dataIndex: 'vehicleStatus',
            align: 'right',
            sorter: true,
            render: (status) => (
                <Tag
                    style={{
                        backgroundColor: parsedStatus(status).bgColor,
                        color: parsedStatus(status).color,
                        borderColor: parsedStatus(status).color,
                    }}
                    icon={parsedStatus(status).icon}
                >
                    <span
                        style={{
                            marginLeft: ".25rem"
                        }}>
                        {t(parsedStatus(status).title)}
                    </span>
                </Tag>
            )
        },
    ];

    const onChange = (pagination, filters, sorter) => {
        pageable.page = pagination.current;
        pageable.pageSize = pagination.pageSize;
        if (sorter.order) {
            pageable.sort = `${sorter.field + ","}${sorter.order === "ascend" ? "asc" : sorter.order === "descend" ? "desc" : ""}`
        } else {
            pageable.sort = "createdDateTime,desc"
        };
        setPageable({ ...pageable })
        fetchVehicles()
    }

    return (
        <div className="layout-content-padding">
            <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "1rem" }}>
                <Button
                    onClick={() => navigate("/driver/add-vehicle")}
                    type="primary"
                    icon={<i
                        style={{ marginRight: ".5rem" }}
                        className="bi bi-plus-circle-fill"></i>}>
                    {t("Yeni Ekle")}
                </Button>
            </div>
            <div>
                <Table
                    rowKey={val => val.id}
                    loading={isLoading}
                    columns={columns}
                    dataSource={vehicles}
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
                            navigate(`/driver/vehicle-detail/${e.id}`)
                        }
                    })}
                />
            </div>
        </div >
    )
}

export default Vehicles;