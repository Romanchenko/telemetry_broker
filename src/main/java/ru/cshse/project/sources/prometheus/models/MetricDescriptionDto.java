package ru.cshse.project.sources.prometheus.models;

/**
 * @author apollin
 */

import lombok.Builder;
import lombok.Data;
import lombok.Value;

/**
 *  {
 *       "target": {
 *         "instance": "localhost:9090",
 *         "job": "prometheus"
 *       },
 *       "metric": "go_memstats_gc_cpu_fraction",
 *       "type": "gauge",
 *       "help": "The fraction of this program's available CPU time used by the GC since the program started.",
 *       "unit": ""
 *     },
 */
@Data
public class MetricDescriptionDto {
    private TargetDto target;
    private String metric;
    private String type;
    private String help;
    private String unit;
}
