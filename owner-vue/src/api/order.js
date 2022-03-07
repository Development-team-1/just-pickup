import axios from "axios";

export default {
    requestPrevOrder(startDate, endDate, page) {
        const options = {
            params: {
                startDate: startDate,
                endDate: endDate,
                page: page
            }
        }
        return axios.get( process.env.VUE_APP_ORDER_URL + "/order/prev-order", options);
    },
    requestOrder(orderDate, lastOrderId) {
        const options = {
            params: {
                orderDate: orderDate,
                lastOrderId: lastOrderId
            }
        }
        return axios.get(process.env.VUE_APP_ORDER_URL + "/order/order-main", options);
    }
}