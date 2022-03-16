import Vue from 'vue';
import App from './App.vue';
import vuetify from "@/plugins/vuetify";
import router from "./router/router.js";
import axios from "axios";
import auth from "@/api/auth";
import jwt from "@/common/jwt";

axios.defaults.withCredentials = true;
Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  render: h => h(App),
}).$mount('#app')

axios.interceptors.request.use(function (config) {
  config.headers.Authorization = "Bearer " + jwt.getToken();
  return config;
});
Vue.filter('currency', function (value) {
  var num = new Number(value);
  return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,")
});

axios.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;
      if (error.response.status === 401) {
        let code = error.response.data.code;
        try {
          if (code === "EXPIRED") {
            const accessToken = await auth.requestReissue();
            originalRequest.headers.Authorization = "Bearer " + accessToken;
            return axios(originalRequest);
          }
        } catch (error2) {
          window.location.href = process.env.VUE_APP_BASEURL+"/login";
          alert("권한이 없습니다. 다시 로그인 해주세요");
          return Promise.reject(error2);
        }
      }
      return Promise.reject(error);
    }
);