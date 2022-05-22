package ru.cshse.project.settings.controller.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

/**
 * @author apollin
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClickHouseStats {
    double cpuUsageUser;
    double cpuUsageSystem;
    double cpuUsageIowait;
    double diskFreeBytesFraction;
}
