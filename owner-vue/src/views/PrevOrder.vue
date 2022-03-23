<template>
  <div class="dashboard">
    <v-subheader class="py-0 d-flex justify-space-between rounded-lg">
      <h3>지난 주문</h3>
      <v-btn color="success" v-on:click="search">
        검색
      </v-btn>
    </v-subheader>
    <br>

    <v-row>
      <date-picker :label=" '시작일' " @inputDate="inputStartDate"></date-picker>
      <date-picker :label=" '종료일' " @inputDate="inputEndDate"></date-picker>
    </v-row>

    <v-row>
      <div class="subtitle-1">{{startDate}} ~ {{endDate}} 내역</div>
    </v-row>
    <br>

    <v-data-table
        :headers="headers"
        :items="orders"
        :items-per-page="itemsPerPage"
        hide-default-footer
        class="elevation-1"
    >
      <template v-slot:item.detail="{ item }">
        <order-detail-dialog
            :dialog="false"
            :orderId="item.orderId"
        ></order-detail-dialog>
      </template>
    </v-data-table>
    <div class="text-center pt-2">
      <v-pagination
          v-model="page"
          :length="pageCount"
          :total-visible="totalVisible"
      ></v-pagination>
    </div>
  </div>
</template>

<script>
import orderApi from '../api/order.js';

import DatePicker from "@/components/DatePicker";
import OrderDetailDialog from "@/components/OrderDetailDialog";

const moment = require('moment');

export default {
  name: "PrevOrder",
  components: {
    'date-picker': DatePicker,
    "order-detail-dialog": OrderDetailDialog
  },
  mounted: function() {
    this.search();
  },
  watch: {
    "page": function () {
      this.search();
    }
  },
  data: function() {
    return {
      // date
      startDate: '',
      endDate: '',

      // pagination
      page: 1,
      pageCount: 1,
      itemsPerPage: 10,
      totalVisible: 10,

      // data table
      headers: [
        {
          text: '주문번호',
          align: 'start',
          sortable: false,
          value: 'orderId',
        },
        { text: '주문상태', value: 'orderStatus' },
        { text: '주문시간', value: 'orderTime' },
        { text: '주문상품', value: 'orderItemNames' },
        { text: '결제금액', value: 'orderPrice' },
        { text: '닉네임', value: 'userName' },
        { text: '상세보기', value: 'detail', sortable: false}
      ],
      orders: [],
    }
  },
  methods: {
    search: function() {
      if(!this.checkDate()) return;

      orderApi.requestPrevOrder(this.startDate, this.endDate, this.page - 1)
          .then( (response) => {
            this.renderList(response.data);
          })
          .catch( (error) => {
            console.log(error);
          });
    },
    renderList: function(json) {
      const orders = json.data.orders;

      this.orders = [];
      orders.forEach(order => {

        this.orders.push({
          orderId: order.orderId,
          orderStatus: this.$options.filters.getOrderStatusName(order.orderStatus),
          orderTime: order.orderTime,
          orderItemNames: this.getOrderItemName(order.orderItems),
          orderPrice: this.$options.filters.currency(order.orderPrice),
          userName: order.userName
        });
      })

      // pagination setting
      const page = json.data.page;
      this.page = page.startPage + 1;
      this.pageCount = page.totalPage;
    },
    checkDate: function() {
      if (!this.startDate || !this.endDate) {
        alert("시작일과 종료일을 입력해주세요.");
        return false;
      }

      if (moment(this.startDate).isAfter(this.endDate)) {
        alert("시작일은 종료일보다 클 수 없습니다.");
        return false;
      }

      return true;
    },
    inputStartDate: function(value) {
      this.startDate = value;
    },
    inputEndDate: function(value) {
      this.endDate = value;
    },
    getOrderItemName: function(orderItems) {
      const orderItemLength = orderItems.length;
      const orderItemName = orderItems[0].orderItemName;
      if (orderItemLength == 1) {
        return orderItemName;
      } else if (orderItemLength > 1) {
        return orderItemName + " 외 " + (orderItemLength - 1) + "건";
      }
    }
  }
}
</script>

<style scoped>
</style>