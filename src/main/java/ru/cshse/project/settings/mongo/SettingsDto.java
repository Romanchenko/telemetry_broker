package ru.cshse.project.settings.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import ru.cshse.project.settings.MetricSettings;

/**
 * @author apollin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDto {
    private static final String ID_FIELD = "_id";
    private static final String EXPORT_ENABLED_FIELD = "exportEnabledField";

    @BsonId
    @BsonProperty(ID_FIELD)
    private String metricName;

    @BsonProperty(EXPORT_ENABLED_FIELD)
    private boolean exportEnabled;

    public MetricSettings map() {
        return new MetricSettings(
                metricName,
                exportEnabled
        );
    }
}
