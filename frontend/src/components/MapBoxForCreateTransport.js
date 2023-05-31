import * as React from 'react';
import Map, { Marker, NavigationControl } from 'react-map-gl';

const MapBoxForCreateTransport = ({ longitude, latitude, onDragMarker }) => {
    const mapRef = React.useRef();
    const [isMapLoaded, setIsMapLoaded] = React.useState(false)

    const onMapLoad = React.useCallback(() => {
        setIsMapLoaded(true)
    }, []);

    const onSelectCity = () => {
        mapRef.current?.flyTo({ center: [longitude, latitude], duration: 3000, zoom: 16 });
    }

    React.useEffect(() => {
        if (isMapLoaded) {
            onSelectCity();
        }
    }, [longitude || latitude])


    return (
        <Map
            ref={mapRef}
            onLoad={onMapLoad}
            mapboxAccessToken={process.env.REACT_APP_MAPBOX_TOKEN}
            initialViewState={{
                longitude: longitude ? longitude : 35.7244443,
                latitude: latitude ? latitude : 39.2153791,
                zoom: longitude || latitude ? 16 : 4.5,
                bearing: 0,
                pitch: 0
            }}
            style={{ width: "100%", height: "100%", minHeight: "150px" }
            }
            mapStyle="mapbox://styles/mapbox/light-v10"
            locale={defaultLocale}
        >
            <NavigationControl position="bottom-right" />
            <Marker
                longitude={longitude ? longitude : 35.7244443}
                latitude={latitude ? latitude : 39.2153791}
                draggable
                onDragEnd={e => {
                    onDragMarker({ longitude: e.lngLat.lng, latitude: e.lngLat.lat })
                }}
            />
        </Map >
    );
};
export default MapBoxForCreateTransport;

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