<template>
  <v-card width="800" class="mx-auto mt-5">
    <v-card-title>
      <h1>Login</h1>
    </v-card-title>
    <v-card-text>
      <v-form ref="form" lazy-validation>
        <v-text-field
            v-model="email"
            :rules="[v => /.+@.+\..+/.test(v) || 'E-mail must be valid', v => !!v || '이메일은 필수 값입니다']"
            label="이메일"
            prepend-icon="mdi-account-circle"
            v-on:keydown.enter="login"
        ></v-text-field>
        <v-text-field
            v-model="password"
            :rules="[v => !!v || '비밀번호는 필수 값입니다']"
            label="비밀번호"
            type="Password"
            prepend-icon="mdi-lock"
            append-icon="mdi-eye-off"
            v-on:keydown.enter="login"
        ></v-text-field>
      </v-form>
    </v-card-text>
    <v-divider></v-divider>
    <v-card-actions>
      <v-btn color="success" v-on:click="links('/register')">Register</v-btn>
      <v-spacer></v-spacer>
      <v-btn color="info"
             v-on:click="login"
      >
        Login
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import userApi from '../api/user.js'
import jwt from "@/common/jwt";

export default {
  name: "LoginUser",
  mounted() {
    if (false == jwt.isExpired()) {
      this.$router.push('/order');
    }
  },
  data: function() {
    return {
      email: 'owner@gmail.com',
      password: '1234'
    }
  },
  methods: {
    links: function(url) {
      this.$router.push(url);
    },
    login: async function() {
      if (!this.$refs.form.validate()) return;

      const flag = await userApi.requestLoginUser(this.email, this.password);

      if (flag) await this.$router.push('/order');
    }
  }
}
</script>

<style scoped>

</style>