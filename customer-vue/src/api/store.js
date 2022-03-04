import axios from "axios";

export default {
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
}