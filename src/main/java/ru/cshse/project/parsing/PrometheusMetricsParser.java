package ru.cshse.project.parsing;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author apollin
 */
public class PrometheusMetricsParser {

    private static final String HELP_LINE_PREFIX = "# HELP";
    private static final String TYPE_LINE_PREFIX = "# TYPE";

    public static ParsingState start() {
        return new ParsingState(
                ParsingState.Status.START,
                new ArrayList<>()
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

    }

}
