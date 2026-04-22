package com.fitnessproject.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitnessproject.core.data.model.UserSession;

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
        MockitoAnnotations.openInMocks(this);

        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(mockPrefs);

        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor);
        when(mockEditor.putLong(anyString(), anyLong())).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.remove(anyString())).thenReturn(mockEditor);
        when(mockEditor.clear()).thenReturn(mockEditor);

        sessionManager = SessionManager.getInstance(mockContext);
    }

    @Test
    public void getCurrentSession_WhenNoSession_ReturnsNull() {
        when(mockPrefs.getBoolean("has_session", false)).thenReturn(false);

        UserSession session = sessionManager.getCurrentSession();
        assertNull("Session should be null when no active session flag is set", session);
        assertFalse(sessionManager.hasActiveSession());
    }

    @Test
    public void getCurrentSession_WhenGuestSession_ReturnsGuest() {
        when(mockPrefs.getBoolean("has_session", false)).thenReturn(true);
        when(mockPrefs.getBoolean("is_guest", true)).thenReturn(true);

        UserSession session = sessionManager.getCurrentSession();

        assertTrue(sessionManager.hasActiveSession());
        assertTrue(session.isGuest());
        assertNull(session.getUserId());
        assertNull(session.getUsername());
    }

    @Test
    public void getCurrentSession_WhenUserSession_ReturnsUser() {
        when(mockPrefs.getBoolean("has_session", false)).thenReturn(true);
        when(mockPrefs.getBoolean("is_guest", true)).thenReturn(false);
        when(mockPrefs.getLong("user_id", -1)).thenReturn(42L);
        when(mockPrefs.getString(eq("username"), anyString())).thenReturn("TestGuy");

        UserSession session = sessionManager.getCurrentSession();

        assertTrue(sessionManager.hasActiveSession());
        assertFalse(session.isGuest());
        assertEquals(Long.valueOf(42L), session.getUserId());
        assertEquals("TestGuy", session.getUsername());
    }

    @Test
    public void startUserSession_PersistsValues() {
        sessionManager.startUserSession(123L, "MyUser");

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
    public void clearSession_WipesAllPrefs() {
        sessionManager.clearSession();
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }
}

