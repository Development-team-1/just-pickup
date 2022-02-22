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
      <v-menu
          v-model="isDuePop"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
              v-model="dueDate"
              label="시작일"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
            v-model="dueDate"
            @input="isDuePop = false"
        ></v-date-picker>
      </v-menu>

      <v-menu
          v-model="isEndPop"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
              v-model="endDate"
              label="종료일"
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
            v-model="endDate"
            @input="isEndPop = false"
        ></v-date-picker>
      </v-menu>
    </v-row>

    <v-row>
      <h1>{{ dueDate }}, {{ endDate }}</h1>
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
// import axios from 'axios';
import orderApi from '../api/order.js';

const moment = require('moment');

export default {
  name: "PrevOrder",
  data: function() {
    return {
      dueDate: moment().format('YYYY-MM-DD'),
      isDuePop: false,
      endDate: moment().format('YYYY-MM-DD'),
      isEndPop: false,

      page: 1,
      pageCount: 1,
      itemsPerPage: 10,
      totalVisible: 10,
      headers: [
        {
          text: '주문번호',
          align: 'start',
          sortable: false,
          value: 1,
        },
        { text: '주문상태', value: 'orderStatus' },
        { text: '주문시간', value: 'orderTime' },
        { text: '주문상품', value: 'orderItemNames' },
        { text: '결제금액', value: 'orderPrice' },
        { text: '닉네임', value: 'userName' },
      ],
      orders: [

      ],
    }
  },
  methods: {
    search: function() {
      if(!this.checkDate()) return;

      this.orders = [];
      orderApi.requestPrevOrder(this.dueDate, this.endDate, this.page - 1)
          .then( (response) => {
            this.renderList(response.data);
          })
          .catch( (error) => {
            console.log(error);
            console.log(error.response.data);
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
      if (!this.dueDate || !this.endDate) {
        alert("시작일과 종료일을 입력해주세요.");
        return false;
      }

      if (moment(this.dueDate).isAfter(this.endDate)) {
        alert("시작일은 종료일보다 클 수 없습니다.");
        return false;
      }

      return true;
    }
  },
  watch: {
    "page": function (newPage) {
      orderApi.requestPrevOrder(this.dueDate, this.endDate, newPage - 1)
          .then( (response) => {
            this.renderList(response.data);
          })
          .catch( (error) => {
            console.log(error);
            console.log(error.response.data);
          });
    }
  }
}
</script>

<style scoped>
</style>