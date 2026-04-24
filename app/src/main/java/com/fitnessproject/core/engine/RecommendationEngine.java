package com.fitnessproject.core.engine;

import android.content.Context;
import com.fitnessproject.core.data.DataLoader;
import com.fitnessproject.core.data.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {

    public static class Recommendation {
        public String title;
        public String description;
        public List<String> items;

        public Recommendation(String title, String description, List<String> items) {
            this.title = title;
            this.description = description;
            this.items = items;
        }
    }

    public Recommendation getRecommendation(Context context, String exerciseId, String choiceId) {
        try {
            JSONObject nextStepsData = DataLoader.getNextSteps(context);
            List<String> recommendationItems = new ArrayList<>();
            
            // 1. Suggested Corrective Exercises based on errors
            if (exerciseId != null && choiceId != null) {
                JSONObject suggestions = nextStepsData.getJSONObject("suggestions");
                if (suggestions.has(exerciseId)) {
                    JSONObject exerciseSuggestions = suggestions.getJSONObject(exerciseId);
                    if (exerciseSuggestions.has(choiceId)) {
                        JSONArray items = exerciseSuggestions.getJSONArray(choiceId);
                        for (int i = 0; i < items.length(); i++) {
                            recommendationItems.add("Corrective: " + items.getString(i));
                        }
                    }
                }
            }

            // 2. Dynamic Plan based on Progress and User Feedback
            DatabaseHelper db = new DatabaseHelper(context);
            int workoutCount = db.getWorkoutCount();
            
            JSONObject plans = nextStepsData.getJSONObject("plans");
            JSONObject tier;
            
            if (workoutCount < 5) {
                tier = plans.getJSONObject("beginner");
            } else if (workoutCount < 20) {
                tier = plans.getJSONObject("intermediate");
            } else {
                tier = plans.getJSONObject("advanced");
            }

            JSONArray variations = tier.getJSONArray("variations");
            
            // Default variation is 'none'
            JSONObject selectedVariation = variations.getJSONObject(0); 

            // Logic to pick variation based on choiceId (user feedback)
            if (choiceId != null) {
                for (int i = 0; i < variations.length(); i++) {
                    JSONObject v = variations.getJSONObject(i);
                    String condition = v.getString("condition");
                    if (choiceId.contains(condition)) {
                        selectedVariation = v;
                        break;
                    }
                }
            }

            String title = selectedVariation.has("title") ? selectedVariation.getString("title") : tier.getString("title");
            String description = selectedVariation.has("description") ? selectedVariation.getString("description") : tier.getString("description");

            JSONArray planItems = selectedVariation.getJSONArray("items");
            for (int i = 0; i < planItems.length(); i++) {
                recommendationItems.add(planItems.getString(i));
            }

            return new Recommendation(title, description, recommendationItems);

        } catch (Exception e) {
            e.printStackTrace();
            return new Recommendation("General Plan", "Keep consistent with your current routine!", new ArrayList<>());
        }
    }
}
