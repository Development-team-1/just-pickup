import Vue from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify';
import router from './router';
import axios from "axios";
import auth from "./api/auth.js"

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
      try {
        const originalRequest = error.config;
        if (error.response.status === 401) {
          // access token 만료 시
          if (error.response.data.code == "EXPIRED") {
            const accessToken = await auth.requestReissue();

            originalRequest.headers.Authorization = "Bearer " + accessToken;
            return axios(originalRequest);
          }
          // 그외 에러일 시
          alert("로그인 정보가 일치하지 않습니다.");
          await router.replace('/login');
          return;
        }
      } catch (error) {
          alert("로그인 정보가 일치하지 않습니다.");
          await router.replace('/login');
          return;
      }
    return Promise.reject(error);
  }
);