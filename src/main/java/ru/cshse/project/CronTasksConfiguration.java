package ru.cshse.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.cshse.project.importer.MetricsImportTask;

/**
 * @author apollin
 */
@Configuration
@EnableScheduling
public class CronTasksConfiguration {

    @Bean
    public MetricsImportTask metricsImportTask() {
        return new MetricsImportTask();
    }
}
