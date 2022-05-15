package ru.cshse.project.settings.mongo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import ru.cshse.project.settings.MetricSettings;
import ru.cshse.project.settings.SettingsDao;

/**
 * @author apollin
 */
@AllArgsConstructor
public class SettingsMongoDao implements SettingsDao {
    private final MongoCollection<SettingsDto> collection;

    @Override
    public List<MetricSettings> getAllSettings() {
        return StreamSupport
                .stream(collection.find(Filters.empty()).spliterator(), false)
                .map(SettingsDto::map)
                .collect(Collectors.toList());

    }

    @Override
    public Optional<MetricSettings> get(String name) {
        return Optional.ofNullable(collection.find(Filters.eq(name)).first()).map(SettingsDto::map);
    }
}
