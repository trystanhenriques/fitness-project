package com.fitnessproject.core.model;

import java.util.List;

public class EvaluationResult {
    private final String exerciseId;
    private final List<String> cueIds;

    public EvaluationResult(String exerciseId, List<String> cueIds) {
        this.exerciseId = exerciseId;
        this.cueIds = cueIds;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public List<String> getCueIds() {
        return cueIds;
    }
}
