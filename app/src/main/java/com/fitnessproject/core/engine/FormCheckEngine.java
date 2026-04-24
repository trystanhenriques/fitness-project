package com.fitnessproject.core.engine;

import android.content.Context;
import com.fitnessproject.core.data.DataLoader;
import com.fitnessproject.core.model.Answer;
import com.fitnessproject.core.model.EvaluationResult;
import org.json.JSONArray;
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
                            Object rule = questionRules.get(a.getChoiceId());
                            if (rule instanceof JSONArray) {
                                JSONArray array = (JSONArray) rule;
                                for (int i = 0; i < array.length(); i++) {
                                    cues.add(array.getString(i));
                                }
                            } else {
                                cues.add(rule.toString());
                            }
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
