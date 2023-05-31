import { useEffect, useLayoutEffect, useState } from 'react';
import '../pages/homePage/homePage.css';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { changeLanguageRequest, getMeRequest, logoutRequest } from '../api/controllers/account-controller';
import { useDispatch, useSelector } from 'react-redux';
import { setUser } from "../redux/actions/userActions";
import { LogoutOutlined, UserOutlined, SnippetsOutlined } from '@ant-design/icons';
import AntNotification from '../components/AntNotification';
import { Menu, Dropdown, Button, Modal, Select } from 'antd';
import RequestError from './RequestError';
import { useTranslation } from 'react-i18next';
import moment from 'moment';
import 'moment/locale/tr'
import 'moment/locale/en-nz'

const PublicNavbar = () => {

    const { t, i18n } = useTranslation("common");
    const { Option } = Select;
    const [currentLanguage, setCurrentLanguage] = useState();
    const currentUser = useSelector((state) => state.user);

    const changeLanguageHandler = async (lang) => {
        if (!currentUser) {
            localStorage.setItem('currentLanguage', lang)
            setCurrentLanguage(lang)
            i18n.changeLanguage(lang)
            moment.locale(
                localStorage.getItem('currentLanguage') === 'TURKISH' ? 'tr' :
                    localStorage.getItem('currentLanguage') === 'ENGLISH' ? 'en-nz' :
                        'tr'
            )
            localStorage.setItem('currentLanguageCode', lang === 'ENGLISH' ? 'en' : 'TURKISH' ? 'tr' : 'en')
        } else {
            try {
                let res = await changeLanguageRequest(lang)
                if (res) {
                    localStorage.setItem('currentLanguage', lang)
                    setCurrentLanguage(lang)
                    i18n.changeLanguage(lang)
                    moment.locale(
                        localStorage.getItem('currentLanguage') === 'TURKISH' ? 'tr' :
                            localStorage.getItem('currentLanguage') === 'ENGLISH' ? 'en-nz' :
                                'tr'
                    )
                    localStorage.setItem('currentLanguageCode', lang === 'ENGLISH' ? 'en' : 'TURKISH' ? 'tr' : 'en')
                    /* window.location.reload() */
                }
            } catch (error) {
                RequestError(error);
            }
        }
    }

    const languageControl = () => {
        if (localStorage.getItem('currentLanguage')) {
            i18n.changeLanguage(localStorage.getItem('currentLanguage'))
            setCurrentLanguage(localStorage.getItem('currentLanguage'))
            moment.locale(
                localStorage.getItem('currentLanguage') === 'TURKISH' ? 'tr' :
                    localStorage.getItem('currentLanguage') === 'ENGLISH' ? 'en-nz' :
                        'tr'
            )
        } else {
            localStorage.setItem('currentLanguage', "ENGLISH")
            localStorage.setItem('currentLanguageCode', "en")
            moment.locale('tr')
        }
    }

    useEffect(() => {
        languageControl()

    }, [localStorage.getItem('currentLanguage')])

    const location = useLocation().pathname;
    const dispatch = useDispatch();
    const navigate = useNavigate();

    function handleClickResponsiveMenu() {
        var x = document.getElementById("navbar-menu");
        if (x.className === "navbar-menu") {
            x.className += " responsive";
        } else {
            x.className = "navbar-menu";
        }
    };

    const checkAuthentication = async () => {
        try {
            let res = await getMeRequest();
            if (res) {
                dispatch(setUser(res.data));
            }
        } catch (error) {
            if (error?.response?.status === 401) {
                dispatch(setUser(null));
            }
        }
    }

    useLayoutEffect(() => {
        checkAuthentication()
    }, [])

    const user = useSelector((state) => state.user);

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

    const showLogoutConfirm = () => {
        Modal.confirm({
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

    const menu = () => {
        switch (user?.role) {
            case "ROLE_DRIVER":
                return (
                    <Menu
                        items={[
                            {
                                key: '1',
                                icon: <i className="bi bi-grid"></i>,
                                label: (
                                    <Link to="/driver"> {t("Panelim")} </Link>
                                ),
                            },
                            {
                                key: '2',
                                icon: <SnippetsOutlined />,
                                label: (
                                    <Link to={"/driver/my-offers"}> {t("Tekliflerim")} </Link>
                                ),
                            },
                            {
                                key: '3',
                                icon: <UserOutlined />,
                                label: (
                                    <Link to="/driver/my-account"> {t("Hesabım")} </Link>
                                ),
                            },
                            {
                                type: "divider",
                            },
                            {
                                key: '4',
                                icon: <LogoutOutlined />,
                                label: (
                                    <div onClick={() => showLogoutConfirm()}> {t("Çıkış Yap")} </div>
                                ),
                            },
                        ]}
                    />
                )

            case "ROLE_CLIENT":
                return (
                    <Menu
                        items={[
                            {
                                key: '1',
                                icon: <i className="bi bi-grid"></i>,
                                label: (
                                    <Link to="/client"> {t("Panelim")} </Link>
                                ),
                            },
                            {
                                key: '2',
                                icon: <SnippetsOutlined />,
                                label: (
                                    <Link to={"/client/my-transports"}> {t("İlanlarım")} </Link>
                                ),
                            },
                            {
                                key: '3',
                                icon: <UserOutlined />,
                                label: (
                                    <Link to="/client/my-account"> {t("Hesabım")} </Link>
                                ),
                            },
                            {
                                type: "divider",
                            },
                            {
                                key: '4',
                                icon: <LogoutOutlined />,
                                label: (
                                    <div onClick={() => showLogoutConfirm()}> {t("Çıkış Yap")} </div>
                                ),
                            },
                        ]}
                    />
                )

            case "ROLE_ADMIN":
                return (
                    <Menu
                        items={[
                            {
                                key: '1',
                                icon: <i className="bi bi-grid"></i>,
                                label: (
                                    <Link to="/admin/tum-ilanlar">Panelim</Link>
                                ),
                            },
                            {
                                type: "divider",
                            },
                            {
                                key: '4',
                                icon: <LogoutOutlined />,
                                label: (
                                    <div onClick={() => showLogoutConfirm()}>Çıkış Yap</div>
                                ),
                            },
                        ]}
                    />
                )

            default:
                break;
        }
    };

    return (
        <header className={location === "/" ? "homepage-navbar" : "homepage-navbar "}>
            <div className="navbar-items">
                <Link to="/">
                    <img className="navbar-logo" src="/assets/img/logo.svg" alt="Lojister" />
                </Link>
                <button className="icon" onClick={() => handleClickResponsiveMenu()}>
                    <i className="bi bi-list"></i>
                </button>
                <div className="navbar-menu" id="navbar-menu">
                    <Link className="menu-item" to="/about-us"> {t("Hakkımızda")} </Link>
                    <Link className="menu-item" to="/contact"> {t("İletişim")} </Link>
                    {
                        user &&
                        <Link
                            to={`${user.role === "ROLE_DRIVER" ? "/driver" : user.role === "ROLE_CLIENT" ? "/client" : ""}/tracking`}
                            className="menu-item tracking" >
                            <i style={{ marginRight: ".25rem" }} className="bi bi-geo"></i> {t("Taşıma Takibi")}
                        </Link>
                    }
                    <div className="navbar-divider" />
                    <div className='language-selection'>
                        <Select
                            // value={currentLanguage}
                            value="TURKISH"
                            // onChange={(e) => changeLanguageHandler(e)}
                            // dropdownStyle={{ position: "fixed", }}
                        >
                            <Option value="TURKISH">TR</Option>
                            <Option value="ENGLISH" disabled>EN</Option>
                        </Select>
                    </div>
                    <div className="navbar-divider" />
                    {
                        !user &&
                        <Link className="menu-item" to="/login"> {t("Giriş Yap")} </Link>
                    }
                    {
                        !user &&
                        <Link className="menu-item-register" to="/register"> {t("Üye Ol")} </Link>
                    }
                    {
                        user &&
                        <Dropdown trigger="click" overlay={menu} placement="bottomRight" arrow>
                            <Button type="primary" style={{ padding: "8px 16px", height: "auto" }}>
                                {user?.firstName}<i style={{ marginLeft: ".5rem" }} className="bi bi-chevron-down"></i>
                            </Button>
                        </Dropdown>
                    }
                </div>
            </div>
        </header>
    );
};

export default PublicNavbar;