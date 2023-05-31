import { ActionTypes } from "../contants/action-types";

const initialState = null;

export const transportReducer = (state = initialState, { type, payload }) => {
    switch (type) {
        case ActionTypes.SET_SAVED_TRANSPORT:
            return payload
        default:
            return state
    }
}