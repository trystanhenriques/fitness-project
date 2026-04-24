package com.fitnessproject.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fitnessproject.R;
import com.fitnessproject.ui.auth.AuthActivity;

public class SettingsActivity extends AppCompatActivity {

    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Map UI components safely
        TextView txtAccountStatus = findViewById(R.id.txtAccountStatus);
        TextView txtRegisteredExplanation = findViewById(R.id.txtRegisteredExplanation);
        TextView txtGuestExplanation = findViewById(R.id.txtGuestExplanation);
        Button btnLogOut = findViewById(R.id.btnLogOut);
        Button btnLoginRegister = findViewById(R.id.btnLoginRegister);

        // Bind the manual factory since this architecture doesn't use Dagger/Hilt
        SettingsViewModelFactory factory = new SettingsViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);

        // Observe the current text identifier outputted natively by the view model
        viewModel.getAccountStatusText().observe(this, txtAccountStatus::setText);

        // Swap visual elements perfectly according to MVVM states (no logic needed intrinsically)
        viewModel.getIsGuestUser().observe(this, isGuest -> {
            if (isGuest != null) {
                if (isGuest) {
                    btnLogOut.setVisibility(View.GONE);
                    txtRegisteredExplanation.setVisibility(View.GONE);

                    btnLoginRegister.setVisibility(View.VISIBLE);
                    txtGuestExplanation.setVisibility(View.VISIBLE);
                } else {
                    btnLoginRegister.setVisibility(View.GONE);
                    txtGuestExplanation.setVisibility(View.GONE);

                    btnLogOut.setVisibility(View.VISIBLE);
                    txtRegisteredExplanation.setVisibility(View.VISIBLE);
                }
            }
        });

        // Clear the App backstack preventing floating memory bugs post-logout
        viewModel.getNavigateToAuthEvent().observe(this, navigate -> {
            if (navigate != null && navigate) {
                Intent returnIntent = new Intent(this, AuthActivity.class);
                returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(returnIntent);

                // Clear the event flag and destroy Settings view
                viewModel.onNavigatedToAuth();
                finish();
            }
        });

        // Delegate routing seamlessly to viewmodel functions
        btnLogOut.setOnClickListener(v -> viewModel.onLogoutClicked());
        btnLoginRegister.setOnClickListener(v -> viewModel.onLoginRegisterClicked());
    }
}

