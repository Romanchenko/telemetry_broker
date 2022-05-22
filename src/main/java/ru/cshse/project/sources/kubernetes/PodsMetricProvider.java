package ru.cshse.project.sources.kubernetes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cshse.project.models.PrometheusMetricDto;
import ru.cshse.project.parsing.PrometheusMetricsParser;
import ru.cshse.project.sources.MetricsProvider;

/**
 * @author apollin
 */
public class PodsMetricProvider implements MetricsProvider {

    private static final Logger logger = LoggerFactory.getLogger(PodsMetricProvider.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final PodsRegistry registry;
    private final PlainMetricsClient client;
    private final int threadCount;

    public PodsMetricProvider(
            PodsRegistry registry,
            PlainMetricsClient client,
            int threadCount
    ) {
        this.registry = registry;
        this.client = client;
        this.threadCount = threadCount;
    }

    @Override
    public Stream<PrometheusMetricDto> get() {
        var pods = registry.getAll();
        logger.info("Will try to get metrics from {} pods", pods.size());
        var executor = Executors.newFixedThreadPool(threadCount);
        var futures = pods.stream()
                .map(pod -> executor.submit(new Job(pod)))
                .collect(Collectors.toList());

        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("Executor did not terminate in {}", TIMEOUT, e);
        }
        return futures.stream().flatMap(future -> {
            try {
                return Optional.of(future.get()).stream();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to get form future", e);
                return Stream.empty();
            }
        }).flatMap(PodsMetricProvider::map);
    }

    private static Stream<PrometheusMetricDto> map(JobResult jobResult) {
        var plainStringResult = jobResult.getPlainString();
        if (plainStringResult.isEmpty()) {
            return Stream.empty();
        }
        var reader = new BufferedReader(new StringReader(plainStringResult.get()));
        var state = PrometheusMetricsParser.start();
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                logger.error("Got IOException", e);
                continue;
            }
            if (line == null) {
                break;
            }
            PrometheusMetricsParser.read(line, state);
        }
        PrometheusMetricsParser.finish(state);

        return state.getProcessed().stream().map(metric -> enrich(metric, jobResult.getPod()));
    }

    private static PrometheusMetricDto enrich(PrometheusMetricDto metric, Pod pod) {
        var labels = new HashMap<>(metric.getLabels());
        labels.put("ip", pod.getIp());
        labels.put("app", pod.getAppName());
        labels.put("namespace", pod.getNamespace());
        return metric.toBuilder().labels(labels).build();
    }

    private final class Job implements Callable<JobResult> {

        private final Pod pod;

        private Job(Pod pod) {
            this.pod = pod;
        }

        @Override
        public JobResult call() {
            Optional<String> result = Optional.empty();
            try {
                result = Optional.of(client.get(pod.getIp()));
            } catch (Exception e) {
                logger.error("Could not get metrics from {}, {}", pod.getAppName(), pod.getIp(), e);
            }
            return new JobResult(
                    result,
                    pod
            );
        }
    }

    @Value
    private static class JobResult {
        Optional<String> plainString;
        Pod pod;
    }
}
