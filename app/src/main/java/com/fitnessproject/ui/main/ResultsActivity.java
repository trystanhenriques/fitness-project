package com.fitnessproject.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;

import com.fitnessproject.core.data.DataLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<String> cues = getIntent().getStringArrayListExtra("cues");

        TextView txtCues = findViewById(R.id.txtCues);

        if (cues == null || cues.isEmpty()) {
            txtCues.setText("No cues available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String cue : cues) {
            sb.append("• ").append(cue).append("\n\n");
        }
        txtCues.setText(sb.toString());

        displayNextSteps();
    }

    private void displayNextSteps() {
        TextView txtNextSteps = findViewById(R.id.txtNextSteps);
        String exerciseId = getIntent().getStringExtra("exerciseId");
        String choiceId = getIntent().getStringExtra("choiceId"); // Need to pass this from engine

        try {
            JSONObject nextStepsData = DataLoader.getNextSteps(this);
            StringBuilder sb = new StringBuilder();

            // 1. Suggesed Exercises based on errors
            if (exerciseId != null && choiceId != null) {
                JSONObject suggestions = nextStepsData.getJSONObject("suggestions");
                if (suggestions.has(exerciseId)) {
                    JSONObject exerciseSuggestions = suggestions.getJSONObject(exerciseId);
                    if (exerciseSuggestions.has(choiceId)) {
                        JSONArray items = exerciseSuggestions.getJSONArray(choiceId);
                        sb.append("Corrective Exercises:\n");
                        for (int i = 0; i < items.length(); i++) {
                            sb.append("- ").append(items.getString(i)).append("\n");
                        }
                        sb.append("\n");
                    }
                }
            }

            // 2. Beginner Plan
            JSONObject beginnerPlan = nextStepsData.getJSONObject("beginner_plan");
            sb.append(beginnerPlan.getString("title")).append(":\n");
            sb.append(beginnerPlan.getString("description")).append("\n");

            txtNextSteps.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            txtNextSteps.setText("Keep consistent with your current routine!");
        }
    }
}