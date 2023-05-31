import axios from "axios";

export const getNotificationsRequest = () => axios.get(`/notification`);

export const createNotificationRequest = (data) => axios.post(`/notification`, data);

export const getNotificationByIdRequest = (id) => axios.get(`/notification/${id}`);

export const deleteNotificationRequest = (id) => axios.delete(`/notification/${id}`);