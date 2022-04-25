package ru.cshse.project.db;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cshse.project.models.Mapper;
import ru.cshse.project.models.PrometheusMetricDto;

/**
 * @author apollin
 */
@Service
public class ClickHouseExportService {

    private final MetricsDao metricsDao;

    @Autowired
    public ClickHouseExportService(MetricsDao metricsDao) {
        this.metricsDao = metricsDao;
    }

    public void export(Collection<PrometheusMetricDto> prometheusMetrics) {
        metricsDao.insert(prometheusMetrics.stream().map(Mapper::map).collect(Collectors.toList()));
    }
}
