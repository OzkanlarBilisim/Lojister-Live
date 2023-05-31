import React from 'react'
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import MapBoxForTransports from '../../components/MapBoxForTransports'
import MapBoxForTransportsAbroad from '../../components/MapBoxForTransportsAbroad';

export default function Transports() {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("İlanlar")
    const [transportRegion, setTransportRegion] = useState("inland")

    return (
        <div style={{ position: "relative", width: "100%", height: "100%" }}>
            {transportRegion === 'inland' ?
                <MapBoxForTransports scrollControl={true} setTransportRegion={setTransportRegion} />
                :
                transportRegion === 'abroad' ?
                    <MapBoxForTransportsAbroad setTransportRegion={setTransportRegion} /> : <></>
            }
        </div>
    )
}
