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

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;

    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private TextView textError;
    private ProgressBar progressBar;
    private Button btnRegister;
    private Button btnBackToLogin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editUsername = view.findViewById(R.id.editUsername);
        editPassword = view.findViewById(R.id.editPassword);
        editConfirmPassword = view.findViewById(R.id.editConfirmPassword);
        textError = view.findViewById(R.id.textError);
        progressBar = view.findViewById(R.id.progressBar);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnBackToLogin = view.findViewById(R.id.btnBackToLogin);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthViewModelFactory factory = new AuthViewModelFactory(requireContext());
        authViewModel = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String pwd = editPassword.getText().toString();
            String confirm = editConfirmPassword.getText().toString();

            if (!pwd.equals(confirm)) {
                textError.setVisibility(View.VISIBLE);
                textError.setText("Passwords do not match");
                return;
            }
            String uname = editUsername.getText().toString();
            authViewModel.register(uname, pwd);
        });

        btnBackToLogin.setOnClickListener(v -> authViewModel.navigateToLogin());

        authViewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            boolean isLoading = state.getStatus() == AuthUiState.Status.LOADING;
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnRegister.setEnabled(!isLoading);
            btnBackToLogin.setEnabled(!isLoading);

            if (state.getStatus() == AuthUiState.Status.ERROR) {
                textError.setVisibility(View.VISIBLE);
                textError.setText(state.getErrorMessage());
            } else {
                textError.setVisibility(View.GONE);
            }
        });
    }
}

