<template>
  <v-app id="inspire">
    <side-bar :drawer="drawer" @drawerEvent="drawer = !drawer"></side-bar>
    <top-bar @drawerEvent="drawer = !drawer"
              :notificationCounts="notificationCounts"/>
    <v-main style="background: #f5f5f540">
      <v-container class="py-8, px-6" fluid>
        <router-view
            v-on:plusCount="notificationCounts++"
            v-on:minusCount="notificationCounts--"
        ></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Sidebar from './Sidebar.vue'
import Topbar from "./Topbar.vue";
import notificationApi from "@/api/notification";

export default {
  name: "DashboardLayout",
  components: {
    'side-bar': Sidebar,
    'top-bar': Topbar
  },
  mounted() {
    this.searchNotificationCounts();
  },
  data: function() {
    return {
      drawer: true,
      notificationCounts: 0
    }
  },
  methods: {
    searchNotificationCounts: async function() {
      const response = await notificationApi.countsNotification();
      this.notificationCounts = response.data.data;
    }
  }
}
</script>

<style scoped>

</style>