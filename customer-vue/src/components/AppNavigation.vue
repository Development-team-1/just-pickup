<template>
  <v-app-bar
      app
      dense
      color="white"
      elevation="1"

  >
    <v-app-bar-nav-icon @click="$router.back()">
      <v-icon>mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>

    <v-spacer></v-spacer>
    <v-toolbar-title style="position: absolute; left: 50%; transform:translateX(-50%);">
      <v-img :src="require('@/assets/just-logo.png')"></v-img>
    </v-toolbar-title>
    <v-spacer></v-spacer>

      <v-btn
          color="white"
          elevation="0"
          @click="goNotification"
      >
        <v-badge
          :content="notificationCounts"
          :value="notificationCounts"
          color="orange"
          overlap
        >
            <v-icon>mdi-bell-outline</v-icon>
        </v-badge>
      </v-btn>
    <v-btn
        color="white"
        elevation="0"
        @click="logout"
    >
        <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import authApi from "@/api/auth";

export default {
  name: "AppNavigation",
  props: ["notificationCounts"],
  methods: {
    goNotification: function() {
      this.$router.push('/notification');
    },
    logout: function () {
      if(confirm("로그아웃하시겠습니까?")){
        authApi.logout();

      }
    }
  }
}
</script>

<style scoped>
</style>