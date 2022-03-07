<template>
  <v-container
      fill-height
  >
    <v-row>
      <v-col>
        <v-card>

          <v-card-title>Hello</v-card-title>
          <v-card-text>Nice to meet you</v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col>
        <div class="text-h5" >즐겨찾는 매장입니다.</div>
        <slide-store
          :store-list="favoriteStoreList"
        ></slide-store>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import slideStore from "@/components/SlideStore";
import storeApi from "@/api/store";

export default {
  name: "HomeView",
  components:{
    slideStore
  },
  data(){
    return{
      latitude:0,
      longitude:0,
      favoriteStoreList:{
        data:[],
        isActive:'',

      },
    }
  },
  async mounted() {
    const location = await this.getLocation();
    this.latitude = location.latitude;
    this.longitude = location.longitude;
    storeApi.getFavoriteStore(this.latitude,this.longitude)
    .then(response =>{
      this.favoriteStoreList.isActive = 'd-none'
      this.favoriteStoreList.data = response.data.data;
    });
  },
  methods:{
    getLocation: async function() {
      console.log("initGeoLocation");
      return new Promise(function (resolve, reject) {
        if ('geolocation' in navigator) {
          navigator.geolocation.getCurrentPosition((position) => {
            const coords = position.coords;

            resolve({
              latitude: coords.latitude,
              longitude: coords.longitude
            })
          })
        } else {
          reject();
        }
      });
    },
  }
}
</script>

<style scoped>

</style>