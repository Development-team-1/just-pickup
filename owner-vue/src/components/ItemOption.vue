<template>
  <v-dialog
      v-model="dialog"
      width="500"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          elevation="2"
          icon
          v-bind="attrs"
          v-on="on"
      ><v-icon>mdi-pencil</v-icon></v-btn>
    </template>

    <v-card>
      <v-card-title class="text-h5 grey lighten-2">
        추가하기
      </v-card-title>

      <v-card-text>
        <v-text-field
            label="옵션명*"
            v-model="data"
            @keyup.enter="addItemOption"
            required
        />
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions >
        <v-spacer></v-spacer>
        <v-btn
            color="primary"
            text
            @click="addItemOption"
        >
          Save
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "MenuItem",
  data(){
    return {
      dialog : false,
      data: "",
    }
  },
  props:{
    optionType:String
  },
  watch:{
    dialog(){
      this.data = '';
      if(this.dialog) this.$emit("init");
    }
  },
  methods:{
    addItemOption : function () {
      if(!this.data) return;

      this.dialog = false
      this.$emit('addItemOption',this.data,this.optionType)
    }
  }
}
</script>

<style scoped>

</style>