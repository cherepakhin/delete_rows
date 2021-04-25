<template>
  <div class="drop-table-form">
    <v-form>
      <v-row>
        <v-col
            offset="4"
            cols="4"
            sm="2"
            md="2"
        >
          <v-btn v-on:click="dropTable" color="primary" style="min-width: 100%;">Удалить таблицу</v-btn>
        </v-col>
      </v-row>
    </v-form>
  </div>
</template>

<script>
export default {
  name: "DropTableForm",
  props: {
    dropTableOnClick: Function
  },
  methods: {
    dropTable: function() {
     this.dropTableOnClick();
    }
  }
};
</script>
