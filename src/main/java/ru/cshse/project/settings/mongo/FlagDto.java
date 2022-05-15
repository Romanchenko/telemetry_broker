package ru.cshse.project.settings.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import ru.cshse.project.settings.Flag;

/**
 * @author apollin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlagDto {
    public static final String ID_FIELD = "_id";
    public static final String VALUE_FIELD = "value";

    @BsonId
    @BsonProperty(ID_FIELD)
    private String key;

    @BsonProperty(VALUE_FIELD)
    private boolean value;


    public Flag map() {
        return new Flag(
                key, value
        );
    }

}
