export default {
  requestRegisterUser(user) {
    return axios.post("http://localhost:8001/user-service/store-owner", user);
  },

  async requestLoginUser(email, password) {
    const user = {
      email: email,
      password: password
    }

    try {
      const response = await axios.post("http://localhost:8001/user-service/login", user);
      console.log(response);
      const AUTH_TOKEN = response.data.data.access_token;
      localStorage.setItem('access_token', AUTH_TOKEN);
      axios.defaults.headers.common['Authorization'] =  AUTH_TOKEN;

      return true;
    } catch (err) {
      console.log("Error = ", err);
      alert("로그인 실패!");

      return false;
    }

  }
}

import axios from "axios";
