import { Button, Table, Form, Modal, Row, Col, Input, Menu, Dropdown, Popconfirm, Select } from 'antd';
import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next';
import NumberFormat from 'react-number-format';
import { useSelector } from 'react-redux';
import { createEmployeeRequest, deleteEmployeeByIdRequest, getMyEmployeeRequest } from '../../api/controllers/client-controller';
import AntNotification from '../../components/AntNotification';
import { Countries } from '../../components/Countries';
import RequestError from '../../components/RequestError';
import ShowProfilePhoto from '../../components/ShowProfilePhoto';

export default function Users() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Kullanıcılar")
    const { Option } = Select;
    const currentUser = useSelector((state) => state.user);

    const [form] = Form.useForm();
    const formValues = {
        email: "",
        firstName: "",
        lastName: "",
        password: "",
        passwordConfirm: "",
        phone: "",
        selectedCountryCode: "+90",
    }

    const [isLoading, setIsLoading] = useState(true)
    const [employees, setEmployees] = useState([])
    const [employee, setEmployee] = useState(formValues)
    const [createEmployeeModal, setCreateEmployeeModal] = useState(false)
    const [pageable, setPageable] = useState({
        page: 1,
        pageSize: 10,
        totalSize: null,
        sort: "createdDateTime,desc",
    })

    const fetchEmployees = async () => {
        setIsLoading(true)
        try {
            let res = await getMyEmployeeRequest(pageable);
            if (res) {
                setEmployees(res.data.content);
                setPageable({ ...pageable, totalSize: res.data.totalElements })
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false)
    }

    const createEmployees = async () => {
        setIsLoading(true)
        employee.phone = employee.selectedCountryCode + "~" + employee.phone
        setEmployee({ ...employee })
        try {
            let res = await createEmployeeRequest(employee);
            if (res) {
                onClose();
                fetchEmployees();
                AntNotification({ type: "success", message: t("İşlem başarılı!"), description: t("Kullanıcı eklendi.") });
            }
        } catch (error) {
            employee.phone = employee.phone.split(/[~]/g)[1]
            setEmployee({ ...employee })
            RequestError(error);
        }
        setIsLoading(false)
    }

    useEffect(() => {
        fetchEmployees();
    }, [])

    const onClose = () => {
        form.setFieldsValue(formValues);
        setEmployee(formValues);
        setCreateEmployeeModal(false);
    }

    const onChange = (pagination, filters, sorter) => {
        pageable.page = pagination.current;
        pageable.pageSize = pagination.pageSize;
        if (sorter.order) {
            pageable.sort = `${sorter.field + ","}${sorter.order === "ascend" ? "asc" : sorter.order === "descend" ? "desc" : ""}`
        } else {
            pageable.sort = "createdDateTime,desc"
        };
        setPageable({ ...pageable });
        fetchEmployees();
    }

    const onDelete = async (id) => {
        try {
            let res = await deleteEmployeeByIdRequest(id);
            if (res) {
                fetchEmployees();
                AntNotification({ type: "success", message: t("İşlem başarılı!"), description: t("Kullanıcı silindi.") });
            }
        } catch (error) {
            RequestError(error);
        }
    }

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
            title: '',
            dataIndex: "id",
            align: 'right',
            width: 0,
            render: (id) => {
                return (
                    <Dropdown overlay={menu(id)} placement="bottomRight" arrow={{ pointAtCenter: true }}>
                        <Button type="text" shape="round" icon={<i className="bi bi-three-dots-vertical"></i>}></Button>
                    </Dropdown>
                )
            }
        }
    ];

    const menu = (id) => {
        return (
            <Menu
                items={[
                    {
                        label: (
                            <Popconfirm
                                title={t("Kullanıcıyı silmek istediğinize emin misiniz?")}
                                placement="bottomLeft"
                                onConfirm={() => onDelete(id)}
                                okText={t("Evet")}
                                cancelText={t("Hayır")}
                            >
                                <div
                                    style={{ minWidth: 100, color: "var(--red)" }}
                                >
                                    <i class="bi bi-trash"></i> {t("Sil")}
                                </div>
                            </Popconfirm>
                        ),
                    },
                ]}
            />

        )
    };

    return (
        <div className="layout-content-padding">
            {currentUser.role === "ROLE_CLIENT" || currentUser.role === "ROLE_DRIVER" ?
                <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: "1rem" }}>
                    <Button
                        onClick={() => setCreateEmployeeModal(true)}
                        type="primary"
                        icon={<i style={{ marginRight: ".5rem" }}
                            className="bi bi-plus-circle-fill"></i>}> {t("Yeni Ekle")} </Button>
                </div> :
                <></>
            }
            <div>
                <Table
                    rowKey={val => val.id}
                    loading={isLoading}
                    columns={columns}
                    dataSource={employees}
                    onChange={onChange}
                    className="myAdsTable"
                    pagination={{
                        total: pageable.totalSize,
                        pageSize: pageable.pageSize,
                        current: pageable.page,
                    }}
                />
            </div>
            <Modal
                visible={createEmployeeModal}
                title={t("Yeni kullanıcı ekleyin")}
                onCancel={onClose}
                onOk={createEmployees}
                cancelText={t("İptal")}
                okText={t("Ekle")}
                okButtonProps={{
                    loading: isLoading,
                    disabled: employee.passwordConfirm.length < 1 || employee.passwordConfirm !== employee.password
                }}
                centered
            >
                <Form
                    form={form}
                    layout='vertical'
                    initialValues={employee}
                    onFinish={createEmployees}
                    scrollToFirstError={true}
                >
                    <Row gutter={[12, 12]}>
                        <Col span={24}>
                            <Form.Item
                                label={t("İsim")}
                                name="firstName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Kullanıcı ismini yazın")}
                                    onChange={e => {
                                        setEmployee({ ...employee, firstName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Soyisim")}
                                name="lastName"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Kullanıcı soyismini yazın")}
                                    onChange={e => {
                                        setEmployee({ ...employee, lastName: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Telefon")}
                                name="phone"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <NumberFormat
                                    addonBefore={<Select
                                        style={{ width: 100 }}
                                        name='selectedCountryCode'
                                        showSearch
                                        size='large'
                                        value={employee.selectedCountryCode}
                                        onChange={(value) => {
                                            employee.selectedCountryCode = Countries.find(v => v.dial_code === value)?.dial_code
                                            setEmployee({ ...employee })
                                        }}
                                        filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                    >
                                        {
                                            Countries.map((v, i) =>
                                                <Option key={i} value={v.dial_code}>{v.code + " " + v.dial_code}</Option>
                                            )
                                        }
                                    </Select>}
                                    size="large"
                                    format="### ### ## ## ## ## ## ## ##"
                                    placeholder={t("Kullanıcı telefonunu yazın")}
                                    customInput={Input}
                                    onValueChange={({ value }) => {
                                        employee.phone = `${value}`
                                        setEmployee({ ...employee })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Email")}
                                name="email"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input
                                    size="large"
                                    placeholder={t("Kullanıcı mail adresini yazın")}
                                    onChange={e => {
                                        setEmployee({ ...employee, email: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Şifre")}
                                name="password"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                            >
                                <Input.Password
                                    size="large"
                                    placeholder={t("Kullanıcı şifresini yazın")}
                                    onChange={e => {
                                        setEmployee({ ...employee, password: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                        <Col span={24}>
                            <Form.Item
                                label={t("Şifre Tekrar")}
                                name="passwordConfirm"
                                rules={[{ required: true, message: t('Lütfen bu alanı doldurun!') }]}
                                validateStatus={employee.passwordConfirm.length > 0 && employee.passwordConfirm !== employee.password && "error"}
                                help={employee.passwordConfirm.length > 0 && employee.passwordConfirm !== employee.password && "Şifreler uyuşmuyor"}
                            >
                                <Input.Password
                                    size="large"
                                    placeholder={t("Kullanıcı şifresini tekrar yazın")}
                                    onChange={e => {
                                        setEmployee({ ...employee, passwordConfirm: e.target.value })
                                    }}
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>
        </div >
    )
}
