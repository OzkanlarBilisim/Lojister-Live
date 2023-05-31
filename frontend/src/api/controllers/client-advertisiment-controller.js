import axios from "axios";

export const getMyAdvertisementForClientRequest = ({ isActive, pageable }) => axios.get(`/clientAdvertisement/myAdvertisement/status/${isActive}?page=${pageable.page - 1}&size=${pageable.pageSize}${pageable.sort && '&sort=' + pageable.sort}`);;

export const getAllAdvertisementRequest = () => axios.get(`/clientAdvertisement`);

export const getActiveAdvertisementRequest = () => axios.get(`/clientAdvertisement/active`);

export const getHiddenAdvertisementRequest = () => axios.get(`/clientAdvertisement/hidden`);

export const getFinishedAdvertisementRequest = () => axios.get(`/clientAdvertisement/finished`);

export const hideAdvertisementRequest = (id) => axios.get(`/clientAdvertisement/${id}/hide`);

export const getAllAdvertisementMinimalHideRequest = ({ filter, sort, regionAdvertisementTypeList }) => axios.post(`/clientAdvertisement/minimal/hide?sort=${sort}`, filter);

export const getAllAdvertisementYurtdisi =()=> axios.get(`abroud/getAll`);

export const getAdvertisimentByStatusCargoOnTheWayRequest = ({ filter, sort }) => axios.post(`/clientAdvertisement/cargo_on_the_way?sort=${sort}`, filter);

export const getAdvertisementSimpleHideByIdRequest = (id) => axios.get(`/clientAdvertisement/${id}/simple/hide`);

export const getAllAdvertisementYurtdisiDetay = (id) => axios.get(`/abroud/${id}`);

export const getAllAdvertisementYurtdisiClientList = (id) => axios.get(`/abroud/client/${id}`);

export const getAllAdvertisementYurtdisiDetayFirstBid = (companyId, adwertisamentId) => axios.get(`/abroudBid/companyId/${companyId}/adwertisamentId/${adwertisamentId}`);

//export const getAdvertisementRouteRequest = (id) => axios.get(`/clientAdvertisement/${id}/getRoute`);
//export const getAdvertisementRouteRequest = (id) => axios.get(`/map/rota/${id}`);
export const getAdvertisementRouteRequest = (routeCordinate) => axios.get(`https://api.mapbox.com/directions/v5/mapbox/driving/`+routeCordinate+`.json?geometries=polyline&steps=true&overview=full&access_token=`+process.env.REACT_APP_MAPBOX_TOKEN);

export const getTransportById = (id) => axios.get(`/clientAdvertisement/${id}`);

export const getTransportByIdAbroud = (id) => axios.get(`/abroudBid/bid/and/advert/request/${id}`);

