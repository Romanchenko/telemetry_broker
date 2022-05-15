package ru.cshse.project.settings;


import java.util.List;
import java.util.Optional;

/**
 * @author apollin
 */
public interface FlagsDao {
    List<Flag> getAllFlags();
    Optional<Flag> getFlag(String key);
    void setFlag(String key, boolean value);
}
