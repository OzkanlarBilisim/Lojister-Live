import { useCallback, useEffect, useRef, useState } from 'react';
import Map, { Layer, Marker, NavigationControl, Source } from 'react-map-gl';
import { Button, Divider, Modal, Popover, Select, Tag, Radio, message } from 'antd';
import routeStart from "../assets/img/routeStart.svg";
import routeFinish from "../assets/img/routeFinish.svg"
import markerSvg from "../assets/img/marker.svg"
import { getAdvertisementRouteRequest, getAllAdvertisementMinimalHideRequest } from '../api/controllers/client-advertisiment-controller';
import { getDistrictByProvinceIdRequest, getNeighborhoodRequest, getProvinceRequest } from '../api/controllers/addresses-controller';
import { CustomButton } from './CustomButton';
import TransportCard from './TransportCard';
import AntNotification from './AntNotification';
import SuspenseFallbackInline from './SuspenseFallbackInline';
import TransportInfoForMap from './TransportInfoForMap';
import HomepageCreateAd from './createTransportSteps/HomepageCreateAd';
import { useSelector } from 'react-redux';
import RequestError from './RequestError';
import NoneContent from './NoneContent';
import { useTranslation } from 'react-i18next';

const MapBoxForTransports = ({ scrollControl, setTransportRegion }) => {
    const { t, i18n } = useTranslation("common");

    const currentUser = useSelector((state) => state.user);

    const mapRef = useRef();

    const defaultFilterValue = {
        startingAddressProvince: null,
        startingAddressDistrict: null,
        startingAddressNeighborhood: null,
        dueAddressProvince: null,
        dueAddressDistrict: null,
        dueAddressNeighborhood: null,
        clientAdvertisementTypeList: []
    };

    //control show & hide for ads menu 
    function handleClickShowHideAds() {
        var x = document.getElementById("map-transport");
        let m = document.getElementById("map-info-card");

        if (x.className === "map-transport") {
            x.className += " active";
            if (m) {
                m.style.marginRight = "1rem";
            }
        } else {
            x.className = "map-transport";
            setFilterPopover(false)
            if (m) {
                m.style.marginRight = "auto";
            }
        }
    };

    //all states
    const [markerValues, setMarkerValues] = useState("starting")
    const [createAdModal, setCreateAdModal] = useState(false)
    const [isFiltered, setIsFiltered] = useState(false)
    const [adsLoading, setAdsLoading] = useState(true)
    const [transports, setTransports] = useState([])
    const [selectedTransport, setSelectedTransport] = useState(null)
    const [filterValues, setFilterValues] = useState(defaultFilterValue);
    const [sortValues, setSortValues] = useState("createdDateTime")
    const [filterPopover, setFilterPopover] = useState(false);
    const [sortPopover, setSortPopover] = useState(false)
    const [coordinates, setCoordinates] = useState([]);
    const [coordinatesx, setCoordinatesx] = useState([]);

    //get all ads
    const getAds = async () => {
        setAdsLoading(true)
        try {
            let res = await getAllAdvertisementMinimalHideRequest({ filter: filterValues, sort: sortValues });
            if (res) {
                setTransports(res.data.filter(v => (v.startingAddress.lat !== null && v.startingAddress.lat !== 0) && (v.startingAddress.lng !== null && v.startingAddress.lng !== 0))/* res.data */)
            }
        } catch (error) {
            RequestError(error);
        }
        setAdsLoading(false)
    }

    //get route for selected ad
    const getAdRoute = async () => {
        try {
            var routeCordinate = selectedTransport?.startingAddress?.lng+"%2C"+selectedTransport?.startingAddress?.lat+"%3B"+selectedTransport?.dueAddress?.lng+"%2C"+selectedTransport?.dueAddress?.lat;
            let res = await getAdvertisementRouteRequest(routeCordinate);
            if(res){
                var lngLtdList = [];
                res.data.routes[0].legs[0].steps.forEach((step) => {
                    step.intersections.forEach((intersection) => {
                      var lngltd = {
                        latitude: intersection.location[1],
                        longitude: intersection.location[0]
                      }
                      lngLtdList.push(lngltd);
                    });
                  });
                  setCoordinates(lngLtdList);
            }
        } catch (error) {
            message.error(t("Rota bulunamadı."));
            setCoordinates([]);
        }
    }

    //control for ads
    useEffect(() => {
        if (filterValues.startingAddressProvince || filterValues.dueAddressProvince || filterValues.clientAdvertisementTypeList.length > 0) {
            setIsFiltered(true)
        } else {
            setIsFiltered(false)
        }
        getAds();
    }, [sortValues, filterValues])

    //control for route
    useEffect(() => {
        if (selectedTransport) {
            getAdRoute();
        } else {
            setCoordinates([])
        }
    }, [selectedTransport])

    //customize scroll block for mapbox
    let scrollBlock = document.querySelector(".mapboxgl-scroll-zoom-blocker");
    if (scrollBlock) {
        scrollBlock.style.color = "var(--primary)"
        scrollBlock.style.backgroundColor = "rgba(255, 255, 255, .7)"
    }

    //on press esc
    const escFunction = useCallback((event) => {
        if (event.keyCode === 27) {
            let menu = document.getElementById("map-transport");
            if (filterPopover) {
                setFilterPopover(false)
            } else if (menu.className === "map-transport active") {
                handleClickShowHideAds()
            };
        }
    }, []);

    useEffect(() => {
        document.addEventListener("keydown", escFunction);

        return () => {
            document.removeEventListener("keydown", escFunction);
        };
    }, [escFunction]);

    //states
    const [isMapLoaded, setIsMapLoaded] = useState(false)

    //check for maps loading
    const onMapLoad = useCallback(() => {
        setIsMapLoaded(true)
    }, []);

    const clickSameMarkers = (v) => {
        setSelectedTransport(null)
        AntNotification({ type: "info", message: t("Bu konumda birden fazla ilan bulunmaktadır. Açılan menüden konumdaki ilanları filtreli şekilde inceleyebilirsiniz."), placement: "bottomRight" });
        let menu = document.getElementById("map-transport");
        if (menu.className === "map-transport") {
            handleClickShowHideAds()
        }
        filterValues.startingAddressProvince = v.startingAddress.province;
        filterValues.startingAddressDistrict = v.startingAddress.district;
        filterValues.startingAddressNeighborhood = v.startingAddress.neighborhood;
        setFilterValues({ ...filterValues });
    }


    //control fit bounds for select or unselect transport
    const controlFitBounds = () => {
        let menu = document.getElementById("map-transport");
        if (isMapLoaded && coordinates.length > 0) {
            if (menu.className === "map-transport active") {
                handleClickShowHideAds()
            }
            let x = coordinates.map(val => [val.longitude, val.latitude]);
            if (menu.className === "map-transport active") {
                mapRef.current.fitBounds(calcBoundsFromCoordinates(x), { padding: { bottom: 300, left: 500, top: 100, right: 100 }, duration: 3000 });
            } else {
                mapRef.current.fitBounds(calcBoundsFromCoordinates(x), { padding: { bottom: 300, left: 100, top: 100, right: 100 }, duration: 3000 });
            }
        } else if (isMapLoaded && transports.length > 0) {
            let x
            if (markerValues === "starting") {
                x = transports.map(v => [v.startingAddress.lng, v.startingAddress.lat]);
            } else if (markerValues === "due") {
                x = transports.map(v => [v.dueAddress.lng, v.dueAddress.lat]);
            }
            if (menu.className === "map-transport active") {
                mapRef.current.fitBounds(calcBoundsFromCoordinates(x), { padding: { bottom: 100, left: 500, top: 100, right: 100 }, duration: 3000 });
            } else {
                mapRef.current.fitBounds(calcBoundsFromCoordinates(x), { padding: 100, duration: 3000 });
            }
        } else if (isMapLoaded) {
            mapRef.current.fitBounds([
                [25.88332, 42.10188],
                [45.21243, 35.66611]
            ], { padding: 50, duration: 1500 }); //Türkiye fit bounds
        }
    };

    useEffect(() => {
        /*  if (coordinates.length === 0 && isMapLoaded) {
             mapRef.current.flyTo({ center: [35.7244443, 39.2153791], essential: true, zoom: 5, padding: 0 })
         } */
        controlFitBounds();
    }, [coordinates, transports, isMapLoaded, isFiltered, markerValues]);


    //calc fitbounds for mapbox
    const getSWCoordinates = (coordinatesCollection) => {
        const lowestLng = Math.min(
            ...coordinatesCollection.map((coordinates) => coordinates[0])
        );
        const lowestLat = Math.min(
            ...coordinatesCollection.map((coordinates) => coordinates[1])
        );

        return [lowestLng, lowestLat];
    }

    const getNECoordinates = (coordinatesCollection) => {
        const highestLng = Math.max(
            ...coordinatesCollection.map((coordinates) => coordinates[0])
        );
        const highestLat = Math.max(
            ...coordinatesCollection.map((coordinates) => coordinates[1])
        );

        return [highestLng, highestLat];
    }

    const calcBoundsFromCoordinates = (coordinatesCollection) => {
        return [
            getSWCoordinates(coordinatesCollection),
            getNECoordinates(coordinatesCollection),
        ];
    }

    const resizeMap = () => {
        if (isMapLoaded) {
            setTimeout(() => {
                mapRef?.current?.resize();
                controlFitBounds();
            }, 200);
        }
    };

    document?.getElementById("layout-button")?.addEventListener("click", resizeMap);

    return (
        <>
            <div className="map-transport active" id="map-transport">
                <button onClick={() => {
                    handleClickShowHideAds();
                    controlFitBounds();
                }} className="map-transport-show-hide-btn" id="map-transport-show-hide-btn">
                    <i className="bi bi-chevron-right"></i>
                    <i className="bi bi-x"></i>
                </button>
                <div className="map-transport-container">
                    <div className="map-transport-header">
                        <p className="map-transport-info">{t("Aktif")} <b>{transports.length}</b> {t("ilan arasından seçim yapın!")}</p>
                        <div className="map-transport-filter-sort">
                            <Popover
                                visible={filterPopover}
                                placement="bottomLeft"
                                title={t("İlanları filtreleyin!")}
                                trigger="click"
                                content={<FilterTransports getAds={getAds} filterValues={filterValues} setFilterValues={setFilterValues} setFilterPopover={setFilterPopover} isFiltered={isFiltered} />} >
                                <Button style={{ textAlign: "left" }}
                                    onClick={() => setFilterPopover(!filterPopover)}
                                    icon={<i style={{ marginRight: "8px" }} className="bi bi-filter"></i>}>{t("Filtrele")}{isFiltered && <Tag style={{ marginLeft: ".5rem" }} closable color="#3B97D2" onClose={() => setFilterValues(defaultFilterValue)} >
                                        +1
                                    </Tag>}</Button>
                            </Popover>
                            <Popover
                                visible={sortPopover}
                                placement="bottomRight"
                                title={t("İlanları sıralayın!")}
                                trigger="click"
                                content={<SortTransports sortValues={sortValues} setSortValues={setSortValues} />}>
                                <Button style={{ textAlign: "left" }}
                                    onClick={() => setSortPopover(!sortPopover)} icon={<i style={{ marginRight: "8px" }} className="bi bi-sort-down"></i>}>{t("Sırala")}</Button>
                            </Popover>
                        </div>
                        {
                            currentUser?.role !== "ROLE_DRIVER" &&
                            < Divider style={{ margin: 0 }} />
                        }

                        {
                            currentUser?.role !== "ROLE_DRIVER" &&
                            <Button onClick={() => setCreateAdModal(true)} type="primary">{t("Hemen siz de ilan oluşturun!")}</Button>
                        }
                    </div>
                    <div className="map-transport-list">
                        {
                            transports.length > 0 && !adsLoading ?
                                transports.map((val, i) => (
                                    <TransportCard
                                        onClick={() => {
                                            setSelectedTransport(val); handleClickShowHideAds();
                                        }}
                                        isActive={val.id === selectedTransport?.id}
                                        key={i}
                                        data={val}
                                    />
                                )) : adsLoading ?
                                    <SuspenseFallbackInline /> :
                                    <NoneContent vertical title={t("İlan bulunamadı!")} />
                        }
                    </div>
                </div>
            </div>
            <div style={{ width: "100%", height: "100%", position: "relative" }}>
                {
                    coordinates.length === 0 &&
                    <div className="choose-marker-value">
                        <Radio.Group
                            value={markerValues}
                            onChange={e => setMarkerValues(e.target.value)}
                            options={[
                                {
                                    label: t("Yükleme Noktaları"),
                                    value: "starting",
                                },
                                {
                                    label: t("Teslimat Noktaları"),
                                    value: "due",
                                }
                            ]}
                            optionType="button"
                            buttonStyle="solid"
                        />
                    </div>
                }
                {
                    coordinates.length === 0 &&
                    <div className="choose-marker-value2">
                        <Button size='large' type='primary' onClick={() => setTransportRegion('abroad')}>{t("Yurt Dışı İlanları Görüntüle")}</Button>
                    </div>
                }
                {
                    selectedTransport &&
                    <TransportInfoForMap
                        selectedTransport={selectedTransport}
                        setSelectedTransport={setSelectedTransport}
                        handleClickShowHideAds={handleClickShowHideAds}
                    />
                }
                <Map
                    ref={mapRef}
                    id="mapRef"
                    onLoad={onMapLoad}
                    mapboxAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
                    initialViewState={{
                        longitude: 37.7542449,
                        latitude: 29.102217,
                        zoom: 3,
                        bearing: 0,
                        pitch: 0
                    }}
                    mapStyle="mapbox://styles/mapbox/light-v10"
                    locale={defaultLocale}
                    cooperativeGestures={scrollControl}
                >
                    <NavigationControl position="top-right" />
                    {
                        coordinates[0] &&
                        <>
                            <Marker
                                longitude={coordinates[0].longitude}
                                latitude={coordinates[0].latitude}
                            >
                                <img src={routeStart} alt="" />
                            </Marker>
                            <Marker
                                longitude={coordinates[coordinates.length - 1].longitude}
                                latitude={coordinates[coordinates.length - 1].latitude}
                            >
                                <img src={routeFinish} alt="" />
                            </Marker>
                        </>
                    }
                    {
                        transports?.length > 0 &&
                        transports.map((v, i) => {
                            let x;
                            let lng;
                            let lat;
                            if (markerValues === "starting") {
                                x = transports.filter(f => f.startingAddress.lat === v.startingAddress.lat && f.startingAddress.lng === v.startingAddress.lng);
                                lng = v.startingAddress.lng;
                                lat = v.startingAddress.lat;
                            } else if (markerValues === "due") {
                                x = transports.filter(f => f.dueAddress.lat === v.dueAddress.lat && f.dueAddress.lng === v.dueAddress.lng);
                                lng = v.dueAddress.lng;
                                lat = v.dueAddress.lat;
                            }

                            return (
                                <Marker
                                    key={i}
                                    onClick={() => x.length > 1 ? clickSameMarkers(v) : setSelectedTransport(v)}
                                    longitude={lng}
                                    latitude={lat}
                                    anchor="bottom"
                                >
                                    <div>
                                        <img className="marker-image" src={markerSvg} alt="" />
                                        {
                                            <span className="marker-count">{x.length}</span>
                                        }
                                    </div>

                                    {/*  <div className="same-location-marker">{x.length}</div> */}
                                </Marker>
                            )
                        })
                    }
                    <Source
                        type="geojson"
                        data={{
                            type: "Feature",
                            geometry: {
                                type: "LineString",
                                coordinates: coordinates.map(val => [val.longitude, val.latitude]),
                            }
                        }}>
                        <Layer
                            type="line"
                            layout={{ "line-join": "round", "line-cap": "round" }}
                            paint={{ "line-color": "#3B97D2", "line-width": 2 }}
                        />
                    </Source>
                </Map >
            </div>
            <Modal width={1000} centered title={t("Hemen ilan oluşturun!")} visible={createAdModal} /* onOk={handleOk} */ onCancel={() => setCreateAdModal(false)} footer={null}>
                <HomepageCreateAd setCreateAdModal={setCreateAdModal} />
            </Modal>
        </>
    );
};
export default MapBoxForTransports;

const defaultLocale = {
    'AttributionControl.ToggleAttribution': 'İlişkilendirmeyi aç/kapat',
    'AttributionControl.MapFeedback': 'Geri bildirim',
    'FullscreenControl.Enter': 'Tam ekran görünümü',
    'FullscreenControl.Exit': 'Tam ekrandan çık',
    'GeolocateControl.FindMyLocation': 'Konumunuzu gösterin',
    'GeolocateControl.LocationNotAvailable': 'Konum bulunamadı',
    'LogoControl.Title': 'Mapbox',
    'Map.Title': 'Harita',
    'NavigationControl.ResetBearing': 'Yönü kuzeye çevir',
    'NavigationControl.ZoomIn': 'Yakınlaştır',
    'NavigationControl.ZoomOut': 'Uzaklaştır',
    'ScaleControl.Feet': 'ft',
    'ScaleControl.Meters': 'm',
    'ScaleControl.Kilometers': 'km',
    'ScaleControl.Miles': 'mi',
    'ScaleControl.NauticalMiles': 'nm',
    'ScrollZoomBlocker.CtrlMessage': 'Haritayı yakınlaştırmak için ctrl tuşuna basıp ekranı kaydırın',
    'ScrollZoomBlocker.CmdMessage': 'Haritayı yakınlaştırmak için ⌘ tuşuna basıp ekranı kaydırın',
    'TouchPanBlocker.Message': 'Haritada gezinmek için iki parmağınızı kullanın',
};


const FilterTransports = ({ getAds, filterValues, setFilterValues, setFilterPopover, isFiltered }) => {
    const { t, i18n } = useTranslation("common");

    const [provinces, setProvinces] = useState([]);
    const [startingDistricts, setStartingDistricts] = useState([]);
    const [dueDistricts, setDueDistricts] = useState([]);
    const [startingNeighborhoods, setStartingNeighborhoods] = useState([]);
    const [dueNeighborhoods, setDueNeighborhoods] = useState([]);

    //control for address
    const setStartingAddress = async () => {
        //fetch province
        try {
            let provinceData = await getProvinceRequest();
            if (provinceData) {
                setProvinces(provinceData.data)
                //fetch district
                if (filterValues.startingAddressProvince) {
                    let findProvince = provinceData.data.find(v => v.provinceName === filterValues.startingAddressProvince);
                    try {
                        let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                        if (districtData) {
                            setStartingDistricts(districtData.data)
                            //fetch neighborhood
                            if (filterValues.startingAddressDistrict) {
                                let findDistrict = districtData.data.find(v => v.districtName === filterValues.startingAddressDistrict);
                                try {
                                    let res = await getNeighborhoodRequest(findDistrict.id);
                                    if (res) {
                                        setStartingNeighborhoods(res.data)
                                    }
                                } catch (error) {
                                    RequestError(error);
                                }
                            }
                        }
                    } catch (error) {
                        RequestError(error);
                    }
                }
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        setStartingAddress()
    }, [filterValues.startingAddressProvince, filterValues.startingAddressDistrict])

    //control for address
    const setDueAddress = async () => {
        //fetch province
        try {
            let provinceData = await getProvinceRequest();
            if (provinceData) {
                setProvinces(provinceData.data)
                //fetch district
                if (filterValues.dueAddressProvince) {
                    let findProvince = provinceData.data.find(v => v.provinceName === filterValues.dueAddressProvince);
                    try {
                        let districtData = await getDistrictByProvinceIdRequest(findProvince.id);
                        if (districtData) {
                            setDueDistricts(districtData.data)
                            //fetch neighborhood
                            if (filterValues.dueAddressDistrict) {
                                let findDistrict = districtData.data.find(v => v.districtName === filterValues.dueAddressDistrict);
                                try {
                                    let res = await getNeighborhoodRequest(findDistrict.id);
                                    if (res) {
                                        setDueNeighborhoods(res.data)
                                    }
                                } catch (error) {
                                    RequestError(error);
                                }
                            }
                        }
                    } catch (error) {
                        RequestError(error);
                    }
                }
            }
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        setDueAddress()
    }, [filterValues.dueAddressProvince, filterValues.dueAddressDistrict])

    const selectTypes = (type) => {
        if (filterValues.clientAdvertisementTypeList.some(v => v === type)) {
            let x = filterValues.clientAdvertisementTypeList.filter(v => v !== type);
            setFilterValues({ ...filterValues, clientAdvertisementTypeList: x });
        } else {
            filterValues.clientAdvertisementTypeList.push(type);
            setFilterValues({ ...filterValues });
        }
    }

    const resetFilter = async () => {
        setFilterValues({
            startingAddressProvince: null,
            startingAddressDistrict: null,
            startingAddressNeighborhood: null,
            dueAddressProvince: null,
            dueAddressDistrict: null,
            dueAddressNeighborhood: null,
            clientAdvertisementTypeList: []
        });
        setFilterPopover(false);
        setStartingDistricts([]);
        setStartingNeighborhoods([]);
        setDueDistricts([]);
        setDueNeighborhoods([]);
        getAds({
            startingAddressProvince: null,
            startingAddressDistrict: null,
            startingAddressNeighborhood: null,
            dueAddressProvince: null,
            dueAddressDistrict: null,
            dueAddressNeighborhood: null,
            clientAdvertisementTypeList: []
        });
    }

    return (
        <>
            <div className="map-filter-transport">
                <h6>{t("Taşıma Tipi")}</h6>
                <div className="transport-type-filter">
                    <button
                        onClick={() => selectTypes("FTL")}
                        style={{ color: filterValues.clientAdvertisementTypeList.some(v => v === "FTL") ? "var(--primary)" : "var(--dark)" }}
                    >
                        <img src={`/assets/img/types/${filterValues.clientAdvertisementTypeList.some(v => v === "FTL") ? "active" : ""}FTL.svg`} alt="" />
                        {t("FTL")}
                    </button>
                    <button
                        onClick={() => selectTypes("PARTIAL")}
                        style={{ color: filterValues.clientAdvertisementTypeList.some(v => v === "PARTIAL") ? "var(--primary)" : "var(--dark)" }}
                    >
                        <img src={`/assets/img/types/${filterValues.clientAdvertisementTypeList.some(v => v === "PARTIAL") ? "active" : ""}PARTIAL.svg`} alt="" />
                        {t("Parsiyel")}
                    </button>
                    <button
                        onClick={() => selectTypes("CONTAINER")}
                        style={{ color: filterValues.clientAdvertisementTypeList.some(v => v === "CONTAINER") ? "var(--primary)" : "var(--dark)" }}
                    >
                        <img src={`/assets/img/types/${filterValues.clientAdvertisementTypeList.some(v => v === "CONTAINER") ? "active" : ""}CONTAINER.svg`} alt="" />
                        {t("Konteyner")}
                    </button>
                    <button style={{ opacity: .5 }} disabled>
                        <img src="/assets/img/types/cargo.svg" alt="" />
                        {t("Kargo")}
                    </button>
                </div>

                <div className="transport-address-filter">
                    <div className="address-divider">
                        <div className="address-divider-from" />
                        <div className="address-divider-line" />
                        <div className="address-divider-to" />
                    </div>
                    <div className="address-selects">
                        <h6>{t("Yükleme Adresi")}</h6>
                        <div className="select-address">
                            <Select
                                allowClear
                                placeholder={t("İl seçin")}
                                size="large"
                                showSearch
                                filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                value={filterValues.startingAddressProvince}
                                onChange={v => {
                                    setStartingDistricts([])
                                    setStartingNeighborhoods([])
                                    filterValues.startingAddressProvince = v;
                                    filterValues.startingAddressDistrict = null;
                                    filterValues.startingAddressNeighborhood = null;
                                    setFilterValues({ ...filterValues });
                                }}>
                                {
                                    provinces.map(v =>
                                        <Select.Option key={v.id} value={v.provinceName}>{v.provinceName}</Select.Option>
                                    )
                                }
                            </Select>
                            <Select
                                allowClear
                                placeholder={t("İlçe seçin")}
                                size="large"
                                showSearch
                                filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                disabled={startingDistricts.length === 0}
                                value={filterValues.startingAddressDistrict}
                                onChange={v => {
                                    setStartingNeighborhoods([])
                                    filterValues.startingAddressDistrict = v;
                                    filterValues.startingAddressNeighborhood = null;
                                    setFilterValues({ ...filterValues });
                                }}>
                                {
                                    startingDistricts.map(v =>
                                        <Select.Option key={v.id} value={v.districtName}>{v.districtName}</Select.Option>
                                    )
                                }
                            </Select>
                            <Select
                                allowClear
                                placeholder={t("Mahalle seçin")}
                                size="large"
                                showSearch
                                filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                disabled={startingNeighborhoods.length === 0}
                                value={filterValues.startingAddressNeighborhood}
                                onChange={v => setFilterValues({ ...filterValues, startingAddressNeighborhood: v })}
                            >
                                {
                                    startingNeighborhoods.map(v =>
                                        <Select.Option key={v.id} value={v.neighborhoodName}>{v.neighborhoodName}</Select.Option>
                                    )
                                }
                            </Select>
                        </div>
                        <h6>{t("Teslimat Adresi")}</h6>
                        <div className="select-address">
                            <Select
                                allowClear
                                placeholder={t("İl seçin")}
                                size="large"
                                showSearch
                                filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                value={filterValues.dueAddressProvince}
                                onChange={v => {
                                    setDueDistricts([])
                                    setDueNeighborhoods([])
                                    filterValues.dueAddressProvince = v;
                                    filterValues.dueAddressDistrict = null;
                                    filterValues.dueAddressNeighborhood = null;
                                    setFilterValues({ ...filterValues });
                                }}>
                                {
                                    provinces.map(v =>
                                        <Select.Option key={v.id} value={v.provinceName}>{v.provinceName}</Select.Option>
                                    )
                                }
                            </Select>
                            <Select
                                allowClear
                                placeholder={t("İlçe seçin")}
                                size="large"
                                showSearch
                                filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                disabled={dueDistricts.length === 0}
                                value={filterValues.dueAddressDistrict}
                                onChange={v => {
                                    setDueNeighborhoods([])
                                    filterValues.dueAddressDistrict = v;
                                    filterValues.dueAddressNeighborhood = null;
                                    setFilterValues({ ...filterValues });
                                }}>
                                {
                                    dueDistricts.map(v =>
                                        <Select.Option key={v.id} value={v.districtName}>{v.districtName}</Select.Option>
                                    )
                                }
                            </Select>
                            <Select
                                allowClear
                                placeholder={t("Mahalle seçin")}
                                size="large"
                                showSearch
                                filterOption={(input, option) => option.children.toLowerCase().includes(input.toLowerCase())}
                                disabled={dueNeighborhoods.length === 0}
                                value={filterValues.dueAddressNeighborhood}
                                onChange={v => setFilterValues({ ...filterValues, dueAddressNeighborhood: v })}
                            >
                                {
                                    dueNeighborhoods.map(v =>
                                        <Select.Option key={v.id} value={v.neighborhoodName}>{v.neighborhoodName}</Select.Option>
                                    )
                                }
                            </Select>
                        </div>
                    </div>
                </div>
            </div >
            <div style={{ display: "flex", gap: "1rem", marginTop: "1rem" }}>
                <Button disabled={!isFiltered} style={{ width: "100%" }} onClick={() => resetFilter()}>{t("Temizle")}</Button>
                <Button disabled={!isFiltered} style={{ width: "100%" }} onClick={() => {
                    setFilterPopover(false);
                }} type="primary">{t("Filtrele")}</Button>
            </div>
        </>
    )
}

const SortTransports = ({ sortValues, setSortValues }) => {
    const { t, i18n } = useTranslation("common");

    return (
        <div className="map-sort-transport">
            <CustomButton style={{ justifyContent: "flex-start" }} onClick={() => setSortValues("createdDateTime")} color={sortValues === "createdDateTime" ? "primary-light" : "var(--white)"} >{t("İlan tarihine göre")}</CustomButton>
            <CustomButton style={{ justifyContent: "flex-start" }} onClick={() => setSortValues("adStartingDate")} color={sortValues === "adStartingDate" ? "primary-light" : "var(--white)"} >{t("Yükleme tarihine göre")}</CustomButton>
            <CustomButton style={{ justifyContent: "flex-start" }} onClick={() => setSortValues("adDueDate")} color={sortValues === "adDueDate" ? "primary-light" : "var(--white)"} >{t("Teslimat tarihine göre")}</CustomButton>
        </div>
    )
}