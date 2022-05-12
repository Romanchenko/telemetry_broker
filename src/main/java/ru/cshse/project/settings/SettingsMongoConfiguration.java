package ru.cshse.project.settings;

import java.util.Arrays;

import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
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
        System.setProperty("javax.net.ssl.trustStore", "/opt/yandex/keystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "storepass");

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClient client = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(
                                new ServerAddress(mongoConnectionProperties.getHost(), mongoConnectionProperties.getPort())
                        )))
                .applyToSslSettings(builder ->
                        builder.enabled(true))
                .credential(MongoCredential.createCredential(
                        mongoConnectionProperties.getUser(),
                        "main",
                        mongoConnectionProperties.getPassword().toCharArray())
                )
                .codecRegistry(pojoCodecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build());
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
