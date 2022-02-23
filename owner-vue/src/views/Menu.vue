<template>
  <div>
    <v-subheader class="py-0 d-flex justify-space-between rounded-lg">
      <h3>메뉴 관리</h3>
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
import axios from "axios";

export default {
  name: "Category",
  components:{
  },
  data() {
    return {
      icons: {
        mdiContentSave,
        mdiPlus,
        mdiDelete,
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
        { text: '이름', value: 'name' },
        { text: '카테고리', value: 'categoryName' },
        { text: '가격', value: 'price' },
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
      axios({
        method:'get',
        url:'/store-service/item',
        params : searchParam,
        responseType:'json'
      })
      .then(function (response) {
        const page = response.data.data.page;
        vm.menus = response.data.data.itemList;
        vm.page = page.startPage+1;
        vm.pageCount = page.totalPage;
      });
    }
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
  mounted() {
    this.getMenu()
  }
}
</script>

<style scoped>

</style>