package com.fitnessproject.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitnessproject.core.data.model.UserSession;

/**
 * Singleton managing active session storage using SharedPreferences.
 * Determines if a user (or guest) is actively currently logged-in between app launches.
 */
public class SessionManager {

    private static final String PREF_NAME = "fitness_session_prefs";
    private static final String KEY_IS_GUEST = "is_guest";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_HAS_SESSION = "has_session";

    private final SharedPreferences prefs;

    // Singleton backing field
    private static volatile SessionManager instance;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * Restores the session stored locally if any. Returns a Guest on freshly cleared state.
     */
    public UserSession getCurrentSession() {
        if (!hasActiveSession()) {
            return null; // Signals we need login
        }

        boolean isGuest = prefs.getBoolean(KEY_IS_GUEST, true);
        if (isGuest) {
            return UserSession.createGuestSession();
        }

        long userId = prefs.getLong(KEY_USER_ID, -1);
        if (userId == -1) {
            // Unintended state, return a guest instead.
            return UserSession.createGuestSession();
        }

        String username = prefs.getString(KEY_USERNAME, "UnknownUser");
        return UserSession.createUserSession(userId, username);
    }

    /**
     * Persist strong user context when they login securely or register freshly.
     */
    public void startUserSession(Long userId, String username) {
        prefs.edit()
            .putBoolean(KEY_HAS_SESSION, true)
            .putBoolean(KEY_IS_GUEST, false)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .apply();
    }

    /**
     * Switch context intentionally to a Guest session.
     */
    public void startGuestSession() {
        prefs.edit()
            .putBoolean(KEY_HAS_SESSION, true)
            .putBoolean(KEY_IS_GUEST, true)
            .remove(KEY_USER_ID)
            .remove(KEY_USERNAME)
            .apply();
    }

    /**
     * Checks if there is any active session (user or guest).
     */
    public boolean hasActiveSession() {
        return prefs.getBoolean(KEY_HAS_SESSION, false);
    }

    /**
     * Flushes currently tracked context completely.
     */
    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
