<template>
  <v-app>
    <app-navigation
      v-bind:notificationCounts="notificationCounts"></app-navigation>
    <v-main>
      <v-container class="px-8 py-8">
        <router-view
          v-on:plusCount="notificationCounts++"
          v-on:minusCount="notificationCounts--"></router-view>
      </v-container>
    </v-main>
    <bottom-navigation></bottom-navigation>
  </v-app>
</template>

<script>
import AppNavigation from "../../components/AppNavigation.vue";
import BottomNavigation from "../../components/BottomNavigation.vue";
import notificationApi from "../../api/notification";

export default {
  name: "HomeLayout",
  components: {
    'app-navigation': AppNavigation,
    "bottom-navigation": BottomNavigation
  },
  mounted() {
    this.searchNotificationCounts();
  },
  data: function() {
    return {
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
  .container {
    max-width: 768px;
    background-color: white;
    height: 100%;
  }
  main {
    background-color: #f2f2f2!important;
  }
</style>