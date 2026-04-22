package com.fitnessproject.ui.history;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;

import java.util.Arrays;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        ListView listView = findViewById(R.id.listHistory);

        // Placeholder data (we’ll replace with SQLite)
        List<String> items = Arrays.asList(
                "Bench Press — 185 x 5 (3 sets)",
                "Squat — 225 x 5 (3 sets)",
                "Deadlift — 275 x 3 (2 sets)"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );
        listView.setAdapter(adapter);
    }
}