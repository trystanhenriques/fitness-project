package com.fitnessproject.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.data.model.UserAccount;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionManagerTest {

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockPrefs;

    @Mock
    private SharedPreferences.Editor mockEditor;

    private SessionManager sessionManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(mockPrefs);

        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor);
        when(mockEditor.putLong(anyString(), anyLong())).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.remove(anyString())).thenReturn(mockEditor);
        when(mockEditor.clear()).thenReturn(mockEditor);

        SessionManager.clearInstanceForTest();
        sessionManager = SessionManager.getInstance(mockContext);
    }

    @Test
    public void getCurrentSession_WhenNoSession_ReturnsNull() {
        when(mockPrefs.getBoolean(eq("has_session"), anyBoolean())).thenReturn(false);

        UserSession session = sessionManager.getCurrentSession();
        assertNull("Session should be null when no active session flag is set", session);
        assertFalse(sessionManager.isLoggedIn());
    }

    @Test
    public void getCurrentSession_WhenGuestSession_ReturnsGuest() {
        when(mockPrefs.getBoolean(eq("has_session"), anyBoolean())).thenReturn(true);
        when(mockPrefs.getBoolean(eq("is_guest"), anyBoolean())).thenReturn(true);

        UserSession session = sessionManager.getCurrentSession();

        assertTrue(sessionManager.isLoggedIn());
        assertTrue(session.isGuest());
        assertNull(session.getUserId());
        assertNull(session.getUsername());
    }

    @Test
    public void getCurrentSession_WhenUserSession_ReturnsUser() {
        when(mockPrefs.getBoolean(eq("has_session"), anyBoolean())).thenReturn(true);
        when(mockPrefs.getBoolean(eq("is_guest"), anyBoolean())).thenReturn(false);
        when(mockPrefs.getLong(eq("user_id"), anyLong())).thenReturn(42L);
        when(mockPrefs.getString(eq("username"), anyString())).thenReturn("TestGuy");

        UserSession session = sessionManager.getCurrentSession();

        assertTrue(sessionManager.isLoggedIn());
        assertFalse(session.isGuest());
        assertEquals(Long.valueOf(42L), session.getUserId());
        assertEquals("TestGuy", session.getUsername());
    }

    @Test
    public void startRegisteredSession_PersistsValues() {
        UserAccount account = new UserAccount();
        account.setUserId(123L);
        account.setUsername("MyUser");

        sessionManager.startRegisteredSession(account);

        verify(mockEditor).putBoolean("has_session", true);
        verify(mockEditor).putBoolean("is_guest", false);
        verify(mockEditor).putLong("user_id", 123L);
        verify(mockEditor).putString("username", "MyUser");
        verify(mockEditor).apply();
    }

    @Test
    public void startGuestSession_SavesGuestFlagAndClearsUserIds() {
        sessionManager.startGuestSession();

        verify(mockEditor).putBoolean("has_session", true);
        verify(mockEditor).putBoolean("is_guest", true);
        verify(mockEditor).remove("user_id");
        verify(mockEditor).remove("username");
        verify(mockEditor).apply();
    }

    @Test
    public void logout_ClearsActiveSessionOnly() {
        sessionManager.logout();

        // Assert it clears the shared preferences (the session)
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }

    @Test
    public void clearSession_WipesAllPrefs() {
        sessionManager.clearSession();
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }
}
