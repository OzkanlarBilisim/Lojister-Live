import React, { useEffect, useState } from 'react'
import { Table, Button, Tag } from 'antd';
import { useNavigate } from 'react-router';
import { getMyDriversRequest } from '../../api/controllers/driver-controller';
import ShowProfilePhoto from '../../components/ShowProfilePhoto';
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';

function Drivers() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Sürücüler")
    const currentUser = useSelector((state) => state.user);

    const [isLoading, setIsLoading] = useState(true)
    const [drivers, setDrivers] = useState([])
    const [pageable, setPageable] = useState({
        page: 1,
        pageSize: 10,
        totalSize: null,
        sort: "createdDateTime,desc",
    })
    const navigate = useNavigate();

    const fetchDrivers = async () => {
        setIsLoading(true)
        try {
            let res = await getMyDriversRequest(pageable);
            if (res) {
                setDrivers(res.data.content);
                setPageable({ ...pageable, totalSize: res.data.totalElements })
            }
            setIsLoading(false)
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        fetchDrivers();
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
                return {
                    title: "Belge yüklenmesi bekleniyor",
                    color: "var(--orange)",
                    bgColor: "var(--orange-light)",
                    icon: <i className="bi bi-clock-history"></i>,
                }
            case "REVIEW":
                return {
                    title: "Değerlendiriliyor",
                    color: "var(--blue)",
                    bgColor: "var(--blue-light)",
                    icon: <i className="bi bi-clock-history"></i>,
                }
            case "REVIEW_SENT":
                return {
                    title: "Provizyon bekliyor",
                    color: "var(--red)",
                    bgColor: "var(--red-light)",
                    icon: <i className="bi bi-clock-history"></i>,
                }
            case "REVISION":
                return {
                    title: "Değerlendiriliyor",
                    color: "var(--blue)",
                    bgColor: "var(--blue-light)",
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
    };

    const columns = [
        {
            title: '',
            dataIndex: 'id',
            width: 50,
            render: (id) => (
                <ShowProfilePhoto profileId={id} />
            )
        },
        {
            title: t('İsim Soyismi'),
            dataIndex: 'firstName',
            width: 250,
            sorter: true,
            render: (v, row) => (
                row.firstName + " " + row.lastName
            )
        },
        {
            title: t('Mail Adresi'),
            dataIndex: 'email',
            sorter: true,
        },
        {
            title: t('Telefon'),
            dataIndex: 'phone',
            sorter: true,
            render: (v, row) => (
                <div style={{ minWidth: "160px" }}>
                    {row.phone.replace("~", " ")}
                </div>
            )
        },
        {
            title: t('Durum'),
            dataIndex: 'status',
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
        fetchDrivers()
    }

    return (
        <div className="layout-content-padding">
            {currentUser.role === "ROLE_CLIENT" || currentUser.role === "ROLE_DRIVER" ?
                <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "1rem" }}>
                    <Button
                        onClick={() => navigate("/driver/add-driver")}
                        type="primary"
                        icon={<i style={{ marginRight: ".5rem" }}
                            className="bi bi-plus-circle-fill"></i>}>{t("Yeni Ekle")}</Button>
                </div>
                : <></>
            }
            <div>
                <Table
                    rowKey={val => val.id}
                    loading={isLoading}
                    columns={columns}
                    dataSource={drivers}
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
                            navigate(`/driver/driver-detail/${e.id}`)
                        }
                    })}
                />
            </div>
        </div>
    )
}

export default Drivers;