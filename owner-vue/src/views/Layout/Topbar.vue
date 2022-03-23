<template>
  <v-app-bar app elevate-on-scroll elevation="3" color="white">
    <v-app-bar-nav-icon @click="$emit('drawerEvent')"></v-app-bar-nav-icon>
    <v-spacer />
    <v-spacer />
    <v-menu offset-y>
      <template v-slot:activator="{ attrs, on }">
        <span
          class="mx-5 mr-10"
          style="cursor: pointer"
          v-bind="attrs"
          v-on="on"
        >
          <v-btn @click="goNotification"
                 elevation="0"
                 color="white"
          >
            <v-badge :content="notificationCounts"
                     :value="notificationCounts"
                     color="red" offset-y="10" offset-x="10">
              <v-icon>mdi-bell</v-icon>
            </v-badge>
          </v-btn>
        </span>
      </template>
    </v-menu>
    <v-menu offset-y>
      <template v-slot:activator="{ attrs, on }">
        <span style="cursor: pointer" v-bind="attrs" v-on="on">
          <v-chip link>
            <v-badge dot bottom color="green" offset-y="10" offset-x="10">
              <v-avatar size="40">
                <v-icon>mdi-account-circle</v-icon>
              </v-avatar>
            </v-badge>
            <span class="ml-3">{{ userInfo.name }}</span>
          </v-chip>
        </span>
      </template>
      <v-list width="250" class="py-0">
        <v-list-item two-line>
          <v-list-item-avatar>
            <v-icon>mdi-account-circle</v-icon>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title>{{ userInfo.name }}</v-list-item-title>
            <v-list-item-subtitle>Logged In</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-divider />
        <v-list-item link @click="logout">
          <v-list-item-icon>
            <v-icon>mdi-logout</v-icon>
          </v-list-item-icon>
          <v-list-item-title>
            Logout
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script>
import authApi from "../../api/auth";

export default {
  name: "Topbar",
  props: ["notificationCounts","userInfo"],
  methods: {

    logout: async function() {
      await authApi.logout();
    },
    goNotification: function() {
      this.$router.push("/notification");
    },

  }
};
</script>

<style scoped></style>
