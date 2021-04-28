package ru.stm.delete_rows.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stm.delete_rows.aspect.annotation.LogExecutionTime;
import ru.stm.delete_rows.service.DatabaseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static ru.stm.delete_rows.constants.Queries.*;

/**
 * Сервис удаления
 */
@Slf4j
public class DeleteUtilsService {
    private static final String DATE_COLUMN_NAME = "ddate";
    private static final String ID_COLUMN_NAME = "id";
    private final DatabaseService databaseService;

    public DeleteUtilsService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Удаление через delete from table where id in (select id from table where ... limit portion
     *
     * @param table    имя таблицы
     * @param fromDate удалять старее этой даты
     * @param portion  удалять порциями по portion рядов
     */
    @LogExecutionTime
    @Transactional
    public void methodDeleteFromSelect(String table, String fromDate, Integer portion) {
        Integer count = databaseService.queryForObject(
                format(SELECT_COUNT_OF_RECORDS_BY_DATE, table, fromDate),
                Integer.class);
        if (count == null || count == 0) {
            log.info("В таблице {} нечего удалять", table);
            return;
        }
        int countCicle = count / portion + 1;
        log.info("В таблице {} записей для удаления {}", table, count);
        String sql = format(
                DELETE_DATA_BY_SELECT,
                table, table, fromDate, portion);
        for (int i = 0; i < countCicle; i++) {
            databaseService.execute(sql);
            log.info("Таблица: {}. Удалено {} из {} ", table, i * portion, count);
        }
    }

    /**
     * Генерация таблицы в БД
     *
     * @param table  имя таблицы
     * @param length кол-во записей
     */
    @LogExecutionTime
    @Transactional
    public void createTable(String table, Integer length) {
        log.info("Создание таблицы {} c длиной {}", table, length);
        databaseService.execute(format(CREATE_TABLE_BY_NAME, table));
        generateRows(table, length);
    }

    /**
     * Добавляет N записей в указанную таблицу с шагом времени 1 сек
     * @param table имя таблицы
     * @param length кол-во записей
     */
    void generateRows(String table, Integer length) {
        // Генерация записей с шагом в 1 сек
        log.info("Начало генерации строк");
        LocalDateTime now = LocalDateTime.now();
        List<Map<String, LocalDateTime>> recordsBatch = new ArrayList<>();
        IntStream.range(0, length)
                .mapToObj(i -> Collections.singletonMap(DATE_COLUMN_NAME, now.plusSeconds(i)))
                .forEach(item -> insertData(recordsBatch, table, item));
        log.info("Генерация строк завершена");
    }
    /**
     * Добавляет N записей в указанную таблицу с заданным временем
     * @param table имя таблицы
     * @param length кол-во записей
     * @param date дата записи
     */
    public void insertRows(String table, Integer length, LocalDateTime date) {
        List<Map<String, LocalDateTime>> records = new ArrayList<>();
        IntStream.range(0, length)
                .mapToObj(i -> Collections.singletonMap(DATE_COLUMN_NAME, date))
                .forEach(item -> insertData(records, table, item));
    }

    /**
     * Вставляет строки в таблицу батчами по 100 000
     * @param records текущий батч
     * @param table имя таблицы
     * @param insertRow строка для записи
     */
    private void insertData(List<Map<String, LocalDateTime>> records, String table, Map<String, LocalDateTime> insertRow) {
        records.add(insertRow);
        if (records.size() > 100000) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(databaseService.getDataSource())
                    .withTableName(table)
                    .usingGeneratedKeyColumns(ID_COLUMN_NAME)
                    .usingColumns(DATE_COLUMN_NAME);
            simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
            records.clear();
        }
    }

    /**
     * Удаление таблицы
     *
     * @param table имя таблицы
     */
    @LogExecutionTime
    @Transactional
    public void dropTable(String table) {
        log.info("Удаление таблицы {}", table);
        databaseService.execute(format(DROP_TABLE_BY_NAME, table));
    }
}
