package com.fitnessproject.ui.planner;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fitnessproject.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlanDisplayActivity extends AppCompatActivity {
    private static final Pattern DAY_HEADER_PATTERN = Pattern.compile("(^Day \\d+.*$)", Pattern.MULTILINE);
    private static final Pattern SECTION_HEADER_PATTERN = Pattern.compile("(^Safety Notes$)", Pattern.MULTILINE);

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
            txtPlanContent.setText(stylePlanText(plan));
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private CharSequence stylePlanText(String plan) {
        SpannableStringBuilder styled = new SpannableStringBuilder(plan);
        applyHeaderStyle(styled, DAY_HEADER_PATTERN, 1.15f);
        applyHeaderStyle(styled, SECTION_HEADER_PATTERN, 1.1f);
        return styled;
    }

    private void applyHeaderStyle(SpannableStringBuilder builder, Pattern pattern, float sizeMultiplier) {
        Matcher matcher = pattern.matcher(builder.toString());
        while (matcher.find()) {
            builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    matcher.start(1),
                    matcher.end(1),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new RelativeSizeSpan(sizeMultiplier),
                    matcher.start(1),
                    matcher.end(1),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
