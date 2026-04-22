package com.fitnessproject.core.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents an exercise definition (e.g., Bench Press, Squat).
 *
 * Design Decision:
 * - We use a nullable Long for userId to allow registered users to create
 *   their own custom exercises.
 * - If userId is null, it acts as a "Global" or "Default" exercise available
 *   to everyone (including guests).
 */
@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public Long exerciseId;

    // Nullable context mapping. Null = Global App Exercise, Long = Custom User Exercise
    public Long userId;

    public String name;
    public String targetMuscleGroup;
}
