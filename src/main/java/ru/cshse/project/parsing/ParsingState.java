package ru.cshse.project.parsing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.cshse.project.models.PrometheusMetricDto;

/**
 * @author apollin
 */
@Data
@AllArgsConstructor
public class ParsingState {
    private Status status;
    private final List<PrometheusMetricDto> processed;
    private PrometheusMetricDto currentMetric;

    public void addMetric(PrometheusMetricDto metricDto) {
        processed.add(metricDto);
    }

    public void createNewMetric(String name) {
        currentMetric = PrometheusMetricDto.builder().name(name).build();
    }

    public void addMetricType(String type) {
        currentMetric = currentMetric.toBuilder().type(type).build();
    }

    public void addMetricValue(BigDecimal value) {
        currentMetric = currentMetric.toBuilder().value(value).build();
    }

    public void addMetricLabel(String labelName, String labelValue) {
        if (currentMetric.getLabels() == null) {
            currentMetric = currentMetric.toBuilder().labels(new HashMap<>()).build();
        }
        currentMetric.getLabels().put(labelName, labelValue);
    }

    public void clearLabels() {
        currentMetric = currentMetric.toBuilder().labels(new HashMap<>()).build();
    }


    public enum Status {
        START,
        PROCESSED_HELP,
        PROCESSED_TYPE,
        PROCESSING_DATA,
        FINISHED;
    }
}
