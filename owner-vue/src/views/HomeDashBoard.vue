<template>

  <v-container fluid>
    <v-row>

      <v-col
          cols="12"
          sm="6"
      >
        <v-card elevation="2" class="rounded-lg" style="border-left: 5px solid #f69653">
          <v-card-title primary-title>
            <div>
              <div class="grey--text">{{ salesAmount.title }}</div>
            </div>
            <v-card-text class="d-flex justify-space-between align-center">
              <h3 class="headline">{{salesAmount.data | currency}}원</h3>
              <v-avatar size="60"  >
                <v-icon
                    :color="salesAmount.color"
                    size="64"
                >
                  mdi-currency-usd
                </v-icon>
              </v-avatar>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>

      <v-col
          cols="12"
          sm="6"
      >
        <v-card elevation="2" class="rounded-lg" style="border-left: 5px solid #ed6856">
          <v-card-title primary-title>
            <div>
              <div class="grey--text">{{ bestSellItem.title }}</div>
            </div>
            <v-card-text class="d-flex justify-space-between align-center">
              <h3 class="headline">{{bestSellItem.data.itemName}} {{ bestSellItem.data.sumCounts }}개</h3>
              <v-avatar size="60"  >
                <v-icon
                    :color="bestSellItem.color"
                    size="64"
                >
                  mdi-coffee
                </v-icon>
              </v-avatar>
            </v-card-text>
          </v-card-title>
        </v-card>
      </v-col>
      <v-col cols="12">
        <v-card  style="border-left: 5px solid #00a8e0">
          <v-card-title>
            <v-icon
                :color="sellAmountAWeeks.color"
                class="mr-12"
                size="64"
            >
              mdi-currency-usd
            </v-icon>
            {{ sellAmountAWeeks.title }}
          </v-card-title>

          <v-sheet color="transparent">
            <CustomChart
                v-if="sellAmountAWeeks.loaded"
                :chart-data="sellAmountAWeeks.data"
                :options="sellAmountAWeeks.options"/>
          </v-sheet>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
import orderApi from "@/api/order";
import CustomChart from "@/js/CustomChart";

export default {
  name: "HomeDashBoard",
  components:{
    CustomChart
  },
  props: ['userInfo'],
  data() {
    return {
      salesAmount: {
        title: "금일 판매금액",
        color: "#f69653",
        data: {}
      },
      bestSellItem: {
        title: "주간베스트 판매상품",
        color: "#ed6856",
        data: {}
      },
      sellAmountAWeeks: {
        title: "주간 판매금액 그래프",
        color: "#00a8e0",
        loaded:false,
        options:{
          responsive: true,
          maintainAspectRatio: false,


          scales: {
            xAxes: [{
              ticks:{
                fontColor : 'rgba(12, 13, 13, 1)',
                fontSize : 14
              },
            }],
            yAxes: [{
              ticks: {
                fontColor : 'rgba(12, 13, 13, 1)',
                fontSize : 14,
                userCallback:function (ele) {
                  return Number(ele).toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,") + "원"
                }
              },
            }]
          }

        },
        data: {
          labels: [],
          datasets: [

          ]
        },


      },
    }
  },
  methods: {
    async getDashboardData() {
      const response = await orderApi.findDashboard()
      this.salesAmount.data = response.data.data.salesAmount
      this.bestSellItem.data = response.data.data.bestSellItem

      var saleDataset={
        label: '일별 판매 금액',
        backgroundColor: '#00a8e0',
        borderColor: 'rgb(75, 192, 192)',
        fill: false,
        data: [],
      }
      for (const ele of response.data.data.sellAmountAWeeks) {
        this.sellAmountAWeeks.data.labels.push(ele.sellDate)
        saleDataset.data.push( ele.sellAmount)
      }
      this.sellAmountAWeeks.data.datasets.push(saleDataset)

      this.sellAmountAWeeks.loaded = true
    },
  },
  async mounted() {
     this.getDashboardData()

  }
}
</script>

<style scoped>

</style>