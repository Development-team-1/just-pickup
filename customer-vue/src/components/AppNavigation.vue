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
    <v-btn icon>
      <v-icon>mdi-magnify</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
export default {
  name: "AppNavigation"
}
</script>

<style scoped>

</style>