<template>
    <div >
      <v-row>
        <v-col>
          <v-img :src="require('@/assets/store.jpeg')"
                 :aspect-ratio="16/9"
          />
        </v-col>
      </v-row>
      <v-row class="mt-5" >
        <v-col  >
          <div class="text-h3" >
            {{itemData.name}}
          </div>
        </v-col>
      </v-row>
      <v-row class="mt-5" >
        <v-col class="text-h5">
            <span class="text-h5">가격</span>
        </v-col>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-col >
          <v-container>
            <div class="text-h5">{{ itemData.price }}원</div>
          </v-container>
        </v-col>
      </v-row>
      <v-divider/>
      <v-row class="mt-5">
        <v-col>
            <div class="text-h5">수량</div>
        </v-col>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-col >
          <v-container>
            <v-text-field
                readonly
                v-model="setItem.count"
                append-outer-icon="mdi-plus"
                prepend-icon="mdi-minus"
                filled
                hide-details
                @click:append-outer="addItemCount(1)"
                @click:prepend="addItemCount(-1)"
            />
          </v-container>

        </v-col>
      </v-row>
      <v-divider/>
      <v-row class="mt-5">
        <v-col>
          <div class="text-h5"> 필수 옵션</div>
          <v-radio-group v-model="setItem.requireOption">
            <v-radio
                v-for="itemOption in requireGroup"
                :key="itemOption.id"
                :label="itemOption.name"
                :value="itemOption.id"
            />
          </v-radio-group>
        </v-col>
      </v-row>
      <v-divider/>
      <v-row class="mt-5">
        <v-col>
          <div class="text-h5"> 추가 옵션</div>
            <v-checkbox
                class="shrink mr-2 mt-0"
                v-model="setItem.otherOptions"
                v-for="itemOption in otherGroup"
                :key="itemOption.id"
                :label="itemOption.name"
                :value="itemOption.id"
            />
        </v-col>
      </v-row>
      <v-btn
          block
          @click="addItem"
      >담기</v-btn>
    </div>
</template>

<script>
import storeApi from "@/api/store";
import orderApi from "@/api/order";

export default {
  name: "ItemDetail",
  async beforeMount() {
    console.log(this.$route.params)
    this.itemId = this.$route.params.itemId
    this.setItem.storeId = this.storeId;
    await this.getItemData()
  },
  computed: {
    requireGroup:function(){
      return this.parseGroup('REQUIRED')
    },
    otherGroup:function(){
      return this.parseGroup('OTHER')
    },
  },
  data: function() {
    return {
      storeId: -1,
      itemId: -1,
      itemData: {
        itemOptions:[],
      },
      setItem:{
        count:1,
        storeId:-1,
        itemId:-1,
        price:0,
        otherOptions:[],
      },
    }
  },
  methods: {
    getItemData: async function (){
      storeApi.fetchItem(this.itemId)
      .then(response=>{
        this.itemData = response.data.data
        this.setItem.itemId = this.itemData.id
        this.setItem.price = this.itemData.price
        this.storeId = this.itemData.storeId
        this.setItem.storeId = this.itemData.storeId
        this.$emit('getStoreId', this.storeId)
      })
      .catch(error=>{
        console.log(error)
        this.$router.push("/")
      })

    },
    parseGroup: function (type){
      let group= [];
      for (const groupElement of this.itemData.itemOptions) {
        if(groupElement.optionType===type) group.push(groupElement)
      }
      return group;
    },
    addItemCount: function (v){
      this.setItem.count =
          this.setItem.count+v >=1? this.setItem.count+v: 1;
    },
    addItem: function(){
      if(!this.validItem())
        return;

      orderApi.addItemToBasket(this.setItem)
          .then(response=>{
            console.log(response)
            this.$router.push("/store/"+this.storeId)
          })
          .catch(error=>{
            console.log(error.response)
          })
    },
    validItem(){
      if(this.setItem.count <= 0 || isNaN(this.setItem.count)){
        alert("수량이 잘못되었습니다.")
        this.setItem.count =1
        return false
      }
      return true

    }

  },
}
</script>

<style scoped>
</style>