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
            :order-id="card.orderId"
            :userName="card.userName"
            :itemNames="card.itemNames"
            :orderTime="card.orderTime"
            :orderStatus="card.orderStatus"
        >
        </order-card>
      </v-col>
    </v-row>

    <br><br><br>
    <v-row justify="center" v-if="showButton">
      <v-btn rounded outlined color="primary"
             @click="more">더보기</v-btn>
    </v-row>
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
      showButton: false
    }
  },
  methods: {
    search: function() {
      this.cards = [];
      this.lastOrderId = null;
      OrderApi.requestOrder(this.date, this.lastOrderId)
        .then( (response) => {
          this.renderCard(response.data);
        })
        .catch( (error) => {
          console.log(error);
        })
    },
    renderCard: function (json) {
      const orders = json.data;
      const size = orders.length;

      if (size === 0) {
        alert("검색 데이터가 없습니다.");
        this.showButton = false;
      } else {
        this.showButton = true;
      }

      orders.forEach( (order, index) => {
        if (index === (size - 1)) {
          this.lastOrderId = order.orderId;
        }

        let orderItemNames = []
        order.orderItemResponses.forEach( (orderItem) => {
          orderItemNames.push(orderItem.itemId);
        })

        this.cards.push({
          orderId: order.orderId,
          userName: order.orderId,
          itemNames: orderItemNames,
          orderTime: order.orderTime,
          orderStatus: order.orderStatus
        })
      });
    },
    inputDate: function(value) {
      this.date = value;
    },
    more: function() {
      OrderApi.requestOrder(this.date, this.lastOrderId)
          .then( (response) => {
            this.renderCard(response.data);
          })
          .catch( (error) => {
            console.log(error);
          })
    }
  }
}
</script>

<style scoped>

</style>