package ru.cshse.project.importer;

import com.google.common.collect.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.cshse.project.db.ClickHouseExportService;
import ru.cshse.project.sources.MetricsProvider;

@Component
public class MetricsImportTask {

    private static final Logger logger = LoggerFactory.getLogger(MetricsImportTask.class);


    private final ClickHouseExportService exportService;
    private final MetricsProvider metricsProvider;
    private final int batchSize;

    @Autowired
    public MetricsImportTask(
            @Value("${clickhouse.export.batch.size:50}") int batchSize,
            ClickHouseExportService exportService,
            @Qualifier("prometheusMetricsProvider") MetricsProvider metricsProvider
    ) {
        this.batchSize = batchSize;
        this.exportService = exportService;
        this.metricsProvider = metricsProvider;
    }

    @Scheduled(fixedDelayString = "${import.task.fixedDelay.in.milliseconds}")
    public void run() {
        logger.info("Running export iteration with batch size {}", batchSize);
        Iterators.partition(metricsProvider.get().iterator(), batchSize)
            .forEachRemaining(exportService::export);
    }

}