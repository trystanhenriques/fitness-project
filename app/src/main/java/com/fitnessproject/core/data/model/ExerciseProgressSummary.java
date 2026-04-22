package com.fitnessproject.core.data.model;

/**
 * A computed data class representing the summary of user progress.
 *
 * Design Decision:
 * - This is NOT a Room @Entity. Progress summaries shouldn't be duplicated in the
 *   database since they can easily become desynchronized from the actual logs.
 * - Instead, a DAO @Query computes this dynamically (e.g., retrieving max weight lifted)
 *   from WorkoutLogEntry and WorkoutSet joins per user account dynamically.
 */
public class ExerciseProgressSummary {

    // Which user context generated this computed progress summary
    private final Long userId;

    private final long exerciseId;
    private final String exerciseName;
    private final float maxWeightLifted;
    private final int totalSetsCompleted;
    private final long lastWorkoutDate;

    public ExerciseProgressSummary(Long userId, long exerciseId, String exerciseName,
                                   float maxWeightLifted, int totalSetsCompleted, long lastWorkoutDate) {
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.maxWeightLifted = maxWeightLifted;
        this.totalSetsCompleted = totalSetsCompleted;
        this.lastWorkoutDate = lastWorkoutDate;
    }

    public Long getUserId() { return userId; }
    public long getExerciseId() { return exerciseId; }
    public String getExerciseName() { return exerciseName; }
    public float getMaxWeightLifted() { return maxWeightLifted; }
    public int getTotalSetsCompleted() { return totalSetsCompleted; }
    public long getLastWorkoutDate() { return lastWorkoutDate; }
}
