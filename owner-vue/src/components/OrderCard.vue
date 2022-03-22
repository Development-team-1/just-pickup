<template>
  <div>
    <v-card>
      <v-toolbar elevation="1" dense>
        <v-toolbar-title>{{ userName }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <order-detail-dialog
            :dialog="false"
            :orderId="this.id"
        ></order-detail-dialog>
      </v-toolbar>
      <v-card-title v-if="itemNames.length == 1">
        {{ itemNames[0] }}
      </v-card-title>
      <v-card-title v-if="itemNames.length > 1">
        {{ itemNames[0] }} 외 {{ itemNames.length - 1 }}건
      </v-card-title>
      <v-card-subtitle>{{ orderStatus | getOrderStatusName }}</v-card-subtitle>
      <v-card-text>{{ orderTime }}</v-card-text>
      <v-card-actions>
        <v-row v-if="orderStatus === 'PLACED'">
          <v-col sm="6">
            <v-btn
                block depressed color="#006A95" class="white--text"
                @click="accepted"
            >
              주문수락하기
            </v-btn>
          </v-col>
          <v-col sm="6">
            <v-btn block depressed color="#FF1400" class="white--text"
                   @click="rejected"
            >
              주문거절하기
            </v-btn>
          </v-col>
        </v-row>
        <v-row v-else-if="orderStatus === 'ACCEPTED'">
          <v-col>
            <v-btn block depressed color="#58ADA0" class="white--text"
                   @click="waiting"
            >
              픽업 요청하기
            </v-btn>
          </v-col>
        </v-row>
        <v-row v-else-if="orderStatus === 'REJECTED'">
          <v-col>
            <v-btn block disabled>
              거절됨
            </v-btn>
          </v-col>
        </v-row>
        <v-row v-else-if="orderStatus === 'WAITING'">
          <v-col>
            <v-btn block depressed color="#FF5C00" class="white--text"
                   @click="finished"
            >
              고객 수령완료하기
            </v-btn>
          </v-col>
        </v-row>
        <v-row v-else-if="orderStatus === 'FINISHED'">
          <v-col>
            <v-btn block depressed color="#F9E0AF" class="grey--text">
              픽업 완료됨
            </v-btn>
          </v-col>
        </v-row>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import orderApi from "../api/order";
import OrderDetailDialog from "@/components/OrderDetailDialog";

export default {
  name: "OrderCard",
  components: {
    "order-detail-dialog": OrderDetailDialog
  },
  data: function() {
    return {
      dialog: false
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
    accepted: async function() {
      try {
        await orderApi.patchOrder(this.id, 'ACCEPTED');
        this.$emit("accepted");
        alert("해당 주문이 수락 되었습니다.");
      } catch(error) {
        console.log(error);
      }
    },
    rejected: async function() {
      try {
        await orderApi.patchOrder(this.id, 'REJECTED');
        this.$emit("rejected");
        alert("해당 주문이 거절 되었습니다.");

      } catch(error) {
        console.log(error);
      }
    },
    waiting: async function() {
      try {
        await orderApi.patchOrder(this.id, 'WAITING');
        this.$emit("waiting");
        alert("해당 주문의 픽업이 요청 되었습니다.");

      } catch(error) {
        console.log(error);
      }
    },
    finished: async function() {
      try {
        await orderApi.patchOrder(this.id, 'FINISHED');
        this.$emit("finished");
        alert("해당 주문이 픽업완료 처리 되었습니다.");

      } catch(error) {
        console.log(error);
      }
    },
  }
}
</script>

<style scoped>

</style>