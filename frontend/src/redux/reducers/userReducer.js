import { ActionTypes } from "../contants/action-types";

const initialState = null;

export const userReducer = (state = initialState, { type, payload }) => {
    switch (type) {
        case ActionTypes.SET_USER:
            return payload
        default:
            return state
    }
}