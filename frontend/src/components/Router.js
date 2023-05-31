import React, { useLayoutEffect } from 'react';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';

import Homepage from '../pages/homePage/homePage';
import Login from '../pages/login';
import Register from '../pages/register';
import NotFound from './404/404';

import ClientRouter from './ClientRouter';

import ClientDashboard from '../pages/client/dashboard';
import MyAds from '../pages/client/my-transports';
import Tracking from '../pages/client/tracking';
import Reports from '../pages/client/reports';
import Users from '../pages/client/users';
import ClientSettings from '../pages/client/settings';
import Support from '../pages/client/support';
import TransportDetail from '../pages/client/transport-detail';
import TransportDetailAbroud from '../pages/client/transport-detail-abroud';
import CreateAd from '../pages/client/create-transport';

import DriverRouter from './DriverRouter';

import DriverDashboard from '../pages/driver/dashboard';
import DriverDrivers from '../pages/driver/drivers';
import DriverOffers from '../pages/driver/my-offers';
import DriverTracking from '../pages/driver/tracking';
import DriverVehicles from '../pages/driver/vehicles';
import DriverSettings from '../pages/driver/settings';
import DriverReports from '../pages/driver/reports';
import OffersDetail from '../pages/driver/offer-detail';
import OffersDetailAbroud from '../pages/driver/offer-detail-abroud';
import DriverSupport from '../pages/driver/support';
import Transports from '../pages/driver/transports';
import AboutUs from '../pages/about-us';
import Contact from '../pages/contact';
import AddDriver from '../pages/driver/add-driver';
import AddVehicle from '../pages/driver/add-vehicle';
import VehicleDetail from '../pages/driver/vehicle-detail';
import DriverDetail from '../pages/driver/driver-detail';
import MyAccountClient from '../pages/client/my-account';
import MyAccountDriver from '../pages/driver/my-account';
import AuthDeliver from '../pages/auth-deliver';
import Faq from '../pages/faq';
import YetkiBelgesi from '../pages/yetki-belgesi';
import IptalVeIadeKosullari from '../pages/iptal-ve-iade-kosullari';
import GizlilikVeCerezPolitikasi from '../pages/gizlilik-ve-cerez-politikasi';
import KVKKHakkinda from '../pages/kvkk-hakkinda';
import AdminRouter from './AdminRouter';

import AdminTumIlanlar from '../pages/admin/ilanlar/tum-ilanlar'
import AdminAktifIlanlar from '../pages/admin/ilanlar/aktif-ilanlar'
import AdminBitmisIlanlar from '../pages/admin/ilanlar/bitmis-ilanlar'
import AdminGizlenmisIlanlar from '../pages/admin/ilanlar/gizlenmis-ilanlar'
import AdminTumOdemeler from '../pages/admin/odemeler/tum-odemeler';
import AdminBekleyenOdemeler from '../pages/admin/odemeler/bekleyen-odemeler';
import AdminTamamlananOdemeler from '../pages/admin/odemeler/tamamlanan-odemeler';
import AdminSirketler from '../pages/admin/sirketler';
import AdminIrsaliyeBelgeleri from '../pages/admin/irsaliye-belgeleri';
import AdminOnayBekleyenSuruculer from '../pages/admin/suruculer/onay-bekleyen-suruculer';
import AdminRevizyonBekleyenSuruculer from '../pages/admin/suruculer/revizyon-bekleyen-suruculer';
import AdminOnayBekleyenAraclar from '../pages/admin/araclar/onay-bekleyen-araclar';
import AdminRevizyonBekleyenAraclar from '../pages/admin/araclar/revizyon-bekleyen-araclar';
import AdminSurucuDokumanTipleri from '../pages/admin/tanimlamalar/surucu-dokuman-tipleri';
import AdminAracDokumanTipleri from '../pages/admin/tanimlamalar/arac-dokuman-tipleri';
import AdminAracTipleri from '../pages/admin/tanimlamalar/arac-tipleri';
import AdminDorseTipleri from '../pages/admin/tanimlamalar/dorse-tipleri';
import AdminPaketlemeTipleri from '../pages/admin/tanimlamalar/paketleme-tipleri';
import AdminYuklemeTipleri from '../pages/admin/tanimlamalar/yukleme-tipleri';
import AdminYukTipleri from '../pages/admin/tanimlamalar/yuk-tipleri';
import AdminZeminTipleri from '../pages/admin/tanimlamalar/zemin-tipleri';
import AdminDorseOzellikleri from '../pages/admin/tanimlamalar/dorse-ozellikleri';
import AdminParaBirimi from '../pages/admin/tanimlamalar/para-birimi';
import AdminHakkimizda from '../pages/admin/hakkimizda';
import AdminSSS from '../pages/admin/sss';
import AdminKullaniciOlusturma from '../pages/admin/kullanici-olusturma';
import PaymentControle from '../pages/admin/payment-controle';
import PrivacyPolicy from '../pages/privacy-policy';
import AdsAbroadId from '../pages/AdsAbroudId';
import CurrentPayment from '../pages/client/current-payment';
import Deneme from '../components/deneme';


const Router = () => {

    const Wrapper = ({ children }) => {
        const location = useLocation();
        useLayoutEffect(() => {
            document.documentElement.scrollTo({
                top: 0,
                left: 0,
                behavior: 'smooth'
            });
        }, [location.pathname]);
        return children
    }

    return (
        <BrowserRouter>
            <Wrapper>
                <Routes>
                    <Route index path="/" element={<Homepage />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/about-us" element={<AboutUs />} />
                    <Route path="/contact" element={<Contact />} />
                    <Route path="/faq" element={<Faq />} />
                    <Route path="/yetki-belgesi" element={<YetkiBelgesi />} />
                    <Route path="/gizlilik-ve-cerez-politikasi" element={<GizlilikVeCerezPolitikasi />} />
                    <Route path="/iptal-ve-iade-kosullari" element={<IptalVeIadeKosullari />} />
                    <Route path="/kvkk-hakkinda" element={<KVKKHakkinda />} />
                    <Route path="/privacy-policy" element={<PrivacyPolicy />} />
                    <Route path="/adsAbroadId/:advertID" element={<AdsAbroadId />} />


                    <Route path="/auth-delivery" element={<AuthDeliver />} />

                    <Route exact path='/client' element={<ClientRouter />}>
                        <Route path='/client/' element={<ClientDashboard />} />
                        <Route path='/client/my-transports' element={<MyAds />} />
                        <Route path='/client/transport-detail/:transportId' element={<TransportDetail />} />
                        <Route path='/client/transport-detail-abroud/:transportId' element={<TransportDetailAbroud />} />
                        <Route path='/client/create-transport' element={<CreateAd />} />
                        <Route path='/client/tracking' element={<Tracking />} />
                        <Route path='/client/reports' element={<Reports />} />
                        <Route path='/client/users' element={<Users />} />
                        <Route path='/client/settings' element={<ClientSettings />} />
                        <Route path='/client/support' element={<Support />} />
                        <Route path='/client/my-account' element={<MyAccountClient />} />
                        <Route path='/client/current-payment' element={<CurrentPayment />} />
                        <Route path='/client/deneme' element={<Deneme />} />
                    </Route>

                    <Route exact path='/driver' element={<DriverRouter />}>
                        <Route path='/driver/' element={<DriverDashboard />} />
                        <Route path='/driver/transports' element={<Transports />} />
                        <Route path='/driver/my-offers' element={<DriverOffers />} />
                        <Route path='/driver/offer-detail/:offerId' element={<OffersDetail />} />
                        <Route path='/driver/offer-detail-abroud/:offerId' element={<OffersDetailAbroud />} />
                        <Route path='/driver/tracking' element={<DriverTracking />} />
                        <Route path='/driver/reports' element={<DriverReports />} />
                        <Route path='/driver/vehicles' element={<DriverVehicles />} />
                        <Route path='/driver/add-vehicle' element={<AddVehicle />} />
                        <Route path='/driver/vehicle-detail/:vehicleId' element={<VehicleDetail />} />
                        <Route path='/driver/drivers' element={<DriverDrivers />} />
                        <Route path='/driver/add-driver' element={<AddDriver />} />
                        <Route path='/driver/driver-detail/:driverId' element={<DriverDetail />} />
                        <Route path='/driver/support' element={<DriverSupport />} />
                        <Route path='/driver/settings' element={<DriverSettings />} />
                        <Route path='/driver/my-account' element={<MyAccountDriver />} />
                    </Route>

                    <Route exact path='/admin' element={<AdminRouter />}>
                        <Route path='/admin/tum-ilanlar' element={<AdminTumIlanlar />} />
                        <Route path='/admin/aktif-ilanlar' element={<AdminAktifIlanlar />} />
                        <Route path='/admin/bitmis-ilanlar' element={<AdminBitmisIlanlar />} />
                        <Route path='/admin/gizlenmis-ilanlar' element={<AdminGizlenmisIlanlar />} />
                        <Route path='/admin/tum-odemeler' element={<AdminTumOdemeler />} />
                        <Route path='/admin/bekleyen-odemeler' element={<AdminBekleyenOdemeler />} />
                        <Route path='/admin/tamamlanan-odemeler' element={<AdminTamamlananOdemeler />} />
                        <Route path='/admin/sirketler' element={<AdminSirketler />} />
                        <Route path='/admin/irsaliye-belgeleri' element={<AdminIrsaliyeBelgeleri />} />
                        <Route path='/admin/onay-bekleyen-suruculer' element={<AdminOnayBekleyenSuruculer />} />
                        <Route path='/admin/revizyon-bekleyen-suruculer' element={<AdminRevizyonBekleyenSuruculer />} />
                        <Route path='/admin/onay-bekleyen-araclar' element={<AdminOnayBekleyenAraclar />} />
                        <Route path='/admin/revizyon-bekleyen-araclar' element={<AdminRevizyonBekleyenAraclar />} />
                        <Route path='/admin/surucu-dokuman-tipleri' element={<AdminSurucuDokumanTipleri />} />
                        <Route path='/admin/arac-dokuman-tipleri' element={<AdminAracDokumanTipleri />} />
                        <Route path='/admin/arac-tipleri' element={<AdminAracTipleri />} />
                        <Route path='/admin/dorse-tipleri' element={<AdminDorseTipleri />} />
                        <Route path='/admin/paketleme-tipleri' element={<AdminPaketlemeTipleri />} />
                        <Route path='/admin/yukleme-tipleri' element={<AdminYuklemeTipleri />} />
                        <Route path='/admin/yuk-tipleri' element={<AdminYukTipleri />} />
                        <Route path='/admin/zemin-tipleri' element={<AdminZeminTipleri />} />
                        <Route path='/admin/dorse-ozellikleri' element={<AdminDorseOzellikleri />} />
                        <Route path='/admin/para-birimi' element={<AdminParaBirimi />} />
                        <Route path='/admin/hakkimizda' element={<AdminHakkimizda />} />
                        <Route path='/admin/sss' element={<AdminSSS />} />
                        <Route path='/admin/kullanici-olusturma' element={<AdminKullaniciOlusturma />} />
                        <Route path='/admin/payment-controle' element={<PaymentControle />} />
                    </Route>

                    <Route path='*' element={<NotFound />} />
                </Routes>
            </Wrapper>
        </BrowserRouter >
    )
}

export default Router;