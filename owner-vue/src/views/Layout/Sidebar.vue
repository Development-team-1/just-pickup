<template>
  <!-- <v-navigation-drawer v-model="drawer" app> -->
  <v-navigation-drawer
      v-model="drawer"
      app>
    <v-img
      height="140"
      class="pa-4"
      src="https://preview.pixlr.com/images/800wm/1439/2/1439104804.jpg"

    >
      <div class="text-center">
        <v-avatar class="mb-4" color="grey darken-1" size="64">
          <v-img
            aspect-ratio="30"
            src="https://yt3.ggpht.com/esazPAO03T0f0vKdByJvkDy6MSwjyG5f-c_2S2CJapszQ3KPQyZarpoqvgv0Us0atUbILytj=s88-c-k-c0x00ffffff-no-rj"
          />
        </v-avatar>
        <h2 class="white--text">Web Burden</h2>
      </div>
    </v-img>
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
        {name: "지난 주문", url: "/prev-order", icon: "mdi-home-outline"},
        {name: "카테고리", url: "/category", icon: "mdi-magnify"},
        {name: "주문", url: "/order", icon: "mdi-cards-heart"},
        {name: "메뉴 관리", url: "/menu", icon: "mdi-cards-heart-outline"},
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
