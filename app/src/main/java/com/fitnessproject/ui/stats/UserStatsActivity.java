package com.fitnessproject.ui.stats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;
import com.fitnessproject.core.data.DatabaseHelper;
import com.fitnessproject.ui.workout.NewWorkoutActivity;

import java.util.Locale;

public class UserStatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DatabaseHelper db = new DatabaseHelper(this);
        DatabaseHelper.UserStatsSummary stats = db.getUserStatsSummary();

        bindStats(stats);

        Button btnEmptyStart = findViewById(R.id.btnStatsEmptyStart);
        btnEmptyStart.setOnClickListener(v ->
                startActivity(new Intent(this, NewWorkoutActivity.class)));
    }

    private void bindStats(DatabaseHelper.UserStatsSummary stats) {
        LinearLayout emptyState = findViewById(R.id.emptyStatsState);
        LinearLayout contentState = findViewById(R.id.statsContent);

        if (!stats.hasData) {
            emptyState.setVisibility(View.VISIBLE);
            contentState.setVisibility(View.GONE);
            return;
        }

        emptyState.setVisibility(View.GONE);
        contentState.setVisibility(View.VISIBLE);

        setText(R.id.txtStatsHeroValue, String.valueOf(stats.totalWorkouts));
        setText(R.id.txtStatsHeroLabel, stats.lastWorkoutDayLabel + " • " + stats.lastWorkoutDateLabel);

        setText(R.id.txtTotalWorkoutsValue, String.valueOf(stats.totalWorkouts));
        setText(R.id.txtTotalSetsValue, String.valueOf(stats.totalSets));
        setText(R.id.txtTotalRepsValue, String.valueOf(stats.totalReps));
        setText(R.id.txtDistinctExercisesValue, String.valueOf(stats.distinctExercises));

        setText(R.id.txtLast7Value, String.valueOf(stats.workoutsLast7Days));
        setText(R.id.txtLast30Value, String.valueOf(stats.workoutsLast30Days));
        setText(R.id.txtAvgWeekValue, formatOneDecimal(stats.averageWorkoutsPerWeek));
        setText(R.id.txtActiveDaysValue, String.valueOf(stats.activeDaysThisMonth));

        setText(R.id.txtMostLoggedValue, stats.mostLoggedExerciseLabel);
        setText(R.id.txtHeaviestValue, stats.heaviestSetLabel);
        setText(R.id.txtFavoriteCategoryValue, stats.favoriteCategoryLabel);
    }

    private void setText(int viewId, String value) {
        TextView textView = findViewById(viewId);
        textView.setText(value);
    }

    private String formatOneDecimal(double value) {
        return String.format(Locale.US, "%.1f", value);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
