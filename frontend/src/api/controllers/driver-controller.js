import axios from "axios";

export const getDriverDashboardRequest = () => axios.get(`/driver/dashboardInformation`);

export const getDriversRequest = () => axios.get(`/driver`);

export const getMyDriversRequest = (pageable) => axios.get(`/driver/myDrivers?page=${pageable.page - 1}&size=${pageable.pageSize}&sort=${pageable.sort}`);

export const getMyDriversAll = () => axios.get(`/driver/myDrivers/min`);

export const createDriverRequest = (driver) => axios.post(`/driver`, driver);

export const getDriverByIdRequest = (driverId) => axios.get(`/driver/${driverId}`);

export const updateDriverRequest = (driverId, form) => axios.put(`/driver/${driverId}`, form);

export const changeDriverPasswordRequest = (password) => axios.post(`/driver`, password);

export const getDriverReviewStatusRequest = () => axios.get(`/driver/status/review`);

export const getDriverRevisionStatusRequest = () => axios.get(`/driver/status/revision`);

export const updateDriverStatusRequest = (status) => axios.post(`/driver/status/update`, status);

export const getNotificationSettingsRequest = () => axios.get(`/driver/getNotificationSetting`);

export const updateNotificationSettingsRequest = (settings) => axios.put(`/driver/updateNotificationSetting`, settings);