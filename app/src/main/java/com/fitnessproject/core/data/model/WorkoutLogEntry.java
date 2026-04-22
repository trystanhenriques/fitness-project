package com.fitnessproject.core.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Represents a workout log entry.
 * Associates a workout session with a specific user.
 * 
 * Design Decision:
 * - userId is a nullable Long. This natively allows Guest users to log workouts 
 *   offline without needing a fake "0" user ID account. If userId is null, it 
 *   belongs to the local guest profile.
 */
@Entity(
    tableName = "workout_logs",
    foreignKeys = @ForeignKey(
        entity = UserAccount.class,
        parentColumns = "userId",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("userId")}
)
public class WorkoutLogEntry {
    @PrimaryKey(autoGenerate = true)
    public Long workoutId;

    // Nullable reference. Null = Guest, Long = Registered User
    public Long userId;

    public long timestamp;
    public String notes;
}

