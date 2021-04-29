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
            </v-row>
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
                                    v-model="tableDto.date"
                                    label="Выберите дату"
                                    prepend-icon="mdi-calendar"
                                    readonly
                                    v-bind="attrs"
                                    v-on="on"
                            ></v-text-field>
                        </template>
                        <v-date-picker
                                v-model="tableDto.date"
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
                    <v-btn v-on:click="addRows" color="primary" style="min-width: 100%;">Добавить строки</v-btn>
                </v-col>
            </v-row>
        </v-form>
    </div>
</template>

<script>
    export default {
        name: "InsertRowsFrom",
        props: {
            insertRowsOnClick: Function,
        },
        data: function() {
            return {
                tableDto: {
                    length: 0,
                    date: new Date().toISOString().substr(0, 10),
                },
                menuDate: false,
            };
        },
        methods: {
            addRows: function() {
                this.insertRowsOnClick(this.tableDto);
            }
        }
    };
</script>
