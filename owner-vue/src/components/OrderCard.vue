<template>
  <v-card>
    <v-toolbar elevation="1" dense>
      <v-toolbar-title>{{ userName }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn outlined color="grey grey lighten-1" small>상세보기</v-btn>
    </v-toolbar>
    <v-card-title v-if="itemNames.length == 1">
      {{ itemNames[0] }}
    </v-card-title>
    <v-card-title v-if="itemNames.length > 1">
      {{ itemNames[0] }} 외 {{ itemNames.length - 1 }}건
    </v-card-title>
    <v-card-subtitle></v-card-subtitle>
    <v-card-text>{{ orderTime }}</v-card-text>
    <v-card-actions>
      <v-row v-if="orderStatus === 'ORDER'">
        <v-col sm="6">
          <v-btn
              block depressed color="success"
              @click="placed"
          >
            주문 수령
          </v-btn>
        </v-col>
        <v-col sm="6">
          <v-btn block depressed color="error"
            @click="reject"
          >
            주문 거절
          </v-btn>
        </v-col>
      </v-row>
      <v-row v-else-if="orderStatus === 'PLACED'">
        <v-col>
          <v-btn block depressed color="primary">
            수락됨
          </v-btn>
        </v-col>
      </v-row>
      <v-row v-else-if="orderStatus === 'REJECT'">
        <v-col>
          <v-btn block depressed color="blue-grey" class="white--text">
            거절됨
          </v-btn>
        </v-col>
      </v-row>
    </v-card-actions>
  </v-card>
</template>

<script>
import orderApi from "../api/order";

export default {
  name: "OrderCard",
  data: function() {
    return {

    };
  },
  props: {
    id: Number,
    userName: String,
    itemNames: [],
    orderTime: String,
    orderStatus: String
  },
  methods: {
    placed: async function() {
      try {
        await orderApi.patchOrder(this.id, 'PLACED');
        this.$emit("placed");
        alert("해당 주문이 수락 되었습니다.");
      } catch(error) {
        console.log(error);
      }
    },
    reject: async function() {
      try {
        await orderApi.patchOrder(this.id, 'REJECT');
        this.$emit("reject");
        alert("해당 주문이 거절 되었습니다.");

      } catch(error) {
        console.log(error);
      }
    }
  }
}
</script>

<style scoped>

</style>