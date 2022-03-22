<template>
  <div>
    <div id="nav">
      <v-chip-group
          mandatory
          center-active
          show-arrows
          active-class="primary--text"
          v-model="tagIndex"
      >
        <v-chip
            v-for="tag in tags"
            :key="tag"
        >
          {{ tag }}
        </v-chip>
      </v-chip-group>
    </div>

    <br>
    <div id="content">
      <div v-for="category in categories" :key="category.id">
        <h3 ref="focusTag">
          {{ category.name }}
        </h3>
        <br>
        <v-card
            class="mx-auto mb-5"
            outlined
            v-for="item in category.items"
            @click="itemDetail(item.id)"
            :key="item.id"
        >
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="text-h5 mb-3">
                {{ item.name }}
              </v-list-item-title>
              <div class="text--primary mb-5">
                {{ item.price }}원
              </div>
              <v-list-item-subtitle>
              </v-list-item-subtitle>
            </v-list-item-content>

            <v-list-item-avatar
                tile
                size="100"
            >
              <v-img :src="require('@/assets/store.jpeg')"></v-img>
            </v-list-item-avatar>
          </v-list-item>
        </v-card>
        <br>
      </div>
    </div>
    <div align="right" >
      <v-btn
          color="primary"
          dark
          right
          fab
          @click="toOrder"
      >
        <v-icon>mdi-basket</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script>
import storeApi from "../api/store";

export default {
  name: "StoreView",
  props: ["storeId"],
  mounted() {
    if (!this.storeId) {
      alert("잘못된 요청입니다.");
      return;
    }
    this.$emit('getStoreId', this.storeId)
    this.search(this.storeId);
  },
  data: () => ({
    tags: [],
    categories: [],
    tagIndex: 0
  }),
  watch: {
    tagIndex: function(newValue) {
      this.$refs.focusTag[newValue].scrollIntoView({behavior: 'smooth', block: 'center'});
    }
  },
  methods: {
    search: async function(storeId) {
      const response = await storeApi.requestCategoriesWithItem(storeId);
      this.render(response.data);
    },
    render: function(json) {
      this.categories = json.data.categories;
      this.categories.forEach(category => {
        this.tags.push(category.name);
      })
    },
    itemDetail: function (itemId) {
      this.$router.push("/item/"+itemId)
    },
    toOrder(){
      if(confirm("주문화면으로 이동할까요?")){
        this.$router.push("/order")
      }
    }
  }
}
</script>

<style scoped>
#nav {
  color: white;
  background-color: white;
  position: sticky;
  top: 0;
  z-index: 999;
}

#content {
  display: block !important;
  z-index: -1;
}
</style>