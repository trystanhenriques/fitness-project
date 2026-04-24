package com.fitnessproject.core.data.repository;

import com.fitnessproject.core.data.db.UserDao;
import com.fitnessproject.core.data.model.AuthResult;
import com.fitnessproject.core.data.model.Credentials;
import com.fitnessproject.core.data.model.UserAccount;
import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.security.PasswordHasher;
import com.fitnessproject.core.session.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthRepositoryTest {

    private UserDao mockUserDao;
    private SessionManager mockSessionManager;
    private AuthRepository authRepository;

    @Before
    public void setUp() {
        mockUserDao = mock(UserDao.class);
        mockSessionManager = mock(SessionManager.class);
        authRepository = new AuthRepository(mockUserDao, mockSessionManager);
    }

    @Test
    public void register_WithEmptyUsername_FailsValidation() {
        Credentials creds = new Credentials("", "validPass");
        AuthResult<UserSession> result = authRepository.register(creds);

        assertFalse(result.isSuccess());
        assertEquals("Username cannot be empty", result.getErrorMessage());
        verify(mockUserDao, never()).insertUser(any());
    }

    @Test
    public void register_WithShortPassword_FailsValidation() {
        Credentials creds = new Credentials("ValidUser", "12345");
        AuthResult<UserSession> result = authRepository.register(creds);

        assertFalse(result.isSuccess());
        assertEquals("Password must be at least 6 characters", result.getErrorMessage());
        verify(mockUserDao, never()).insertUser(any());
    }

    @Test
    public void register_WhenUsernameExists_ReturnsError() {
        Credentials creds = new Credentials("ExistingUser", "validPass");
        when(mockUserDao.existsByUsername("ExistingUser")).thenReturn(true);

        AuthResult<UserSession> result = authRepository.register(creds);

        assertFalse(result.isSuccess());
        assertEquals("Username is already taken", result.getErrorMessage());
        verify(mockUserDao, never()).insertUser(any());
    }

    @Test
    public void register_WhenValid_SucceedsAndSetsSession() {
        String testUser = "NewUser";
        Credentials creds = new Credentials(testUser, "validPass");
        UserSession dummySession = UserSession.createUserSession(1L, testUser);

        when(mockUserDao.existsByUsername(testUser)).thenReturn(false);
        when(mockUserDao.insertUser(any(UserAccount.class))).thenReturn(1L);
        when(mockSessionManager.getCurrentSession()).thenReturn(dummySession);

        AuthResult<UserSession> result = authRepository.register(creds);

        assertTrue(result.isSuccess());
        verify(mockSessionManager).startRegisteredSession(any(UserAccount.class));
    }

    @Test
    public void login_WithMissingCredentials_ReturnsError() {
        Credentials creds = new Credentials(null, null);
        AuthResult<UserSession> result = authRepository.login(creds);

        assertFalse(result.isSuccess());
        assertEquals("Invalid credentials.", result.getErrorMessage());
    }

    @Test
    public void login_WithValidCredentials_SucceedsAndUpdatesLastLoginAt() {
        String testUser = "ValidUser";
        String testPass = "correctPassword";
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword(testPass, salt);

        UserAccount mockedAccount = new UserAccount();
        mockedAccount.setUserId(99L);
        mockedAccount.setUsername(testUser);
        mockedAccount.setPasswordSalt(salt);
        mockedAccount.setPasswordHash(hash);

        when(mockUserDao.getUserByUsername(testUser)).thenReturn(mockedAccount);

        AuthResult<UserSession> result = authRepository.login(new Credentials(testUser, testPass));

        assertTrue("Login should succeed with correct password", result.isSuccess());
        verify(mockUserDao).updateLastLogin(anyLong(), anyLong());
        verify(mockSessionManager).startRegisteredSession(any(UserAccount.class));
    }

    @Test
    public void login_WithWrongPassword_ReturnsError() {
        String testUser = "ValidUser";
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hashPassword("correctPassword", salt);

        UserAccount mockedAccount = new UserAccount();
        mockedAccount.setUserId(99L);
        mockedAccount.setUsername(testUser);
        mockedAccount.setPasswordSalt(salt);
        mockedAccount.setPasswordHash(hash);

        when(mockUserDao.getUserByUsername(testUser)).thenReturn(mockedAccount);

        AuthResult<UserSession> result = authRepository.login(new Credentials(testUser, "wrongPassword"));

        assertFalse("Login should fail with wrong password", result.isSuccess());
        assertEquals("Invalid username or password", result.getErrorMessage());
        verify(mockSessionManager, never()).startRegisteredSession(any(UserAccount.class));
    }

    @Test
    public void continueAsGuest_CallsSessionManager() {
        authRepository.continueAsGuest();
        verify(mockSessionManager).startGuestSession();
    }
}
