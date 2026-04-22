package com.fitnessproject.ui.auth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fitnessproject.core.data.db.AppDatabase;
import com.fitnessproject.core.data.repository.AuthRepository;
import com.fitnessproject.core.session.SessionManager;

/**
 * Factory for creating the AuthViewModel with its required Repository dependency.
 * Since we don't have a dependency injection framework (like Hilt/Dagger), this manually
 * provides the necessary instances.
 */
public class AuthViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepository authRepository;

    public AuthViewModelFactory(Context context) {
        // Initialize dependencies purely using the application context
        Context appContext = context.getApplicationContext();
        AppDatabase database = AppDatabase.getInstance(appContext);
        SessionManager sessionManager = SessionManager.getInstance(appContext);

        this.authRepository = new AuthRepository(database.userDao(), sessionManager);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

