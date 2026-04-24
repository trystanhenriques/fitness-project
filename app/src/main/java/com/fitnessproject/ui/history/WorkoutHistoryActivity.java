package com.fitnessproject.ui.history;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DatabaseHelper;

import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        ListView listView = findViewById(R.id.listHistory);

        DatabaseHelper db = new DatabaseHelper(this);
        List<String> items = db.getAllWorkouts();

        if (items.isEmpty()) {
            items.add("No workouts logged yet. Start training!");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );
        listView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
