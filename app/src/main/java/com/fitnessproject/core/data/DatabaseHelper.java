package com.fitnessproject.core.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fitnessproject.core.data.model.UserSession;
import com.fitnessproject.core.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness_project.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EXERCISE = "exercise";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USER_ID = "user_id";

    private final Context appContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.appContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE + " TEXT, " +
                COLUMN_WEIGHT + " TEXT, " +
                COLUMN_REPS + " TEXT, " +
                COLUMN_SETS + " TEXT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_WORKOUTS + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER");
        }
    }

    public void addWorkout(String exercise, String weight, String reps, String sets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE, exercise);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_SETS, sets);

        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            values.putNull(COLUMN_USER_ID);
        } else {
            values.put(COLUMN_USER_ID, currentUserId);
        }

        db.insert(TABLE_WORKOUTS, null, values);
        db.close();
    }

    public List<String> getAllWorkouts() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_USER_ID + " IS NULL ORDER BY id DESC",
                    null
            );
        } else {
            cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY id DESC",
                    new String[]{String.valueOf(currentUserId)}
            );
        }

        if (cursor.moveToFirst()) {
            do {
                String entry = cursor.getString(1) + " - " +
                        cursor.getString(2) + " lbs x " +
                        cursor.getString(3) + " reps (" +
                        cursor.getString(4) + " sets)";
                list.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getProgressForExercise(String exercise) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            cursor = db.query(
                    TABLE_WORKOUTS,
                    null,
                    COLUMN_EXERCISE + "=? AND " + COLUMN_USER_ID + " IS NULL",
                    new String[]{exercise},
                    null,
                    null,
                    "id DESC"
            );
        } else {
            cursor = db.query(
                    TABLE_WORKOUTS,
                    null,
                    COLUMN_EXERCISE + "=? AND " + COLUMN_USER_ID + "=?",
                    new String[]{exercise, String.valueOf(currentUserId)},
                    null,
                    null,
                    "id DESC"
            );
        }

        if (cursor.moveToFirst()) {
            do {
                String entry = cursor.getString(2) + " lbs x " +
                        cursor.getString(3) + " reps (" +
                        cursor.getString(4) + " sets)";
                list.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteAllWorkouts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            db.delete(TABLE_WORKOUTS, COLUMN_USER_ID + " IS NULL", null);
        } else {
            db.delete(TABLE_WORKOUTS, COLUMN_USER_ID + "=?", new String[]{String.valueOf(currentUserId)});
        }
        db.close();
    }

    public int getWorkoutCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_USER_ID + " IS NULL",
                    null
            );
        } else {
            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_USER_ID + " = ?",
                    new String[]{String.valueOf(currentUserId)}
            );
        }

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    private Long getCurrentUserIdOrNull() {
        UserSession session = SessionManager.getInstance(appContext).getCurrentSession();
        if (session != null && !session.isGuest()) {
            return session.getUserId();
        }
        return null;
    }
}
