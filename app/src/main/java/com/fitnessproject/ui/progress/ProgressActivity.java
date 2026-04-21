package com.fitnessproject.ui.progress;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Spinner spinner = findViewById(R.id.spinnerProgressExercise);
        TextView txtLastTime = findViewById(R.id.txtLastTime);
        ListView listRecent = findViewById(R.id.listRecent);

        String[] exercises = {"Bench Press", "Squat", "Deadlift", "Overhead Press", "Row"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                exercises
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Placeholder data for MVP UI (we’ll replace with SQLite queries)
        txtLastTime.setText("Bench Press — 185 x 5 (3 sets)");

        List<String> recent = Arrays.asList(
                "190 x 5 (3 sets) — today",
                "185 x 5 (3 sets) — last week",
                "175 x 5 (3 sets) — two weeks ago"
        );

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                recent
        );
        listRecent.setAdapter(listAdapter);

        // Later: add a listener so changing the spinner updates Last time + Recent from SQLite
    }
}