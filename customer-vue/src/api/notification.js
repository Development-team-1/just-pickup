import axios from "axios";

const url = process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL + "/notification-service";

export default {
  requestNotification() {
    return axios.get(url + "/notifications");
  },
  patchNotification(id, isRead) {
    const body = {
      read: isRead
    }

    return axios.patch(url + "/notification/" + id, body)
  },
  countsNotification() {
    return axios.get(url + "/api/notification/counts");
  }
}