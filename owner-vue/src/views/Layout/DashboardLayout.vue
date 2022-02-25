<template>
  <v-app id="inspire">
    <side-bar v-bind:drawer="drawer"></side-bar>
    <top-bar v-on:drawEvent="drawer = !drawer"></top-bar>
    <v-main style="background: #f5f5f540">
      <v-container class="py-8, px-6" fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Sidebar from './Sidebar.vue'
import Topbar from "./Topbar.vue";

export default {
  name: "DashboardLayout",
  components: {
    'side-bar': Sidebar,
    'top-bar': Topbar
  },
  data: function() {
    return {
      drawer: null
    }
  }
}
</script>

<style scoped>

</style>