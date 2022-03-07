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
      return axios.get(process.env.VUE_APP_STORE_API_URL + '/store/search', options);
    },
    getCategoryList(){
        return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/category/');
    },
    putCategoryList(data){
        return axios({
            method:'put',
            url:process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/category',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json;charset=UTF-8'
            },
            data: data,
            responseType:'json'
        })
    },
    getItemById(itemId){
        return axios.get(process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/item/'+itemId)
    },
    saveItem(method, itemData){
        return axios({
            method:method,
            url: process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/store-service/item',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json;charset=UTF-8'
            },
            data: itemData,
            responseType:'json'
        })
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

}