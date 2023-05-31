import ApiProvider from "./api/ApiProvider";
import Router from "./components/Router";
import './main.less'
import "bootstrap-icons/font/bootstrap-icons.css";
import 'mapbox-gl/dist/mapbox-gl.css';
import FirebaseProvider from "./components/FirebaseProvider";
import mapboxgl from '!mapbox-gl'; // eslint-disable-line import/no-webpack-loader-syntax
mapboxgl.accessToken = process.env.REACT_APP_MAPBOX_TOKEN;


function App() {
  return (
    <ApiProvider>
      <FirebaseProvider>
        <Router />
      </FirebaseProvider>
    </ApiProvider>
  );
}

export default App;