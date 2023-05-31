import axios from "axios";

export const getAllClientRequest = () => axios.get(`/client`);

export const getClientByIdRequest = (clientId) => axios.get(`/client/${clientId}`);

export const updateClientByIdRequest = (clientId) => axios.put(`/client/${clientId}`);

export const changeClientPasswordRequest = (password) => axios.post(`/client/changePassword`, password);

export const getClientDashboardRequest = () => axios.get(`/client/dashboardInformation`);

export const createEmployeeRequest = (employee) => axios.post(`/client/employee`, employee);

export const deleteEmployeeByIdRequest = (employeeId) => axios.delete(`/client/employee/${employeeId}`);

export const getAccountSettingsRequest = () => axios.get(`/client/getAccountSetting`);

export const getNotificationSettingsRequest = () => axios.get(`/client/getNotificationSetting`);

export const getMyDefaultAddressRequest = () => axios.get(`/client/myDefaultAddress`);

export const getMyEmployeeRequest = (pageable) => axios.get(`/client/myEmployee?page=${pageable.page - 1}&size=${pageable.pageSize}&sort=${pageable.sort}`);

export const updateAccountSettingsRequest = (settings) => axios.put(`/client/updateAccountSetting`, settings);

export const updateNotificationSettingsRequest = (settings) => axios.put(`/client/updateNotificationSetting`, settings);