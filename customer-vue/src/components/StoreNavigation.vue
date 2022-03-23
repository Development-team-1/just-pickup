<template>
  <v-app-bar
      app
      dense
      color="white"
      elevation="1"
      hide-on-scroll
      absolute
  >
    <v-app-bar-nav-icon @click="$router.back()">
      <v-icon>mdi-arrow-left</v-icon>
    </v-app-bar-nav-icon>
    <v-spacer></v-spacer>
    <v-toolbar-title style="position: absolute; left: 50%; transform:translateX(-50%);">
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
    <v-btn
        color="white"
        elevation="0"
        @click="logout"
    >
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import storeApi from "@/api/store";
import authApi from "@/api/auth";

export default {
  name: "StoreNavigation",
  props: ["store"],
  data: function() {
    return {
      storeId:-1,
      favoriteStore:{
        status:true,
        color:null,
        icon: 'mdi-heart-outline'
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
        this.favoriteStore.icon = "mdi-heart"
      }else{
        this.favoriteStore.color = null
        this.favoriteStore.icon = "mdi-heart-outline"
      }
      this.favoriteStore.status= !this.favoriteStore.status

    },
    logout: function () {
      if(confirm("로그아웃하시겠습니까?")){
        authApi.logout();

      }
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