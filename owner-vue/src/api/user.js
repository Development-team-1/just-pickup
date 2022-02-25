import axios from "axios";

export default {

    requestRegisterUser(user) {
        return axios.post("http://localhost:8001/user-service/store-owner", user);
    }
}