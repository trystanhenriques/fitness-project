package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}