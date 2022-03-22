<template>
  <div>
    <v-list-item three-line>
      <v-list-item-content>
        <v-list-item-title class="text-h5 mb-3">
          {{ orderInfo.storeName }}
          <div class="grey--text caption">
          </div>
        </v-list-item-title>
        <v-list-item-subtitle class="mb-5">
          {{ orderInfo.orderStatus | getOrderStatusName }}
        </v-list-item-subtitle>
        <div class="text-body-1 mb-5">
          주문번호: {{ orderInfo.id }}
        </div>
        <div class="text--primary">
          주문일시: {{ orderInfo.orderTime }}
        </div>
      </v-list-item-content>
    </v-list-item>

    <v-divider class="my-4"></v-divider>

    <v-list-item two-line
      v-for="order in orderItems" :key="order.id"
    >
      <v-list-item-content>
        <v-list-item-title class="mb-2">
          {{order.name}} {{order.count}}개
        </v-list-item-title>
        <v-list-item-subtitle v-if="order.options.length > 0">
          ㄴ {{order.options}}
        </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action>
        <v-list-item-action-text class="text-body-1">
          {{order.totalPrice | currency}}원
        </v-list-item-action-text>
      </v-list-item-action>
    </v-list-item>

    <v-divider class="my-4"></v-divider>

    <v-simple-table class="no-border-table">
      <template v-slot:default>
        <tbody>
        <tr>
          <td><span class="orange--text"><b>합계</b></span></td>
          <td class="text-right">{{orderInfo.orderPrice | currency}}</td>
        </tr>
        <tr>
          <td>포인트사용</td>
          <td class="text-right">0</td>
        </tr>
        <tr>
          <td>쿠폰 할인</td>
          <td class="text-right">0</td>
        </tr>
        <tr>
          <td><span class="orange--text"><b>결제금액</b></span></td>
          <td class="text-right"><b>{{orderInfo.orderPrice | currency}}</b></td>
        </tr>
        </tbody>
      </template>
    </v-simple-table>

  </div>
</template>

<script>
import orderApi from "../api/order";

export default {
  name: "OrderDetail",
  props: ["orderId"],
  data: function() {
    return {
      user: {},
      orderInfo: {},
      orderItems: []
    };
  },
  mounted() {
    this.search();
  },
  methods: {
    search: async function() {
      const response = await orderApi.getOrderDetail(this.orderId);
      this.render(response.data);
    },
    render: function(json) {
      const orderDetail = json.data;

      const orderUser = orderDetail.user;
      this.user = {
        id: orderUser.id,
        name: orderUser.name,
        phoneNumber: orderUser.phoneNumber
      };

      this.orderInfo = {
        id: orderDetail.id,
        orderTime: orderDetail.orderTime,
        orderPrice: orderDetail.orderPrice,
        orderStatus: orderDetail.orderStatus,
        storeName: orderDetail.storeName
      };

      const orderItems = orderDetail.orderItems;
      orderItems.forEach(orderItem => {

        let orderItemOptions = []
        orderItem.options.forEach(option => {
          const optionTypeName = option.optionType == "REQUIRED" ? "필수" : "기타";
          orderItemOptions.push(option.name + "(" + optionTypeName + ")");
        });

        this.orderItems.push({
          id: orderItem.id,
          itemId: orderItem.itemId,
          totalPrice: orderItem.totalPrice,
          count: orderItem.count,
          name: orderItem.name,
          options: orderItemOptions.join(",")
        });
      });

    }
  }
}
</script>

<style scoped>
  .no-border-table tbody tr td {
    border-bottom: none !important;
  }
</style>