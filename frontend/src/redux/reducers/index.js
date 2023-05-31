import { combineReducers } from "redux";
import { userReducer } from "./userReducer";
import { transportReducer } from "./transportReducer";

const reducers = combineReducers({
    user: userReducer,
    savedTransport: transportReducer,
});

export default reducers;