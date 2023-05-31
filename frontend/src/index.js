import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import store from "./redux/store"
import { Provider } from 'react-redux';
import 'moment/locale/tr'
import { ConfigProvider } from 'antd';
import './i18n';
import trTR from 'antd/lib/locale/tr_TR';
import enUS from 'antd/lib/locale/en_US';

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <ConfigProvider locale={trTR}>
        <App />
      </ConfigProvider>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root')
);