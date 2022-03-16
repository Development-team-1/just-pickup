import axios from "axios";
import jwt from '../common/jwt.js';

export default {
    requestRegisterUser(user) {
        return axios.post(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+"/user-service/store-owner", user);
    },
    geUserData() {
        return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+"/user-service/customer/", );
    },

    async requestLoginUser(email, password) {
        const user = {
            email: email,
            password: password
        }

        try {
            const response = await axios.post(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+"/user-service/login", user);
            const data = response.data.data;

            jwt.saveToken(data.accessToken);
            jwt.saveExpiredTime(data.expiredTime);

            axios.defaults.headers.common['Authorization'] =  "Bearer " + data.accessToken;

            return true;
        } catch (err) {
            console.log("Error = ", err);
            return false;
        }

    }
}

