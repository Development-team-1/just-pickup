import axios from "axios";

export default {
  requestSearchStore(latitude, longitude, storeName, page) {
    const options = {
      params: {
        latitude: latitude,
        longitude: longitude,
        storeName: storeName,
        page: page
      }
    }
    return axios.get("http://localhost:8000/store-service/search-store", options);
  }
}