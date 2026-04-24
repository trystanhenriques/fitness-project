package com.fitnessproject.core.data.repository;

import androidx.lifecycle.LiveData;

import com.fitnessproject.core.data.db.WorkoutDao;
import com.fitnessproject.core.data.model.WorkoutLogEntry;
import com.fitnessproject.core.session.SessionManager;

import java.util.List;

/**
 * Concrete implementation showing how user-linked data should be retrieved
 * securely filtered by exactly the current session.
 */
public class WorkoutRepository extends BaseRepository {

    private final WorkoutDao workoutDao;

    public WorkoutRepository(WorkoutDao workoutDao, SessionManager sessionManager) {
        super(sessionManager);
        this.workoutDao = workoutDao;
    }

    /**
     * Determines whether the user is a guest or logged-in and fetches strictly
     * the partitioned logs for their session state.
     * Prevents guest mode from accidentally seeing registered user data and vice-versa.
     */
    public LiveData<List<WorkoutLogEntry>> getMyWorkoutLogs() {
        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            // It's a guest: ONLY fetch null-id values.
            return workoutDao.getGuestLogs();
        } else {
            // It's a user: ONLY fetch matched longs.
            return workoutDao.getLogsForUser(currentUserId);
        }
    }

    /**
     * Example inserting a workout ensuring the correct relationship ID binds smoothly.
     */
    public void saveWorkoutLog(String notes) {
        WorkoutLogEntry entry = new WorkoutLogEntry();
        // getCurrentUserIdOrNull() accurately returns null if Guest, or Long if registered!
        entry.userId = getCurrentUserIdOrNull();
        entry.notes = notes;
        entry.timestamp = System.currentTimeMillis();

        // Normally invoked via background executor/threads...
        workoutDao.insertWorkoutLog(entry);
    }
}

