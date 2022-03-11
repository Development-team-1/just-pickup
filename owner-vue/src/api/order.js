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
        return axios.get( process.env.VUE_APP_API_URL + "/order/prev-order", options);
    },
    requestOrder(orderDate, lastOrderId) {
        const options = {
            params: {
                orderDate: orderDate,
                lastOrderId: lastOrderId
            }
        }
        return axios.get(process.env.VUE_APP_API_URL + "/order/order-main", options);
    },
    patchOrder(orderId, orderStatus) {
        const body = {
            orderStatus: orderStatus
        }
        return axios.patch(process.env.VUE_APP_OWNER_SERVICE_BASEURL + "/order-service/order/" + orderId, body);
    }
}