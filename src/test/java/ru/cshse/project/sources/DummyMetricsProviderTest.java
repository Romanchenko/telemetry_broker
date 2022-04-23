package ru.cshse.project.sources;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author apollin
 */
class DummyMetricsProviderTest {

    @Test
    void test() {
        String path = new File("src/test/java/ru/cshse/project/sources/test_metrics")
                .getAbsolutePath();
        var provider = new DummyMetricsProvider(path);
        var metrics = provider.get().collect(Collectors.toList());
        assertEquals(8, metrics.size());
        var firstMetric = metrics.get(0);
        assertEquals("endpoint_no_pod", firstMetric.getName());
        assertEquals(0.0, firstMetric.getValue().doubleValue());
        assertEquals("gauge", firstMetric.getType());


        for (int i = 1; i < 6; i++) {
            assertEquals("go_gc_duration_seconds", metrics.get(i).getName());
            assertEquals("summary", metrics.get(i).getType());
        }
        assertEquals(0.000116036, metrics.get(3).getValue().doubleValue());
        assertEquals(Map.of("quantile", "\"0.5\""), metrics.get(3).getLabels());

        assertEquals("go_gc_duration_seconds_sum", metrics.get(6).getName());
        assertEquals(0.394481746, metrics.get(6).getValue().doubleValue());
        assertEquals("summary", metrics.get(6).getType());
        assertEquals("go_gc_duration_seconds_count", metrics.get(7).getName());
        assertEquals(630, metrics.get(7).getValue().doubleValue());
        assertEquals("summary", metrics.get(7).getType());
    }

}