package ru.cshse.project.settings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cshse.project.settings.SettingsService;
import ru.cshse.project.settings.controller.models.ClickHouseStats;
import ru.cshse.project.settings.nodeexporter.NodeExporterService;

/**
 * @author apollin
 */
@RestController
public class SettingsController {
    private final SettingsService settingsService;
    private final NodeExporterService nodeExporterService;

    @Autowired
    public SettingsController(SettingsService settingsService, NodeExporterService nodeExporterService) {
        this.settingsService = settingsService;
        this.nodeExporterService = nodeExporterService;
    }

    @PostMapping("/settings/export/enable")
    public String enableTask() {
        settingsService.enableTask();
        return "ENABLED";
    }

    @PostMapping("/settings/export/disable")
    public String disableTask() {
        settingsService.disableTask();
        return "DISABLED";
    }

    @GetMapping("/info/clickhouse/system")
    public ClickHouseStats getClickHouseStats() {
        return nodeExporterService.getStats();
    }
}
