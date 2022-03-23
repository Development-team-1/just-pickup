<template>
  <v-dialog
      v-model="propDialog"
      scrollable
      width="1000px"
  >
    <template v-slot:activator="{on, attrs}">
      <v-btn outlined color="grey grey lighten-1" small
             @click="clickDetail"
             v-bind="attrs"
             v-on="on"
      >
        상세보기
      </v-btn>
    </template>

    <v-card>
      <v-toolbar>
        <span class="text-h5">주문 상세내역</span>
      </v-toolbar>
      <v-card-text class="pa-12">
        <v-row>
          <v-col cols="12" sm="6">
            <h1>{{orderInfo.storeName}}</h1><br>
            <h3>{{orderId}}. {{orderInfo.orderStatus | getOrderStatusName }}</h3><br>
            주문일시: {{ orderInfo.orderTime }}<br>
            주 문 자: {{ user.name }}<br>
            휴대번호: {{ user.phoneNumber }}<br><br><br><br>
            <v-simple-table class="no-border-table">
              <template v-slot:default>
                <tbody>
                <tr>
                  <td><span class="orange--text"><b>합계</b></span></td>
                  <td>{{ orderInfo.orderPrice | currency }}</td>
                </tr>
                <tr>
                  <td>포인트사용</td>
                  <td>0</td>
                </tr>
                <tr>
                  <td>쿠폰 할인</td>
                  <td>0</td>
                </tr>
                <tr>
                  <td><span class="orange--text"><b>결제금액</b></span></td>
                  <td><b>{{ orderInfo.orderPrice | currency }}</b></td>
                </tr>
                </tbody>
              </template>
            </v-simple-table>
          </v-col>
          <v-col cols="12" sm="6">
            <v-simple-table class="no-border-table">
              <template v-slot:default>
                <thead>
                <tr>
                  <th class="text-left">메뉴</th>
                  <th class="text-left">수량</th>
                  <th class="text-left">금액</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="orderItem in orderItems" :key="orderItem.id" class="">
                  <td>
                    {{orderItem.name}}<br>
                    <span class="grey--text" v-if="orderItem.options.length > 0">ㄴ {{orderItem.options}}</span>
                  </td>
                  <td>{{orderItem.count}}</td>
                  <td>{{orderItem.totalPrice | currency}}</td>
                </tr>
                </tbody>
              </template>
            </v-simple-table>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            color="grey"
            outlined
            bold
            @click="propDialog = false"
        >
          닫기
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>

  </v-dialog>
</template>

<script>
import orderApi from "../api/order";

export default {
  name: "OrderDetailDialog",
  props: ["dialog", "orderId"],
  watch: {
    dialog: function() {
      this.propDialog = !this.propDialog
    }
  },
  data: function() {
    return {
      propDialog: false,
      user: {},
      orderInfo: {},
      orderItems: []
    };
  },
  methods: {
    clickDetail: async function() {
      this.user = {};
      this.orderInfo = {};
      this.orderItems = [];
      const response = await orderApi.getOrderDetail(this.orderId);
      this.render(response.data);
    },
    render(json) {
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