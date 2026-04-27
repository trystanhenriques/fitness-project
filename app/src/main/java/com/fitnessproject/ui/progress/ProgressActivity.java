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
import com.fitnessproject.ui.common.GroupedWorkoutListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProgressActivity extends AppCompatActivity {

    private static final String CATEGORY_ALL = "All Categories";

    private DatabaseHelper db;
    private TextView txtLastTime;
    private ListView listRecent;
    private Spinner categorySpinner;
    private Spinner exerciseSpinner;

    private final Map<String, Set<String>> exerciseToCategories = new LinkedHashMap<>();
    private final Map<String, String> normalizedExerciseKeyToDisplay = new LinkedHashMap<>();
    private final List<String> categoryList = new ArrayList<>();
    private final List<String> filteredExerciseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        db = new DatabaseHelper(this);
        categorySpinner = findViewById(R.id.spinnerProgressCategory);
        exerciseSpinner = findViewById(R.id.spinnerProgressExercise);
        txtLastTime = findViewById(R.id.txtLastTime);
        listRecent = findViewById(R.id.listRecent);

        buildExerciseCategoryIndex();
        setupCategorySpinner();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void buildExerciseCategoryIndex() {
        exerciseToCategories.clear();
        normalizedExerciseKeyToDisplay.clear();

        Map<String, List<String>> presetNameToCategories = DataLoader.getExerciseNameToCategoriesMap(this);
        for (Map.Entry<String, List<String>> entry : presetNameToCategories.entrySet()) {
            putExerciseWithCategories(entry.getKey(), entry.getValue());
        }

        List<DatabaseHelper.ExerciseGroupEntry> trackedEntries = db.getTrackedExercisesWithGroups();
        for (DatabaseHelper.ExerciseGroupEntry entry : trackedEntries) {
            String exerciseName = entry.getExerciseName();
            List<String> trackedCategories = DataLoader.parseCategoryCsv(entry.getExerciseGroup());
            if (trackedCategories.isEmpty()) {
                trackedCategories = DataLoader.getExerciseCategories(this, exerciseName);
            }
            putExerciseWithCategories(exerciseName, trackedCategories);
        }

        categoryList.clear();
        categoryList.add(CATEGORY_ALL);
        for (String category : DataLoader.getDefaultCategories()) {
            if (containsCategory(category)) {
                categoryList.add(category);
            }
        }
    }

    private void putExerciseWithCategories(String exerciseName, List<String> categories) {
        if (exerciseName == null) return;
        String displayName = exerciseName.trim();
        if (displayName.isEmpty()) return;

        String key = displayName.toLowerCase(Locale.US);
        String canonicalName = normalizedExerciseKeyToDisplay.get(key);

        List<String> normalizedCategories = categories == null || categories.isEmpty()
                ? DataLoader.parseCategoryCsv(null)
                : categories;

        if (canonicalName == null) {
            normalizedExerciseKeyToDisplay.put(key, displayName);
            exerciseToCategories.put(displayName, new LinkedHashSet<>(normalizedCategories));
            return;
        }

        Set<String> existingCategories = exerciseToCategories.get(canonicalName);
        if (existingCategories == null) {
            existingCategories = new LinkedHashSet<>();
            exerciseToCategories.put(canonicalName, existingCategories);
        }
        existingCategories.addAll(normalizedCategories);
    }

    private boolean containsCategory(String category) {
        for (Set<String> categories : exerciseToCategories.values()) {
            if (categories.contains(category)) {
                return true;
            }
        }
        return false;
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                categoryList
        );
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryList.get(position);
                refreshExerciseSpinnerForCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                refreshExerciseSpinnerForCategory(CATEGORY_ALL);
            }
        });
    }

    private void refreshExerciseSpinnerForCategory(String selectedCategory) {
        filteredExerciseList.clear();

        for (Map.Entry<String, Set<String>> entry : exerciseToCategories.entrySet()) {
            String exercise = entry.getKey();
            Set<String> categories = entry.getValue();
            if (CATEGORY_ALL.equals(selectedCategory) || categories.contains(selectedCategory)) {
                filteredExerciseList.add(exercise);
            }
        }

        Collections.sort(filteredExerciseList, String::compareToIgnoreCase);

        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner,
                filteredExerciseList
        );
        exerciseAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        exerciseSpinner.setAdapter(exerciseAdapter);

        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (filteredExerciseList.isEmpty()) {
                    showNoDataState("No exercises available for this category.");
                    return;
                }
                updateProgress(filteredExerciseList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showNoDataState("Select an exercise to view progress.");
            }
        });

        if (filteredExerciseList.isEmpty()) {
            showNoDataState("No exercises available for this category.");
        } else {
            updateProgress(filteredExerciseList.get(0));
        }
    }

    private void updateProgress(String exercise) {
        List<String> progress = db.getProgressForExerciseGroupedByDate(exercise);
        String latestSet = db.getLatestSetSummaryForExercise(exercise);
        if (progress.isEmpty()) {
            txtLastTime.setText("No data yet for " + exercise);
            listRecent.setAdapter(new ArrayAdapter<>(this, R.layout.item_list, new String[]{"No recent sets"}));
        } else {
            txtLastTime.setText(latestSet == null ? "—" : "Last set: " + latestSet);
            listRecent.setAdapter(new GroupedWorkoutListAdapter(this, progress));
        }
    }

    private void showNoDataState(String message) {
        txtLastTime.setText(message);
        listRecent.setAdapter(new ArrayAdapter<>(this, R.layout.item_list, new String[]{"No recent sets"}));
    }
}
