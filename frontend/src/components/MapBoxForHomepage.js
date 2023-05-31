import * as React from 'react';
import Map, { Layer, Marker, NavigationControl, Source } from 'react-map-gl';
import routeStart from "../assets/img/routeStart.svg";
import routeFinish from "../assets/img/routeFinish.svg"
import AntNotification from './AntNotification';
import markerSvg from "../assets/img/marker.svg"
import { useTranslation } from 'react-i18next';

const MapBoxForHomepage = ({ coordinates, transports, setSelectedTransport, handleClickShowHideAds, filterValues, setFilterValues, isFiltered, markerValues }) => {
    const { t, i18n } = useTranslation("common");

    const mapRef = React.useRef();

    //states
    const [isMapLoaded, setIsMapLoaded] = React.useState(false)

    //check for maps loading
    const onMapLoad = React.useCallback(() => {
        setIsMapLoaded(true)
    }, []);

    const clickSameMarkers = (v) => {
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
        if (isMapLoaded && coordinates.length > 0) {
            let menu = document.getElementById("map-transport");
            if (menu.className === "map-transport active") {
                handleClickShowHideAds()
            }
            let x = coordinates.map(val => [val.longitude, val.latitude]);
            mapRef.current.fitBounds(calcBoundsFromCoordinates(x), { padding: { bottom: 300, left: 100, top: 100, right: 100 }, duration: 4500 });
        } else if (isMapLoaded && transports.length > 0) {
            let x
            if (markerValues === "starting") {
                x = transports.map(v => [v.startingAddress.lng, v.startingAddress.lat]);
            } else if (markerValues === "due") {
                x = transports.map(v => [v.dueAddress.lng, v.dueAddress.lat]);
            }
            mapRef.current.fitBounds(calcBoundsFromCoordinates(x), { padding: 100, duration: 4500 });
        } else if (isMapLoaded) {
            mapRef.current.fitBounds([
                [25.88332, 42.10188],
                [45.21243, 35.66611]
            ], { padding: 50, duration: 1500 }); //Türkiye fit bounds
        }
    };

    React.useEffect(() => {
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

    return (
        <Map
            ref={mapRef}
            onLoad={onMapLoad}
            mapboxAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
            initialViewState={{
                longitude: 35.7244443,
                latitude: 39.2153791,
                zoom: 3,
                bearing: 0,
                pitch: 0
            }}
            style={{ width: "100%", height: "100%", minHeight: "150px" }
            }
            mapStyle="mapbox://styles/mapbox/light-v10"
            locale={defaultLocale}
            cooperativeGestures={true}
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
                        >
                            <div>
                                <img className="marker-image" src={markerSvg} alt="" />
                                {
                                    x.length > 1 &&
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
    );
};
export default MapBoxForHomepage;

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