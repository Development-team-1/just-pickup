<template>
  <div>

    <order-history-card
        v-for="card in cards"
        v-bind:key="card.orderId"
        :card="card"
    ></order-history-card>

    <br>

    <v-row justify="center" v-if="hasNext">
      <v-btn @click="more"
             elevation="2"
             color="blue-grey"
             class="ma-2 white--text"
      >
        <b>더보기</b>
        <v-icon>mdi-chevron-down</v-icon>
      </v-btn>
    </v-row>

    <v-overlay :value="isLoading">
      <v-progress-circular
          indeterminate
          size="64"
      ></v-progress-circular>
    </v-overlay>
  </div>
</template>

<script>
import orderApi from "../api/order.js"

import OrderHistoryCard from "../components/OrderHistoryCard";

export default {
  name: "OrderHistory",
  components: {OrderHistoryCard},
  mounted() {
    this.search();
  },
  data: function() {
    return {
      isLoading: true,
      page: 0,
      cards: [],
      hasNext: false
    }
  },
  methods: {
    search: async function() {
      this.isLoading = true;
      try {
        this.page = 0;
        const response = await orderApi.requestOrderHistory(this.page);
        this.cards = [];
        this.renderCard(response.data);
      } catch (error) {
        console.log(error);
      }
      this.isLoading = false;
    },
    more: async function() {
      this.isLoading = true;
      try {
        this.page += 1;
        const response = await orderApi.requestOrderHistory(this.page);
        this.renderCard(response.data)
      } catch (error) {
        console.log(error);
      }
      this.isLoading = false;
    },
    renderCard: function(json) {
      const data = json.data;

      this.hasNext = data.hasNext;
      const orders = data.orders;

      orders.forEach( (order) => {
        let orderItemNames = [];
        order.orderItems.forEach(orderItem => {
          orderItemNames.push(orderItem.orderItemName);
        })

        this.cards.push({
          orderId: order.orderId,
          orderTime: order.orderTime,
          storeName: order.storeName,
          orderPrice: order.orderPrice,
          orderStatus: order.orderStatus,
          orderItemNames: orderItemNames.join(", ")
        })
      });
    }
  }
}
</script>

<style scoped>

</style>