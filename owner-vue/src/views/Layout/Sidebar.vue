<template>
  <!-- <v-navigation-drawer v-model="drawer" app> -->
  <v-navigation-drawer
      v-model="drawer"
      app>
    <v-img
      height="140"
      class="px-7 mx-7"
      :src="require('@/assets/just-logo.png')"
      contain/>
    <v-divider></v-divider>
    <v-list>
      <v-list-item v-for="(link,index) in links" :key="link.icon" link @click="clickGo(index, link.url)">
        <v-list-item-icon>
          <v-icon>{{ link.icon }}</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>{{ link.name }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: "Sidebar",
  props: ["drawer"],
  data() {
    return {
      links: [
        {name: "지난 주문", url: "/prev-order", icon: "mdi-clipboard-check-outline"},
        {name: "카테고리", url: "/category", icon: "mdi-shape-outline"},
        {name: "주문", url: "/order", icon: "mdi-order-numeric-ascending"},
        {name: "메뉴 관리", url: "/menu", icon: "mdi-cog-outline"},
      ],
    };
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
};
</script>

<style scoped></style>
