package com.fitnessproject.core.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fitnessproject.core.data.model.Exercise;
import com.fitnessproject.core.data.model.UserAccount;
import com.fitnessproject.core.data.model.WorkoutGoal;
import com.fitnessproject.core.data.model.WorkoutLogEntry;
import com.fitnessproject.core.data.model.WorkoutSet;

/**
 * Primary point of SQLite connection via Room for App persistence.
 * Holds all DAO references for local data.
 */
@Database(entities = {
        UserAccount.class,
        WorkoutLogEntry.class,
        WorkoutSet.class,
        WorkoutGoal.class,
        Exercise.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract WorkoutDao workoutDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fitness_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
