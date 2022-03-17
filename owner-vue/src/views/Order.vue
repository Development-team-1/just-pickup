<template>
  <div class="dashboard">
    <v-subheader class="py-0 d-flex justify-space-between rounded-lg">
      <h3>주문</h3>
      <v-btn color="success" @click="search">
        검색
      </v-btn>
    </v-subheader>
    <br>

    <v-row>
      <date-picker :label=" '주문일' " @inputDate="inputDate"></date-picker>
    </v-row>

    <v-row>
      <v-col v-for="card in cards" cols="4" :key="card.orderId">
        <order-card
            :id="card.orderId"
            :userName="card.userName"
            :itemNames="card.itemNames"
            :orderTime="card.orderTime"
            :orderStatus="card.orderStatus"
            @accepted="card.orderStatus = 'ACCEPTED'"
            @rejected="card.orderStatus = 'REJECTED'"
            @waiting="card.orderStatus = 'WAITING'"
            @finished="card.orderStatus = 'FINISHED'"
        >
        </order-card>
      </v-col>
    </v-row>

    <br><br>
    <v-row justify="center" v-if="hasNext">
      <v-btn color="#006A95" outlined
             @click="more">더보기</v-btn>
    </v-row>
    <br>
  </div>
</template>

<script>
import OrderApi from '../api/order.js';

import OrderCard from '../components/OrderCard.vue'
import DatePicker from "@/components/DatePicker";

export default {
  name: "Order",
  components: {
    OrderCard,
    DatePicker
  },
  mounted() {
    this.search();
  },
  data: () => {
    return {
      date: '',
      cards: [],
      lastOrderId: null,
      hasNext: false
    }
  },
  methods: {
    search: async function() {
      this.cards = [];
      this.lastOrderId = null;

      const response = await OrderApi.requestOrder(this.date, this.lastOrderId);

      this.renderCard(response.data)
    },
    renderCard: function (json) {
      console.log(json);
      const orders = json.data.orders;
      const size = orders.length;

      this.hasNext = json.data.hasNext;

      orders.forEach( (order, index) => {
        if (index === (size - 1)) {
          this.lastOrderId = order.id;
        }

        let orderItemNames = [];
        order.orderItems.forEach( (orderItem) => {
          orderItemNames.push(orderItem.itemName);
        })

        this.cards.push({
          orderId: order.id,
          userName: order.userName,
          itemNames: orderItemNames,
          orderTime: order.orderTime,
          orderStatus: order.orderStatus
        })
      });
    },
    inputDate: function(value) {
      this.date = value;
    },
    more: async function() {
      const response = await OrderApi.requestOrder(this.date, this.lastOrderId);
      this.renderCard(response.data);
    }
  }
}
</script>

<style scoped>

</style>