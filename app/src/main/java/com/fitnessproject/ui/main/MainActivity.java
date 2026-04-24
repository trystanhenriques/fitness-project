package com.fitnessproject.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.ui.formcheck.FormCheckStartActivity;
import com.fitnessproject.ui.planner.PlanBuilderActivity;
import com.fitnessproject.ui.settings.SettingsActivity;
import com.fitnessproject.ui.workout.WorkoutTrackerActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use your Home layout
        setContentView(R.layout.activity_home);

        Button btnFormCheck = findViewById(R.id.btnFormCheck);
        btnFormCheck.setOnClickListener(v ->
                startActivity(new Intent(this, FormCheckStartActivity.class)));

        Button btnWorkoutTracking = findViewById(R.id.btnWorkoutTracking);
        btnWorkoutTracking.setOnClickListener(v ->
                startActivity(new Intent(this, WorkoutTrackerActivity.class)));

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        Button btnPlanBuilder = findViewById(R.id.btnPlanBuilder);
        btnPlanBuilder.setOnClickListener(v ->
                startActivity(new Intent(this, PlanBuilderActivity.class)));
    }
}
