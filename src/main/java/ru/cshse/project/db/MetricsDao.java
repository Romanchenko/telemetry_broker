package ru.cshse.project.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.virtusai.clickhouseclient.ClickHouseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cshse.project.models.CHMetricDto;

/**
 * @author apollin
 */
@Component
public class MetricsDao {

    private static final String DATABASE = "metrics";
    private static final String TABLE = "metrics";

    private final ClickHouseClient client;

    @Autowired
    public MetricsDao(ClickHouseClient client) {
        this.client = client;
    }

    public void insert(CHMetricDto metric) {
        Object[] entry = {metric.getId(), metric.getTs(), metric.getValue(), metric.getLabels()};
        List<Object[]> rows = new ArrayList<>();
        rows.add(entry);
        try {
            client.post(getInsertInto(), rows).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static String getInsertInto() {
        return String.format("INSERT INTO %s.%s", DATABASE, TABLE);
    }

}
