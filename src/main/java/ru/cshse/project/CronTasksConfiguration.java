package ru.cshse.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.cshse.project.db.ClickHouseExportService;
import ru.cshse.project.importer.MetricsImportTask;
import ru.cshse.project.sources.MetricsProvider;

/**
 * @author apollin
 */
@Configuration
@EnableScheduling
public class CronTasksConfiguration {

    @Bean
    public MetricsImportTask metricsImportTask(
            ClickHouseExportService exportService, MetricsProvider metricsProvider
    ) {
        return new MetricsImportTask(exportService, metricsProvider);
    }
}
