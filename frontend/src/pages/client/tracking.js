import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next';
import { getAdvertisimentByStatusCargoOnTheWayRequest } from '../../api/controllers/client-advertisiment-controller'
import MapBoxForTracking from '../../components/MapBoxForTracking'
import RequestError from '../../components/RequestError';

const Tracking = () => {
    const { t, i18n } = useTranslation("common");
    document.title = 'Lojister - ' + t("Takip")

    const defaultFilterValue = {
        startingAddressProvince: null,
        startingAddressDistrict: null,
        startingAddressNeighborhood: null,
        dueAddressProvince: null,
        dueAddressDistrict: null,
        dueAddressNeighborhood: null,
        clientAdvertisementTypeList: []
    };
    const [filterValues, setFilterValues] = useState(defaultFilterValue);
    const [sortValues, setSortValues] = useState("createdDateTime")

    const [adsLoading, setAdsLoading] = useState(true)
    const [transports, setTransports] = useState([])

    //get all ads
    const getAds = async () => {
        setAdsLoading(true)
        try {
            let res = await getAdvertisimentByStatusCargoOnTheWayRequest({ filter: filterValues, sort: sortValues });
            if (res) {
                setTransports(res.data)
            }
            setAdsLoading(false)
        } catch (error) {
            RequestError(error);
        }
    }

    useEffect(() => {
        getAds();
    }, [])

    return (
        <div style={{ position: "relative", width: "100%", height: "100%" }}>
            <MapBoxForTracking
                transports={transports}
                getAds={getAds}
                adsLoading={adsLoading}
                filterValues={filterValues}
                setFilterValues={setFilterValues}
                sortValues={sortValues}
                setSortValues={setSortValues}
                defaultFilterValue={defaultFilterValue}
            />
        </div>
    )
}
export default Tracking;