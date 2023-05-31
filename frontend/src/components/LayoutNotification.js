import { Popover, List, Button, Badge } from 'antd';
import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next';
import { getNotificationsRequest } from '../api/controllers/notification-controller';
import RequestError from './RequestError';

const LayoutNotification = () => {
    const { t, i18n } = useTranslation("common");

    const [notifications, setNotifications] = useState([]);

    const getNotifications = async () => {
        try {
            let res = await getNotificationsRequest();
            if (res) {
                setNotifications(res.data)
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        getNotifications();
    }, [])

    const content = (
        <div style={{ display: "grid", gap: ".5rem" }}>
            <List
                style={{ width: 300, maxHeight: 300, overflow: "auto" }}
                itemLayout="horizontal"
                dataSource={notifications}
                locale={{ emptyText: t("Yeni bildirim yok!") }}
                renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                            //avatar={<Avatar src="https://joeschmoe.io/api/v1/random" />}
                            //title={item.title}
                            description={item.message}
                        />
                    </List.Item>
                )}
            />
            <Button disabled={notifications.length < 1} style={{ width: "100%" }}>
                {t("Tümünü göster")}
            </Button>
        </div>
    );

    return (
        <Popover
            placement="bottomRight"
            content={content}
            title={t("Bildirimler")}
            trigger="click"
        >
            <Badge color="var(--primary)" count={notifications.length}>
                <button className="notification">
                    <i className="bi bi-bell"></i>
                </button>
            </Badge>
        </Popover>
    )
}

export default LayoutNotification;