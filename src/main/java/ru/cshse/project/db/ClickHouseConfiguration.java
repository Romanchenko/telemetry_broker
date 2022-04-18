package ru.cshse.project.db;

import java.net.InetSocketAddress;

import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseCredentials;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apollin
 */
@Configuration
public class ClickHouseConfiguration {

    private static final ClickHouseProtocol PREFERRED_PROTOCOL = ClickHouseProtocol.HTTP;

    @Bean
    public ClickHouseNode clickHouseNode(
            @Value("${ch.connection.string}") String connectionString,
            @Value("${ch.connection.port}") int port,
            @Value("${ch.user}") String user,
            @Value("${ch.password}") String password
    ) {
        // connect to localhost, use default port of the preferred protocol
        return ClickHouseNode
                .builder()
                .port(PREFERRED_PROTOCOL)
                .address(PREFERRED_PROTOCOL, InetSocketAddress.createUnresolved(connectionString, port))
                .database("metrics")
                .credentials(ClickHouseCredentials.fromUserAndPassword(user, password))
                .build();
    }

    @Bean
    public ClickHouseClient clickHouseClient() {
        return ClickHouseClient.newInstance(PREFERRED_PROTOCOL);
    }
}
