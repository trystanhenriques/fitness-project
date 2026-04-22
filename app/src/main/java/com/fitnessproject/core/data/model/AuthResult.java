package com.fitnessproject.core.data.model;

/**
 * Generic wrapper for passing status and data from the Repository
 * back up to the ViewModel cleanly without involving UI side-effects directly.
 */
public class AuthResult<T> {
    private final boolean success;
    private final T data;
    private final String errorMessage;

    private AuthResult(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> AuthResult<T> success(T data) {
        return new AuthResult<>(true, data, null);
    }

    public static <T> AuthResult<T> error(String errorMessage) {
        return new AuthResult<>(false, null, errorMessage);
    }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getErrorMessage() { return errorMessage; }
}

