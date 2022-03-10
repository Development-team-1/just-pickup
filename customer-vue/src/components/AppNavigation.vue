<template>
  <v-app-bar
      app
      dense
      color="white"
      elevation="1"

  >
    <v-app-bar-nav-icon>
      <v-icon>mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>
    <v-spacer></v-spacer>
    <v-toolbar-title>
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
  </v-app-bar>
</template>

<script>
export default {
  name: "AppNavigation",
  props: ["notificationCounts"],
  methods: {
    goNotification: function() {
      this.$router.push('/notification');
    }
  }
}
</script>

<style scoped>
</style>