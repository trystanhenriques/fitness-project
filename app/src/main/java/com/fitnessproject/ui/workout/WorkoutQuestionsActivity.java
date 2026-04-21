package com.fitnessproject.ui.workout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.engine.FormCheckEngine;
import com.example.myapplication.model.Answer;
import com.example.myapplication.model.EvaluationResult;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_questions);

        String incomingExerciseId = getIntent().getStringExtra("exercise_id");
        final String exerciseId = (incomingExerciseId == null) ? "bench" : incomingExerciseId;

        RadioGroup rg = findViewById(R.id.rgChoices);
        Button btnComplete = findViewById(R.id.btnComplete);

        // For now: one question with three options
        RadioButton rbShoulders = new RadioButton(this);
        rbShoulders.setText("Shoulders");
        rbShoulders.setId(1);

        RadioButton rbWrists = new RadioButton(this);
        rbWrists.setText("Wrists");
        rbWrists.setId(2);

        RadioButton rbNone = new RadioButton(this);
        rbNone.setText("No discomfort");
        rbNone.setId(3);

        rg.addView(rbShoulders);
        rg.addView(rbWrists);
        rg.addView(rbNone);

        btnComplete.setOnClickListener(v -> {
            int checkedId = rg.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "Please select an option.", Toast.LENGTH_SHORT).show();
                return;
            }

            String choiceId = "none";
            if (checkedId == 1) choiceId = "shoulders";
            else if (checkedId == 2) choiceId = "wrists";

            FormCheckEngine engine = new FormCheckEngine();
            EvaluationResult result = engine.evaluate(
                    exerciseId,
                    Arrays.asList(new Answer("bench_q1_discomfort_location", choiceId))
            );

            Intent i = new Intent(WorkoutQuestionsActivity.this, ResultsActivity.class);
            i.putStringArrayListExtra("cues", new ArrayList<>(result.getCueIds()));
            startActivity(i);
        });
    }
}

