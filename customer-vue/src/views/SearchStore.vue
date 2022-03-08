<template>
  <div>
    <v-row>
      <v-text-field
          v-model="storeName"
          @keyup.enter="search"
          @click:append="search"
          append-icon="mdi-magnify"
          label="지역, 가게명으로 찾아보세요"
          filled
      >
      </v-text-field>
    </v-row>

    <v-row>
      <v-alert
          border="left"
          colored-border
          color="deep-purple accent-4"
          elevation="2"
          dense
      >
        거리 순으로 가까운 매장을 찾아왔어요 😁
      </v-alert>
    </v-row>

    <v-row>
      <v-col v-for="card in cards" v-bind:key="card.storeId" sm="6">
        <v-card
            v-bind:data-id="card.storeId"
            v-on:click="clickCard(card.storeId)"
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
    <v-row justify="center" v-if="hasNext">
      <v-btn @click="more"
             elevation="2"
             color="blue-grey"
             class="ma-2 white--text"
             >
        <b>더보기</b>
        <v-icon>mdi-chevron-down</v-icon>
      </v-btn>
    </v-row>

    <v-overlay :value="isLoading">
      <v-progress-circular
          indeterminate
          size="64"
      ></v-progress-circular>
    </v-overlay>
  </div>
</template>

<script>
import storeApi from "../api/store.js";

export default {
  name: "SearchStore",
  async mounted() {
    const location = await this.getLocation();

    this.latitude = location.latitude;
    this.longitude = location.longitude;

    await this.search();
  },
  data: function() {
    return {
      isLoading: true,
      latitude: null,
      longitude: null,
      storeName: "",
      page: 0,
      cards: [],
      hasNext: false
    }
  },
  methods: {
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
    search: async function() {
      this.isLoading = true;
      try {
        this.page = 0;
        const response = await storeApi.requestNearbyStore(this.latitude, this.longitude, this.storeName, this.page);
        this.cards = [];
        this.renderCard(response.data);
      } catch (error) {
        console.log(error);
      }
      this.isLoading = false;
    },
    more: async function() {
      this.isLoading = true;
      try {
        this.page += 1;
        const response = await storeApi.requestNearbyStore(this.latitude, this.longitude, this.storeName, this.page);
        this.renderCard(response.data);
      } catch (error) {
        console.log(error);
      }
      this.isLoading = false;
    },
    renderCard: function(json) {
      this.hasNext = json.data.hasNext;
      const stores = json.data.stores;

      stores.forEach( (store) => {
        this.cards.push({
          storeId: store.id,
          name: store.name,
          distance: store.distance,
          favoriteCounts: store.favoriteCounts
        })
      });
    },
    clickCard(storeId) {
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