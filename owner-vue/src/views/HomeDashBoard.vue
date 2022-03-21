<template>

  <v-container fluid>
    <v-row>

      <v-col
          v-for="(item, index) in items1"
          :key="`sheet-${index}`"
          cols="12"
          sm="6"
      >
        <v-card elevation="2" class="rounded-lg">
          <v-card-text class="d-flex justify-space-between align-center">
            <div>
              <strong>{{ item.title }}</strong> <br>
              <span>00~00</span>
            </div>
            <v-avatar size="60" :color="item.color" style="border: 3px solid #444">
              <span style="color: white">{{item.amount}} </span>
            </v-avatar>
          </v-card-text>
          <v-card-actions class="d-flex justify-space-between">


          </v-card-actions>
        </v-card>
      </v-col>
      <v-col cols="12">
        <v-card>
          <v-card-title>
            <v-icon
                :color="checking ? 'red lighten-2' : 'indigo'"
                class="mr-12"
                size="64"
                @click="takePulse"
            >
              mdi-currency-usd
            </v-icon>
            판매금액
          </v-card-title>

          <v-sheet color="transparent">
            <v-sparkline
                :key="String(avg)"
                :smooth="16"
                :gradient="['#f72047', '#ffd200', '#1feaea']"
                :line-width="3"
                :value="heartbeats"
                auto-draw
                stroke-linecap="round"
            />
          </v-sheet>
        </v-card>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
import orderApi from "@/api/order";

export default {
  name: "HomeDashBoard",
  props: ['userInfo'],
  data() {
    return {
      salesAmount:{},
      bestSellItem:{},
      sellAmountAWeeks:{},
    }
  },mounted() {
    let vm = this
    orderApi.findDashboard().then(response => {
      console.log(response)
      vm.salesAmount = this.data.salesAmount
      vm.bestSellItem = this.data.bestSellItem
      vm.sellAmountAWeeks = this.data.sellAmountAWeeks
    }).catch(error =>{
      console.log(error.response)
    });
  }
}
</script>

<style scoped>

</style>