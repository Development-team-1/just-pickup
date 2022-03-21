<template>
  <v-app id="inspire">
    <side-bar :drawer="drawer" @drawerEvent="drawer = !drawer" :userInfo="userInfo"/>
    <top-bar @drawerEvent="drawer = !drawer"
             :notificationCounts="notificationCounts"
             :userInfo="userInfo"
    />
    <v-main style="background: #f5f5f540">
      <v-container class="py-8, px-6" fluid>
        <router-view
            v-on:plusCount="notificationCounts++"
            v-on:minusCount="notificationCounts--"
            :userInfo="userInfo"
        ></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Sidebar from './Sidebar.vue'
import Topbar from "./Topbar.vue";
import notificationApi from "@/api/notification";
import userApi from "@/api/user";

export default {
  name: "DashboardLayout",
  components: {
    'side-bar': Sidebar,
    'top-bar': Topbar
  },
  async mounted() {
    await this.searchNotificationCounts();
    // 사용자 정보 가져오기
    this.userInfo = await this.getUserInfo();
  },
  data: function() {
    return {
      drawer: true,
      notificationCounts: 0,
      userInfo: {},
    }
  },
  methods: {
    searchNotificationCounts: async function() {
      const response = await notificationApi.countsNotification();
      this.notificationCounts = response.data.data;
    },
    getUserInfo: async function() {
      const response = await userApi.requestUserInfo();
      return response.data.data;
    },
  }
}
</script>

<style scoped>

</style>