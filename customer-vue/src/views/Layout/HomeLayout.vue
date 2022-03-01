<template>
  <div id="page">
    <top-bar></top-bar>
    <footer-bar></footer-bar>
    <div class="page-content header-clear-medium">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import TopBar from "./TopBar.vue";
import FooterBar from "./FooterBar.vue";

export default {
  name: "HomeLayout",
  components: {
    'top-bar': TopBar,
    'footer-bar': FooterBar
  }
}
</script>

<style scoped>

</style>