package com.fitnessproject.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ArrayList<String> cues = getIntent().getStringArrayListExtra("cues");

        TextView txtCues = findViewById(R.id.txtCues);

        if (cues == null || cues.isEmpty()) {
            txtCues.setText("No cues available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String cue : cues) {
            sb.append("• ").append(cue).append("\n\n");
        }
        txtCues.setText(sb.toString());
    }
}