package com.fitnessproject.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.session.SessionManager;

/**
 * ViewModel managing the business logic and state for the Settings screen.
 * Resolves whether the current user is a guest or a registered user,
 * and handles account management actions like logging out.
 */
public class SettingsViewModel extends ViewModel {

    private final SessionManager sessionManager;

    // UI State exposing the display text for the current user
    private final MutableLiveData<String> accountStatusText = new MutableLiveData<>();

    // UI State defining if the user is a guest (affects button labels)
    private final MutableLiveData<Boolean> isGuestUser = new MutableLiveData<>();

    // Navigation event to trigger a return to the AuthActivity
    private final MutableLiveData<Boolean> navigateToAuthEvent = new MutableLiveData<>();

    public SettingsViewModel(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        loadSessionState();
    }

    /**
     * Initializes the view state based on the current session.
     */
    private void loadSessionState() {
        UserSession currentSession = sessionManager.getCurrentSession();

        if (currentSession != null && !currentSession.isGuest()) {
            isGuestUser.setValue(false);
            accountStatusText.setValue("Signed in as: " + currentSession.getUsername());
        } else {
            isGuestUser.setValue(true);
            accountStatusText.setValue("Using Guest Mode");
        }
    }

    public LiveData<String> getAccountStatusText() {
        return accountStatusText;
    }

    public LiveData<Boolean> getIsGuestUser() {
        return isGuestUser;
    }

    public LiveData<Boolean> getNavigateToAuthEvent() {
        return navigateToAuthEvent;
    }

    /**
     * Called when a registered user clicks "Log Out".
     * Clears the current session and triggers navigation back to the authentication flow.
     */
    public void onLogoutClicked() {
        sessionManager.logout();
        navigateToAuthEvent.setValue(true);
    }

    /**
     * Called when a guest clicks "Log In / Register".
     * Clears the guest session and triggers navigation back to the authentication flow.
     */
    public void onLoginRegisterClicked() {
        sessionManager.logout(); // Works cleanly for guests as well by clearing tracking prefs
        navigateToAuthEvent.setValue(true);
    }

    /**
     * Call this from the Activity/Fragment after handling the navigation event
     * to prevent re-triggering on configuration changes.
     */
    public void onNavigatedToAuth() {
        navigateToAuthEvent.setValue(false);
    }
}

