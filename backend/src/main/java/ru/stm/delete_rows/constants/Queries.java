package ru.stm.delete_rows.constants;

public class Queries {
    public static final String SELECT_COUNT_OF_RECORDS_BY_DATE = "select count(*) from %s where ddate < '%s'";
    public static final String DELETE_DATA_BY_SELECT = "delete from %s where id in (select id from %s where ddate < '%s' limit %d)";
    public static final String CREATE_TABLE_BY_NAME = "create table if not exists %s (id SERIAL PRIMARY KEY, ddate timestamp )";
    public static final String DROP_TABLE_BY_NAME = "drop table %s";
    public static final String SELECT_COUNT_OF_RECORD_BY_TABLE_NAME = "select count(*) from %s";
    public static final String TRUNCATE_TABLE_BY_NAME = "truncate %s";
    public static final String CREATE_TEMP_TABLE_BY_SELECT = "create table if not exists temp_table as select * from %s where ddate > '%s'";
    public static final String RENAME_TEMP_TABLE = "alter table temp_table rename to %s";

    private Queries() {
        throw new IllegalStateException("Utility query class");
    }
}
