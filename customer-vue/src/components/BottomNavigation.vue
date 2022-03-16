<template>
  <v-bottom-navigation
      app
      fixed
      grow
      :value="value"
      color="primary"
  >
    <v-btn v-for="(item, index) in links" v-bind:key="index" @click="clickGo(index, item.url)">
      <span>{{ item.name }}</span>
      <v-icon>{{ item.icon }}</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: "BottomNavigation",
  data: function() {
    return {
      value: 0,
      links: [
        {name: "홈", url: "/", icon: "mdi-home-outline"},
        {name: "검색", url: "/search", icon: "mdi-magnify"},
        {name: "즐겨찾기", url: "/favorite", icon: "mdi-cards-heart-outline"},
        {name: "주문내역", url: "/history", icon: "mdi-clipboard-check-outline"},
        {name: "마이페이지", url: "/mypage", icon: "mdi-account-outline"}
      ]
    }
  },
  methods: {
    clickGo: function (index, url) {
      this.value = index;
      this.$router.push(url);
    }
  },mounted() {
    const path =this.$route.path
    this.links.forEach((link, index) => {
      if(link.url === path){
        this.value = index
      }
    })
  }
}
</script>

<style scoped>

</style>