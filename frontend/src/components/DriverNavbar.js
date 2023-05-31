import { Divider, Modal } from 'antd'
import React from 'react'
import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';
import { Link, useMatch, useResolvedPath, useNavigate } from 'react-router-dom';
import { logoutRequest } from '../api/controllers/account-controller';
import { setUser } from '../redux/actions/userActions';
import AntNotification from './AntNotification';
import RequestError from './RequestError';

function CustomLink({ children, to, ...props }) {

    let resolved = useResolvedPath(to);
    let match = useMatch({ path: resolved.pathname, end: true });

    return (
        <Link
            className={match ? "layout-menu-item active" : "layout-menu-item"}
            to={to}
            {...props}
        >
            {children}
        </Link>
    );
}
export default function DriverNavbar() {
    const { t, i18n } = useTranslation("common");

    const navigate = useNavigate();
    const { confirm } = Modal;
    const dispatch = useDispatch();

    const onClickLogout = async () => {
        try {
            let res = logoutRequest();
            if (res) {
                navigate("/")
                dispatch(setUser(null));
                localStorage.removeItem("token")

                AntNotification({ type: "success", message: t("Başarıyla çıkış yapıldı!") })
            }
        } catch (error) {
            RequestError(error);
        }
    };

    const showDeleteConfirm = () => {
        confirm({
            centered: true,
            title: t("Çıkış yapmak istediğinize emin misiniz?"),
            okText: t("Evet"),
            okType: 'primary',
            cancelText: t("Hayır"),
            onOk() {
                onClickLogout()
            },
        });
    };

    return (
        <div className="layout-menu">
            <CustomLink to="/driver"><i className="bi bi-grid"></i>{t("Panelim")} </CustomLink>
            <CustomLink to="/driver/transports"><i className="bi bi-bookmarks"></i>{t("İlanlar")}</CustomLink>
            <CustomLink to="/driver/my-offers"><i className="bi bi-bookmark-plus"></i>{t("Tekliflerim")}</CustomLink>
            <CustomLink to="/driver/tracking"><i className="bi bi-cursor"></i>{t("Taşıma Takibi")}</CustomLink>
            <CustomLink to="/driver/reports"><i className="bi bi-clipboard-data"></i>{t("Raporlarım")} </CustomLink>
            <CustomLink to="/driver/vehicles"><i className="bi bi-truck"></i>{t("Araçlarım")}</CustomLink>
            <CustomLink to="/driver/drivers"><i className="bi bi-person"></i>{t("Sürücülerim")} </CustomLink>
            <CustomLink to="/driver/settings"><i className="bi bi-gear"></i>{t("Ayarlarım")}</CustomLink>
            <Divider style={{ margin: ".1rem 0" }} />
            <CustomLink to="/driver/support"><i className="bi bi-question-circle"></i>{t("Destek")} </CustomLink>
            <button
                onClick={() => showDeleteConfirm()}
                className="layout-menu-item"
                style={{
                    border: "none",
                    backgroundColor: "transparent",
                    cursor: "pointer",
                }}>
                <i className="bi bi-box-arrow-right"></i>{t("Çıkış Yap")}
            </button>
        </div>
    )
}
