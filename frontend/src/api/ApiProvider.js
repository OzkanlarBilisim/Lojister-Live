import axios from "axios"
import { useMemo } from "react"

let domain = window.location.hostname;
export let BASE_URL = "https://www.lojister.com/api/v2";
export let BASE_URL_FRONTEND = "https://www.lojister.com/"

// export let google_api_key = "AIzaSyC6zxNYdl1DdHGruL7M-3f_EoEEoMSaLfg"  // Bu info@lojister.com hesabından alınan key
export let google_api_key = "AIzaSyC0m3PP1EoJoJOrKX8ibVouPAIsXKBO9jA"

switch (domain) {
    case "localhost":
      BASE_URL = "http://localhost:8083/api/v2";
      BASE_URL_FRONTEND = "http://localhost:3000/"
      break;
    case "www.lojister.com":
      BASE_URL = "https://www.lojister.com/api/v2";
      BASE_URL_FRONTEND = "https://www.lojister.com/"
      break;
    case "www.test.lojister.com":
      BASE_URL = "https://test.lojister.com/api/v2";
      BASE_URL_FRONTEND =  "https://test.lojister.com/";
      break;
    default:
      console.log("Invalid domain");
      break;
  }


axios.defaults.baseURL = BASE_URL;

const ApiProvider = (props) => {

    useMemo(() => {
        axios.interceptors.request.use(
            async (config) => {
                const token = localStorage.getItem("token")
                axios.defaults.headers["Accept-Language"] = localStorage.getItem('currentLanguageCode')
                if (token) {
                    config.headers["Authorization"] = `Bearer ${token}`
                }
                return config
            }
        );
        axios.interceptors.response.use(
            async (response) => {
                if (response?.data?.token) {
                    localStorage.setItem("token", response?.data?.token)
                }
                return response;
            }
        );
    }, [])
    return props.children
}

export default ApiProvider;