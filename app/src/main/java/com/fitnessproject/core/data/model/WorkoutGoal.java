package com.fitnessproject.core.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Represents a personalized fitness goal for a user.
 *
 * Design Decision:
 * - guests also have access to "userId == null" goals representing their active session
 *   offline.
 */
@Entity(
    tableName = "workout_goals",
    foreignKeys = @ForeignKey(
        entity = UserAccount.class,
        parentColumns = "userId",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("userId")}
)
public class WorkoutGoal {
    @PrimaryKey(autoGenerate = true)
    public Long goalId;

    // Nullable reference. Null = Guest, Long = Registered User
    public Long userId;

    public long exerciseId;
    public float targetWeight;
    public int targetReps;
}
