import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import axios from "axios";
import customUtil from './util/customUtil'

Vue.config.productionTip = false
Vue.prototype.$axios = axios;
Vue.prototype.$customUtil = customUtil;

console.log(process.env)

new Vue({
  vuetify,
  router,
  render: h => h(App)
}).$mount('#app')

