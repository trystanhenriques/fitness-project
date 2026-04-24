package com.fitnessproject.ui.settings;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fitnessproject.core.session.SessionManager;

/**
 * Factory for creating the SettingsViewModel with its required dependencies.
 * Since we don't have dependency injection (like Hilt/Dagger), this manually
 * provides the SessionManager instance seamlessly.
 */
public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private final SessionManager sessionManager;

    public SettingsViewModelFactory(Context context) {
        // Initialize dependencies purely using the application context
        Context appContext = context.getApplicationContext();
        this.sessionManager = SessionManager.getInstance(appContext);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(sessionManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

