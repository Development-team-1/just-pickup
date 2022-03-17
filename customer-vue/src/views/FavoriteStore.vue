<template>
  <div>
    <v-row>
      <v-alert
          border="left"
          colored-border
          color="deep-purple accent-4"
          elevation="2"
          dense
      >
        즐겨찾기를 누른 매장이에요 😳
      </v-alert>
    </v-row>
    <v-row>
      <v-col
          :class="isLoading"
          sm="6"
      >
        <v-skeleton-loader
            sm="12"
            elevation="1"
            type="card"
        />
      </v-col>
      <v-col v-for="card in cards" v-bind:key="card.storeId" sm="6">
        <v-card
            v-bind:data-id="card.storeId"
            @click="clickCard(card.storeId)"
        >
          <v-img
              height="180"
              :src="require('@/assets/store.jpeg')"
          ></v-img>
          <v-card-title>{{ card.name }}</v-card-title>
          <v-card-text>
            <v-row>
              <div class="orange--text ms-4">
                <v-icon color="orange" dense>mdi-heart</v-icon>
                {{ card.favoriteCounts }}
              </div>
              <div class="grey--text ms-4">
                <v-icon dense>mdi-map-marker</v-icon>
                {{ card.distance }}
              </div>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>

    </v-row>

    <br><br>
  </div>
</template>

<script>
import storeApi from "@/api/store";

export default {
  name: "FavoriteStore",
  async mounted() {
    const location = await this.getLocation();

    this.latitude = location.latitude;
    this.longitude = location.longitude;

    await this.search();
  },
  data: function() {
    return {
      isLoading: '',
      latitude: null,
      longitude: null,
      cards: [],
    }
  },
  methods: {
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
    search: async function() {
      try {
        const response = await storeApi.getFavoriteStore(this.latitude, this.longitude,);
        this.cards = [];
        this.isLoading='d-none'
        this.renderCard(response.data);

      } catch (error) {
        console.log(error);
      }
    },
    renderCard: function(json) {
      const stores = json.data;

      stores.forEach( (store) => {
        this.cards.push({
          storeId: store.id,
          name: store.name,
          distance: store.distance,
          favoriteCounts: store.favoriteCounts
        })
      });
    },
    clickCard: function(storeId) {
      this.$router.push({
        name: "store",
        params: {storeId: storeId}
      })
    }
  }
}
</script>

<style scoped>

</style>