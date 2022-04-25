package ru.cshse.project.db;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseColumn;
import com.clickhouse.client.ClickHouseConfig;
import com.clickhouse.client.ClickHouseFormat;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseRequest;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.client.ClickHouseResponseSummary;
import com.clickhouse.client.data.BinaryStreamUtils;
import com.clickhouse.client.data.ClickHouseArrayValue;
import com.clickhouse.client.data.ClickHousePipedStream;
import com.clickhouse.client.data.ClickHouseRowBinaryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cshse.project.models.CHMetricDto;

/**
 * @author apollin
 */
@Component
public class MetricsDao {

    private static final Logger logger = LoggerFactory.getLogger(MetricsDao.class);

    private static final String DATABASE = "metrics";
    private static final String TABLE = "metrics";
    private static final ClickHouseFormat PREFERRED_FORMAT = ClickHouseFormat.RowBinaryWithNamesAndTypes;


    private ClickHouseNode server;
    private final ClickHouseClient client;

    @Autowired
    public MetricsDao(ClickHouseNode server, ClickHouseClient client) {
        this.server = server;
        this.client = client;
    }

    public void insert(List<CHMetricDto> metrics) {
        try {
            ClickHouseRequest.Mutation request = client.connect(server).write().table(TABLE)
                    .format(ClickHouseFormat.RowBinary);
            ClickHouseConfig config = request.getConfig();
            CompletableFuture<ClickHouseResponse> future = null;
            try (ClickHousePipedStream stream = new ClickHousePipedStream(config.getWriteBufferSize(),
                    config.getMaxQueuedBuffers(), config.getSocketTimeout())) {

                future = request.data(stream.getInput()).execute();

                for (var metric : metrics) {
                    writeMetric(stream, metric, config);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            // response should be always closed
            try (ClickHouseResponse response = future.get()) {
                ClickHouseResponseSummary summary = response.getSummary();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void writeMetric(
            ClickHousePipedStream stream, CHMetricDto metric, ClickHouseConfig config
    ) throws IOException {
        /**
         * ID
         */
        BinaryStreamUtils.writeUuid(stream, metric.getId());

        /**
         * ts
         */
        BinaryStreamUtils.writeDateTime64(stream,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(metric.getTs()), TimeZone.getDefault().toZoneId()),
                TimeZone.getDefault());

        /**
         * name
         */
        BinaryStreamUtils.writeString(stream, metric.getName());


        /**
         * value
         */
        BinaryStreamUtils.writeDecimal128(stream, metric.getValue(), 5);

        /**
         * labels
         */
        ClickHouseArrayValue<String> array = ClickHouseArrayValue.of(metric.getLabels().toArray(new String[0]));
        ClickHouseRowBinaryProcessor.getMappedFunctions().serialize(array, config,
                ClickHouseColumn.of("labels", "Array(String)"), stream);

        /**
         * Le label (Optional)
         */
        if (metric.getLe() != null) {
            BinaryStreamUtils.writeString(stream, metric.getLe());
        } else {
            BinaryStreamUtils.writeNull(stream);
        }

        /**
         * Quantile label (Optional)
         */
        if (metric.getQuantile() != null) {
            BinaryStreamUtils.writeString(stream, metric.getQuantile());
        } else {
            BinaryStreamUtils.writeNull(stream);
        }
    }
}
