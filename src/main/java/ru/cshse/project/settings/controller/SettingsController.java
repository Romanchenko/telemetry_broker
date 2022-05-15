package ru.cshse.project.settings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cshse.project.settings.SettingsService;

/**
 * @author apollin
 */
@RestController
public class SettingsController {
    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping("/settings/export/enable")
    public String enableTask() {
        settingsService.enableTask();
        return "OK";
    }

    @PostMapping("/settings/export/disable")
    public String disableTask() {
        settingsService.disableTask();
        return "OK";
    }
}
