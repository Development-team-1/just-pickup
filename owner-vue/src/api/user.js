import jwt from '../common/jwt.js';

export default {
  requestRegisterUser(user) {
    return axios.post(process.env.VUE_APP_USER_URL + "/store-owner", user);
  },

  async requestLoginUser(email, password) {
    const user = {
      email: email,
      password: password
    }

    try {
      const response = await axios.post( process.env.VUE_APP_USER_URL +"/login", user);
      const data = response.data.data;

      jwt.saveToken(data.accessToken);
      jwt.saveExpiredTime(data.expiredTime);

      axios.defaults.headers.common['Authorization'] =  "Bearer " + data.accessToken;

      return true;
    } catch (err) {
      console.log("Error = ", err);
      return false;
    }

  },
  requestUserInfo() {
    return axios.get(process.env.VUE_APP_OWNER_SERVICE_BASEURL + '/user-service/store-owner');
  }
}

import axios from "axios";
