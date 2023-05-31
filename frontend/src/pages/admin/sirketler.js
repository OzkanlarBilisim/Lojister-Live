import React, { useEffect, useState } from 'react'
import { Button, Divider, Modal, Table, InputNumber, Input } from 'antd'
import { getCompaniesRequest, updateCompanyCommissionRateRequest } from '../../api/controllers/company-controller';
import moment from 'moment';
import SuspenseFallbackInline from '../../components/SuspenseFallbackInline';
import RequestError from '../../components/RequestError';
import NumberFormat from 'react-number-format';

const AdminSirketler = () => {
    const [companies, setCompanies] = useState([]);
    const [selectedCompany, setSelectedCompany] = useState({});
    const [isVisible, setIsVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [form, setForm] = useState({
        commissionRate: 5,
        companyId: null,
    })

    const fetchCompanies = async () => {
        setIsLoading(true);
        try {
            let res = await getCompaniesRequest();
            if (res) {
                setCompanies(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
        setIsLoading(false);
    }

    useEffect(() => {
        fetchCompanies();
    }, [])

    const openModal = (company) => {
        setIsVisible(true);
        setSelectedCompany(company);
        form.commissionRate = company.commissionRate;
        form.companyId = company.id;
        setForm({ ...form });
    }

    const closeModal = () => {
        setIsVisible(false);
        setSelectedCompany({});
        setForm({
            commissionRate: 5,
            companyId: null,
        })
    }

    const handleUpdateComissionRate = async () => {
        try {
            let res = await updateCompanyCommissionRateRequest(form);
            if (res) {
                closeModal();
                fetchCompanies();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: '1',
        },
        {
            title: 'Şirket Adı',
            dataIndex: 'commercialTitle',
            key: '2',
        },
        {
            title: 'Komisyon Oranı %',
            dataIndex: 'commissionRate',
            key: '3',
        },
        {
            title: 'Kayıt Tarihi',
            dataIndex: 'createdDateTime',
            key: '4',
            render: e => moment(e).format("LLL")
        },
        {
            title: 'İncele',
            align: 'center',
            dataIndex: 'id',
            key: '4',
            render: (e, row) => <Button onClick={() => openModal(row)}>İncele</Button>
        },
    ];

    const Label = ({ title, text }) => (
        <div style={{ marginBottom: "1rem" }}>
            <p style={{ fontSize: "12px", opacity: ".7", fontWeight: "500" }}>{title}</p>
            <p>{text ? text : "-"}</p>
        </div>
    )

    return (
        <>
            <Table
                columns={columns}
                dataSource={companies}
                loading={isLoading}
                rowKey={e => e?.id}
                scroll={{ x: "auto" }}
            />

            <Modal
                title="Şirket detayları"
                centered
                visible={isVisible}
                footer={null}
                onCancel={() => closeModal()}
            >
                {
                    selectedCompany ?
                        <div >
                            <Label
                                title="Telefon"
                                text={selectedCompany?.phone}
                            />
                            <Label
                                title="Vergi Dairesi"
                                text={selectedCompany?.taxAdministration}
                            />
                            <Label
                                title="Vergi No"
                                text={selectedCompany?.taxNumber}
                            />
                            <Label
                                title="Komisyon Oranı"
                                text={
                                    <div style={{ display: "flex", gap: ".5rem" }}>
                                        <NumberFormat
                                            customInput={Input}
                                            onValueChange={({ value }) => setForm({ ...form, commissionRate: value })}
                                            value={form?.commissionRate} />
                                        <Button type="primary" onClick={() => handleUpdateComissionRate()}>Kaydet</Button>
                                    </div>
                                }
                            />
                        </div> :
                        <SuspenseFallbackInline />
                }
            </Modal>
        </>
    )
}

export default AdminSirketler;