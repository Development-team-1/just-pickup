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
        return axios.get("http://localhost:8001/order-service/prevOrder", options);
    }
}