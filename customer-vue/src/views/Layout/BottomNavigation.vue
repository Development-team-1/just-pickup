<template>
  <v-bottom-navigation
      grow
      :value="value"
      color="primary"
  >
    <v-btn>
      <span>홈</span>
      <v-icon>mdi-home-outline</v-icon>
    </v-btn>
    <v-btn>
      <span>검색</span>
      <v-icon>mdi-magnify</v-icon>
    </v-btn>
    <v-btn>
      <span>즐겨찾기</span>
      <v-icon>mdi-cards-heart-outline</v-icon>
    </v-btn>
    <v-btn>
      <span>주문내역</span>
      <v-icon>mdi-clipboard-check-outline</v-icon>
    </v-btn>
    <v-btn>
      <span>마이페이지</span>
      <v-icon>mdi-account-outline</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: "BottomNavigation",
  data: function() {
    return {
      value: 0
    }
  }
}
</script>

<style scoped>

</style>