<template>
  <div >
    <v-slide-group
      show-arrows="desktop"
    >
      <v-slide-item
          :class="storeList.isActive"
          v-for="n in 3"
          :key="n"
      >
        <v-skeleton-loader
            class="my-3 mx-3"
            elevation="7"
            height="250"
            width="165"
            type="card"
            :key="n"
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
        >
          <v-skeleton-loader>
          <v-img
              height="165"
              width="165"
              src="https://cdn.vuetifyjs.com/images/cards/cooking.png"
          ></v-img>
          <v-card-subtitle style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">{{item.name}}</v-card-subtitle>
          <v-card-text>
            거리 : {{item.distance}}
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
  props:{
    storeList:Array,
  },

}
</script>

<style scoped>

</style>