package ru.cshse.project.settings;

import lombok.Value;

/**
 * @author apollin
 */
@Value
public class MetricSettings {
    String metricName;
    boolean exportEnabled;
}
