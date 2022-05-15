package ru.cshse.project.settings.mongo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author apollin
 */
@Configuration
@ConfigurationProperties(prefix = "settings.mongo")
@Data
public class MongoConnectionProperties {
    private String uri;
}
