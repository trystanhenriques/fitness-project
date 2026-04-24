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

        // 2. Process Free-text Description (Enhanced Keyword Matching)
        if (userDescription != null && !userDescription.isEmpty()) {
            String desc = userDescription.toLowerCase();
            boolean matched = false;

            if (desc.contains("back") || desc.contains("spine") || desc.contains("arch")) {
                cues.add("Back Feedback: Maintain a neutral spine. If you feel tightness, focus on core bracing and checking your hip hinge.");
                matched = true;
            }
            if (desc.contains("knee") || desc.contains("joint") || desc.contains("cave")) {
                cues.add("Knee Feedback: Ensure your knees are tracking over your toes. Avoid letting them cave inward during the lift.");
                matched = true;
            }
            if (desc.contains("shoulder") || desc.contains("neck") || desc.contains("trap")) {
                cues.add("Shoulder/Neck Feedback: Keep your shoulders set back and down. Avoid shrugging or excessive neck tension.");
                matched = true;
            }
            if (desc.contains("wrist") || desc.contains("grip") || desc.contains("hand")) {
                cues.add("Grip/Wrist Feedback: Keep your wrists 'stacked' and straight. Squeeze the bar hard to stabilize the joint.");
                matched = true;
            }
            if (desc.contains("elbow") || desc.contains("arm")) {
                cues.add("Elbow Feedback: Lead the movement with your elbows and ensure they aren't flaring out excessively.");
                matched = true;
            }
            if (desc.contains("heavy") || desc.contains("struggle") || desc.contains("stuck")) {
                cues.add("Intensity Feedback: If you are struggling with form, consider reducing the weight by 10% to master the movement.");
                matched = true;
            }
            
            if (desc.contains("pain") || desc.contains("hurt") || desc.contains("sharp")) {
                cues.add("CRITICAL SAFETY: You mentioned pain. Stop this exercise immediately. Sharp pain is a sign of injury; consult a professional.");
                matched = true;
            }

            if (!matched) {
                cues.add("Analysis: We've noted your feedback. Focus on a controlled tempo (2 seconds down, 1 second up) to improve stability.");
            }
        }

        if (cues.isEmpty()) {
            cues.add("Keep focusing on steady tempo and full range of motion.");
        }

        return new EvaluationResult(exerciseId, cues);
    }
}
