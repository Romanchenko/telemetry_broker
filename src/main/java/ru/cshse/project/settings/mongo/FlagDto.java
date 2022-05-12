package ru.cshse.project.settings.mongo;

import lombok.Value;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import ru.cshse.project.settings.Flag;

/**
 * @author apollin
 */
@Value
public class FlagDto {
    public static final String ID_FIELD = "_id";
    public static final String VALUE_FIELD = "value";

    @BsonId
    @BsonProperty(ID_FIELD)
    String key;

    @BsonProperty(VALUE_FIELD)
    boolean value;

    public Flag map() {
        return new Flag(
                key, value
        );
    }

}
