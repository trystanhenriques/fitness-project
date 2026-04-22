package com.fitnessproject.core.engine;

import android.content.Context;
import com.fitnessproject.core.data.DataLoader;
import com.fitnessproject.core.model.Answer;
import com.fitnessproject.core.model.EvaluationResult;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FormCheckEngine {

    public EvaluationResult evaluate(Context context, String exerciseId, List<Answer> answers) {
        List<String> cues = new ArrayList<>();
        JSONObject rules = DataLoader.getRules(context);

        try {
            if (rules.has(exerciseId)) {
                JSONObject exerciseRules = rules.getJSONObject(exerciseId);
                for (Answer a : answers) {
                    if (exerciseRules.has(a.getQuestionId())) {
                        JSONObject questionRules = exerciseRules.getJSONObject(a.getQuestionId());
                        if (questionRules.has(a.getChoiceId())) {
                            cues.add(questionRules.getString(a.getChoiceId()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cues.isEmpty()) {
            cues.add("Keep focusing on steady tempo and full range of motion.");
        }

        return new EvaluationResult(exerciseId, cues);
    }
}
