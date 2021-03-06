package ru.cshse.project.sources.prometheus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.cshse.project.sources.MetricsProvider;

/**
 * @author apollin
 */
@Configuration
public class PrometheusProviderConfiguration {
    @Bean
    public PrometheusClient prometheusClient(
            @Value("${prometheus.base.url}") String prometheusUrl,
            @Value("${prometheus.port.my}") String port
    ) {
        return new PrometheusClient(prometheusUrl, port);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "basic.metrics", name = "mechanism", havingValue = "prometheus")
    public MetricsProvider prometheusMetricsProvider(
            PrometheusClient prometheusClient,
            MetadataCache metadataCache
    ) {
        return new PrometheusMetricsProvider(prometheusClient, metadataCache);
    }
}
