package ru.cshse.project.sources.kubernetes;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.cshse.project.sources.MetricsProvider;

/**
 * @author apollin
 */
@Configuration
public class PodsMetricsProviderConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "basic.metrics", name = "mechanism", havingValue = "standalone")
    public MetricsProvider podsMetricProvider(
            PodsRegistry registry,
            PlainMetricsClient client,
            @org.springframework.beans.factory.annotation.Value("${pods.metrics.collection.thread.count}") int threadCount
    ) {
        return new PodsMetricProvider(
                registry,
                client,
                threadCount
        );
    }
}
