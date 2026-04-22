package com.fitnessproject.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitnessproject.core.data.model.AuthResult;
import com.fitnessproject.core.data.model.Credentials;
import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.data.repository.AuthRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Shared ViewModel for handling both Login and Registration flows.
 * Exposes LiveData for UI state and screen navigation to keep Fragments thin.
 */
public class AuthViewModel extends ViewModel {

    public enum AuthScreen {
        LOGIN,
        REGISTER
    }

    private final AuthRepository authRepository;
    
    // Executor for offloading database calls from the main thread
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // LiveData for the authentication process state
    private final MutableLiveData<AuthUiState> uiState = new MutableLiveData<>(AuthUiState.idle());
    
    // LiveData for controlling which fragment is currently displayed
    private final MutableLiveData<AuthScreen> currentScreen = new MutableLiveData<>(AuthScreen.LOGIN);

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<AuthUiState> getUiState() {
        return uiState;
    }

    public LiveData<AuthScreen> getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Attempts to log in the user.
     */
    public void login(String username, String password) {
        if (!validateInput(username, password)) return;

        uiState.setValue(AuthUiState.loading());

        executorService.execute(() -> {
            Credentials credentials = new Credentials(username, password);
            AuthResult<UserSession> result = authRepository.login(credentials);
            
            if (result.isSuccess()) {
                uiState.postValue(AuthUiState.success());
            } else {
                uiState.postValue(AuthUiState.error(result.getErrorMessage()));
            }
        });
    }

    /**
     * Attempts to register a new user.
     */
    public void register(String username, String password) {
        if (!validateInput(username, password)) return;

        uiState.setValue(AuthUiState.loading());

        executorService.execute(() -> {
            Credentials credentials = new Credentials(username, password);
            AuthResult<UserSession> result = authRepository.register(credentials);
            
            if (result.isSuccess()) {
                uiState.postValue(AuthUiState.success());
            } else {
                uiState.postValue(AuthUiState.error(result.getErrorMessage()));
            }
        });
    }

    /**
     * Starts a guest session and navigates away.
     */
    public void continueAsGuest() {
        uiState.setValue(AuthUiState.loading());
        
        executorService.execute(() -> {
            authRepository.continueAsGuest();
            uiState.postValue(AuthUiState.guestSuccess());
        });
    }

    /**
     * Basic input validation before hitting the repository.
     */
    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            uiState.setValue(AuthUiState.error("Username is required"));
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            uiState.setValue(AuthUiState.error("Password is required"));
            return false;
        }
        if (password.length() < 6) {
            uiState.setValue(AuthUiState.error("Password must be at least 6 characters"));
            return false;
        }
        return true;
    }

    /**
     * Resets state after successful navigation to prevent re-triggering events.
     */
    public void resetState() {
        uiState.setValue(AuthUiState.idle());
    }

    /**
     * Navigates the UI to the Registration screen.
     */
    public void navigateToRegister() {
        uiState.setValue(AuthUiState.idle());
        currentScreen.setValue(AuthScreen.REGISTER);
    }

    /**
     * Navigates the UI back to the Login screen.
     */
    public void navigateToLogin() {
        uiState.setValue(AuthUiState.idle());
        currentScreen.setValue(AuthScreen.LOGIN);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}

