package com.fitnessproject.core.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.fitnessproject.core.data.model.UserAccount;
import com.fitnessproject.core.session.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DatabaseHelperAccountIsolationTest {

    private Context context;
    private SessionManager sessionManager;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("fitness_project.db");
        sessionManager = SessionManager.getInstance(context);
        sessionManager.clearSession();
    }

    @Test
    public void workoutHistory_IsSeparatedForGuestAndEachUserAccount() {
        DatabaseHelper db = new DatabaseHelper(context);

        sessionManager.startGuestSession();
        db.addWorkout("Squat", "135", "5", "3");
        assertEquals(1, db.getWorkoutCount());
        assertEquals(1, db.getAllWorkouts().size());

        startRegisteredSession(101L, "alice");
        assertEquals(0, db.getWorkoutCount());
        assertTrue(db.getAllWorkouts().isEmpty());
        db.addWorkout("Bench Press", "155", "5", "3");
        assertEquals(1, db.getWorkoutCount());

        startRegisteredSession(202L, "bob");
        assertEquals(0, db.getWorkoutCount());
        assertTrue(db.getAllWorkouts().isEmpty());
        db.addWorkout("Deadlift", "225", "5", "1");
        assertEquals(1, db.getWorkoutCount());

        startRegisteredSession(101L, "alice");
        assertEquals(1, db.getWorkoutCount());
        assertEquals(1, db.getAllWorkouts().size());

        sessionManager.startGuestSession();
        assertEquals(1, db.getWorkoutCount());
        assertEquals(1, db.getAllWorkouts().size());
    }

    @Test
    public void deleteAllWorkouts_OnlyDeletesForActiveSession() {
        DatabaseHelper db = new DatabaseHelper(context);

        startRegisteredSession(11L, "user1");
        db.addWorkout("Bench Press", "165", "5", "3");

        startRegisteredSession(22L, "user2");
        db.addWorkout("Overhead Press", "95", "8", "3");

        sessionManager.startGuestSession();
        db.addWorkout("Air Squat", "0", "15", "2");

        startRegisteredSession(11L, "user1");
        assertEquals(1, db.getWorkoutCount());
        db.deleteAllWorkouts();
        assertEquals(0, db.getWorkoutCount());

        startRegisteredSession(22L, "user2");
        assertEquals(1, db.getWorkoutCount());

        sessionManager.startGuestSession();
        assertEquals(1, db.getWorkoutCount());
    }

    private void startRegisteredSession(long userId, String username) {
        UserAccount account = new UserAccount();
        account.setUserId(userId);
        account.setUsername(username);
        sessionManager.startRegisteredSession(account);
    }
}
