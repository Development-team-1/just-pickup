import axios from "axios";

export default {
  requestNearbyStore(latitude, longitude, storeName, page, size) {
    const options = {
      params: {
        latitude: latitude,
        longitude: longitude,
        storeName: storeName,
        page: page,
        size: size
      }
    }
    return axios.get(process.env.VUE_APP_STORE_API_URL + '/store/search', options);
  },
  getItemById(itemId){
      return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/item/'+itemId)
  },
  getFavoriteStore(latitude, longitude,){
      const options = {
          params: {
              latitude: latitude,
              longitude: longitude,
          }
      }
    return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/api/customer/store/favorite',options)
  },
  getCategoryList(){
      return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/category/');
  },
  fetchItem(itemId){
      return axios.get(process.env.VUE_APP_STORE_API_URL+'/item/'+itemId)
  },  
  requestCategoriesWithItem(storeId) {
    const options = {
      params: {
        "storeId": storeId
      }
    }
    return axios.get(process.env.VUE_APP_STORE_API_URL + "/categories", options);
  },
  requestStore(storeId) {
    return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL + "/store-service/store/" + storeId);
  },
  getFavoriteStoreByStoreId(storeId) {
    return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL + "/store-service/api/customer/favoriteStore/" + storeId);
  },
  markStar(storeId) {
    return axios.patch(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL + "/store-service/api/customer/favoriteStore/" + storeId);
  }

}