package com.fitnessproject.ui.planner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;

public class PlanDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_display);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weekly Plan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String plan = getIntent().getStringExtra("plan_text");
        String planGoal = getIntent().getStringExtra("plan_goal");
        String planSplit = getIntent().getStringExtra("plan_split");
        String planDescription = getIntent().getStringExtra("plan_description");

        TextView txtPlanGoal = findViewById(R.id.txtPlanGoal);
        TextView txtPlanSplit = findViewById(R.id.txtPlanSplitName);
        TextView txtPlanDescription = findViewById(R.id.txtPlanDescription);
        TextView txtPlanContent = findViewById(R.id.txtPlanContent);
        if (planGoal != null) {
            txtPlanGoal.setText(planGoal + " Plan");
        }
        if (planSplit != null) {
            txtPlanSplit.setText(planSplit);
        }
        if (planDescription != null) {
            txtPlanDescription.setText(planDescription);
        }
        if (plan != null) {
            txtPlanContent.setText(plan);
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
