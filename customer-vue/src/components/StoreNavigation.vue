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
    <v-btn
      icon
      @click="markStar()"
    >
      <v-icon
        :color="favoriteStore.color"
      >{{ favoriteStore.icon }}</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import storeApi from "@/api/store";

export default {
  name: "StoreNavigation",
  props: ["store"],
  data: function() {
    return {
      storeId:-1,
      favoriteStore:{
        status:true,
        color:null,
        icon: 'mdi-star-outline'
      }
    }
  },
  methods:{

    markStar:function () {
      this.changeStarStatus()
      var vm = this
      storeApi.markStar(this.storeId)
          .then(()=>{
            vm.favoriteStore.status?
                alert('즐겨찾는 매장에서 제외되었습니다.') : alert('즐겨찾는 매장에 등록되었습니다.')
          })
    },
    getFavoriteStoreByStoreId: function (storeId){
      var vm = this
      storeApi.getFavoriteStoreByStoreId(storeId)
          .then((response)=>{
            if(response.data.data.exist) vm.changeStarStatus()
          })
    },
    changeStarStatus(){

      if(this.favoriteStore.status){
        this.favoriteStore.color = "rgb(255, 152, 0)"
        this.favoriteStore.icon = "mdi-star"
      }else{
        this.favoriteStore.color = null
        this.favoriteStore.icon = "mdi-star-outline"
      }
      this.favoriteStore.status= !this.favoriteStore.status

    }
  },
  mounted() {
    console.log(this.$route.params)
    this.storeId =this.$route.params.storeId
    this.getFavoriteStoreByStoreId(this.storeId)
  }
}
</script>

<style scoped>
</style>