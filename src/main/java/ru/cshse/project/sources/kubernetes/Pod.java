package ru.cshse.project.sources.kubernetes;

import lombok.Value;

/**
 * @author apollin
 */
@Value
public class Pod {
    String ip;
    String appName;
    String namespace;
}
