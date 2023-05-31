import { configureStore } from '@reduxjs/toolkit';
import reducers from "./reducers/index";
import throttle from 'lodash.throttle';
import { saveState, loadState } from "../components/ReduxLocalstorage"

const store = configureStore({
    reducer: reducers,
    preloadedState: loadState(),
});

store.subscribe(
    throttle(() => saveState(store.getState()), 1000)
);

export default store;