<template>
  <v-container >
    <v-row justify="center">
      <v-col>
        <div class="text-h5">
          회원님과<br>
          <b><span class="deep-orange--text">가까이 있는 매장</span></b>이에요!
        </div>
        <slide-store
            :store-list="nearbyStores"
            :id="'nearby'"
        ></slide-store>
      </v-col>
    </v-row>

    <v-divider
        class="my-5"
    ></v-divider>

    <v-row justify="center">
      <v-col>
        <div class="text-h5" >
          회원님이<br>
          <b><span class="deep-orange--text">즐겨찾는 매장</span></b>이에요!
        </div>
        <slide-store
          :store-list="favoriteStoreList"
          :id="'favorite'"
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
      nearbyStores: {
        data: [],
        isActive: ''
      }
    }
  },
  async mounted() {
    const location = await this.getLocation();
    this.latitude = location.latitude;
    this.longitude = location.longitude;

    await this.requestFavoriteStore()

    await this.requestNearbyStore();

  },
  methods:{
    getLocation: async function() {
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
    requestFavoriteStore: async function() {
      try {
        const response = await storeApi.getFavoriteStore(this.latitude,this.longitude)
        this.favoriteStoreList.isActive = 'd-none'
        this.favoriteStoreList.data = response.data.data;
      } catch (error) {
        console.log(error);
      }
    },
    requestNearbyStore: async function() {
      try {
        const response = await storeApi.requestNearbyStore(this.latitude, this.longitude, "", 0, 10);
        const stores = response.data.data.stores;
        stores.forEach(store => {
          this.nearbyStores.data.push({
            id: store.id,
            name: store.name,
            distance: store.distance,
            favoriteCounts: store.favoriteCounts
          });
          this.nearbyStores.isActive = 'd-none';
        })
      } catch (error) {
        console.log(error);
      }
    },
  }
}
</script>

<style scoped>

</style>