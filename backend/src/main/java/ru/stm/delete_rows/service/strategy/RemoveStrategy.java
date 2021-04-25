package ru.stm.delete_rows.service.strategy;

public interface RemoveStrategy {
    void remove(String table, String date);
    boolean isRecommended(long percent);
}
