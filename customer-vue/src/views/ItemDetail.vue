<template>
  <v-main>
    <v-container >
      <v-row>
        <v-col>
          <v-img :src="require('@/assets/store.jpeg')"
                 :aspect-ratio="16/9"
          />
        </v-col>
      </v-row>
      <v-row >
        <v-col >
          <v-subheader >
            {{itemData.name}}
          </v-subheader>
        </v-col>
      </v-row>
      <v-row >
        <v-col class="text-h3">
          <v-container>
            <span class="text-h3">가격</span>
          </v-container>
        </v-col>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-col >
          <v-container>
            <div class="text-h3">{{ itemData.price }}원</div>
          </v-container>
        </v-col>
      </v-row>
      <v-divider/>
      <v-row >
        <v-col >
          <v-container>
            <div class="text-h3">수량</div>
          </v-container>
        </v-col>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-col >
          <v-container>
            <v-text-field
                v-model="setItem.count"
                append-outer-icon="mdi-plus"
                prepend-icon="mdi-minus"
                filled
                label="수량"
                type="number"
                @click:append-outer="addItemCount(1)"
                @click:prepend="addItemCount(-1)"
            />
          </v-container>

        </v-col>
      </v-row>
      <v-divider/>
      <v-row>
        <v-col>
          <v-container>
          <div class="text-h3"> 필수 옵션</div>
          <v-radio-group v-model="setItem.requireOption">
            <v-radio
                v-for="itemOption in requireGroup"
                :key="itemOption.id"
                :label="itemOption.name"
                :value="itemOption.id"
            />
          </v-radio-group>
          </v-container>
        </v-col>
      </v-row>
      <v-divider/>
      <v-row>
        <v-col>
          <v-container>
          <div class="text-h3"> 추가 옵션</div>
            <v-checkbox
                class="shrink mr-2 mt-0"
                v-model="setItem.otherOptions"
                v-for="itemOption in otherGroup"
                :key="itemOption.id"
                :label="itemOption.name"
                :value="itemOption.id"
            />
          </v-container>
        </v-col>
      </v-row>
      <v-btn
          block
          @click="addItem"
      >담기</v-btn>
    </v-container>
  </v-main>
</template>

<script>
import storeApi from "@/api/store";
import orderApi from "@/api/order";

export default {
  name: "ItemDetail",
  mounted() {
    console.log(this.$route.params)
    this.storeId = this.$route.params.storeId
    this.itemId = this.$route.params.itemId
    this.setItem.storeId = this.storeId;
    this.getItemData()
  },
  computed: {
    requireGroup:function(){
      return this.parseGroup('REQUIRED')
    },
    otherGroup:function(){
      return this.parseGroup('OTHER')
    }

  },
  data: function() {
    return {
      storeId: -1,
      itemId: -1,
      itemData: {
        itemOptions:[],
      },
      setItem:{
        count:0,
        storeId:-1,
        itemId:-1,
        price:0,
        requireOption:{},
        otherOptions:[],
      },
    }
  },
  methods: {
    getItemData: function (){
      storeApi.getItemById(this.itemId)
      .then(response=>{
        console.log(response)
        this.itemData = response.data.data;
        this.setItem.itemId = this.itemData.id;
        this.setItem.price = this.itemData.price;
      })
      .catch(error=>{
        console.log(error)
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
          this.setItem.count+v >=0? this.setItem.count+v: 0;
    },
    addItem: function(){
      console.log(this.setItem)
      orderApi.addItemToBasket(this.setItem)
          .then(response=>{
            console.log(response)
          })
          .catch(error=>{
            console.log(error)
          })
    }
  },
}
</script>

<style scoped>
.container {
  max-width: 768px;
  background-color: white;
  height: 100%;
}
main {
  background-color: #f2f2f2!important;
}
</style>