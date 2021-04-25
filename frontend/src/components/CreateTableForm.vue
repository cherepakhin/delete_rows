<template>
  <div>
    <v-form>
      <v-row>
        <v-col cols="12" md="4">
          <v-text-field
            v-model="tableDto.length"
            label="Кол-во записей"
            required
            type="number"
          />
        </v-col>
        <v-col
            cols="4"
            sm="2"
            md="2"
        >
          <v-btn v-on:click="createTable" color="primary" style="min-width: 100%;">Создать таблицу</v-btn>
        </v-col>
      </v-row>
    </v-form>
  </div>
</template>

<script>
export default {
  name: "CreateTableForm",
  props: {
    createTableOnClick: Function,
  },
  data: function() {
    return {
      tableDto: {
        length: 100000
      }
    };
  },
  methods: {
    createTable: function() {
      console.log(`tableDto=${JSON.stringify(this.tableDto)}`);
     this.createTableOnClick(this.tableDto.length);
    }
  }
};
</script>
