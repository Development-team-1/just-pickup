import Vue from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import router from './router';
import axios from "axios";
import auth from "./api/auth.js";

axios.defaults.withCredentials = true;

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    if (error.response.status === 401) {
      let code = error.response.data.code;
      if (code === "EXPIRED") {
        console.log("## expired");
        try {
          const accessToken = await auth.requestReissue();
          originalRequest.headers.Authorization = "Bearer " + accessToken;
          return axios(originalRequest);
        } catch (reissueError) {
          window.location.href = "/";
          alert("권한이 없습니다. 다시 로그인 해주세요");
        }
       }
      window.location.href = "/";
      alert("권한이 없습니다. 다시 로그인해주세요.");
    }
    return Promise.reject(error);
  }
);