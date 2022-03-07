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
        <div class="text-overline" >즐겨찾는 매장입니다.</div>
        <slide-store
          :store-list="favoriteStoreList"
        ></slide-store>
      </v-col>
    </v-row>
    <v-overlay :value="isLoading">
      <v-progress-circular
          indeterminate
          size="64"
      ></v-progress-circular>
    </v-overlay>
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
      isLoading: true,
      latitude:0,
      longitude:0,
      favoriteStoreList:[],
    }
  },
  async mounted() {
    const location = await this.getLocation();
    this.latitude = location.latitude;
    this.longitude = location.longitude;
    storeApi.getFavoriteStore(this.latitude,this.longitude)
    .then(response =>{
      this.favoriteStoreList = response.data.data;
    });
    this.isLoading = false
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