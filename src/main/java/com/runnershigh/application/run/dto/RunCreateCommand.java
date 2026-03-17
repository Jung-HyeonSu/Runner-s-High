package com.runnershigh.application.run.dto;

public record RunCreateCommand(
        Long courseId,
        double distance
) {
}
