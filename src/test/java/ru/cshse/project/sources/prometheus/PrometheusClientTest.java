package ru.cshse.project.sources.prometheus;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author apollin
 */
class PrometheusClientTest {

    @Test
    @Disabled
    public void test() {
        var client = new PrometheusClient("localhost", "9090");
        var targetMetricsResponse = client.getTargetsMetadata();
        System.out.println(targetMetricsResponse.getData().size());
        var metric = client.getMetric(targetMetricsResponse.getData().get(0).getMetric());
        System.out.println(metric.get());
    }
}