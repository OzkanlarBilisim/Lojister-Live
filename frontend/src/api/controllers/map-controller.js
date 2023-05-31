import axios from "axios";

export const getRouteRequest = (latLogs) => axios.post(`/map/getRoute`, latLogs);