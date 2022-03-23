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
  },
  saveOrder(order){
    //만약 동시요청으로 장바구니가 두개 생겨버린다면?
    return axios.post(process.env.VUE_APP_ORDER_API_URL + "/order/orders", order);
  },
  getOrder() {
    return axios.get(process.env.VUE_APP_ORDER_API_URL + "/order/orders");
  },
  deleteOrderItem(orderItemId){
    return axios.delete(process.env.VUE_APP_ORDER_API_URL + "/orderItem/"+orderItemId);
  },
  getOrderDetail(orderId) {
    return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL + "/order-service/api/order-detail/" + orderId);
  }
}