package com.fitnessproject.ui.formcheck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class FormCheckStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_check_start);

        Spinner spinner = findViewById(R.id.spinnerExercise);
        Button btnContinue = findViewById(R.id.btnContinue);

        // Hardcoded list for now (we’ll move this to JSON later)
        String[] exercises = {"Bench Press", "Squat", "Deadlift", "Overhead Press", "Row"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                exercises
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnContinue.setOnClickListener(v -> {
            String selected = (String) spinner.getSelectedItem();
            String exerciseId = mapNameToId(selected);

            //Next screen (we’ll create it in Step 3)
            Intent i = new Intent(FormCheckStartActivity.this, WorkoutQuestionsActivity.class);
            startActivity(i);
            i.putExtra("exercise_id", exerciseId);
            startActivity(i);
        });
    }

    private String mapNameToId(String name) {
        if (name.equals("Bench Press")) return "bench";
        if (name.equals("Squat")) return "squat";
        if (name.equals("Deadlift")) return "deadlift";
        if (name.equals("Overhead Press")) return "ohp";
        return "row";
    }
}