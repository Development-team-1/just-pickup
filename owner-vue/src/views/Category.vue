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
      <draggable v-model="categoryList" >
        <v-expansion-panel
            v-for="item in categoryList" :key="item.order" class="category-item"
        >
          <v-expansion-panel-header>
            {{ item.name }}
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
          </v-expansion-panel-content>
        </v-expansion-panel>
      </draggable>
    </v-expansion-panels>

  </div>
</template>

<script>
import draggable from 'vuedraggable'
import {
  mdiContentSave,
  mdiPlus,
} from '@mdi/js'
import axios from "axios";

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
      },
      categoryList: [
        {
          name: 'cc',
          no: 1,
        },
        {
          name: 'bb',
          no: 2,
        },
        {
          name: 'aa',
          no: 3,
        },
      ],
    }
  },
  methods:{
    clickAdd:function (){
      alert();
    },
    clickSave:function (){


    },
  },
  created() {
    var vm =this;
    axios({
      method:'get',
      url:'http://localhost:8001/store-service/category',
      responseType:'json'
    })
    .then(function (response) {
      console.log(response.data.data)
      vm.categoryList = response.data.data;
    });
  }
}
</script>

<style scoped>

</style>