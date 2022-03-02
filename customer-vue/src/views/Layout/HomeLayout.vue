<template>
  <v-app>
    <app-navigation></app-navigation>
    <v-main style="background-color: #f2f2f2!important;">
      <v-container class="px-8 py-8" style="background-color: white; height: 100%">
        <router-view></router-view>
      </v-container>
    </v-main>
    <bottom-navigation></bottom-navigation>
  </v-app>
</template>

<script>
import AppNavigation from "../../components/AppNavigation.vue";
import BottomNavigation from "../../components/BottomNavigation.vue";

export default {
  name: "HomeLayout",
  components: {
    'app-navigation': AppNavigation,
    "bottom-navigation": BottomNavigation
  }
}
</script>

<style scoped>
  .container {
    max-width: 960px;
  }
</style>