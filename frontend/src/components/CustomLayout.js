import React, { useEffect, useRef, useState } from 'react'
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom'
import ClientNavbar from './ClientNavbar'
import DriverNavbar from './DriverNavbar'
import LayoutNotification from './LayoutNotification';
import ShowProfilePhoto from './ShowProfilePhoto';
import { MenuFoldOutlined, MenuUnfoldOutlined, } from '@ant-design/icons';
import { Select } from 'antd';
import { useTranslation } from 'react-i18next';
import moment from 'moment';
import 'moment/locale/tr'
import 'moment/locale/en-nz'
import { changeLanguageRequest } from '../api/controllers/account-controller';
import RequestError from './RequestError';


export default function CustomLayout({ children }) {

    const { t, i18n } = useTranslation("common");
    const { Option } = Select;
    const [currentLanguage, setCurrentLanguage] = useState();

    const changeLanguageHandler = async (lang) => {
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

    const navbar = useRef();

    const currentUser = useSelector((state) => state.user);
    const [collapse, setCollapse] = useState(window.innerWidth < 820 ? false : true);

    //close navbar without div
    const handleClickView = e => {
        if (navbar.current && !navbar.current.contains(e.target) && !collapse) {
            setCollapse(false);
        }
    };

    useEffect(() => {
        if (window.innerWidth < 820) {
            document.addEventListener("click", handleClickView);
        }
        return () => {
            document.removeEventListener("click", handleClickView);
        };
    }, []);

    const Balance = () =>{
        return(
            <>  {currentUser?.current&&
                    <>
                        <div>Bakiye:</div>
                        <p>{currentUser?.tl}₺</p>
                        <p>{currentUser?.usd}$</p>
                        <p>{currentUser?.euro}€</p>
                    </>
                }
            </>
        )
    }

    return (
        <div className={collapse ? "layout open" : "layout"} id="layout">
            <div className="layout-navbar" ref={navbar} >
                <div className='layout-language'>
                    <Link style={{ zIndex: 1 }} to="/">
                        <img src="/assets/img/logo.svg" alt="Lojister" />
                    </Link>
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
                </div>
                {currentUser.role === "ROLE_DRIVER" ? <DriverNavbar /> : <ClientNavbar />}
                <Link to={`/${currentUser.role === "ROLE_DRIVER" ? 'driver' : currentUser.role === "ROLE_CLIENT" ? 'client' : ''}/my-account`} className="layout-user">
                    <ShowProfilePhoto />
                    {currentUser.firstName + " " + currentUser.lastName}
                </Link>
            </div>
            <div className="layout-wrapper">
                <div className="layout-header">
                    <div className="layout-header-left">
                        <button id="layout-button" ref={navbar} onClick={() => setCollapse(!collapse)} >
                            {
                                collapse ? <MenuFoldOutlined style={{ color: "var(--dark)" }} /> : <MenuUnfoldOutlined style={{ color: "var(--dark)" }} />
                            }
                        </button>
                        <span>
                            {t("Merhaba")} <b>{currentUser.firstName + " " + currentUser.lastName}</b>, {t("Lojister’a hoşgeldin!")}
                        </span>
                    </div>
                    <div className="layout-header-right">
                        {currentUser.role === "ROLE_CLIENT" &&
                            <Balance />
                        }

                        <LayoutNotification />
                        {
                            currentUser.role === "ROLE_CLIENT" &&
                            <Link to="/client/create-transport">
                                <button className="creteAd">
                                    <i className="bi bi-plus-circle"></i>
                                    <span>{t("Yeni İlan Oluştur")}</span>
                                </button>
                            </Link>
                        }
                    </div>
                    <Link className="mobile-logo" to="/">
                        <img src="/assets/img/logo.svg" alt="Lojister" />
                    </Link>
                </div>
                {children}
            </div>
        </div>
    )
};