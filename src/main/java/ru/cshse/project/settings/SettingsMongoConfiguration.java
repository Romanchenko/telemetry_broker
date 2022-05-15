package ru.cshse.project.settings;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.cshse.project.settings.mongo.FlagDto;
import ru.cshse.project.settings.mongo.FlagsMongoDao;
import ru.cshse.project.settings.mongo.MongoConnectionProperties;
import ru.cshse.project.settings.mongo.SettingsDto;
import ru.cshse.project.settings.mongo.SettingsMongoDao;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author apollin
 */
@Import({
        MongoConnectionProperties.class
})
@Configuration
public class SettingsMongoConfiguration {

    private static final String DATABASE = "main";

    @Bean
    public MongoDatabase mongoDatabase(
            MongoConnectionProperties mongoConnectionProperties
    ) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient client = MongoClients.create(mongoConnectionProperties.getUri());
        return client
                .getDatabase(DATABASE)
                .withCodecRegistry(pojoCodecRegistry);
    }


    @Bean
    public SettingsDao settingsDao(MongoDatabase database) {
        return new SettingsMongoDao(database.getCollection("settings", SettingsDto.class));
    }

    @Bean
    public FlagsDao flagsDao(MongoDatabase database) {
        return new FlagsMongoDao(database.getCollection("flags", FlagDto.class));
    }
}
