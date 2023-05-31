import React, { useEffect } from 'react'
import PublicFooter from '../components/PublicFooter';
import PublicNavbar from '../components/PublicNavbar';
import { Collapse } from 'antd'
import { useTranslation } from 'react-i18next';

function KVKKHakkinda() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("KVKK Hakkında")

    return (
        <>
            <PublicNavbar />
            <section className="page">
                <h4 className="page-title"><i className="bi bi-chevron-double-right"></i> {t("KVKK Hakkında")}</h4>
                <div className="page-content">
                    <Collapse accordion defaultActiveKey="1">
                        <Collapse.Panel header={t("KİŞİSEL VERİLERİN KORUNMASI VE İŞLENMESİ POLİTİKASI")} key="1">
                            <iframe style={{ width: "100%", minHeight: "50vh", border: "none", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)" }} src="/assets/document/kisisel-verilerin-korunmasi-ve-islenmesi-hk.pdf"></iframe>
                        </Collapse.Panel>
                        <Collapse.Panel header={t("KİŞİSEL VERİLERİN İŞLENMESİ HAKKINDA AYDINLATMA METNİ")} key="2">
                            <iframe style={{ width: "100%", minHeight: "50vh", border: "none", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)" }} src="/assets/document/ucuncu-kisi-musteri-yuk-sahibi-tasiyici.pdf"></iframe>
                        </Collapse.Panel>
                        <Collapse.Panel header={t("ÖZKANLAR BİLİŞİM İÇ VE DIŞ TİCARET LİMİTED ŞTİ’ye (LOJİSTER) VERİ SAHİBİ BAŞVURU FORMU")} key="3">
                            <iframe style={{ width: "100%", minHeight: "50vh", border: "none", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)" }} src="/assets/document/ilgili-kisi-basvuru-formu.pdf"></iframe>
                        </Collapse.Panel>
                        <Collapse.Panel header={t("KİŞİSEL VERİLERİN İŞLENMESİ HAKKINDA AYDINLATMA METNİ")} key="4">
                            <iframe style={{ width: "100%", minHeight: "50vh", border: "none", borderRadius: "1rem", overflow: "hidden", boxShadow: "var(--default-shadow)" }} src="/assets/document/internet-sitesi-aydinlatma-metni.pdf"></iframe>
                        </Collapse.Panel>
                    </Collapse>
                </div>
            </section>
            <PublicFooter />
        </>
    )
}

export default KVKKHakkinda;