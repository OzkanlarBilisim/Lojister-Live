import {
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    LogoutOutlined,
} from '@ant-design/icons';
import { Layout, Menu } from 'antd';
import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { logoutRequest } from '../api/controllers/account-controller';
import { setUser } from '../redux/actions/userActions';
import AntNotification from './AntNotification';
const { Header, Sider, Content } = Layout;

const AdminLayout = ({ children }) => {
    const { pathname } = useLocation();
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState(false);

    const onClickLogout = async () => {
        try {
            let res = logoutRequest();
            if (res) {
                navigate("/")
                dispatch(setUser(null));
                localStorage.removeItem("token")
                AntNotification({ type: "success", message: "Başarıyla çıkış yapıldı!" })
            }
        } catch (error) {

        }
    }

    return (
        <Layout hasSider>
            <Sider
                style={{
                    overflow: 'auto',
                    height: '100vh',
                    position: 'fixed',
                    left: 0,
                    top: 0,
                    bottom: 0,
                }}
                theme="light"
                trigger={null}
                collapsible
                collapsed={collapsed}
            >
                <div style={{ padding: "1rem" }} >
                    <Link to="/">
                        <img style={{ width: "100%" }} src="/assets/img/logo.svg" alt="" />
                    </Link>
                </div>
                <Menu
                    theme="light"
                    mode="inline"
                    defaultSelectedKeys={['1']}
                    selectedKeys={pathname}
                    items={[
                        {
                            type: 'divider',
                        },
                        {
                            key: '/admin/ilanlar',
                            label: 'İlanlar',
                            children: [
                                {
                                    key: '/admin/tum-ilanlar',
                                    label: 'Tüm İlanlar',
                                    onClick: () => navigate("/admin/tum-ilanlar"),
                                },
                                {
                                    key: '/admin/aktif-ilanlar',
                                    label: 'Aktif İlanlar',
                                    onClick: () => navigate("/admin/aktif-ilanlar"),
                                },
                                {
                                    key: '/admin/bitmis-ilanlar',
                                    label: 'Bitmiş İlanlar',
                                    onClick: () => navigate("/admin/bitmis-ilanlar"),
                                },
                                {
                                    key: '/admin/gizlenmis-ilanlar',
                                    label: 'Gizlenmiş İlanlar',
                                    onClick: () => navigate("/admin/gizlenmis-ilanlar"),
                                },
                            ]
                        },
                        {
                            key: '/admin/odemeler',
                            label: 'Ödemeler',
                            children: [
                                {
                                    key: '/admin/tum-odemeler',
                                    label: 'Tüm Ödemeler',
                                    onClick: () => navigate("/admin/tum-odemeler"),
                                },
                                {
                                    key: '/admin/bekleyen-odemeler',
                                    label: 'Bekleyen Ödemeler',
                                    onClick: () => navigate("/admin/bekleyen-odemeler"),
                                },
                                {
                                    key: '/admin/tamamlanan-odemeler',
                                    label: 'Tamamlanan Ödemeler',
                                    onClick: () => navigate("/admin/tamamlanan-odemeler"),
                                },
                            ]
                        },
                        {
                            key: '/admin/sirketler',
                            label: 'Şirketler',
                            onClick: () => navigate("/admin/sirketler"),
                        },
                        {
                            key: '/admin/irsaliye-belgeleri',
                            label: 'İrsaliye Belgeleri',
                            onClick: () => navigate("/admin/irsaliye-belgeleri"),
                        },
                        {
                            key: '/admin/suruculer',
                            label: 'Sürücüler',
                            children: [
                                {
                                    key: '/admin/onay-bekleyen-suruculer',
                                    label: 'Onay Bekleyen Sürücüler',
                                    onClick: () => navigate("/admin/onay-bekleyen-suruculer"),
                                },
                                {
                                    key: '/admin/revizyon-bekleyen-suruculer',
                                    label: 'Revizyon Bekleyen Sürücüler',
                                    onClick: () => navigate("/admin/revizyon-bekleyen-suruculer"),
                                },
                            ]
                        },
                        {
                            key: '/admin/araclar',
                            label: 'Araçlar',
                            children: [
                                {
                                    key: '/admin/onay-bekleyen-araclar',
                                    label: 'Onay Bekleyen Araçlar',
                                    onClick: () => navigate("/admin/onay-bekleyen-araclar"),
                                },
                                {
                                    key: '/admin/revizyon-bekleyen-araclar',
                                    label: 'Revizyon Bekleyen Araçlar',
                                    onClick: () => navigate("/admin/revizyon-bekleyen-araclar"),
                                },
                            ]
                        },
                        {
                            key: '/admin/tanimlamalar',
                            label: 'Tanımlamalar',
                            children: [
                                {
                                    key: '/admin/surucu-dokuman-tipleri',
                                    label: 'Sürücü Döküman Tipleri',
                                    onClick: () => navigate("/admin/surucu-dokuman-tipleri"),
                                },
                                {
                                    key: '/admin/arac-dokuman-tipleri',
                                    label: 'Araç Döküman Tipleri',
                                    onClick: () => navigate("/admin/arac-dokuman-tipleri"),
                                },
                                {
                                    key: '/admin/arac-tipleri',
                                    label: 'Araç Tipleri',
                                    onClick: () => navigate("/admin/arac-tipleri"),
                                },
                                {
                                    key: '/admin/dorse-tipleri',
                                    label: 'Dorse Tipleri',
                                    onClick: () => navigate("/admin/dorse-tipleri"),
                                },
                                {
                                    key: '/admin/paketleme-tipleri',
                                    label: 'Paketleme Tipleri',
                                    onClick: () => navigate("/admin/paketleme-tipleri"),
                                },
                                {
                                    key: '/admin/yukleme-tipleri',
                                    label: 'Yükleme Tipleri',
                                    onClick: () => navigate("/admin/yukleme-tipleri"),
                                },
                                {
                                    key: '/admin/yuk-tipleri',
                                    label: 'Yük Tipleri',
                                    onClick: () => navigate("/admin/yuk-tipleri"),
                                },
                                {
                                    key: '/admin/zemin-tipleri',
                                    label: 'Zemin Tipleri',
                                    onClick: () => navigate("/admin/zemin-tipleri"),
                                },
                                {
                                    key: '/admin/dorse-ozellikleri',
                                    label: 'Dorse Özellikleri',
                                    onClick: () => navigate("/admin/dorse-ozellikleri"),
                                },
                                {
                                    key: '/admin/para-birimi',
                                    label: 'Para Birimi',
                                    onClick: () => navigate("/admin/para-birimi"),
                                },
                            ]
                        },
                        {
                            key: '/admin/hakkimizda',
                            label: 'Hakkımızda',
                            onClick: () => navigate("/admin/hakkimizda"),
                        },
                        {
                            key: '/admin/sss',
                            label: 'SSS',
                            onClick: () => navigate("/admin/sss"),
                        },
                        {
                            key: '/admin/kullanici-olusturma',
                            label: 'Kullanıcı Oluşturma',
                            onClick: () => navigate("/admin/kullanici-olusturma"),
                        },
                        {
                            key: '/admin/payment-controle',
                            label: 'Ödeme kontrol',
                            onClick: () => navigate("/admin/payment-controle"),
                        },
                        {
                            type: 'divider',
                        },
                        {
                            key: 'logout',
                            label: 'Çıkış yap',
                            icon: <LogoutOutlined />,
                            onClick: () => onClickLogout(),
                        },
                    ]}
                />
            </Sider>
            <Layout
                style={{
                    marginLeft: collapsed ? 80 : 200,
                }}
                className="site-layout"
            >
                <Header
                    className="site-layout-background"
                    style={{
                        padding: "0 1rem",
                    }}
                >
                    {React.createElement(collapsed ? MenuUnfoldOutlined : MenuFoldOutlined, {
                        className: 'trigger',
                        style: { color: "var(--white)" },
                        onClick: () => setCollapsed(!collapsed),
                    })}
                </Header>
                <Content
                    className="site-layout-background"
                    style={{
                        padding: "1rem",
                        backgroundColor: "var(--bg)"
                    }}
                >
                    {children}
                </Content>
            </Layout>
        </Layout>
    );
};

export default AdminLayout;