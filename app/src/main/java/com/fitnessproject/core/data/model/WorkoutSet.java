package com.fitnessproject.core.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

/**
 * Represents a single set within a WorkoutLogEntry for a specific Exercise.
 */
@Entity(
    tableName = "workout_sets",
    foreignKeys = {
        @ForeignKey(
            entity = WorkoutLogEntry.class,
            parentColumns = "workoutId",
            childColumns = "workoutLogId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Exercise.class,
            parentColumns = "exerciseId",
            childColumns = "exerciseId",
            onDelete = ForeignKey.RESTRICT
        )
    },
    indices = {@Index("workoutLogId"), @Index("exerciseId")}
)
public class WorkoutSet {
    @PrimaryKey(autoGenerate = true)
    public Long setId;

    // Associated Workout. No user ID here, its parent `workoutLogId` holds it
    public long workoutLogId; // primitive 'long' because sets can't exist without a log

    public long exerciseId; // primitive 'long' because sets can't exist without an exercise
    
    public int reps;
    public float weight; // kilos/lbs
}
