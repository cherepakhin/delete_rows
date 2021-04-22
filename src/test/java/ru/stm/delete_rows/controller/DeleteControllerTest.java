package ru.stm.delete_rows.controller;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DeleteControllerTest {

    @Test
    void convertTime() {
        LocalDateTime ldt =
                LocalDateTime.parse("2017-02-02 08:59:12",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(ldt);
    }
}