import axios from "axios";

export const assignVehicleRequest = ({ clientTransportProcessId, vehicleId }) => axios.post(`/clientTransportProcess/${clientTransportProcessId}/vehicle?vehicleId=${vehicleId}`);

export const getByClientAdvertisementBidId = (id) => axios.get(`/clientTransportProcess/clientAdvertisementBid/${id}`);

export const getAcceptReceiveRequest = (transportCode) => axios.get(`/clientTransportProcess/acceptReceive?transportCode=${transportCode}`);

export const getTransportProcessByTransportIdRequest = (transportId) => axios.get(`/clientTransportProcess/clientAdvertisement/${transportId}`);

export const getAbroudBidPrice = (transportId) => axios.get(`/abroudBid/adwertisamentId/${transportId}`);

export const getFindMyBid = (bidId, companyId) => axios.get(`/abroudBid/getFindMyBid/${bidId}/${companyId}`);

export const getMyTransportProcessRequest = () => axios.get(`/clientTransportProcess/myTransportProcesses`);

export const startingTransportRequest = (clientTransportProcessId) => axios.post(`/clientTransportProcess/${clientTransportProcessId}/startingTransport/`);

export const startConfirmationTransportRequest = (token) => axios.post(`/clientTransportProcess/startConfirmationTransport?token=${token}`);

export const startConfirmationTransporNoTokentRequest = (transportCode) => axios.get(`/clientTransportProcess/startTransport/transportCode/${transportCode}`);

export const endingTransportRequest = (clientTransportProcessId) => axios.post(`/clientTransportProcess/${clientTransportProcessId}/endingTransport/`);

export const endConfirmationTransportRequest = (token) => axios.post(`/clientTransportProcess/endConfirmationTransport?token=${token}`);

export const submitShipsmentInfoDataSend = (data) => axios.post(`/shipsmentInfo/add`, data);

export const endConfirmationTransporNoTokentRequest = (transportCode) => axios.get(`/clientTransportProcess/endTransport/transportCode/${transportCode}`);

export const currentPaymentapi = (data) => axios.post(`/currentPayment/pay/`, data);

export const afterPayApi = (advertID, insuranceType) => axios.post(`/paymentMethod/afterPay/`+advertID+`/`+insuranceType);

export const getTransportByTransportCodeRequest = (transportCode) => axios.get(`/clientTransportProcess/transportCode/${transportCode}`);

export const getAbroudInsuranceApi = (advertID) => axios.get(`/paymentMethod/getAfterPay/${advertID}`);

export const getDocumentsByTransportProcessIdRequest = (clientTransportProcessId) => axios.get(`/clientTransportProcess/${clientTransportProcessId}/document`);

export const updateStatusForCargoDeliveredRequest = (transportCode) => axios.put(`/clientTransportProcess/cargoDelivered?transportCode=${transportCode}`);

export const updateStatusForContainerReceivedRequest = (transportCode) => axios.put(`/clientTransportProcess/containerReceived?transportCode=${transportCode}`);

