import { Avatar, Collapse, Input } from 'antd'
import React, { useEffect, useState } from 'react'
import { CaretRightOutlined } from '@ant-design/icons';
import { getAllFaq } from '../api/controllers/frequently-asked-questions-controller';
import SuspenseFallbackInline from "./SuspenseFallbackInline"
import { useTranslation } from 'react-i18next';

const FAQ = () => {
    const { t, i18n } = useTranslation("common");

    const { Panel } = Collapse;

    const [faqData, setFaqData] = useState([])
    const [searchInput, setSearchInput] = useState("")
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        (async () => {
            let res = await getAllFaq()
            let newRes = res.data.filter(x => String(x.tr_answer).toLowerCase().includes(searchInput) || String(x.tr_question).toLowerCase().includes(searchInput))
            if (newRes) {
                setFaqData(newRes)
                setIsLoading(false)
            }
        })()
        return () => { }
    }, [searchInput])


    return (
        isLoading ? <SuspenseFallbackInline /> :
            <>
                <div style={{ display: "flex", flexDirection: "column", gap: "1rem" }} className="layout-content-padding">
                    <div style={{ display: "flex", flexDirection: "column", gap: ".5rem", justifyContent: "center", alignItems: "center" }}>
                        <Avatar style={{ backgroundColor: "var(--blue-light)" }} size={64} icon={<i style={{ color: "var(--blue)" }} className="bi bi-question-lg"></i>} />
                        <p style={{ textAlign: "center", fontSize: "1.1rem" }}>{t("Sıkça Sorulan Sorular")}</p>
                    </div>

                    <div className='FAQ'>
                        <Input
                            style={{ display: "flex", justifyContent: "center", maxWidth: "500px", border: "1px solid var(--dark)" }}
                            prefix={<i className="bi bi-search"></i>}
                            size="large"
                            placeholder={t("Arama yapmak istediğiniz soruyu yazabilirsiniz...")}
                            onChange={e => {
                                setSearchInput(e.target.value.toLowerCase())
                            }}
                        />
                    </div>

                    <Collapse
                        accordion
                        bordered={true}
                        defaultActiveKey={['1']}
                        expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                        className="site-collapse-custom-collapse"
                    >
                        {
                            faqData.map((v) => (
                                <Panel header={v.tr_question} key={v.id} className="site-collapse-custom-panel">
                                    <p>{v.tr_answer}</p>
                                </Panel>
                            ))
                        }
                    </Collapse>

                </div >
            </>

    )
}

export default FAQ