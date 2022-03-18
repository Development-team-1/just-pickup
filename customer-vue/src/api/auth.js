import axios from "axios";
import jwt from "@/common/jwt";
import router from "@/router/router";

export default {
  async requestReissue() {
    const config = {
      headers: {
        "X-AUTH-TOKEN": jwt.getToken()
      }
    }

    const res = await axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+"/user-service/auth/reissue", config);

    const accessToken = res.data.data.accessToken;
    jwt.saveToken(accessToken);
    jwt.saveExpiredTime(res.data.data.expiredTime);
    axios.defaults.headers.common['Authorization'] =  "Bearer " + accessToken;

    return accessToken;
  },
  requestCheckAccessToken() {
    axios.defaults.headers.common['Authorization'] =  "Bearer " + jwt.getToken();

    return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+"/user-service/auth/check/access-token");
  },
  logout(){
    axios.post(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+"/user-service/auth/logout",'',{headers: {"X-AUTH-TOKEN": jwt.getToken(),}})
        .finally(()=>{
          router.push('/login')
          jwt.destroyAll()
        })
  }
}