import axios from "axios";
import jwt from "@/common/jwt";

export default {
  async requestReissue() {
    const config = {
      headers: {
        "X-AUTH-TOKEN": jwt.getToken()
      }
    }

    const res = await axios.get("http://localhost:8001/user-service/auth/reissue", config);

    const accessToken = res.data.data.accessToken;
    jwt.saveToken(accessToken);
    jwt.saveExpiredTime(res.data.data.expiredTime);
    axios.defaults.headers.common['Authorization'] =  "Bearer " + accessToken;

    return accessToken;
  },
  requestCheckAccessToken() {
    axios.defaults.headers.common['Authorization'] =  "Bearer " + jwt.getToken();

    return axios.get("http://localhost:8001/user-service/auth/check/access-token");
  }
}