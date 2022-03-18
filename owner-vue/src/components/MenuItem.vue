<template>
  <v-dialog
      v-model="dialog"
      width="500"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          elevation="2"
          icon
          :color="color"
          v-bind="attrs"
          v-on="on"
      ><v-icon>{{ icon }}</v-icon></v-btn>
    </template>

    <v-card>
      <v-card-title>
        <span class="text-h5">{{ name }}</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <v-col
                cols="12"
                sm="12"
                md="12"
            >
              <v-text-field
                  v-model="modalData.itemName"
                  :rules="[() => !!modalData.itemName || 'This field is required']"
                  label="이름*"
                  required
              />
            </v-col>
            <v-col
                cols="12"
                sm="12"
                md="12"
            >
              <v-text-field
                  v-model="modalData.itemPrice"
                  :rules="[() => !!modalData.itemPrice || 'This field is required']"
                  label="가격*"
                  hint="example of helper text only on focus"
              />
            </v-col>
            <v-col
                cols="12"
                sm="12"
            >
              <v-select
                  v-model="modalData.categoryId"
                  :items="modalData.categoryList"
                  :rules="[() => !!modalData.categoryId || 'This field is required']"
                  item-text="name"
                  item-value="categoryId"
                  label="카테고리*"
                  required
              />
            </v-col>

            <v-col
                cols="12"
                sm="10"
            >
              <v-select
                  v-model="modalData.requiredOption"
                  :items="modalData.requiredOption"
                  item-text="name"
                  item-value="id"
                  label="필수 옵션*"
                  multiple
              />
            </v-col>
            <v-col
                cols="12"
                sm="2"
            >
            <item-option
                @addItemOption="addItemOption"
                :optionType="req"
            />
            </v-col>
            <v-col
                cols="12"
                sm="10"
            >
              <v-select
                  v-model="modalData.otherOption"
                  :items="modalData.otherOption"
                  item-text="name"
                  item-value="id"
                  label="기타 옵션"
                  multiple
              />
            </v-col>
            <v-col
                cols="12"
                sm="2"
            >
              <item-option
                  @addItemOption="addItemOption"
                  :optionType="otr"
              />
            </v-col>

          </v-row>
        </v-container>
        <small>*indicates required field</small>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
        >
          Close
        </v-btn>
        <v-btn
            color="blue darken-1"
            text
            @click="save"
        >
          Save
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ItemOption from "@/components/ItemOption";
export default {
  name: "MenuItem",
  components:{
    ItemOption
  },
  data(){
    return {
      dialog : false,
      req: 'REQUIRED',
      otr: 'OTHER',
    }
  },
  watch:{
    dialog(){
      if(this.dialog) this.$emit("init");
    },
  },
  props:{
    modalData: {
      itemName : String,
      itemPrice : Number,
      categoryId: String,
      categoryList : Array,
      requiredOption : Array,
      otherOption : Array,
    },
    icon : String,
    color : String,
    name : String,
  },
  methods:{
    save : function () {
      this.$emit('save')
    },
    addItemOption : function (itemOptionValue,optionType){
      this.$emit("addItemOption",itemOptionValue,optionType)
    }
  }
}
</script>

<style scoped>

</style>