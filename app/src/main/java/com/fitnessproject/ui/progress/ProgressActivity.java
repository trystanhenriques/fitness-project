package com.fitnessproject.ui.progress;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DataLoader;
import com.fitnessproject.core.data.DatabaseHelper;

import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView txtLastTime;
    private ListView listRecent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        db = new DatabaseHelper(this);

        Spinner spinner = findViewById(R.id.spinnerProgressExercise);
        txtLastTime = findViewById(R.id.txtLastTime);
        listRecent = findViewById(R.id.listRecent);

        List<String> exerciseList = DataLoader.getExerciseNames(this);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                exerciseList
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateProgress(exerciseList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updateProgress(String exercise) {
        List<String> progress = db.getProgressForExercise(exercise);

        if (progress.isEmpty()) {
            txtLastTime.setText("No data yet for " + exercise);
            listRecent.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"No recent sets"}));
        } else {
            txtLastTime.setText("Last set: " + progress.get(0));
            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    progress
            );
            listRecent.setAdapter(listAdapter);
        }
    }
}
