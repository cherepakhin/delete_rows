package ru.stm.delete_rows.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.stm.delete_rows.service.DeleteNavigator;
import ru.stm.delete_rows.service.DeleteService;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.context.impl.DeleteMethodContextImpl;
import ru.stm.delete_rows.service.impl.DeleteNavigatorImpl;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    DeleteMethodContext deleteMethodContext() {
        return new DeleteMethodContextImpl();
    }

    @Bean
    DeleteNavigator deleteNavigator(DataSource dataSource, DeleteMethodContext deleteMethodContext) {
        return new DeleteNavigatorImpl(dataSource, deleteMethodContext);
    }

    @Bean
    DeleteService deleteService(DataSource dataSource) {
        return new DeleteService(dataSource);
    }
}
