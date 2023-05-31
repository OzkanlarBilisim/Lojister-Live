import * as React from 'react';
import Map, { NavigationControl, Layer } from 'react-map-gl';

const MapBox = ({ polylineData }) => {

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

    return (
        <Map
            mapboxAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
            initialViewState={{
                longitude: 35.7244443,
                latitude: 39.2153791,
                zoom: 5.25,
            }}
            style={{ width: "100%", height: "100%", minHeight: "150px" }
            }
            mapStyle="mapbox://styles/mapbox/light-v10"
            cooperativeGestures={true}
            locale={defaultLocale}
        >
            <NavigationControl position="bottom-right" />
            {
                polylineData &&
                <Layer
                    {...{
                        id: "zone",
                        type: "fill",
                        source: {
                            type: "geojson",
                            data: {
                                type: "Feature",
                                geometry: {
                                    type: "Polyline",
                                    coordinates: [],
                                },
                            },
                        },
                        layout: {},
                        paint: {
                            "fill-color": "red",
                            "fill-opacity": 1,
                        },
                    }}
                />
            }
        </Map >
    );
};
export default MapBox;