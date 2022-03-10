<template>
  <div>
    <v-row>
      <v-col>
        <div class="text-h4" style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">{{ orderData.storeId }}</div>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        v-for=" orderItem in orderData._orderItemDtos"
        :key = "orderItem.itemId"
      >
        <v-card
            class="mx-auto mb-5"
            outlined
        >
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="text-h5 mb-3">
                {{ orderItem.itemId }}
              </v-list-item-title>
              <v-list-item-subtitle class="mb-5">
               수량 : {{ orderItem.count }}
              </v-list-item-subtitle>
              <div class="text-body-1 mb-5">
                {{ orderItem.itemOptionIds.join(', ')}}
              </div>
              <div class="text--primary">
                합계 : <b> {{ orderItem.count * orderItem.price }} 원</b>
              </div>
            </v-list-item-content>
            <v-list-item-avatar
                tile
                size="100"
            >
              <v-img :src="require('@/assets/store.jpeg')"></v-img>
            </v-list-item-avatar>
          </v-list-item>

          <v-card-actions class="pb-2">
            <v-btn block color="warning">삭제하기</v-btn>
          </v-card-actions>

        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <div> 합계 : {{orderData.totalPrice}} 원</div>
      </v-col>
    </v-row>
        <v-btn
            block
            @click="saveOrder"
            color="primary"
        >주문하기</v-btn>
<!--    <v-btn-->
<!--        style="position: absolute; bottom: 0;transform: translateY(-100%); width: 100%"-->
<!--        @click="orderItems"-->
<!--    >주문하기</v-btn>-->

  </div>
</template>

<script>
import orderApi from "@/api/order";
export default {
  name: "OrderPage",
  mounted() {
    this.getOrder();
  },
  data: function(){
    return {
      orderData:{
        storeId:Number,
        _orderItemDtos:[{
          itemId:Number,
          itemOptionIds:Array,
          price:Number,
          count:Number,
        }],
        totalPrice:Number,

      },
    }
  },
  computed:{
    test:function (a){

      return a;
    }
  },
  methods:{
    saveOrder: function(){

      orderApi.saveOrder()
          .then(()=>{
            alert('주문되었습니다.')
            this.$router.replace("/")
          })
          .catch(error=>{
            console.log(error)
            //this.$router.replace("/")
          })
    },
    getOrder: function(){
      orderApi.getOrder()
          .then(response=>{
            this.orderData=response.data.data
          })
          .catch(error=>{
            console.log(error)
            this.$router.replace("/")
          })
    },
  }
}
</script>

<style scoped>

</style>