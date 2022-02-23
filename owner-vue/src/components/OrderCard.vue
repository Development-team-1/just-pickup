<template>
  <v-card>
    <v-toolbar elevation="1" dense>
      <v-toolbar-title>{{ userName }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn outlined color="grey grey lighten-1" small>상세보기</v-btn>
    </v-toolbar>
    <v-card-title>{{ itemNames.join(", ") }}</v-card-title>
    <v-card-subtitle></v-card-subtitle>
    <v-card-text>{{ orderTime }}</v-card-text>
    <v-card-actions>
      <v-btn v-if="orderStatus === 'PENDING'"
          block depressed color="primary">
        주문 대기
      </v-btn>
      <v-btn v-else-if="orderStatus === 'PLACED'"
             block depressed color="primary">
        주문 수령
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "OrderCard",
  data: function() {
    return {

    };
  },
  props: {
    orderId: Number,
    userName: Number,
    itemNames: [],
    orderTime: String,
    orderStatus: String
  }
}
</script>

<style scoped>

</style>