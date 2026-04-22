package com.fitnessproject.core.data.repository;

import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.session.SessionManager;

/**
 * Example demonstrating how a future Repository (like WorkoutRepository)
 * accesses the current user's session safely.
 *
 * All Repositories handling user-tied data should take SessionManager as a dependency.
 */
public class BaseRepository {

    protected final SessionManager sessionManager;

    public BaseRepository(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Helper to retrieve the current internal user context safely.
     * Use this ID when saving new WorkoutLogs or FormCheck runs to SQLite.
     * @return the database ID for registered users, or null for guests.
     */
    protected Long getCurrentUserIdOrNull() {
        UserSession session = sessionManager.getCurrentSession();
        if (session != null && !session.isGuest()) {
            return session.getUserId(); // Valid Long object ready for Room mapping
        }
        return null; // Guest user - Room will map this directly as 'null' cleanly if allowed by the entity
    }

    /**
     * Checks if the app is purely in guest mode before allowing actions
     * that strictly require a synced or saved profile.
     */
    protected boolean isGuestUser() {
        UserSession session = sessionManager.getCurrentSession();
        return session == null || session.isGuest();
    }
}
