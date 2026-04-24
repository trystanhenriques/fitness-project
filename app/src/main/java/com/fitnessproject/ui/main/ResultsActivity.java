package com.fitnessproject.ui.main;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import com.fitnessproject.ui.formcheck.FormCheckStartActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;

import com.fitnessproject.core.data.DatabaseHelper;
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

        updateTierProgress();
        displayNextSteps();

        Button btnAnotherExercise = findViewById(R.id.btnAnotherExercise);
        btnAnotherExercise.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, FormCheckStartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void updateTierProgress() {
        DatabaseHelper db = new DatabaseHelper(this);
        int workoutCount = db.getWorkoutCount();
        
        ProgressBar pb = findViewById(R.id.progressTier);
        TextView txtTierName = findViewById(R.id.txtTierName);
        TextView txtTierProgress = findViewById(R.id.txtTierProgress);
        
        if (workoutCount < 5) {
            txtTierName.setText("Tier: Beginner Foundation");
            pb.setMax(5);
            pb.setProgress(workoutCount);
            txtTierProgress.setText((5 - workoutCount) + " workouts until Intermediate");
        } else if (workoutCount < 20) {
            txtTierName.setText("Tier: Intermediate Strength");
            pb.setMax(20);
            pb.setProgress(workoutCount);
            txtTierProgress.setText((20 - workoutCount) + " workouts until Advanced Performance");
        } else {
            txtTierName.setText("Tier: Advanced Performance");
            pb.setMax(100);
            pb.setProgress(100);
            txtTierProgress.setText("Top Tier Reached! Keep pushing your limits.");
        }
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

            // 2. Dynamic Plan based on Progress
            DatabaseHelper db = new DatabaseHelper(this);
            int workoutCount = db.getWorkoutCount();
            
            JSONObject plans = nextStepsData.getJSONObject("plans");
            JSONObject selectedPlan;
            
            if (workoutCount < 5) {
                selectedPlan = plans.getJSONObject("beginner");
            } else if (workoutCount < 20) {
                selectedPlan = plans.getJSONObject("intermediate");
            } else {
                selectedPlan = plans.getJSONObject("advanced");
            }

            sb.append(selectedPlan.getString("title")).append(":\n");
            sb.append(selectedPlan.getString("description")).append("\n");
            JSONArray planItems = selectedPlan.getJSONArray("items");
            for (int i = 0; i < planItems.length(); i++) {
                sb.append("- ").append(planItems.getString(i)).append("\n");
            }

            txtNextSteps.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            txtNextSteps.setText("Keep consistent with your current routine!");
        }
    }
}