<template>
  <v-app>
    <task-head/>
    <v-content class="mx-10 mt-6">
      <v-row/>
      <v-row>
        <v-col cols="4" md="4">
          <v-text-field
              v-model="tableName"
              label="Название тестируемой таблицы"
              cols="4" md="4"
              required
              type="text"
          />
        </v-col>
      </v-row>
      <create-table-form v-bind:createTableOnClick="createTable" class="my-5"/>
      <drop-table-form v-bind:drop-table-on-click="dropTable" class="my-5"/>
      <delete-rows-form v-bind:delete-rows-on-click="deleteRows" class="my-5"/>
      <insert-rows-from v-bind:insert-rows-on-click="insertRows" class="my-5"/>
      <v-banner class="grey lighten-3 mt-4">Результат</v-banner>
      <div class="text-center">
        <v-progress-linear
            indeterminate
            color="primary"
            v-show="inprogress"
        ></v-progress-linear>
      </div>
      <div>{{ result }}</div>
    </v-content>
  </v-app>
</template>

<script>
import TaskHead from "./components/TaskHead";
import CreateTableForm from "./components/CreateTableForm";
import DropTableForm from "./components/DropTableForm";
import DeleteRowsForm from "./components/DeleteRowsForm";
import InsertRowsFrom from "./components/InsertRowsForm";
import {AXIOS} from "./util/http_commons";

export default {
  name: "App",

  components: {
    TaskHead,
    CreateTableForm,
    DropTableForm,
    DeleteRowsForm,
    InsertRowsFrom
  },
  data: () => {
    return {
      tableName: "testtable",
      result: "",
      inprogress: false
    };
  },
  methods: {
    createTable: function (length, date) {
      console.log(JSON.stringify({table: this.tableName, length, date}));
      this.startProgress();
      AXIOS.post("/create_table", null, {params: {table: this.tableName, length, date}})
          .then(response => {
            this.showResult(response.data);
          })
          .catch(error => {
            this.showResult(error.response.data);
          });
    },
    dropTable: function () {
      console.log(this.tableName);
      this.startProgress();
      AXIOS.post("/drop_table", null, {params: {table: this.tableName}})
          .then(response => {
            this.showResult(response.data);
          })
          .catch(error => {
            this.showResult(error.response.data);
          });
    },
    deleteRows: function (dateTime) {
      console.log(dateTime)
      this.startProgress();
      AXIOS.post("/delete", {tableName: this.tableName, date: dateTime})
          .then(response => {
            this.showResult(response.data);
          })
          .catch(error => {
            this.showResult(error.response.data);
          });
    },
    insertRows: function (tableDto) {
      this.startProgress();
      AXIOS.post("/insert_rows", null, {params: {...tableDto, table: this.tableName}})
          .then(response => {
            this.showResult(response.data);
          })
          .catch(error => {
            this.showResult(error.response.data);
          });
    },
    startProgress: function () {
      this.inprogress = true;
      this.result = "";
    },
    showResult: function (data) {
      console.log("Result: " + data);
      this.inprogress = false;
      this.$data.result = data;
    }
  }
};
</script>
