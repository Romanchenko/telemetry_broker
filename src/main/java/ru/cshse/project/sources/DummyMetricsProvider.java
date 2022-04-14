package ru.cshse.project.sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import ru.cshse.project.models.PrometheusMetricDto;
import ru.cshse.project.parsing.PrometheusMetricsParser;

/**
 * @author apollin
 */
@Service
public class DummyMetricsProvider implements MetricsProvider {

    private static final String FILE_PATH = new File(
            "src/main/java/ru/cshse/project/sources/dummy_metrics.txt").getAbsolutePath();

    private final String filePath;

    public DummyMetricsProvider() {
        this.filePath = FILE_PATH;
    }

    DummyMetricsProvider(String path) {
        this.filePath = path;
    }

    @Override
    public Stream<PrometheusMetricDto> get() {
        var state = PrometheusMetricsParser.start();
        try (var reader = new BufferedReader(new FileReader(filePath))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                PrometheusMetricsParser.read(line, state);
            }
            PrometheusMetricsParser.finish(state);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return state.getProcessed().stream();
    }
}
