package com.fitnessproject.ui.planner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;

public class PlanBuilderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_builder);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weekly Plan Builder");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Spinner spinnerGoal = findViewById(R.id.spinnerGoal);
        String[] goals = {"Strength", "Hypertrophy", "Endurance", "Custom (Use Description)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goals);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(adapter);

        RadioGroup rgDays = findViewById(R.id.rgDays);
        EditText etGoals = findViewById(R.id.etSpecificGoals);
        Button btnGenerate = findViewById(R.id.btnGeneratePlan);
        TextView txtPlan = findViewById(R.id.txtWeeklyPlan);

        // Define all radio button IDs
        int[] rbIds = {R.id.rb2, R.id.rb3, R.id.rb4, R.id.rb5, R.id.rb6, R.id.rb7};

        // Manually handle mutual exclusivity because of nested Layouts in RadioGroup
        for (int id : rbIds) {
            RadioButton rb = findViewById(id);
            rb.setOnClickListener(view -> {
                for (int otherId : rbIds) {
                    if (otherId != id) {
                        ((RadioButton) findViewById(otherId)).setChecked(false);
                    }
                }
            });
        }

        btnGenerate.setOnClickListener(v -> {
            String days = "3"; // Default
            for (int id : rbIds) {
                RadioButton rb = findViewById(id);
                if (rb.isChecked()) {
                    days = rb.getText().toString();
                    break;
                }
            }

            String goal = spinnerGoal.getSelectedItem().toString();
            String userGoals = etGoals.getText().toString().trim();

            generatePlan(goal, Integer.parseInt(days), userGoals, txtPlan);

            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private void generatePlan(String goal, int days, String userGoals, TextView output) {
        StringBuilder sb = new StringBuilder();
        String lowerGoals = userGoals.toLowerCase();

        // 1. Determine Intensity (Scheme) - Text Keywords OVERRIDE Spinner
        String scheme = " 3x10";
        String intensitySource = "Goal: " + goal;

        if (lowerGoals.contains("strength") || lowerGoals.contains("heavy") || lowerGoals.contains("power")) {
            scheme = " 5x5";
            intensitySource = "Custom Strength Focus";
        } else if (lowerGoals.contains("endurance") || lowerGoals.contains("light") || lowerGoals.contains("high rep") || lowerGoals.contains("cardio")) {
            scheme = " 3x20";
            intensitySource = "Custom Endurance Focus";
        } else if (!goal.equals("Custom (Use Description)")) {
            if (goal.equals("Strength")) scheme = " 5x5";
            else if (goal.equals("Hypertrophy")) scheme = " 3x10";
            else if (goal.equals("Endurance")) scheme = " 3x20";
        }

        sb.append("Your Personalized Weekly Schedule\n");
        sb.append("Focus Mode: ").append(intensitySource).append(" (").append(scheme.trim()).append(")\n\n");

        for (int i = 1; i <= days; i++) {
            sb.append("Day ").append(i).append(":\n");
            boolean isOddDay = (i % 2 != 0);
            boolean addedCustom = false;

            // 2. Base Plan (Unless "Custom" is selected and keywords are found)
            if (!goal.equals("Custom (Use Description)")) {
                if (goal.equals("Strength")) {
                    if (isOddDay) sb.append("- Squat 5x5\n- Bench Press 5x5\n");
                    else sb.append("- Deadlift 1x5\n- Overhead Press 5x5\n");
                } else if (goal.equals("Hypertrophy")) {
                    if (isOddDay) sb.append("- Incline Press 3x10\n- Lat Pulldowns 3x12\n");
                    else sb.append("- Leg Press 3x15\n- Shoulder Press 3x12\n");
                } else {
                    sb.append("- Cardio/Running: 20 mins\n- Planks 3x60s\n");
                }
            }

            // 3. Additive Description Logic (Adds to base OR creates custom)
            if (lowerGoals.contains("leg") || lowerGoals.contains("squat") || lowerGoals.contains("lower body")) {
                sb.append("- [Feedback] Romanian Deadlift ").append(scheme).append("\n- [Feedback] Lunges ").append(scheme).append("\n");
                addedCustom = true;
            }
            if (lowerGoals.contains("chest") || lowerGoals.contains("bench") || lowerGoals.contains("push") || lowerGoals.contains("upper body")) {
                sb.append("- [Feedback] Chest Flys 3x15\n- [Feedback] Pushups 3xMax\n");
                addedCustom = true;
            }
            if (lowerGoals.contains("back") || lowerGoals.contains("pull") || lowerGoals.contains("row")) {
                sb.append("- [Feedback] Face Pulls 3x20\n- [Feedback] Dumbbell Rows ").append(scheme).append("\n");
                addedCustom = true;
            }
            if (lowerGoals.contains("arm") || lowerGoals.contains("bicep") || lowerGoals.contains("tricep")) {
                sb.append("- [Feedback] Bicep Curls 3x15\n- [Feedback] Tricep Extensions 3x15\n");
                addedCustom = true;
            }
            if (lowerGoals.contains("core") || lowerGoals.contains("abs") || lowerGoals.contains("stomach")) {
                sb.append("- [Feedback] Hanging Leg Raises 3x15\n- [Feedback] Russian Twists 3x20\n");
                addedCustom = true;
            }

            // 4. Final Fallback if screen would be empty
            if (!addedCustom && goal.equals("Custom (Use Description)")) {
                sb.append("- Full Body Circuit: 3 rounds\n- Pushups / Squats / Lunges\n- Focus on movement quality.\n");
            }
            sb.append("\n");
        }

        Intent intent = new Intent(this, PlanDisplayActivity.class);
        intent.putExtra("plan_text", sb.toString());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
