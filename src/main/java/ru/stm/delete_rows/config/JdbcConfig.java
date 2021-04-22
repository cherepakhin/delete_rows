package ru.stm.delete_rows.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.stm.delete_rows.service.DeleteService;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    DeleteService deleteService(DataSource dataSource) {
        return new DeleteService(dataSource);
    }
}
