import axios from "axios";

export const getClientAdvertisimentBidRequest = () => axios.get(`/clientAdvertisimentBid`);

export const updateBidStatusRequest = ({ id, status }) => axios.post(`/clientAdvertisementBid/${id}/updateBidStatus?value=${status}`);

export const getAllAdvertisementYurtdisiDriverBidList = (userId) => axios.get(`abroudBid/driver/bid/list/${userId}`);

export const updateAbroudStatusUpdate = (data, bidId) => axios.put(`/abroud/statusUpdate/${bidId}`, data);

// export const updateBidStatusRequest = ({ id, status }) => axios.post(`/clientAdvertisementBid/${id}/updateBidStatus?value=${status}`);

export const createClientAdvertisimentBidRequest = ({ clientAdvertisimentId, bidForm }) => axios.post(`/clientAdvertisementBid/${clientAdvertisimentId}`, bidForm);

export const createClientAdvertisimentBidAbroudRequest = (bidForm) => axios.post(`/abroudBid/add/`, bidForm);

// export const createClientAdvertisimentBidAbroudRequest = (bidForm) => axios.post(`/abroudBid/add/`, bidForm);

export const getClientAdvertisimentBidByIdRequest = (id) => axios.get(`/clientAdvertisementBid/${id}`);

export const deleteClientAdvertisimentBidRequest = (id) => axios.delete(`/clientAdvertisementBid/${id}`);

export const getMyBidsRequest = ({ isActive, pageable }) => axios.get(`/clientAdvertisementBid/getMyBids/${isActive}?page=${pageable.page - 1}&size=${pageable.pageSize}${pageable.sort && '&sort=' + pageable.sort}`);

export const getMyBidsStatusCargoOntheWayRequest = ({ filter, sort }) => axios.post(`/clientAdvertisementBid/myAdvertisementBids/cargoOnTheWay?sort=${sort}`, filter);

export const getMyAdvertisimentBidsRequest = (advertisimentId) => axios.get(`/clientAdvertisementBid/${advertisimentId}`);

export const getMyAdvertisementBids = (advertisimentId) => axios.get(`/clientAdvertisementBid/myAdvertisementBids/${advertisimentId}`);

export const getMyAdvertisementBidsAbroud = (advertisimentId) => axios.get(`/abroudBid/adwertisamentId/${advertisimentId}`);

export const getMyAdvertisimentBidsCountRequest = () => axios.get(`/clientAdvertisementBid/count`);

export const getMyBidsCountRequest = () => axios.get(`/clientAdvertisementBid/myBids/count`);


export const updateAbroudStatusUpdateApproved = (bidId) => axios.put(`/abroudBid/statusApproved/${bidId}`);
export const updateAbroudBidDenied = (bidId) => axios.put(`/abroudBid/statusUpdateDenied/${bidId}`);