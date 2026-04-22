package com.fitnessproject.ui.workout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DataLoader;

import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        Spinner spinner = findViewById(R.id.spinnerWorkoutExercise);
        EditText etWeight = findViewById(R.id.etWeight);
        EditText etReps = findViewById(R.id.etReps);
        EditText etSets = findViewById(R.id.etSets);
        Button btnSave = findViewById(R.id.btnSaveWorkout);

        List<String> exerciseList = DataLoader.getExerciseNames(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                exerciseList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            String exercise = (String) spinner.getSelectedItem();
            String weight = etWeight.getText().toString().trim();
            String reps = etReps.getText().toString().trim();
            String sets = etSets.getText().toString().trim();

            if (weight.isEmpty() || reps.isEmpty() || sets.isEmpty()) {
                Toast.makeText(this, "Please fill out weight, reps, and sets.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this,
                    "Saved: " + exercise + " " + weight + " x " + reps + " (" + sets + " sets)",
                    Toast.LENGTH_LONG).show();

            // Next step: save to SQLite (we’ll do that soon)
            finish();
        });
    }
}