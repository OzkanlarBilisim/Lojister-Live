import axios from "axios";

export const getVehiclesRequest = () => axios.get(`/vehicle`);

export const createVehicleRequest = (vehicle) => axios.post(`/vehicle`, vehicle);

export const getMyVehiclesRequest = (pageable) => axios.get(`/vehicle/myVehicles?page=${pageable.page - 1}&size=${pageable.pageSize}&sort=${pageable.sort}`);

export const getMyVehiclesAll = () => axios.get(`/vehicle/myVehicles/min`);

export const getVehicleByIdRequest = (vehicleId) => axios.get(`/vehicle/${vehicleId}`);

export const updateVehicleRequest = (vehicleId, vehicle) => axios.put(`/vehicle/${vehicleId}`, vehicle);

export const changeDriverRequest = ({ vehicleId, driverId, transportProcessId }) => axios.put(`/vehicle/${vehicleId}/changeDriver?driverId=${driverId}&transportProcessId=${transportProcessId}`);

export const deleteVehicleByIdRequest = (vehicleId) => axios.delete(`/vehicle/${vehicleId}`);

export const updateVehicleDriverRequest = ({ vehicleId, driverId }) => axios.get(`/vehicle/${vehicleId}/changeDriver?driverId=${driverId}`);

export const getUnAssignDriverFromVehicleRequest = (vehicleId) => axios.get(`/vehicle/${vehicleId}/usAssignDriver`);

export const updateLastLocationRequest = (position) => axios.put(`/vehicle/lastLocation`, position);

export const getVehicleReviewStatusRequest = () => axios.get(`/vehicle/status/review`);

export const getVehicleRevisionStatusRequest = () => axios.get(`/vehicle/status/revision`);

export const createVehicleStatusRequest = (vehicleStatus) => axios.post(`/vehicle/status/update`, vehicleStatus);

export const getDriverLastPositionRequest = (transportProcessId) => axios.get(`/vehicle/transportProcess/${transportProcessId}`);