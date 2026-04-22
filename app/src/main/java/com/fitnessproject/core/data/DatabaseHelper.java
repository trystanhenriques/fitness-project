package com.fitnessproject.core.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness_project.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EXERCISE = "exercise";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE + " TEXT, " +
                COLUMN_WEIGHT + " TEXT, " +
                COLUMN_REPS + " TEXT, " +
                COLUMN_SETS + " TEXT, " +
                COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        onCreate(db);
    }

    public void addWorkout(String exercise, String weight, String reps, String sets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE, exercise);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_SETS, sets);
        db.insert(TABLE_WORKOUTS, null, values);
        db.close();
    }

    public List<String> getAllWorkouts() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKOUTS + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String entry = cursor.getString(1) + " — " +
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
        Cursor cursor = db.query(TABLE_WORKOUTS, null, COLUMN_EXERCISE + "=?",
                new String[]{exercise}, null, null, "id DESC");

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
}
