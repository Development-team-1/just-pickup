<template>
  <v-app-bar
      app
      dense
      color="white"
      elevation="1"
      hide-on-scroll
      absolute
  >
    <v-app-bar-nav-icon>
      <v-icon>mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>
    <v-spacer></v-spacer>
    <v-toolbar-title>
      <b>{{store.name}}</b>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn icon>
      <v-icon>mdi-magnify</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
export default {
  name: "StoreNavigation",
  props: ["store"],
}
</script>

<style scoped>
</style>