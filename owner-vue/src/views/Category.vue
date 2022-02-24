<template>
  <div>
    <v-subheader class="py-0 d-flex justify-space-between rounded-lg">
      <h3>Category List</h3>
      <div style="text-align: right">
        <span class="group pa-2">
          <v-btn
              elevation="2"
              icon
              color="success"
              @click="clickAdd"
          ><v-icon>{{ icons.mdiPlus }}</v-icon></v-btn>
        </span>
        <v-btn
            elevation="2"
            icon
            color="primary"
            @click="clickSave"
        ><v-icon>{{ icons.mdiContentSave }}</v-icon></v-btn>
      </div>

    </v-subheader>

    <v-expansion-panels style="display: block">
      <draggable v-model="categoryList" id="categoryEl" >
        <v-expansion-panel
            v-for="category in categoryList" :key="category.categoryId" class="category-item" :data-id="category.categoryId"
        >
          <v-expansion-panel-header >
            <span contenteditable="true" >{{ category.name }}</span>

            <template v-slot:actions>
              <v-btn
                  elevation="2"
                  icon
                  @click.stop="clickDelete($event)"
              ><v-icon>{{ icons.mdiDelete }}</v-icon></v-btn>
            </template>

          </v-expansion-panel-header>
          <v-expansion-panel-content>

            <v-list-item v-for=" item in category.items" :key="item.id" >
              <v-list-item-content>
                <v-list-item-title> {{item.name}}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>

          </v-expansion-panel-content>
        </v-expansion-panel>
      </draggable>
    </v-expansion-panels>

  </div>
</template>

<script>
import draggable from 'vuedraggable'
import {
  mdiContentSave, mdiDelete,
  mdiPlus,
} from '@mdi/js'

export default {
  name: "Category",
  components:{
    draggable
  },
  data() {
    return {
      icons: {
        mdiContentSave,
        mdiPlus,
        mdiDelete,
      },
      categoryList: [

      ],
      deletedList: []
    }
  },
  methods:{
    clickAdd:function (){
      var count = this.categoryList.length+1
      var item = {
        name: 'new Category',
        order:count
      }
      this.categoryList.push(item)
    },
    clickDelete:function (event){
      var category = event.currentTarget.parentNode.parentNode.parentNode;
      let delCategory = {
        categoryId : category.dataset.id,
        name : category.children[0].children[0].innerHTML,
      }
      this.deletedList.push(delCategory)
      category.remove();
    },
    clickSave:function (){
      var vm =this;
      let data = {
        storeId : "1",
        categoryList: [],
        deletedList: vm.deletedList
      }

      var categoryEl = document.querySelector("#categoryEl");
      categoryEl.childNodes.forEach(function(item ,index){
        let category = {
          categoryId : item.dataset.id,
          name : item.children[0].children[0].innerHTML,
          order : index+1
        }
        data.categoryList.push(category)
      })
      this.$axios({
        method:'put',
        url:'/store-service/category',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json;charset=UTF-8'
        },
        data: data,
        responseType:'json'
      })
      .then(function (response) {
        console.log(response)
        vm.deletedList=[]
        vm.getCategoryList()
      });

    },
    getCategoryList:function(){
      var vm =this;
      this.$axios({
        method:'get',
        url:'/store-service/category',
        responseType:'json'
      })
      .then(function (response) {
        console.log(response.data.data)
        vm.categoryList = response.data.data;
      });
    }
  },
  mounted() {
    this.getCategoryList();
  }
}
</script>

<style scoped>

</style>