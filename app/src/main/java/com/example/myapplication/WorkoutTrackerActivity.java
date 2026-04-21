package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkoutTrackerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_tracker);

        Button btnStartNew = findViewById(R.id.btnStartNewWorkout);
        Button btnProgress = findViewById(R.id.btnViewProgress);
        Button btnHistory = findViewById(R.id.btnWorkoutHistory);

        // Temporary placeholders (we’ll create these screens next)
        btnProgress.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, ProgressActivity.class)));
        btnProgress.setOnClickListener(v ->
                Toast.makeText(this, "Progress screen next", Toast.LENGTH_SHORT).show());

        btnHistory.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, WorkoutHistoryActivity.class)));
    }
}