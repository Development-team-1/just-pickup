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

const moment = require('moment');

export default {
  name: "PrevOrder",
  components: {
    'date-picker': DatePicker
  },
  mounted: function() {
    orderApi.requestPrevOrder(this.startDate, this.endDate, this.page - 1)
        .then( (response) => {
          this.renderList(response.data);
        })
        .catch( (error) => {
          console.log(error);
        });
  },
  watch: {
    "page": function (newPage) {
      orderApi.requestPrevOrder(this.startDate, this.endDate, newPage - 1)
          .then( (response) => {
            this.renderList(response.data);
          })
          .catch( (error) => {
            console.log(error);
            console.log(error.response.data);
          });
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
        let orderItemNames = [];
        order.orderItems.forEach(orderItem => {
          orderItemNames.push(orderItem.orderItemId);
        })

        this.orders.push({
          orderId: order.orderId,
          orderStatus: order.orderStatus,
          orderTime: order.orderTime,
          orderItemNames: orderItemNames.join(", "),
          orderPrice: order.orderPrice,
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
    }
  }
}
</script>

<style scoped>
</style>