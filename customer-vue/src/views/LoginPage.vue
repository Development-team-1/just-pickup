<template>
  <v-container
      fill-height
      >
    <v-row>
      <v-col>
        <div align="center" ><v-img
            max-height="150"
            max-width="250"
            :src="logo"></v-img></div>
      </v-col>
    </v-row>
    <v-row
        justify="center"
    >
      <v-col class="align-content-center">


            <v-form ref="form" lazy-validation>
              <v-text-field
                  :rules="[v => /.+@.+\..+/.test(v) || 'E-mail must be valid', v => !!v || '이메일은 필수 값입니다']"
                  label="이메일"
                  prepend-icon="mdi-account-circle"
              ></v-text-field>
              <v-text-field
                  :rules="[v => !!v || '비밀번호는 필수 값입니다']"
                  label="비밀번호"
                  type="Password"
                  prepend-icon="mdi-lock"
                  append-icon="mdi-eye-off"
              ></v-text-field>
              <v-btn
                  block
              >
                Login
              </v-btn>
              <div class="d-block my-7" align="center">
              <v-subheader class="d-inline" >소셜 아이디로 로그인해보세요!</v-subheader>
              </div>
              <div class="d-block " align="center">
                <v-btn
                    class="mx-2"
                    fab
                    small
                >
                  <v-img
                      class="d-inline-block align-lg-center mx-5"
                      style=""
                      max-width="38"
                      max-height="38"
                      :src="logo_naver"
                      @click="login_auth('naver')"
                  />
                </v-btn>
                <v-btn
                    class="mx-2"
                    fab
                    small
                >
                  <v-img
                      class="d-inline-block mx-5"
                      max-width="38"
                      max-height="38"
                      min-width="38"
                      min-height="38"
                      :src="logo_google"
                      @click="login_auth('google')"
                  />
                </v-btn>

              </div>

            </v-form>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
import logo from '@/assets/justLogo.png'
import logo_naver from '@/assets/logo_naver.svg'
import logo_google from '@/assets/logo_google.png'

export default {
  name: "LoginPage",
  data (){
    return {
      logo : logo,
      logo_naver: logo_naver,
      logo_google : logo_google,
      auth_popup: null,
    }
  },
  watch: {
    auth_popup : function () {
      this.auth_popup.addEventListener('beforeunload', function() {
        window.location.href=process.env.VUE_APP_BASEURL;
      });


    }
  },
  methods: {
    login_auth: async function(target) {
      const _url = process.env.VUE_APP_CUSTOMER_SERVICE_BASEURL+'/user-service/oauth2/authorization/'+target
      this.auth_popup = window.open(
          _url,
          "",
          "width=600,height=400,left=200,top=200"
      );

    },
  },
}
</script>

<style scoped>

</style>