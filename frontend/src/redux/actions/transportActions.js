import { ActionTypes } from "../contants/action-types"

export const setSavedTransport = (transport) => {
    return {
        type: ActionTypes.SET_SAVED_TRANSPORT,
        payload: transport,
    };
};