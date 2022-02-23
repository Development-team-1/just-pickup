<template>
  <v-menu
      v-model="isPop"
      :close-on-content-click="false"
      :nudge-right="40"
      transition="scale-transition"
      offset-y
      min-width="auto"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-text-field
          v-model="value"
          :label="label"
          prepend-icon="mdi-calendar"
          readonly
          v-bind="attrs"
          v-on="on"
      ></v-text-field>
    </template>
    <v-date-picker
        v-model="value"
        @input="input">
    </v-date-picker>
  </v-menu>
</template>

<script>
const moment = require('moment');

export default {
  name: "DatePicker",
  props: {
    'label': String
  },
  data: function() {
    return {
      isPop: false,
      value: moment().format('YYYY-MM-DD')
    }
  },
  created: function() {
    this.$emit('inputDate', this.value);
  },
  methods: {
    input: function() {
      this.isPop = false;
      this.$emit('inputDate', this.value);
    }
  }
}
</script>

<style scoped>

</style>