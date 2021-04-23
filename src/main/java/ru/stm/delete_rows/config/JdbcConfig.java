package ru.stm.delete_rows.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.stm.delete_rows.service.DatabaseService;
import ru.stm.delete_rows.service.DeleteNavigator;
import ru.stm.delete_rows.service.DeleteService;
import ru.stm.delete_rows.service.context.DeleteMethodContext;
import ru.stm.delete_rows.service.context.impl.DeleteMethodContextImpl;
import ru.stm.delete_rows.service.impl.DeleteNavigatorImpl;
import ru.stm.delete_rows.service.strategy.RemoveStrategy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JdbcConfig {

    @Bean
    DeleteMethodContext deleteMethodContext() {
        return new DeleteMethodContextImpl();
    }

    @Bean
    DeleteNavigator deleteNavigator(DatabaseService databaseService,
                                    DeleteMethodContext deleteMethodContext,
                                    List<RemoveStrategy> strategyList) {
        return new DeleteNavigatorImpl(deleteMethodContext, databaseService, strategyList);
    }

    @Bean
    DeleteService deleteService(DatabaseService databaseService) {
        return new DeleteService(databaseService);
    }
}
