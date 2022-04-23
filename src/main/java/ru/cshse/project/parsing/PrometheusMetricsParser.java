package ru.cshse.project.parsing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cshse.project.models.PrometheusMetricDto;

/**
 * @author apollin
 */
public class PrometheusMetricsParser {

    private static final Logger logger = LoggerFactory.getLogger(PrometheusMetricsParser.class);

    private static final String HELP_LINE_PREFIX = "# HELP";
    private static final String TYPE_LINE_PREFIX = "# TYPE";

    private PrometheusMetricsParser() {
    }

    public static ParsingState start() {
        return new ParsingState(
                ParsingState.Status.START,
                new ArrayList<>(),
                null
        );
    }

    public static void read(String line, ParsingState state) {
        if (line.startsWith(HELP_LINE_PREFIX)) {
            if (
                    state.getStatus() != ParsingState.Status.START &&
                            state.getStatus() != ParsingState.Status.PROCESSING_DATA
            ) {
                throw new IllegalStateException(
                        String.format("State status is %s, when should be START or PROCESSING_DATA",
                                state.getStatus().name())
                );
            }
            // example : # HELP pilot_xds Number of endpoints connected to this pilot using XDS.
            var splitted = line.split(" ");
            String name = splitted[2];
            var description = String.join(" ", Arrays.asList(splitted).subList(3, splitted.length));
            state.setCurrentMetric(PrometheusMetricDto.builder().name(name).build());
            state.setStatus(ParsingState.Status.PROCESSED_HELP);
            return;
        }
        if (line.startsWith(TYPE_LINE_PREFIX)) {
            if (
                    state.getStatus() != ParsingState.Status.PROCESSED_HELP
            ) {
                throw new IllegalStateException(
                        String.format("State status is %s, when should be PROCESSED_HELP", state.getStatus().name())
                );
            }
            // example : # TYPE pilot_xds gauge
            var splitted = line.split(" ");
            String name = splitted[2];
            var type = splitted[3];
            if (!name.equals(state.getCurrentMetric().getName())) {
                throw new IllegalStateException(String.format("Metric name expected %s, but found %s in TYPE line",
                        state.getCurrentMetric().getName(), name));
            }
            state.addMetricType(type);
            state.setStatus(ParsingState.Status.PROCESSED_TYPE);
            return;
        }
        if (
                state.getStatus() != ParsingState.Status.PROCESSED_TYPE &&
                        state.getStatus() != ParsingState.Status.PROCESSING_DATA
        ) {
            throw new IllegalStateException(
                    String.format("State status is %s, when should be PROCESSED_TYPE or PROCESSING_DATA", state.getStatus().name())
            );
        }
        // example : pilot_xds_push_time_bucket{type="rds",le="10"} 1973

        var splitted = line.split(" ");
        state.addMetricValue(new BigDecimal(splitted[1]));
        addLabels(state, splitted[0]);
        state.addMetric(state.getCurrentMetric());
        state.setStatus(ParsingState.Status.PROCESSING_DATA);
    }

    private static void addLabels(ParsingState state, String s) {
        state.clearLabels();
        var nameFinishPosition = s.indexOf('{');
        var name = s.substring(0, nameFinishPosition == -1 ? s.length() : nameFinishPosition);
        // for cases, when metric has suffix _sum or _count
        state.addMetricName(name);
        if (nameFinishPosition == -1) {
            return;
        }

        var labels = s
                .replace(state.getCurrentMetric().getName(), "")
                .replace("{", "")
                .replace("}", "");

        Arrays.asList(labels.split(",")).forEach(labelAndValue -> {
            var splitted = labelAndValue.split("=");
            state.addMetricLabel(splitted[0], splitted[1]);
        });
    }

    public static void finish(ParsingState state) {
        state.setStatus(ParsingState.Status.FINISHED);
        state.setCurrentMetric(null);
    }
}
