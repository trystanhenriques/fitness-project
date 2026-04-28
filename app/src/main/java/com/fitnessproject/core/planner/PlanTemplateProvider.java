package com.fitnessproject.core.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanTemplateProvider {

    public PlanTemplate getTemplate(GoalType goalType, int daysPerWeek) {
        switch (goalType) {
            case STRENGTH:
                return buildStrengthTemplate(daysPerWeek);
            case HYPERTROPHY:
                return buildHypertrophyTemplate(daysPerWeek);
            case ENDURANCE:
                return buildEnduranceTemplate(daysPerWeek);
            default:
                throw new IllegalArgumentException("Unsupported goal: " + goalType);
        }
    }

    /*
     * Strength templates bias lower reps and higher technical quality so lifters
     * can build skill under load without chasing failure on heavy compounds.
     */
    private PlanTemplate buildStrengthTemplate(int daysPerWeek) {
        switch (daysPerWeek) {
            case 2:
                return template(GoalType.STRENGTH, 2, "Full Body A/B",
                        "Two full-body sessions keep the main lifts frequent while recovery stays manageable.",
                        day(1, "Day 1", "Full Body A",
                                ex("Back Squat", 3, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Bench Press", 3, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Barbell Row", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Romanian Deadlift", 2, "6-8 reps", "Stop 2 reps before failure", null),
                                ex("Plank", 3, "30-60 seconds", "Hold crisp positions", null)
                        ),
                        day(2, "Day 2", "Full Body B",
                                ex("Deadlift", 3, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Overhead Press", 3, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Lat Pulldown or Assisted Pull-Up", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Press", 2, "6-8 reps", "Stop 2 reps before failure", null),
                                ex("Farmer Carry", 3, "30-45 seconds", "Move with control", null)
                        ));
            case 3:
                return template(GoalType.STRENGTH, 3, "Full Body Strength",
                        "Three full-body strength days spread squat, press, and hinge work across the week for steady progress.",
                        day(1, "Day 1", "Squat Focus",
                                ex("Back Squat", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Bench Press", 3, "4-6 reps", "Stop 2 reps before failure", null),
                                ex("Barbell Row", 3, "5-8 reps", "Keep reps smooth", null),
                                ex("Hamstring Curl", 2, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Plank", 3, "30-60 seconds", "Hold crisp positions", null)
                        ),
                        day(2, "Day 2", "Press Focus",
                                ex("Overhead Press", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Deadlift", 3, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Incline Dumbbell Press", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Lat Pulldown", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Side Plank", 2, "30-45 seconds per side", "Stay braced", null)
                        ),
                        day(3, "Day 3", "Bench/Squat Volume",
                                ex("Bench Press", 4, "3-5 reps", "Stop 2 reps before failure", null),
                                ex("Front Squat or Goblet Squat", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Cable Row", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Romanian Deadlift", 3, "6-8 reps", "Stop 2 reps before failure", null),
                                ex("Dumbbell Curl", 2, "8-12 reps", "Optional last rep with clean form", null)
                        ));
            case 4:
                return template(GoalType.STRENGTH, 4, "Upper/Lower Strength",
                        "A four-day upper/lower split gives enough frequency for the main lifts while keeping fatigue organized.",
                        day(1, "Day 1", "Upper Strength A",
                                ex("Bench Press", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Barbell Row", 4, "5-6 reps", "Keep reps powerful and clean", null),
                                ex("Overhead Press", 3, "4-6 reps", "Stop 2 reps before failure", null),
                                ex("Lat Pulldown or Pull-Up", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Triceps Pushdown", 2, "8-10 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(2, "Day 2", "Lower Strength A",
                                ex("Back Squat", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Romanian Deadlift", 3, "5-8 reps", "Stop 2 reps before failure", null),
                                ex("Leg Press", 3, "6-8 reps", "Stop 2 reps before failure", null),
                                ex("Standing Calf Raise", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Hanging Knee Raise", 3, "8-12 reps", "Move with control", null)
                        ),
                        day(3, "Day 3", "Upper Strength B",
                                ex("Overhead Press", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Pull-Up or Lat Pulldown", 4, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Incline Bench Press", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Cable Row", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Dumbbell Lateral Raise", 2, "10-12 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(4, "Day 4", "Lower Strength B",
                                ex("Deadlift", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Front Squat or Goblet Squat", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Hamstring Curl", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Walking Lunge", 2, "8 reps per leg", "Stop 1-2 reps before failure", null),
                                ex("Plank", 3, "45-60 seconds", "Hold crisp positions", null)
                        ));
            case 5:
                return template(GoalType.STRENGTH, 5, "Upper/Lower + Weak Point Day",
                        "Five days allow two main upper/lower exposures plus a lighter technique day to reinforce weak points without maxing out.",
                        day(1, "Day 1", "Upper Strength",
                                ex("Bench Press", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Barbell Row", 4, "5-6 reps", "Keep reps powerful and clean", null),
                                ex("Overhead Press", 3, "4-6 reps", "Stop 2 reps before failure", null),
                                ex("Pull-Up or Lat Pulldown", 3, "5-8 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(2, "Day 2", "Lower Strength",
                                ex("Back Squat", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Romanian Deadlift", 3, "5-8 reps", "Stop 2 reps before failure", null),
                                ex("Leg Press", 3, "6-8 reps", "Stop 2 reps before failure", null),
                                ex("Calf Raise", 3, "8-12 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(3, "Day 3", "Technique/Weak Points",
                                ex("Pause Squat", 3, "3-5 reps", "Use light to moderate load", "Focus on control, not grinding"),
                                ex("Close-Grip Bench Press", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Face Pull", 3, "12-15 reps", "Stop 1-2 reps before failure", null),
                                ex("Core Exercise", 3, "10-15 reps", "Move with control", null)
                        ),
                        day(4, "Day 4", "Upper Strength B",
                                ex("Overhead Press", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Incline Bench Press", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Row", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Lat Pulldown", 3, "6-8 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(5, "Day 5", "Lower Strength B",
                                ex("Deadlift", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Front Squat", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Hamstring Curl", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Farmer Carry", 3, "30-45 seconds", "Move with control", null)
                        ));
            case 6:
                return template(GoalType.STRENGTH, 6, "Strength-Biased Push/Pull/Legs",
                        "A six-day strength-biased push/pull/legs split alternates heavier and volume-focused sessions to spread stress across the week.",
                        day(1, "Day 1", "Push Heavy",
                                ex("Bench Press", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Overhead Press", 3, "4-6 reps", "Stop 2 reps before failure", null),
                                ex("Incline Dumbbell Press", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Triceps Pushdown", 3, "8-10 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(2, "Day 2", "Pull Heavy",
                                ex("Barbell Row", 4, "5-6 reps", "Keep reps powerful and clean", null),
                                ex("Pull-Up or Lat Pulldown", 4, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Cable Row", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Dumbbell Curl", 3, "8-10 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(3, "Day 3", "Legs Heavy",
                                ex("Back Squat", 4, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Romanian Deadlift", 3, "5-8 reps", "Stop 2 reps before failure", null),
                                ex("Leg Press", 3, "6-8 reps", "Stop 2 reps before failure", null),
                                ex("Calf Raise", 3, "8-12 reps", "Stop 1-2 reps before failure", null)
                        ),
                        day(4, "Day 4", "Push Volume",
                                ex("Incline Bench Press", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Dumbbell Shoulder Press", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Chest Fly", 3, "10-12 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Lateral Raise", 3, "10-12 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(5, "Day 5", "Pull Volume",
                                ex("Lat Pulldown", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Chest-Supported Row", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Face Pull", 3, "12-15 reps", "Stop 1-2 reps before failure", null),
                                ex("Hammer Curl", 3, "10-12 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(6, "Day 6", "Legs Volume",
                                ex("Deadlift", 3, "3-5 reps", "Stop 2-3 reps before failure", null),
                                ex("Front Squat or Goblet Squat", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Hamstring Curl", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Walking Lunge", 2, "10 reps per leg", "Stop 1-2 reps before failure", null)
                        ));
            case 7:
                return template(GoalType.STRENGTH, 7, "6 Training Days + Recovery Day",
                        "Seven-day strength planning keeps six structured lifting days, but the final day is active recovery because heavy barbell work every day drives fatigue faster than progress.",
                        mergeDays(buildStrengthTemplate(6).getDays(),
                                day(7, "Day 7", "Active Recovery",
                                        ex("Easy Walk, Bike, or Mobility", 1, "20-40 minutes", "Keep effort easy", null),
                                        ex("Core Stability", 2, "2-3 light sets", "Smooth, low-fatigue reps", null),
                                        ex("Stretching/Mobility", 1, "10 minutes", "Stay relaxed and pain-free", null)
                                )));
            default:
                throw new IllegalArgumentException("Unsupported strength template for days: " + daysPerWeek);
        }
    }

    private PlanTemplate buildHypertrophyTemplate(int daysPerWeek) {
        switch (daysPerWeek) {
            case 2:
                return template(GoalType.HYPERTROPHY, 2, "Full Body Volume",
                        "Two higher-volume full-body sessions cover all major patterns and keep the weekly workload balanced.",
                        day(1, "Day 1", "Full Body A",
                                ex("Back Squat", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Bench Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Lat Pulldown", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Romanian Deadlift", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Lateral Raise", 2, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Cable Curl", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(2, "Day 2", "Full Body B",
                                ex("Deadlift or Trap Bar Deadlift", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Overhead Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Cable Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Press", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Triceps Pushdown", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Hamstring Curl", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ));
            case 3:
                return template(GoalType.HYPERTROPHY, 3, "Full Body Hypertrophy",
                        "Three full-body hypertrophy days spread volume across the week so beginners and intermediates can recover while accumulating quality sets.",
                        day(1, "Day 1", "Full Body A",
                                ex("Squat", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Bench Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Curl", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Lateral Raise", 2, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(2, "Day 2", "Full Body B",
                                ex("Romanian Deadlift", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Overhead Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Lat Pulldown", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Press", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Cable Curl", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(3, "Day 3", "Full Body C",
                                ex("Deadlift or Hip Thrust", 3, "6-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Incline Dumbbell Press", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Walking Lunge", 2, "10-12 reps per leg", "Stop 1-2 reps before failure", null),
                                ex("Triceps Pushdown", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ));
            case 4:
                return template(GoalType.HYPERTROPHY, 4, "Upper/Lower Hypertrophy",
                        "A four-day upper/lower split gives enough volume per muscle group without forcing marathon sessions.",
                        day(1, "Day 1", "Upper A",
                                ex("Bench Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Lat Pulldown", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Incline Dumbbell Press", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Lateral Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Triceps Pushdown", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(2, "Day 2", "Lower A",
                                ex("Back Squat", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Romanian Deadlift", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Press", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Hamstring Curl", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Calf Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(3, "Day 3", "Upper B",
                                ex("Overhead Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Pull-Up or Lat Pulldown", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Dumbbell Bench Press", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Chest-Supported Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Face Pull", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Dumbbell Curl", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(4, "Day 4", "Lower B",
                                ex("Deadlift or Trap Bar Deadlift", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Front Squat or Goblet Squat", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Walking Lunge", 3, "10 reps per leg", "Stop 1-2 reps before failure", null),
                                ex("Leg Extension", 2, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Seated Calf Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ));
            case 5:
                return template(GoalType.HYPERTROPHY, 5, "Body Part / Bro Split",
                        "A five-day body-part split lets intermediate lifters push a bit more volume per session while still training each area with purpose.",
                        day(1, "Day 1", "Chest",
                                ex("Bench Press", 4, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Incline Dumbbell Press", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Chest Fly", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Push-Up", 2, "Near technical failure", "Stop before form breaks", "Bodyweight accessory only"),
                                ex("Triceps Pushdown", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(2, "Day 2", "Back",
                                ex("Pull-Up or Lat Pulldown", 4, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Barbell Row", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Cable Row", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Face Pull", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Dumbbell Curl", 2, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(3, "Day 3", "Legs",
                                ex("Back Squat", 4, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Romanian Deadlift", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Press", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Hamstring Curl", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Calf Raise", 4, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(4, "Day 4", "Shoulders",
                                ex("Overhead Press", 4, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Dumbbell Lateral Raise", 4, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Rear Delt Fly", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Upright Row or Cable Raise", 2, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Shrug", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(5, "Day 5", "Arms + Core",
                                ex("Close-Grip Bench Press", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("EZ-Bar Curl", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Triceps Rope Pushdown", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Hammer Curl", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Hanging Knee Raise", 3, "10-15 reps", "Move with control", null),
                                ex("Plank", 3, "45-60 seconds", "Hold crisp positions", null)
                        ));
            case 6:
                return template(GoalType.HYPERTROPHY, 6, "Push/Pull/Legs x2",
                        "A six-day push/pull/legs rotation is a classic hypertrophy split because it pairs moderate loads with enough weekly volume to practice and grow.",
                        day(1, "Day 1", "Push A",
                                ex("Bench Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Incline Dumbbell Press", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Overhead Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Lateral Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Triceps Pushdown", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(2, "Day 2", "Pull A",
                                ex("Lat Pulldown", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Barbell Row", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Seated Cable Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Face Pull", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Dumbbell Curl", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(3, "Day 3", "Legs A",
                                ex("Back Squat", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Romanian Deadlift", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Leg Press", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Hamstring Curl", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Calf Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(4, "Day 4", "Push B",
                                ex("Overhead Press", 3, "6-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Dumbbell Bench Press", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Chest Fly", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Lateral Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Overhead Triceps Extension", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(5, "Day 5", "Pull B",
                                ex("Pull-Up or Assisted Pull-Up", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Chest-Supported Row", 3, "8-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Cable Row", 3, "10-12 reps", "Stop 1-2 reps before failure", null),
                                ex("Rear Delt Fly", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Hammer Curl", 3, "10-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ),
                        day(6, "Day 6", "Legs B",
                                ex("Deadlift or Trap Bar Deadlift", 3, "5-8 reps", "Stop 1-2 reps before failure", null),
                                ex("Front Squat or Goblet Squat", 3, "8-10 reps", "Stop 1-2 reps before failure", null),
                                ex("Walking Lunge", 3, "10 reps per leg", "Stop 1-2 reps before failure", null),
                                ex("Leg Extension", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                ex("Seated Calf Raise", 3, "12-15 reps", "Optional last set near failure", "Only push close to failure if form stays strict")
                        ));
            case 7:
                return template(GoalType.HYPERTROPHY, 7, "Push/Pull/Legs x2 + Recovery/Pump Day",
                        "The seventh hypertrophy day stays low intensity so extra volume supports recovery instead of burying it.",
                        mergeDays(buildHypertrophyTemplate(6).getDays(),
                                day(7, "Day 7", "Recovery/Pump",
                                        ex("Light Cardio", 1, "20-30 minutes", "Keep effort easy to moderate", null),
                                        ex("Cable Lateral Raise", 2, "15-20 reps", "Stop 1-2 reps before failure", null),
                                        ex("Face Pull", 2, "15-20 reps", "Stop 1-2 reps before failure", null),
                                        ex("Cable Curl", 2, "15-20 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                        ex("Rope Pushdown", 2, "15-20 reps", "Optional last set near failure", "Only push close to failure if form stays strict"),
                                        ex("Mobility", 1, "10 minutes", "Move easily and pain-free", null)
                                )));
            default:
                throw new IllegalArgumentException("Unsupported hypertrophy template for days: " + daysPerWeek);
        }
    }

    private PlanTemplate buildEnduranceTemplate(int daysPerWeek) {
        switch (daysPerWeek) {
            case 2:
                return template(GoalType.ENDURANCE, 2, "Full Body Circuit",
                        "Two endurance-focused full-body circuits keep sessions efficient while building repeatable movement quality and work capacity.",
                        day(1, "Day 1", "Full Body Circuit A",
                                ex("Goblet Squat", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Push-Up or Machine Chest Press", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Seated Row", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell Romanian Deadlift", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Plank", 3, "30-60 seconds", "Hold crisp positions", null),
                                ex("Optional Cardio", 1, "10-20 minutes easy/moderate", "Conversational pace", null)
                        ),
                        day(2, "Day 2", "Full Body Circuit B",
                                ex("Leg Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell Shoulder Press", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Lat Pulldown", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Walking Lunge", 2, "12 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Bicycle Crunch", 3, "20 reps", "Move with control", null),
                                ex("Optional Cardio", 1, "10-20 minutes easy/moderate", "Conversational pace", null)
                        ));
            case 3:
                return template(GoalType.ENDURANCE, 3, "Full Body Conditioning",
                        "Three full-body conditioning days keep resistance work simple and pair well with moderate cardio for general endurance.",
                        day(1, "Day 1", "Full Body A",
                                ex("Goblet Squat", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Push-Up", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Cable Row", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell RDL", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Cardio", 1, "15-20 minutes moderate", "Steady pace", null)
                        ),
                        day(2, "Day 2", "Full Body B",
                                ex("Leg Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell Shoulder Press", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Lat Pulldown", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Step-Up", 2, "12 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Plank", 3, "45 seconds", "Hold crisp positions", null)
                        ),
                        day(3, "Day 3", "Full Body C",
                                ex("Kettlebell Deadlift or Trap Bar Deadlift Light", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Incline Push-Up or Chest Press", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Seated Row", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Bodyweight Squat", 3, "20 reps", "Leave a couple reps in reserve", null),
                                ex("Cardio Intervals", 1, "8-12 rounds of 30s harder / 60s easy", "Work hard but stay controlled", null)
                        ));
            case 4:
                return template(GoalType.ENDURANCE, 4, "Upper/Lower Endurance",
                        "A four-day upper/lower endurance split separates local muscle fatigue and makes it easier to pair resistance work with cardio.",
                        day(1, "Day 1", "Upper A",
                                ex("Machine Chest Press", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Lat Pulldown", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell Shoulder Press", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Cable Row", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Battle Rope or Bike", 1, "8-10 minutes", "Moderate pace", null)
                        ),
                        day(2, "Day 2", "Lower A",
                                ex("Leg Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Goblet Squat", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Walking Lunge", 2, "12-15 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Hamstring Curl", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Cardio", 1, "15-25 minutes moderate", "Steady pace", null)
                        ),
                        day(3, "Day 3", "Upper B",
                                ex("Push-Up", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Seated Row", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Lateral Raise", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Face Pull", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Plank", 3, "45-60 seconds", "Hold crisp positions", null)
                        ),
                        day(4, "Day 4", "Lower B",
                                ex("Romanian Deadlift Light", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Step-Up", 3, "12 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Leg Extension", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Calf Raise", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Cardio Intervals", 1, "10-15 minutes", "Work hard but stay controlled", null)
                        ));
            case 5:
                return template(GoalType.ENDURANCE, 5, "Push/Pull/Legs + Conditioning",
                        "Five endurance days mix simple resistance sessions with dedicated conditioning so the plan feels athletic instead of random.",
                        day(1, "Day 1", "Push Endurance",
                                ex("Machine Chest Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell Shoulder Press", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Incline Push-Up", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Lateral Raise", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Triceps Pushdown", 3, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(2, "Day 2", "Pull Endurance",
                                ex("Lat Pulldown", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Seated Cable Row", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Face Pull", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Rear Delt Fly", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Hammer Curl", 2, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(3, "Day 3", "Legs Endurance",
                                ex("Leg Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Goblet Squat", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Walking Lunge", 3, "12 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Hamstring Curl", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Calf Raise", 3, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(4, "Day 4", "Conditioning",
                                ex("Bike, Row, or Treadmill", 1, "25-35 minutes moderate", "Steady pace", null),
                                ex("Plank", 3, "45-60 seconds", "Hold crisp positions", null),
                                ex("Mountain Climbers", 3, "30 seconds", "Smooth, repeatable effort", null),
                                ex("Mobility", 1, "10 minutes", "Move easily and pain-free", null)
                        ),
                        day(5, "Day 5", "Full Body Light Circuit",
                                ex("Dumbbell Squat", 3, "15 reps", "Stop 2-3 reps before failure", null),
                                ex("Push-Up", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Cable Row", 3, "15 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell RDL", 3, "15 reps", "Stop 2-3 reps before failure", null),
                                ex("Farmer Carry", 3, "30-45 seconds", "Move with control", null)
                        ));
            case 6:
                return template(GoalType.ENDURANCE, 6, "Push/Pull/Legs Endurance x2",
                        "A six-day endurance push/pull/legs split repeats movement patterns often while keeping loads light enough to recover from.",
                        day(1, "Day 1", "Push A",
                                ex("Machine Chest Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Dumbbell Shoulder Press", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Lateral Raise", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Triceps Pushdown", 3, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(2, "Day 2", "Pull A",
                                ex("Lat Pulldown", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Cable Row", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Face Pull", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Curl", 2, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(3, "Day 3", "Legs A",
                                ex("Leg Press", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Goblet Squat", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Walking Lunge", 2, "12 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Calf Raise", 3, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(4, "Day 4", "Push B",
                                ex("Push-Up", 3, "12-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Incline Dumbbell Press Light", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Shoulder Press Machine", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Overhead Triceps Extension", 3, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(5, "Day 5", "Pull B",
                                ex("Seated Row", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Assisted Pull-Up or Pulldown", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Rear Delt Fly", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Hammer Curl", 2, "15-20 reps", "Stop 2-3 reps before failure", null)
                        ),
                        day(6, "Day 6", "Legs B + Conditioning",
                                ex("Romanian Deadlift Light", 3, "12-15 reps", "Stop 2-3 reps before failure", null),
                                ex("Step-Up", 3, "12 reps per leg", "Stop 2-3 reps before failure", null),
                                ex("Leg Extension", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Hamstring Curl", 3, "15-20 reps", "Stop 2-3 reps before failure", null),
                                ex("Cardio", 1, "15-20 minutes moderate", "Steady pace", null)
                        ));
            case 7:
                return template(GoalType.ENDURANCE, 7, "6-Day Endurance PPL + Active Recovery",
                        "A seventh endurance day is still recovery-focused so the week builds capacity instead of stacking hard fatigue every day.",
                        mergeDays(buildEnduranceTemplate(6).getDays(),
                                day(7, "Day 7", "Active Recovery",
                                        ex("Easy Cardio", 1, "20-40 minutes", "Conversational pace", null),
                                        ex("Mobility", 1, "10-15 minutes", "Move easily and pain-free", null),
                                        ex("Core", 2, "2 light sets", "Smooth, low-fatigue reps", null)
                                )));
            default:
                throw new IllegalArgumentException("Unsupported endurance template for days: " + daysPerWeek);
        }
    }

    private PlanTemplate template(GoalType goalType, int daysPerWeek, String splitName, String description, PlanDay... days) {
        return new PlanTemplate(goalType, daysPerWeek, splitName, description, Arrays.asList(days));
    }

    private PlanTemplate template(GoalType goalType, int daysPerWeek, String splitName, String description, List<PlanDay> days) {
        return new PlanTemplate(goalType, daysPerWeek, splitName, description, days);
    }

    private PlanDay day(int dayNumber, String dayName, String focus, PlanExercise... exercises) {
        return new PlanDay(dayNumber, dayName, focus, Arrays.asList(exercises));
    }

    private PlanExercise ex(String exerciseName, int sets, String repRange, String effortNotes, String optionalNotes) {
        return new PlanExercise(exerciseName, sets, repRange, effortNotes, optionalNotes);
    }

    private List<PlanDay> mergeDays(List<PlanDay> existingDays, PlanDay extraDay) {
        List<PlanDay> merged = new ArrayList<>(existingDays);
        merged.add(extraDay);
        return merged;
    }
}
