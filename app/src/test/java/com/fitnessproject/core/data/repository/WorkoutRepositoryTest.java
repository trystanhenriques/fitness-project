package com.fitnessproject.core.data.repository;

import androidx.lifecycle.LiveData;

import com.fitnessproject.core.data.db.WorkoutDao;
import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.data.model.WorkoutLogEntry;
import com.fitnessproject.core.session.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Validates that workout log lookups and saves cleanly partition
 * between the active registered User B, active User A, and the Guest offline profile.
 */
public class WorkoutRepositoryTest {

    @Mock
    private WorkoutDao mockWorkoutDao;

    @Mock
    private SessionManager mockSessionManager;

    private WorkoutRepository workoutRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        workoutRepository = new WorkoutRepository(mockWorkoutDao, mockSessionManager);
    }

    @Test
    public void getMyWorkoutLogs_WhenUserIsGuest_PullsFromGuestProfileOnly() {
        // Arrange
        UserSession guestSession = UserSession.createGuestSession();
        when(mockSessionManager.getCurrentSession()).thenReturn(guestSession);
        LiveData<List<WorkoutLogEntry>> mockGuestLogs = mock(LiveData.class);
        when(mockWorkoutDao.getGuestLogs()).thenReturn(mockGuestLogs);

        // Act
        LiveData<List<WorkoutLogEntry>> result = workoutRepository.getMyWorkoutLogs();

        // Assert
        assertEquals(mockGuestLogs, result);
        verify(mockWorkoutDao).getGuestLogs();
    }

    @Test
    public void getMyWorkoutLogs_WhenUserIsRegistered_PullsFromUserIdOnly() {
        // Arrange
        long userAId = 55L;
        UserSession userSession = UserSession.createUserSession(userAId, "UserA");
        when(mockSessionManager.getCurrentSession()).thenReturn(userSession);

        LiveData<List<WorkoutLogEntry>> mockUserLogs = mock(LiveData.class);
        when(mockWorkoutDao.getLogsForUser(userAId)).thenReturn(mockUserLogs);

        // Act
        LiveData<List<WorkoutLogEntry>> result = workoutRepository.getMyWorkoutLogs();

        // Assert
        assertEquals(mockUserLogs, result);
        verify(mockWorkoutDao).getLogsForUser(userAId);
    }

    @Test
    public void saveWorkoutLog_WhenUserIsGuest_InfersNullId() {
        // Arrange
        UserSession guestSession = UserSession.createGuestSession();
        when(mockSessionManager.getCurrentSession()).thenReturn(guestSession);

        // Act
        workoutRepository.saveWorkoutLog("Guest Leg Day");

        // Assert
        ArgumentCaptor<WorkoutLogEntry> captor = ArgumentCaptor.forClass(WorkoutLogEntry.class);
        verify(mockWorkoutDao).insertWorkoutLog(captor.capture());
        WorkoutLogEntry savedEntry = captor.getValue();

        assertEquals("Guest Leg Day", savedEntry.notes);
        assertNull("Saved workout data for guest MUST map to null ID to isolate correctly", savedEntry.userId);
    }

    @Test
    public void saveWorkoutLog_WhenUserIsRegistered_InfersUserId() {
        // Arrange
        long userBId = 110L;
        UserSession userSession = UserSession.createUserSession(userBId, "UserB");
        when(mockSessionManager.getCurrentSession()).thenReturn(userSession);

        // Act
        workoutRepository.saveWorkoutLog("UserB Push Day");

        // Assert
        ArgumentCaptor<WorkoutLogEntry> captor = ArgumentCaptor.forClass(WorkoutLogEntry.class);
        verify(mockWorkoutDao).insertWorkoutLog(captor.capture());
        WorkoutLogEntry savedEntry = captor.getValue();

        assertEquals("UserB Push Day", savedEntry.notes);
        assertEquals(Long.valueOf(userBId), savedEntry.userId);
    }
}


