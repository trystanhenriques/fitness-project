package com.fitnessproject.ui.settings;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.session.SessionManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Validates UI presentation text and intent events from the SettingsViewModel statically
 */
public class SettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private SessionManager mockSessionManager;

    private SettingsViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void init_WhenRegisteredUser_ShowsSignLogOut() {
        // Arrange
        UserSession userSession = UserSession.createUserSession(4L, "John");
        when(mockSessionManager.getCurrentSession()).thenReturn(userSession);

        // Act
        viewModel = new SettingsViewModel(mockSessionManager);

        // Assert setup states
        assertEquals("Signed in as: John", viewModel.getAccountStatusText().getValue());
        assertFalse(viewModel.getIsGuestUser().getValue());
    }

    @Test
    public void init_WhenGuest_ShowsGuestLogin() {
        // Arrange
        UserSession guestSession = UserSession.createGuestSession();
        when(mockSessionManager.getCurrentSession()).thenReturn(guestSession);

        // Act
        viewModel = new SettingsViewModel(mockSessionManager);

        // Assert setup states
        assertEquals("Using Guest Mode", viewModel.getAccountStatusText().getValue());
        assertTrue(viewModel.getIsGuestUser().getValue());
    }

    @Test
    public void onLogoutClicked_ClearsSessionAndNavigates() {
        // Arrange
        UserSession userSession = UserSession.createUserSession(4L, "John");
        when(mockSessionManager.getCurrentSession()).thenReturn(userSession);
        viewModel = new SettingsViewModel(mockSessionManager);

        // Act
        viewModel.onLogoutClicked();

        // Assert
        verify(mockSessionManager).logout();
        assertTrue(viewModel.getNavigateToAuthEvent().getValue());
    }

    @Test
    public void onLoginRegisterClicked_AsGuest_Navigates() {
        // Arrange
        UserSession guestSession = UserSession.createGuestSession();
        when(mockSessionManager.getCurrentSession()).thenReturn(guestSession);
        viewModel = new SettingsViewModel(mockSessionManager);

        // Act
        viewModel.onLoginRegisterClicked();

        // Assert
        verify(mockSessionManager).logout(); // Verifies guest tracking resets seamlessly
        assertTrue(viewModel.getNavigateToAuthEvent().getValue());
    }
}
