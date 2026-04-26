package com.fitnessproject.ui.workout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DataLoader;
import com.fitnessproject.core.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    private static final String CUSTOM_EXERCISE_OPTION = "Custom Exercise...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        Spinner spinner = findViewById(R.id.spinnerWorkoutExercise);
        EditText etCustomExercise = findViewById(R.id.etCustomExercise);
        View txtCustomExerciseLabel = findViewById(R.id.txtCustomExerciseLabel);
        EditText etWeight = findViewById(R.id.etWeight);
        EditText etReps = findViewById(R.id.etReps);
        EditText etSets = findViewById(R.id.etSets);
        Button btnSave = findViewById(R.id.btnSaveWorkout);

        List<String> exerciseList = new ArrayList<>(DataLoader.getExerciseNames(this));
        exerciseList.add(CUSTOM_EXERCISE_OPTION);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                exerciseList
        );
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = exerciseList.get(position);
                boolean showCustomInput = CUSTOM_EXERCISE_OPTION.equals(selected);
                txtCustomExerciseLabel.setVisibility(showCustomInput ? View.VISIBLE : View.GONE);
                etCustomExercise.setVisibility(showCustomInput ? View.VISIBLE : View.GONE);
                if (!showCustomInput) {
                    etCustomExercise.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtCustomExerciseLabel.setVisibility(View.GONE);
                etCustomExercise.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(v -> {
            String selectedExercise = (String) spinner.getSelectedItem();
            String customExercise = etCustomExercise.getText().toString().trim();
            String exercise = selectedExercise;
            String weight = etWeight.getText().toString().trim();
            String reps = etReps.getText().toString().trim();
            String sets = etSets.getText().toString().trim();

            if (CUSTOM_EXERCISE_OPTION.equals(selectedExercise)) {
                if (customExercise.isEmpty()) {
                    Toast.makeText(this, "Please enter a custom exercise name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                exercise = customExercise;
            }

            if (weight.isEmpty() || reps.isEmpty() || sets.isEmpty()) {
                Toast.makeText(this, "Please fill out weight, reps, and sets.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper db = new DatabaseHelper(this);
            db.addWorkout(exercise, weight, reps, sets);

            Toast.makeText(this, "Workout Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
