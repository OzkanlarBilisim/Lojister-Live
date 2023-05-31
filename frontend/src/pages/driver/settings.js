import React, { useEffect, useState } from 'react'
import { Checkbox } from 'antd';
import { getNotificationSettingsRequest, updateNotificationSettingsRequest } from '../../api/controllers/driver-controller';
import SupportBanner from '../../components/SupportBanner';
import RequestError from '../../components/RequestError';
import { useTranslation } from 'react-i18next';

function Settings() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Ayarlar")

    const [settings, setSettings] = useState({});

    const fetchSettings = async () => {
        try {
            let res = await getNotificationSettingsRequest();
            if (res) {
                setSettings(res.data);
            }
        } catch (error) {
            RequestError(error);
        }
    }

    const handleChecked = async (e) => {
        settings[e.target.name] = e.target.checked;
        try {
            let res = await updateNotificationSettingsRequest(settings);
            if (res) {
                fetchSettings();
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        fetchSettings();
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
                            <td>{t("İlan oluşturulduğunda bildirim al")}</td>
                            <td>
                                <Checkbox
                                    checked={settings?.newAdvertisementMailSending}
                                    name="newAdvertisementMailSending"
                                    onChange={e => handleChecked(e)}
                                />
                            </td>
                            <td>
                                <Checkbox
                                    checked={settings?.newAdvertisementMobileSending}
                                    name="newAdvertisementMobileSending"
                                    onChange={e => handleChecked(e)}
                                />
                            </td>
                        </tr>
                        <tr>
                            <td>{t("İlanlarımın durumlarında değişiklik olduğunda bildirim al")}</td>
                            <td>
                                <Checkbox
                                    checked={settings?.statusChangeAdvertisementEmailSending}
                                    name="statusChangeAdvertisementEmailSending"
                                    onChange={e => handleChecked(e)}
                                />
                            </td>
                            <td>
                                <Checkbox
                                    checked={settings?.statusChangeAdvertisementMobileSending}
                                    name="statusChangeAdvertisementMobileSending"
                                    onChange={e => handleChecked(e)}
                                />
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
