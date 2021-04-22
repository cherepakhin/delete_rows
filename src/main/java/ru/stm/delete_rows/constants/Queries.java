package ru.stm.delete_rows.constants;

public class Queries {
    public static String SELECT_COUNT_OF_RECORDS_BY_DATE = "select count(*) from %s where ddate < '%s'";
    public static String DELETE_DATA_BY_SELECT = "delete from %s where id in (select id from %s where ddate < '%s' limit %d)";
    public static String CREATE_TABLE_BY_NAME = "create table %s (id SERIAL PRIMARY KEY, ddate timestamp )";
    public static String DROP_TABLE_BY_NAME = "drop table %s";
    public static String SELECT_COUNT_OF_RECORD_BY_TABLE_NAME = "select count(*) from %s";

}
