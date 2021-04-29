<template>
  <div>
    <div>Удалить записи старее:</div>
    <v-form>
      <v-row>
        <v-col
            cols="8"
            sm="8"
            md="4"
        >
          <v-menu
              v-model="menuDate"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                  v-model="fromDate"
                  label="Выберите дату"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
                v-model="fromDate"
                @input="menuDate = false"
                locale="ru-ru"
            ></v-date-picker>
          </v-menu>
        </v-col>
        <v-col
            cols="4"
            sm="2"
            md="2"
        >
          <v-btn v-on:click="deleteRows" color="primary" style="min-width: 100%;">Удалить записи</v-btn>
        </v-col>
      </v-row>
    </v-form>
  </div>
</template>

<script>
export default {
  name: "DeleteRowsForm",
  props: {
    deleteRowsOnClick: Function,
  },
  data: function () {
    return {
      fromDate: new Date().toISOString().substr(0, 10),
      menuDate: false,
    };
  },
  methods: {
    deleteRows: function () {
      console.log(this.fromDate);
      this.deleteRowsOnClick(this.fromDate);
    }
  }
};
</script>
