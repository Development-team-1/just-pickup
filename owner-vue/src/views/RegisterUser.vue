<template>
  <v-card width="800" class="mx-auto mt-5">
    <v-card-title>
      <h1>Register</h1>
    </v-card-title>
    <v-card-text>
      <v-form
          ref="form"
          lazy-validation
      >
        <v-text-field
            v-model="email"
            :rules="[v => /.+@.+\..+/.test(v) || 'E-mail must be valid', v => !!v || '이메일은 필수 값입니다']"
            label="이메일"
        ></v-text-field>
        <v-text-field
            v-model="password"
            :rules="[v => !!v || '비밀번호는 필수 값입니다']"
            label="비밀번호"
            type="Password"
            append-icon="mdi-eye-off"
        ></v-text-field>
        <v-text-field
            v-model="name"
            :rules="[v => !!v || '이름은 필수 값입니다']"
            label="이름"
        ></v-text-field>
        <v-text-field
            v-model="phoneNumber"
            :rules="[v => !!v || '전화번호는 필수 값입니다']"
            label="전화번호"
        ></v-text-field>
        <v-text-field
            v-model="businessNumber"
            :rules="[v => !!v || '사업자번호는 필수 값입니다']"
            label="사업자번호"
        ></v-text-field>
      </v-form>
    </v-card-text>
    <v-divider></v-divider>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="info" v-on:click="register">Register</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>

import userApi from '../api/user.js'

export default {
  name: "RegisterUser",
  data: function() {
    return {
      email: '',
      password: '',
      name: '',
      phoneNumber: '',
      businessNumber: ''
    }
  },
  methods: {
    register: function() {
      if (!this.$refs.form.validate()) return;

      const user = {
        email: this.email,
        password: this.password,
        name: this.name,
        phoneNumber: this.phoneNumber,
        businessNumber: this.businessNumber
      }

      userApi.requestRegisterUser(user)
        .then( (response) => {
          if (response.status == '201') {
            alert("사용자 등록이 성공되었습니다.");
            this.$router.push('/login');
          } else {
            alert("사용자 등록에 실패하였습니다. 다시 시도해주세요.");
          }
        })
        .catch( (error) => {
          console.log(error);
          let message = error.response.data.message;
          if (message) alert(message);
        });
    }
  }
}
</script>

<style scoped>

</style>