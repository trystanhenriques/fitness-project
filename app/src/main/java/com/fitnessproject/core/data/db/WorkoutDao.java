package com.fitnessproject.core.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fitnessproject.core.data.model.WorkoutLogEntry;

import java.util.List;

/**
 * Data Access Object for workout logs.
 * Design ensures that guest logs (userId IS NULL) and registered user logs (userId = :userId)
 * are cleanly separated via strict queries.
 */
@Dao
public interface WorkoutDao {

    @Insert
    long insertWorkoutLog(WorkoutLogEntry entry);

    /**
     * Safely retrieves workout logs associated ONLY with a specific registered user.
     */
    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<WorkoutLogEntry>> getLogsForUser(Long userId);

    /**
     * Safely retrieves workout logs associated ONLY with the offline guest profile.
     */
    @Query("SELECT * FROM workout_logs WHERE userId IS NULL ORDER BY timestamp DESC")
    LiveData<List<WorkoutLogEntry>> getGuestLogs();
}

