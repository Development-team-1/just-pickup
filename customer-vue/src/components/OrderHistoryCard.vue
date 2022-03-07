<template>
  <v-card
      class="mx-auto mb-5"
      outlined

  >
    <v-list-item three-line>
      <v-list-item-content>
        <v-list-item-title class="text-h5 mb-3">
          {{ card.storeName }}
          <div class="grey--text caption">
            {{ card.orderTime }}
          </div>
        </v-list-item-title>
        <v-list-item-subtitle class="mb-5">
          {{ card.orderStatus }}
        </v-list-item-subtitle>
        <div class="text-body-1 mb-5">
          {{ card.orderItemNames }}
        </div>
        <div class="text--primary">
          합계 : <b>{{ card.orderPrice }}원</b>
        </div>
      </v-list-item-content>

      <v-list-item-avatar
          tile
          size="100"
      >
        <v-img src="https://cdn.vuetifyjs.com/images/cards/halcyon.png"></v-img>
      </v-list-item-avatar>

    </v-list-item>

    <v-card-actions class="pb-1">
      <v-btn block color="primary">재주문하기</v-btn>
    </v-card-actions>

    <v-card-actions class="pt-1">
      <v-btn block outlined color="primary">자세히보기</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "OrderHistoryCard",
  props: {
    card: {
      storeName: String,
      orderTime: String,
      orderStatus: String,
      orderItemNames: String,
      orderPrice: String
    }
  }
}
</script>

<style scoped>

</style>