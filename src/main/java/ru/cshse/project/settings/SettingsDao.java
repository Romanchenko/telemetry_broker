package ru.cshse.project.settings;

import java.util.List;
import java.util.Optional;

/**
 * @author apollin
 */
public interface SettingsDao {
    List<MetricSettings> getAllSettings();

    Optional<MetricSettings> get(String name);
}
