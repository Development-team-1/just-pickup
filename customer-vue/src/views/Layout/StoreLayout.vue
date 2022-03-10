<template>
  <v-app>
    <store-navigation
        v-bind:store="store">
    </store-navigation>
    <v-main>
      <v-container class="px-8">
        <router-view
            v-on:getStoreId="renderNavigation">
        </router-view>
      </v-container>
    </v-main>
    <bottom-navigation></bottom-navigation>
  </v-app>
</template>

<script>
import StoreNavigation from "../../components/StoreNavigation.vue";
import BottomNavigation from "../../components/BottomNavigation.vue";

import storeApi from "../../api/store";

export default {
  name: "StoreLayout",
  components: {
    'store-navigation': StoreNavigation,
    "bottom-navigation": BottomNavigation
  },
  data: () => ({
    store: {
      id: '',
      name: ''
    }
  }),
  methods: {
    renderNavigation: function(storeId) {
      this.store.id = storeId;
      this.getStore();
    },
    getStore: async function() {
      if (!this.store.id) {
        alert("매장 고유번호가 없습니다. 잠시후에 시도해주세요.");
        return;
      }

      const response = await storeApi.requestStore(this.store.id);
      this.store = response.data.data;
    }
  }
}
</script>

<style scoped>
  .container {
    max-width: 768px;
    background-color: white;
    height: 100%;
  }
  main {
    background-color: #f2f2f2!important;
  }
</style>