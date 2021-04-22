package ru.stm.delete_rows.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Сервис удаления
 */
@Slf4j
public class DeleteService {

    private final JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public DeleteService(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Удаление через delete from table where id in (select id from table where ... limit portion
     *
     * @param table    имя таблицы
     * @param fromDate удалять старее этой даты
     * @param portion  удалять порциями по portion рядов
     */
    public void methodDeleteFromSelect(String table, String fromDate, Integer portion) {
        Integer count = jdbcTemplate.queryForObject(
                format("select count(*) from %s where ddate < '%s'", table, fromDate),
                Integer.class);
        if (count == null || count == 0) {
            log.info("В таблице {} нечего удалять", table);
            return;
        }
        int countCicle = count / portion + 1;
        log.info("В таблице {} записей для удаления {}", table, count);
        String sql = format(
                "delete from %s where id in (select id from %s where ddate < '%s' limit %d)",
                table, table, fromDate, portion);
        for (int i = 0; i < countCicle; i++) {
            jdbcTemplate.execute(sql);
            log.info("Таблица: {}. Удалено {} из {} ", table, i * portion, count);
        }
    }

    /**
     * Генерация таблицы в БД
     *
     * @param table  имя таблицы
     * @param length кол-во записей
     */
    public void createTable(String table, Integer length) {
        log.info("Создание таблицы {} c длиной {}", table, length);
        jdbcTemplate.execute(format("create table %s (id SERIAL PRIMARY KEY, ddate timestamp )", table));
        generateRows(table, length);
    }


    private void generateRows(String table, Integer length) {

        List<Map<String, LocalDateTime>> records =
                Stream.generate(() -> Collections.singletonMap("ddate", LocalDateTime.now()))
                        .limit(length).collect(Collectors.toList());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(table)
                .usingGeneratedKeyColumns("id")
                .usingColumns("ddate");
        simpleJdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
    }

    /**
     * Удаление таблицы
     *
     * @param table имя таблицы
     */
    public void dropTable(String table) {
        log.info("Удаление таблицы {}", table);
        jdbcTemplate.execute(format("drop table %s", table));
    }

}
