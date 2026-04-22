package com.fitnessproject.ui.auth;

/**
 * Represents the various states of the Authentication UI.
 * Used by the ViewModel to expose a single observable state object to the Activities/Fragments.
 */
public class AuthUiState {

    public enum Status {
        IDLE,           // Waiting for user input
        LOADING,        // Authenticating or registering (show spinner)
        SUCCESS,        // Successfully logged in or registered (navigate to main)
        GUEST_SUCCESS,  // Successfully continued as guest (navigate to main)
        ERROR           // Validation or authentication failure (show error message)
    }

    private final Status status;
    private final String errorMessage;

    private AuthUiState(Status status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static AuthUiState idle() {
        return new AuthUiState(Status.IDLE, null);
    }

    public static AuthUiState loading() {
        return new AuthUiState(Status.LOADING, null);
    }

    public static AuthUiState success() {
        return new AuthUiState(Status.SUCCESS, null);
    }

    public static AuthUiState guestSuccess() {
        return new AuthUiState(Status.GUEST_SUCCESS, null);
    }

    public static AuthUiState error(String message) {
        return new AuthUiState(Status.ERROR, message);
    }

    public Status getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

