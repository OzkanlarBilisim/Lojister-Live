import axios from "axios";

export const getAdvertPaymetController = (isReceipt) => axios.get(`/receipt/getAll/`+isReceipt);
export const approveOrDeleteReceipt = (id,deleteOrAproved) => axios.post(`/receipt/approved/`+id+`/deleteOrAproved/`+deleteOrAproved);
export const getReceipt = (id) => axios.get(`/receipt/id/`+id, {
    responseType: 'blob',
  });
  export const PaymentControllReceipt = (data) => axios.post(`/receipt/add/`,data);