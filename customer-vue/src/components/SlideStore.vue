<template>
  <div >
    <v-slide-group
      show-arrows="desktop"
    >
      <v-slide-item
          :class="storeList.isActive"
          v-for="n in 3"
          :key="id + n"
      >
        <v-skeleton-loader
            class="my-3 mx-3"
            elevation="7"
            height="250"
            width="165"
            type="card"
            :key="id + n"
        ></v-skeleton-loader>
      </v-slide-item>
      <v-slide-item
          v-for="item in storeList.data"
          :key="item.id"
      >
        <v-card
            elevation="7"
            class="my-3 mx-3"
            height="250"
            width="165"
            @click="clickStore(item.id)"
        >
          <v-skeleton-loader>
          <v-img
              height="165"
              width="165"
              :src="require('@/assets/store.jpeg')"
          ></v-img>
          <v-card-subtitle style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
            <b>{{item.name}}</b>
          </v-card-subtitle>
          <v-card-text>
            <v-row>
              <div class="orange--text ms-4">
                <v-icon color="orange" small>mdi-heart</v-icon>
                {{ item.favoriteCounts }}
              </div>
              <div class="grey--text ms-4">
                <v-icon small>mdi-map-marker</v-icon>
                {{item.distance}}
              </div>
            </v-row>
          </v-card-text>
          </v-skeleton-loader>
        </v-card>
      </v-slide-item>
    </v-slide-group>
  </div>
</template>

<script>
export default {
  name: "SlideStore",
  props:["storeList", "id"],
  methods: {
    clickStore: function(storeId) {
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