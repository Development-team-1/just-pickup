<template>
  <div>
    <v-card width="800" class="mx-auto mt-5">
      <v-card-title>
        <h1>Register</h1>
      </v-card-title>
      <v-card-text>
        <h3>회원 정보</h3>
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

          <br>
          <h3>매장 정보</h3>
          <v-text-field
              v-model="storeName"
              :rules="[v => !!v || '매장정보는 필수 값입니다']"
              label="매장 이름"
          ></v-text-field>
          <v-text-field
              v-model="storePhoneNumber"
              :rules="[v => !!v || '매장전화번호는 필수 값입니다']"
              label="매장 전화번호"
          ></v-text-field>
          <v-text-field
              v-model="storeAddress"
              :rules="[v => !!v || '매장 주소는 필수 값입니다']"
              label="매장 주소"
              readonly
          >
            <template v-slot:append-outer>
              <v-btn
                  @click="dialog = true"
                  small
              >우편번호 검색</v-btn>
            </template>
          </v-text-field>
          <v-row>
            <v-col sm="6">
              <v-text-field
                  v-model="latitude"
                  :rules="[v => !!v || '매장 위도는 필수 값입니다']"
                  label="위도"
                  readonly
              ></v-text-field>
            </v-col>
            <v-col sm="6">
              <v-text-field
                  v-model="longitude"
                  :rules="[v => !!v || '매장 경도는 필수 값입니다']"
                  label="경도"
                  readonly
              ></v-text-field>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
      <v-divider></v-divider>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="info" v-on:click="register">Register</v-btn>
      </v-card-actions>
    </v-card>
    <v-dialog
        v-model="dialog"
        width="500"
    >
      <VueDaumPostcode
          @complete="complete"
      ></VueDaumPostcode>
    </v-dialog>
    <div id="map"></div>
  </div>
</template>

<script>
import userApi from '../api/user.js'
import { VueDaumPostcode } from 'vue-daum-postcode'

export default {
  name: "RegisterUser",
  components: {
    VueDaumPostcode
  },
  mounted() {
    const script = document.createElement("script");
    // eslint-disable-next-line no-undef
    script.onload = () => kakao.maps.load(this.initMap);
    script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&libraries=services&appkey=013b300ace1c317f0f39ce53201f831b";
    document.head.appendChild(script);
  },
  data: function() {
    return {
      email: 'test@gmail.com',
      password: '1234',
      name: '테스트 계정',
      phoneNumber: '010-1111-2222',
      businessNumber: '03912',

      storeName: '테스트 매장',
      storePhoneNumber: '010-1111-2222',
      storeAddress: '',
      zipcode: '',
      latitude: '',
      longitude: '',

      dialog: false,

      map: '',
    }
  },
  methods: {
    register: async function() {
      if (!this.$refs.form.validate()) return;

      const user = {
        email: this.email,
        password: this.password,
        name: this.name,
        phoneNumber: this.phoneNumber,
        businessNumber: this.businessNumber
      }

      const store = {
        storeName: this.storeName,
        storePhoneNumber: this.storePhoneNumber,
        storeAddress: this.storeAddress,
        zipcode: this.zipcode,
        latitude: this.latitude,
        longitude: this.longitude,
      }

      try {
        await userApi.requestRegisterUser(user, store);
        alert("회원 가입에 성공하였습니다. \n로그인 페이지로 이동합니다.");
        await this.$router.push('/login');
      } catch (error) {
        console.log(error)
      }
    },
    initMap() {
      const container = document.getElementById("map");
      const options = {
        // eslint-disable-next-line no-undef
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 5,
      };

      // eslint-disable-next-line no-undef
      this.map = new kakao.maps.Map(container, options);
    },
    complete: function(addressResult) {
      this.storeAddress = addressResult.address;
      this.zipcode = addressResult.zonecode;
      this.dialog = false;

      Promise.resolve(this.storeAddress).then(value => {
        return new Promise((resolve, reject) => {
          // eslint-disable-next-line no-undef
          const geocoder = new daum.maps.services.Geocoder();

          geocoder.addressSearch(value, (result, status) => {
            // eslint-disable-next-line no-undef
            if (status === daum.maps.services.Status.OK) {
              const {x, y} = result[0];

              resolve({lat: y, long: x})
            } else {
              reject();
            }
          })
        }).then(result => {
          this.latitude = result.lat;
          this.longitude = result.long;
        }).catch(error => {
          console.log("[RegisterUser]", error);
          alert("해당 주소의 위도, 경도를 가져오는 도중 오류가 발생하였습니다. \n다시 시도해주세요.");
        });
      })
    },
  }
}
</script>

<style scoped>

</style>