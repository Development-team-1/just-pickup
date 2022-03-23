<template>
  <v-container
      fill-height
      >
    <v-row >
      <v-col >
        <v-card
            class="mx-auto mb-5 v-alert--border"
            outlined

            elevation="9"

        >
          <v-card-title>내 정보 관리</v-card-title>
          <v-card-text>
            <v-form
                ref="form"
                v-model="userData.valid"
                lazy-validation
                readonly
            >
              <v-text-field
                  v-model="userData.userId"
                  label="id"
                  required
              />
              <v-text-field
                  v-model="userData.email"
                  label="E-mail"
                  required
              />

              <v-text-field
                  v-model="userData.userName"
                  label="Name"
                  required
              />

              <v-text-field
                  v-model="userData.phoneNumber"
                  label="phoneNumber"
                  required
              />
              <v-btn
                  color="orange"
                  block
                  @click="message('준비중입니다.')"
              >수정하기</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import userApi from "@/api/user";

export default {
  name: "MyPage",
  data (){
    return {
      userData:{
        userId:'',
        email:'',
        userName:'',
        phoneNumber:'',
      },
    }
  },
  methods:{
    getUserData(){
      userApi.geUserData().then(response =>{
        this.userData = response.data.data
      }).catch(error =>{
        console.log(error.response)
      })
    },
    message: function(message){
      alert(message)
    },
  },
  mounted() {
    this.getUserData()
  }
}
</script>

<style scoped>

</style>