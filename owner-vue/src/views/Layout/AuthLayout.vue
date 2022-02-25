<template>
  <v-app>
    <v-app-bar app>Just Pickup - 점주용 페이지</v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "AuthLayout"
}
</script>

<style scoped>

</style>