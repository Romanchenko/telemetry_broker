package ru.cshse.project.sources.prometheus;

import org.junit.jupiter.api.Test;

/**
 * @author apollin
 */
class PrometheusClientTest {

    @Test
    public void test() {
        var client = new PrometheusClient("http://localhost", 9090);
        var targetMetricsResponse = client.getTargetsMetadata();
        System.out.println(targetMetricsResponse.getData().size());
    }
}