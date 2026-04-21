package com.example.myapplication.engine;

import com.example.myapplication.model.Answer;
import com.example.myapplication.model.EvaluationResult;

import java.util.ArrayList;
import java.util.List;

public class FormCheckEngine {

    public EvaluationResult evaluate(String exerciseId, List<Answer> answers) {
        List<String> cues = new ArrayList<>();

        // MVP v0: one question for bench
        if ("bench".equals(exerciseId)) {
            for (Answer a : answers) {
                if ("bench_q1_discomfort_location".equals(a.getQuestionId())
                        && "shoulders".equals(a.getChoiceId())) {
                    cues.add("Tuck elbows (about 45°) and keep shoulders set back.");
                } else if ("bench_q1_discomfort_location".equals(a.getQuestionId())
                        && "wrists".equals(a.getChoiceId())) {
                    cues.add("Stack wrists over elbows and keep knuckles up.");
                }
            }
        }

        if (cues.isEmpty()) {
            cues.add("Reduce weight slightly and focus on control and steady tempo.");
        }

        return new EvaluationResult(exerciseId, cues);
    }
}
