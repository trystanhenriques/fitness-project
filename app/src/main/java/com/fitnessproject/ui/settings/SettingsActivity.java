package com.fitnessproject.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FitnessPrefs";
    private static final String KEY_DISCLAIMER_ACCEPTED = "disclaimer_accepted";
    private static final String KEY_TEXT_SIZE = "text_size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings & Disclaimer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Disclaimer Checkbox
        CheckBox cbDisclaimer = findViewById(R.id.cbDisclaimer);
        cbDisclaimer.setChecked(prefs.getBoolean(KEY_DISCLAIMER_ACCEPTED, false));
        cbDisclaimer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DISCLAIMER_ACCEPTED, isChecked).apply();
        });

        // Text Size RadioGroup
        RadioGroup rgTextSize = findViewById(R.id.rgTextSize);
        int savedTextSize = prefs.getInt(KEY_TEXT_SIZE, 1); // 0: Small, 1: Normal, 2: Large
        if (savedTextSize == 0) rgTextSize.check(R.id.rbSmall);
        else if (savedTextSize == 2) rgTextSize.check(R.id.rbLarge);
        else rgTextSize.check(R.id.rbMedium);

        rgTextSize.setOnCheckedChangeListener((group, checkedId) -> {
            int size = 1;
            if (checkedId == R.id.rbSmall) size = 0;
            else if (checkedId == R.id.rbLarge) size = 2;
            prefs.edit().putInt(KEY_TEXT_SIZE, size).apply();
            Toast.makeText(this, "Text size preference saved. Restart app to apply fully.", Toast.LENGTH_SHORT).show();
        });

        // Delete History
        Button btnDeleteHistory = findViewById(R.id.btnDeleteHistory);
        btnDeleteHistory.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete History")
                    .setMessage("Are you sure you want to delete all workout history? This cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        DatabaseHelper db = new DatabaseHelper(this);
                        db.deleteAllWorkouts();
                        Toast.makeText(this, "All history deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
