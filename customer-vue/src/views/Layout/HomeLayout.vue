<template>
  <v-app>
    <app-navigation></app-navigation>
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
    <bottom-navigation></bottom-navigation>
  </v-app>
</template>

<script>
import AppNavigation from "./AppNavigation.vue";
import BottomNavigation from "./BottomNavigation.vue";

export default {
  name: "HomeLayout",
  components: {
    'app-navigation': AppNavigation,
    "bottom-navigation": BottomNavigation
  }
}
</script>

<style scoped>

</style>