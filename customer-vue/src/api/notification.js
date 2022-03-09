import axios from "axios";

const url = process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL + "/notification-service/notification";

export default {
  requestNotification() {
    return axios.get(url);
  }
}