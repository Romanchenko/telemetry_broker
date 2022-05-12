package ru.cshse.project.settings.mongo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.AllArgsConstructor;
import ru.cshse.project.settings.Flag;
import ru.cshse.project.settings.FlagsDao;

/**
 * @author apollin
 */
@AllArgsConstructor
public class FlagsMongoDao implements FlagsDao {

    private final MongoCollection<FlagDto> collection;

    @Override
    public List<Flag> getAllFlags() {
        return StreamSupport
                .stream(
                        collection.find(Filters.empty())
                                .spliterator(),
                        false)
                .map(FlagDto::map)
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Flag> getFlag(String key) {
        return Optional.ofNullable(collection.find(Filters.eq(key)).first()).map(FlagDto::map);
    }

    @Override
    public void setFlag(String key, boolean value) {
        collection.updateOne(Filters.eq(key), Updates.set(FlagDto.VALUE_FIELD, value));
    }
}
