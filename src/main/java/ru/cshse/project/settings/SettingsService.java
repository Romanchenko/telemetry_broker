package ru.cshse.project.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author apollin
 */
@Service
public class SettingsService {

    private static final String EXPORT_TASK_ACTIVE_FLAG = "exportTaskActive";

    private final SettingsDao settingsDao;
    private final FlagsDao flagsDao;

    @Autowired
    public SettingsService(SettingsDao settingsDao, FlagsDao flagsDao) {
        this.settingsDao = settingsDao;
        this.flagsDao = flagsDao;
    }

    public boolean isExportTaskEnabled() {
        return flagsDao.getFlag(EXPORT_TASK_ACTIVE_FLAG).map(Flag::isValue).orElse(false);
    }

    public void enableTask() {
        flagsDao.setFlag(EXPORT_TASK_ACTIVE_FLAG, true);
    }

    public void disableTask() {
        flagsDao.setFlag(EXPORT_TASK_ACTIVE_FLAG, false);
    }
}
