package ru.cshse.project.settings.nodeexporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.cshse.project.models.PrometheusMetricDto;
import ru.cshse.project.parsing.PrometheusMetricsParser;
import ru.cshse.project.settings.controller.models.ClickHouseStats;
import ru.cshse.project.sources.kubernetes.PlainMetricsClient;

/**
 * user: The time spent in userland
 * system: The time spent in the kernel
 * iowait: Time spent waiting for I/O
 * idle: Time the CPU had nothing to do
 * irq&softirq: Time servicing interrupts
 * guest: If you are running VMs, the CPU they use
 * steal: If you are a VM, time other VMs "stole" from your CPUs
 *
 * @author apollin
 */
public class NodeExporterService {

    private static final Logger logger = LoggerFactory.getLogger(NodeExporterService.class);
    private static final String CPU_METRIC_NAME = "node_cpu_seconds_total";
    private static final String AVAILABLE_BYTES_METRIC_NAME = "node_memory_MemTotal_bytes";
    private static final String TOTAL_BYTES_METRIC_NAME = "node_memory_MemAvailable_bytes";
    private static final int NODE_EXPORTER_PORT = 9100;
    private static final String NODE_EXPORTER_ENDPOINT = "/metrics";

    private final String clickHouseIp;


    @Autowired
    public NodeExporterService(@Value("ch.connection.string") String clickHouseIp) {
        this.clickHouseIp = clickHouseIp;
    }

    public ClickHouseStats getStats() {
        var client = new PlainMetricsClient();
        String plainResult = client.get(clickHouseIp, NODE_EXPORTER_PORT, NODE_EXPORTER_ENDPOINT);
        return map(plainResult);
    }


    private ClickHouseStats map(String plainStringResult) {
        var parsedMetrics = parsePlainMetrics(plainStringResult);
        var result = ClickHouseStats.builder().build();
        parsedMetrics
                .stream()
                .filter(metric -> metric.getName().equals(CPU_METRIC_NAME))
                .forEach(metric -> {
                    var labels = metric.getLabels();
                    var mode = labels.get("mode");
                    switch (mode) {
                        case "iowait":
                            result.setCpuUsageIowait(
                                    result.getCpuUsageIowait() + metric.getValue().doubleValue());
                            break;
                        case "system":
                            result.setCpuUsageSystem(
                                    result.getCpuUsageSystem() + metric.getValue().doubleValue());
                            break;
                        case "user":
                            result.setCpuUsageUser(
                                    result.getCpuUsageUser() + metric.getValue().doubleValue());
                            break;
                        default:
                            logger.debug("Do not encounter {} mode", mode);
                            break;

                    }
                });
        BigDecimal available = parsedMetrics.stream()
                .filter(metric -> metric.getName().equals(AVAILABLE_BYTES_METRIC_NAME))
                .map(PrometheusMetricDto::getValue)
                .findAny()
                .orElse(BigDecimal.ZERO);
        BigDecimal total = parsedMetrics.stream()
                .filter(metric -> metric.getName().equals(TOTAL_BYTES_METRIC_NAME))
                .map(PrometheusMetricDto::getValue)
                .findAny()
                .orElse(BigDecimal.ONE);
        result.setDiskFreeBytesFraction(available.divide(total).doubleValue());
        return result;
    }

    private List<PrometheusMetricDto> parsePlainMetrics(String plainStringResult) {
        var reader = new BufferedReader(new StringReader(plainStringResult));
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
        return state.getProcessed();
    }
}
