package ru.cshse.project.db;

import com.virtusai.clickhouseclient.ClickHouseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apollin
 */
@Configuration
public class ClickHouseConfiguration {

    @Bean
    public ClickHouseClient clickHouseClient(
            @Value("${ch.connection.string}") String connectionString,
            @Value("${ch.user}") String user,
            @Value("${ch.password}") String password
    ) {
        return new ClickHouseClient(connectionString, user, password);
    }
}
