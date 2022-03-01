<template>
  <div>
    <v-subheader class="py-0 d-flex justify-space-between rounded-lg">
      <h3>메뉴 관리</h3>
      <MenuItem
          :modalData="modalData"
          :icon="modalSet.icon.register"
          :color="modalSet.color.red"
          :name="modalSet.words.register"
          @save="itemSave"
          @addItemOption="addItemOption"
          @init="getModalData"
      />

    </v-subheader>
    <v-form @submit.prevent>
      <v-container>
        <v-row>
          <v-col
              cols="12"
              md="4"
          >
            <v-text-field
                v-model="word"
                label="검색"
                required
            ></v-text-field>
          </v-col>

        </v-row>
      </v-container>
    </v-form>
    <v-col>
      <v-data-table
          :headers="headers"
          :items="menus"
          :items-per-page="itemsPerPage"
          hide-default-footer
          class="elevation-1"
      >

        <template v-slot:item.modify="{ item }">
          <MenuItem
              :modalData="modalData"
              :icon="modalSet.icon.modify"
              :color="modalSet.color.primary"
              :name="modalSet.words.modify"
              @init="editModalOpen(item)"
              @save="itemSave"
              @addItemOption="addItemOption"
          />
        </template>

      </v-data-table>
      <div class="text-center pt-2">
        <v-pagination
            v-model="page"
            :length="pageCount"
            :total-visible="totalVisible"
            @click="getMenu"
        ></v-pagination>
      </div>
    </v-col>
  </div>
</template>

<script>
import {
  mdiContentSave, mdiDelete,
  mdiPlus,

} from '@mdi/js'
// import axios from "axios";
import MenuItem from "@/views/MenuItem";
import store from "@/api/store";

export default {
  name: "Menu",
  components:{
    MenuItem,
  },
  watch:{
    page:function () {
      this.getMenu();
    },
    word:function () {
      if(this.page == 1)
        this.getMenu()
      else
        this.page =1;
    },
  },
  data() {
    return {
      icons: {
        mdiContentSave,
        mdiPlus,
        mdiDelete,
      },
      modalSet:{
        words:{
          register:"상품 등록",
          modify:"수정 하기",
        },
        icon:{
          register: "mdi-plus",
          modify: "mdi-pencil",
        },
        color:{
          primary: "primary",
          red: "red"
        }
      },
      modalData:{
        itemId : Number,
        itemName : String,
        itemPrice : Number,
        categoryId: Number,
        categoryList : ['카테고리1','카테고리2'],
        requiredOption : [],
        otherOption : []
      },
      page: 1,
      pageCount: 1,
      itemsPerPage: 10,
      totalVisible: 10,
      headers: [
        {
          text: '메뉴번호',
          align: 'start',
          sortable: false,
          value: 'id',
        },
        { text: '이름', value: 'name' , sortable: false},
        { text: '카테고리', value: 'categoryName', sortable: false },
        { text: '가격', value: 'price' , sortable: false},
        { text: '수정', value: 'modify' , sortable: false},
      ],
      word:"",
      menus: [],
    }
  },
  methods:{
    getMenu(){
      var vm = this;
      const searchParam= {
        word: vm.word,
        page: vm.page-1
      }
      this.$axios({
        method:'get',
        url:process.env.VUE_APP_OWNER_SERVICE_BASEURL+'/store-service/item',
        params : searchParam,
        responseType:'json'
      })
      .then(function (response) {
        const page = response.data.data.page;
        vm.menus = response.data.data.itemList;
        vm.page = page.startPage+1;
        vm.pageCount = page.totalPage;
      });
    },
    getModalData:function(){
      var vm =this;
      this.modalData = {
        itemName : '',
        itemPrice : '',
        categoryId: 0,
        categoryList : [],
        requiredOption : [],
        otherOption : []
      }

      store.getCategoryList()
        .then(function (response) {
          response.data.data.forEach(function (ele){
            vm.modalData.categoryList.push(ele)
          })
        });
    },
    editModalOpen:function(item){
      var vm = this
      this.getModalData();

      store.getItemById(item.id)
        .then(function (response) {
          var item = response.data.data;
          vm.modalData.itemId = item.id;
          vm.modalData.itemName = item.name;
          vm.modalData.itemPrice = item.price;
          vm.modalData.categoryId = item.categoryId;
          item.itemOptions.forEach(function(ele){
            if(ele.optionType === "REQUIRED")
              vm.modalData.requiredOption.push(ele)
            else
              vm.modalData.otherOption.push(ele)
          })
        });
    },
    itemSave:function(){
      var method =''
      var itemData = this.modalData;
      if (this.modalData.itemId!=null)
        method='put'
      else
        method='post'

      store.saveItem(method,itemData)
        .then(response => console.log(response))
        .catch(reason => console.log(reason))

    },
    addItemOption:function (itemOptionValue,type){
      var item = {
        name:itemOptionValue,
        optionType:type
      }
      if(type ==='REQUIRED')
        this.modalData.requiredOption.push(item)
      else
        this.modalData.otherOption.push(item)
    }
  },

  mounted() {
    this.getMenu()
    this.getModalData()
  }
}
</script>

<style scoped>

</style>