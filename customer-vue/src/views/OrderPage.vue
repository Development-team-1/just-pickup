<template>
  <div>
    <v-row>
      <v-col>
        <div class="text-h4" style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">{{ orderData.storeName }}</div>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        v-for=" orderItem in orderData.orderItemDtoList"
        :key = "orderItem.id"
      >
        <v-card
            class="mx-auto mb-5"
            outlined
        >
          <v-list-item three-line>
            <v-list-item-content>
              <v-list-item-title class="text-h5 mb-3">
                {{ orderItem.itemName }}
              </v-list-item-title>
              <v-list-item-subtitle class="mb-5">
               수량 : {{ orderItem.count| currency }}
              </v-list-item-subtitle>
              <div class="text-body-1 mb-5" style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
                {{ orderItem.orderItemOptionDtoList   ?
                          orderItem.orderItemOptionDtoList.map(x=>x.name).join(', ')
                          : null}} &nbsp;
              </div>
              <div class="text--primary">
                합계 : <b> {{ orderItem.count * orderItem.price | currency}} 원</b>
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
            <v-btn block color="warning" @click="deleteOrderItem(orderItem)">삭제하기</v-btn>
          </v-card-actions>

        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <div> 합계 : {{orderData.orderPrice | currency}} 원</div>
      </v-col>
    </v-row>
        <v-btn
            block
            @click="saveOrder"
            color="primary"
        >주문하기</v-btn>
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
        // storeName:Number,
        // orderItemDtoList:[{
        //   name:Number,
        //   itemOptionIds:Array,
        //   price:Number,
        //   count:Number,
        // }],
        // orderPrice:Number,
      },
    }
  },
  methods:{
    saveOrder: function(){

      orderApi.saveOrder()
          .then(()=>{
            this.$emit('plusCount')
            alert('주문되었습니다.')
            this.$router.push("/history")
          })
          .catch(error=>{
            alert("문제가 발생하였습니다. 다시 시도해보세요.")
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
            this.$router.back();
            console.log(error.response)
          })
    },
    deleteOrderItem: function(orderItem){
      var vm = this
      const deleteOrderItemId = orderItem.id
      console.log(deleteOrderItemId)
      orderApi.deleteOrderItem(deleteOrderItemId)
          .then(response=>{
            console.log(response)
            alert(response.data.data+"삭제되었습니다.")
            vm.orderData.orderPrice -=orderItem.price* orderItem.count
            vm.orderData.orderItemDtoList.splice(
                vm.orderData.orderItemDtoList.indexOf(orderItem),1
            )
            if(vm.orderData.orderItemDtoList.length ==0)
              this.$router.back()
          })
          .catch(error=>{
            alert("문제가 발생하였습니다.")
            console.log(error.response)
          })

    },
  }
}
</script>

<style scoped>

</style>