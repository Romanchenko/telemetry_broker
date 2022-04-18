package ru.cshse.project.importer;

import com.google.common.collect.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.cshse.project.db.ClickHouseExportService;
import ru.cshse.project.sources.MetricsProvider;

@Component
public class MetricsImportTask {

    private static final Logger logger = LoggerFactory.getLogger(MetricsImportTask.class);

    private static final int BATCH_SIZE = 100;

    private final ClickHouseExportService exportService;
    private final MetricsProvider metricsProvider;

    @Autowired
    public MetricsImportTask(
            ClickHouseExportService exportService,
            MetricsProvider metricsProvider
    ) {
        this.exportService = exportService;
        this.metricsProvider = metricsProvider;
    }

    @Scheduled(fixedDelayString = "${import.task.fixedDelay.in.milliseconds}")
    public void run() {
        logger.info("Running export iteration");
        Iterators.partition(metricsProvider.get().iterator(), BATCH_SIZE)
            .forEachRemaining(exportService::export);
    }

}