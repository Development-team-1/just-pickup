import axios from "axios";

export default {
  requestOrderHistory(page) {
    const options = {
      params: {
        page: page
      }
    }
    return axios.get(process.env.VUE_APP_ORDER_API_URL + "/order/history", options);
  }
}