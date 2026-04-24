package com.fitnessproject.core.data;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open("data/" + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<String> getExerciseNames(Context context) {
        List<String> names = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "exercises.json"));
            for (int i = 0; i < array.length(); i++) {
                names.add(array.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static String mapNameToId(Context context, String name) {
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset(context, "exercises.json"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("name").equals(name)) {
                    return obj.getString("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "bench"; // default
    }

    public static JSONObject getQuestionsForExercise(Context context, String exerciseId) {
        try {
            JSONObject allQuestions = new JSONObject(loadJSONFromAsset(context, "questions.json"));
            if (allQuestions.has(exerciseId)) {
                JSONArray exerciseQuestions = allQuestions.getJSONArray(exerciseId);
                if (exerciseQuestions.length() > 0) {
                    return exerciseQuestions.getJSONObject(0); // For MVP, return the first question
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getRules(Context context) {
        try {
            String json = loadJSONFromAsset(context, "rules.json");
            if (json == null) return new JSONObject();
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public static JSONObject getNextSteps(Context context) {
        try {
            String json = loadJSONFromAsset(context, "next_steps.json");
            if (json == null) return new JSONObject();
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
