package com.fitnessproject.core.data.model;

/**
 * Memory model representing the current logged-in user or a guest session.
 * Used by the UI and ViewModels to know "who" is actively using the local app right now.
 */
public class UserSession {
    private final boolean isGuest;
    private final Long userId; // null if guest
    private final String username; // null if guest
    private final long startedAt;

    private UserSession(boolean isGuest, Long userId, String username, long startedAt) {
        this.isGuest = isGuest;
        this.userId = userId;
        this.username = username;
        this.startedAt = startedAt;
    }

    public static UserSession createGuestSession() {
        return new UserSession(true, null, null, System.currentTimeMillis());
    }

    public static UserSession createUserSession(Long userId, String username) {
        return new UserSession(false, userId, username, System.currentTimeMillis());
    }

    public boolean isGuest() { return isGuest; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public long getStartedAt() { return startedAt; }
}

