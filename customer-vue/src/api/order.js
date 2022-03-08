import axios from "axios";

export default {
  requestOrderHistory(page) {
    const options = {
      params: {
        page: page
      }
    }
    return axios.get(process.env.VUE_APP_ORDER_API_URL + "/order/history", options);
  },
  addItemToBasket(item) {
    item.itemOptionIds =[];
    for (const itemId of item.otherOptions) {
      item.itemOptionIds.push(itemId)
    }
    item.itemOptionIds.push(item.requireOption)
    return axios.post(process.env.VUE_APP_ORDER_API_URL+'/order/item', item);
  }
}