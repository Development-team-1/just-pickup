import axios from "axios";
import jwt from "@/common/jwt";
import router from "@/router";

export default {
  async requestReissue() {
    const config = {
      headers: {
        "X-AUTH-TOKEN": jwt.getToken()
      }
    }

    const res = await axios.get(process.env.VUE_APP_USER_AUTH_URL + "/reissue", config);

    const accessToken = res.data.data.accessToken;
    jwt.saveToken(accessToken);
    jwt.saveExpiredTime(res.data.data.expiredTime);
    axios.defaults.headers.common['Authorization'] =  "Bearer " + accessToken;

    return accessToken;
  },
  requestCheckAccessToken() {
    axios.defaults.headers.common['Authorization'] =  "Bearer " + jwt.getToken();

    return axios.get( process.env.VUE_APP_USER_AUTH_URL +"/check/access-token");
  },
  async logout() {
    const config = {
      headers: {
        "X-AUTH-TOKEN": jwt.getToken()
      }
    }
    try {
      const response = await axios.post(process.env.VUE_APP_USER_AUTH_URL + "/logout", null, config);
      if (response.data.code == 'SUCCESS') {
        jwt.destroyAll();
        await router.push("/login");
      }  else {
        alert("로그아웃 실패");
      }
    } catch (error) {
      console.log("[logout]", error);
    }
  }
}