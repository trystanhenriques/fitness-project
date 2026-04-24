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
        TextView txtPlanContent = findViewById(R.id.txtPlanContent);
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
