import React, { useEffect, useState } from 'react'
import { Checkbox } from 'antd';
import { getAccountSettingsRequest, getNotificationSettingsRequest, updateAccountSettingsRequest, updateNotificationSettingsRequest } from '../../api/controllers/client-controller';
import SupportBanner from '../../components/SupportBanner';
import { Link } from 'react-router-dom';
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';

function Settings() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Ayarlarım")

    const [notificationSettings, setNotificationSettings] = useState({});
    const [accountSettings, setAccountSettings] = useState({});

    const fetchNotificationSettings = async () => {
        try {
            let res = await getNotificationSettingsRequest();
            if (res) {
                setNotificationSettings(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const handleCheckedNotificationSettings = async (e) => {
        notificationSettings[e.target.name] = e.target.checked;
        try {
            let res = await updateNotificationSettingsRequest(notificationSettings);
            if (res) {
                fetchNotificationSettings();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const fetchAccountSettings = async () => {
        try {
            let res = await getAccountSettingsRequest();
            if (res) {
                setAccountSettings(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const handleCheckedAccountSettings = async (e) => {
        accountSettings[e.target.name] = e.target.checked;
        try {
            let res = await updateAccountSettingsRequest(accountSettings);
            if (res) {
                fetchAccountSettings();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        fetchNotificationSettings();
        fetchAccountSettings();
    }, [])

    return (
        <div className="layout-content-padding">
            <div className="settings-card notification">
                <h4 className="title"><i className="bi bi-bell"></i> {t("Bildirim Ayarları")}</h4>
                <table>
                    <thead>
                        <tr>
                            <th></th>
                            <th>{t("Mail")}</th>
                            <th>{t("Mobil Uygulama")}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>{t("İlanlarıma teklif geldiğinde bildirim al")}</td>
                            <td>
                                <Checkbox
                                    checked={notificationSettings?.newAdvertisementBidEmailSending}
                                    name="newAdvertisementBidEmailSending"
                                    onChange={e => handleCheckedNotificationSettings(e)}
                                />
                            </td>
                            <td>
                                <Checkbox
                                    checked={notificationSettings?.newAdvertisementBidMobileSending}
                                    name="newAdvertisementBidMobileSending"
                                    onChange={e => handleCheckedNotificationSettings(e)}
                                />
                            </td>
                        </tr>
                        <tr>
                            <td>{t("İlanlarımın durumlarında değişiklik olduğunda bildirim al")}</td>
                            <td>
                                <Checkbox
                                    checked={notificationSettings?.statusChangeAdvertisementEmailSending}
                                    name="statusChangeAdvertisementEmailSending"
                                    onChange={e => handleCheckedNotificationSettings(e)}
                                />
                            </td>
                            <td>
                                <Checkbox
                                    checked={notificationSettings?.statusChangeAdvertisementMobileSending}
                                    name="statusChangeAdvertisementMobileSending"
                                    onChange={e => handleCheckedNotificationSettings(e)}
                                />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div className="settings-card">
                <h4 className="title"><i className="bi bi-sliders"></i> {t("Hesap Ayarları")}</h4>
                <table>
                    <tbody>
                        <tr>
                            <td>
                                <Checkbox
                                    checked={accountSettings?.createAdvertisementStartingAddressAutoFill}
                                    name="createAdvertisementStartingAddressAutoFill"
                                    onChange={e => handleCheckedAccountSettings(e)}
                                >
                                    {t("İlan oluştururken yükleme noktasını otomatik doldur")}
                                </ Checkbox>
                                <Link to="/client/my-account?tabKey=3" style={{ color: "var(--primary)", fontSize: "12px", fontWeight: 500 }}>{t("Varsayılan kayıtlı bilgiyi değiştir")}</Link>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <SupportBanner />
        </div>
    )
}

export default Settings;
