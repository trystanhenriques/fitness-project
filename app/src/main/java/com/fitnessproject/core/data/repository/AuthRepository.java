package com.fitnessproject.core.data.repository;

import android.database.sqlite.SQLiteConstraintException;

import com.fitnessproject.core.data.db.UserDao;
import com.fitnessproject.core.data.model.AuthResult;
import com.fitnessproject.core.data.model.Credentials;
import com.fitnessproject.core.data.model.UserAccount;
import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.security.PasswordHasher;
import com.fitnessproject.core.session.SessionManager;

/**
 * Repository enforcing business rules on auth workflows.
 * Abstracts local database fetching/saving via DAO securely hashing parameters cleanly
 * away from the ViewModel and UI side-effects cleanly via wrapped AuthResult responses.
 */
public class AuthRepository {

    private final UserDao userDao;
    private final SessionManager sessionManager;

    public AuthRepository(UserDao userDao, SessionManager sessionManager) {
        this.userDao = userDao;
        this.sessionManager = sessionManager;
    }

    /**
     * Conducts a registration using raw string credentials generating Salts locally,
     * failing predictably cleanly on constraint conflict gracefully wrapping a message.
     */
    public AuthResult<UserSession> register(Credentials credentials) {
        if (credentials.getUsername() == null || credentials.getUsername().trim().isEmpty()) {
            return AuthResult.error("Username cannot be empty");
        }
        if (credentials.getPassword() == null || credentials.getPassword().trim().length() < 6) {
            return AuthResult.error("Password must be at least 6 characters");
        }

        String username = credentials.getUsername().trim();
        String password = credentials.getPassword();

        // Check if user already exists
        if (userDao.existsByUsername(username)) {
            return AuthResult.error("Username is already taken");
        }

        // Generate cryptography pieces locally for offline usage safely
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword(password, salt);

        UserAccount newAccount = new UserAccount();
        newAccount.setUsername(username);
        newAccount.setPasswordSalt(salt);
        newAccount.setPasswordHash(hash);
        long now = System.currentTimeMillis();
        newAccount.setCreatedAt(now);
        newAccount.setLastLoginAt(now);
        newAccount.setActive(true);

        try {
            long generatedUserId = userDao.insertUser(newAccount);
            newAccount.setUserId(generatedUserId);
            sessionManager.startRegisteredSession(newAccount);
            return AuthResult.success(sessionManager.getCurrentSession());
        } catch (SQLiteConstraintException e) {
            // Failsafe for double constraint insertion collisions cleanly
            return AuthResult.error("Account creation failed. Username might be taken.");
        } catch (Exception e) {
            return AuthResult.error("Unknown error occurred during registration");
        }
    }

    /**
     * Queries local user info mapping to hash check locally offline effectively securing usage.
     */
    public AuthResult<UserSession> login(Credentials credentials) {
        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            return AuthResult.error("Invalid credentials.");
        }

        String username = credentials.getUsername().trim();
        String password = credentials.getPassword();

        UserAccount storedAccount = userDao.getUserByUsername(username);
        if (storedAccount == null) {
            return AuthResult.error("Invalid username or password");
        }

        // Validate the passed raw password locally offline correctly.
        String expectedHash = storedAccount.getPasswordHash();
        String actualHashAttempt = PasswordHasher.hashPassword(password, storedAccount.getPasswordSalt());

        if (!expectedHash.equals(actualHashAttempt)) {
            return AuthResult.error("Invalid username or password");
        }

        // Update login info correctly tracked securely.
        long now = System.currentTimeMillis();
        storedAccount.setLastLoginAt(now);
        userDao.updateLastLogin(storedAccount.getUserId(), now);

        sessionManager.startRegisteredSession(storedAccount);
        return AuthResult.success(sessionManager.getCurrentSession());
    }

    /**
     * Skips local saving securely directly logging cleanly using SharedPreferences default.
     */
    public void continueAsGuest() {
        sessionManager.startGuestSession();
    }

    /**
     * Checks locally fetched session securely routing if guest otherwise directly.
     */
    public UserSession getCurrentSession() {
        return sessionManager.getCurrentSession();
    }

    /**
     * Logout clears the active session but preserves saved account data.
     * Reverts app globally clearing user state completely safely offline.
     */
    public void logout() {
        sessionManager.clearSession();
    }
}
