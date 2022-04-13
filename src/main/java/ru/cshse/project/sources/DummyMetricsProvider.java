package ru.cshse.project.sources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import ru.cshse.project.models.PrometheusMetricDto;
import ru.cshse.project.parsing.PrometheusMetricsParser;

/**
 * @author apollin
 */
public class DummyMetricsProvider implements MetricsProvider {

    private static final String FILE_PATH = "/Users/apollin/Documents/univer/course_project/telemetry-broker/src/main/java/ru/cshse/project/sources/dummy_metrics.txt";

    private final PrometheusMetricsParser prometheusMetricsParser;
    @Override
    public Stream<PrometheusMetricDto> get() {
        try (var reader = new BufferedReader(new FileReader(FILE_PATH))) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        })
        return null;
    }
}
