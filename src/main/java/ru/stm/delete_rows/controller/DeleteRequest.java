package ru.stm.delete_rows.controller;

import java.time.LocalDate;

/**
 * Параметры запроса на удаление
 */
public class DeleteRequest {
    // Целевая таблица(в таблице д.б. поле id)
    String table = "";
    // Удалять старее этой даты
    LocalDate fromDate;
    // Удалять порциями по portion записей
    Integer portion = 0;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getPortion() {
        return portion;
    }

    public void setPortion(Integer portion) {
        this.portion = portion;
    }

    @Override
    public String toString() {
        return "DeleteRequest{" +
                "table='" + table + '\'' +
                ", fromDate=" + fromDate +
                ", portion=" + portion +
                '}';
    }
}
