package ru.cshse.project.importer;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;

public class MetricsImportTask {
    @Scheduled(fixedDelayString = "${import.task.fixedDelay.in.milliseconds}")
    public void run() {
        System.out.println(Instant.now());
    }

}