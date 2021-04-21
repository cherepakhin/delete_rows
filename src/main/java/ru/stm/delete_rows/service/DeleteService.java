package ru.stm.delete_rows.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stm.delete_rows.controller.DeleteRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Сервис удаления
 */
@Service
@Slf4j
public class DeleteService {

    @Autowired
    private EntityManager entityManager;

    /**
     * Удаление через delete from table where id in (select id from table where ... limit portion
     *
     * @param deleteRequest параметры удаления
     */
    public void methodDeleteFromSelect(DeleteRequest deleteRequest) throws Exception {
        Query quiery = entityManager
                .createNativeQuery("select count(*) from :table where ddate < :fromDate");
        quiery.setParameter("table", deleteRequest.getTable());
        quiery.setParameter("fromDate", deleteRequest.getFromDate());

        int count = Integer.parseInt(quiery.getSingleResult().toString());
        log.info("Для удаления {} записей",count);
    }

    /**
     * Генерация таблицы в БД
     *
     * @param table  имя таблицы
     * @param length кол-во записей
     */
    public void createTable(String table, Integer length) throws Exception {
        Query quiery = entityManager
                .createNativeQuery("create table :table (id bigint, ddate date )");
        quiery.setParameter("table", table);
        generateRows(table, length);
    }

    private void generateRows(String table, Integer length) {
        //TOD: generate rows
    }

    /**
     * Удаление таблицы
     *
     * @param table имя таблицы
     */
    public void dropTable(String table) {
    }
}
