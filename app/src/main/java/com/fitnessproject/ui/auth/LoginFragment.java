package com.fitnessproject.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fitnessproject.R;

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;

    private EditText editUsername;
    private EditText editPassword;
    private TextView textError;
    private ProgressBar progressBar;
    private Button btnLogin;
    private Button btnGuest;
    private Button btnGoToRegister;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        
        // Securely find all views keeping code thin simply wired up properly
        editUsername = view.findViewById(R.id.editUsername);
        editPassword = view.findViewById(R.id.editPassword);
        textError = view.findViewById(R.id.textError);
        progressBar = view.findViewById(R.id.progressBar);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnGuest = view.findViewById(R.id.btnGuest);
        btnGoToRegister = view.findViewById(R.id.btnGoToRegister);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Connect to Activity's shared ViewModel lifecycle strictly
        AuthViewModelFactory factory = new AuthViewModelFactory(requireContext());
        authViewModel = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);

        // Map inputs directly delegating cleanly offline down the stream
        btnLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            authViewModel.login(username, password);
        });

        btnGuest.setOnClickListener(v -> authViewModel.continueAsGuest());
        btnGoToRegister.setOnClickListener(v -> authViewModel.navigateToRegister());

        // Observe the unified State exposing UI error hints simply
        authViewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            boolean isLoading = state.getStatus() == AuthUiState.Status.LOADING;
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
            btnGuest.setEnabled(!isLoading);
            btnGoToRegister.setEnabled(!isLoading);

            if (state.getStatus() == AuthUiState.Status.ERROR) {
                textError.setVisibility(View.VISIBLE);
                textError.setText(state.getErrorMessage());
            } else {
                textError.setVisibility(View.GONE);
            }
        });
    }
}
