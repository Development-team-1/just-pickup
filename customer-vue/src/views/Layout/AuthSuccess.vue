<template>
  <v-app>
    <div>인증에 성공하였습니다.</div>
  </v-app>
</template>

<script>

export default {
  name: "AuthSuccess",
  components: {
  },mounted() {
    opener.document.location.href=process.env.VUE_APP_BASEURL;
    window.close();
  }
}
</script>

<style scoped>
</style>