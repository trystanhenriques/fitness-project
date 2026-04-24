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

    public EvaluationResult evaluate(Context context, String exerciseId, List<Answer> answers, String userDescription) {
        List<String> cues = new ArrayList<>();
        JSONObject rules = DataLoader.getRules(context);

        // 1. Process Multiple Choice Answers
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

        // 2. Process Free-text Description (Simple Keyword Matching for MVP)
        if (userDescription != null && !userDescription.isEmpty()) {
            String desc = userDescription.toLowerCase();
            if (desc.contains("back") || desc.contains("spine")) {
                cues.add("Your description mentions your back. Ensure you are bracing your core and keeping a neutral spine.");
            }
            if (desc.contains("knee") || desc.contains("joint")) {
                cues.add("You mentioned knee discomfort. Check your foot positioning and ensure your knees aren't caving in.");
            }
            if (desc.contains("heavy") || desc.contains("struggle")) {
                cues.add("If the weight feels too heavy to maintain form, don't be afraid to lower it by 5-10%.");
            }
            if (desc.contains("pain") || desc.contains("hurt")) {
                cues.add("IMPORTANT: If you feel sharp pain, stop immediately and consult a professional.");
            }
        }

        if (cues.isEmpty()) {
            cues.add("Keep focusing on steady tempo and full range of motion.");
        }

        return new EvaluationResult(exerciseId, cues);
    }
}
