package com.runnershigh.presentation.run.dto;

import com.runnershigh.application.run.dto.RunCreateCommand;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class RunCreateRequest {

    private Long courseId;

    @Positive
    private double distance;

    public RunCreateCommand toCommand() {
        return new RunCreateCommand(courseId, distance);
    }
}
