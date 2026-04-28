package com.fitnessproject.core.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlanPersonalizationEngine {

    /*
     * We keep personalization additive and deterministic.
     * The base template stays reliable, and specific notes only layer in a few
     * compatible accessories instead of trying to invent an entire plan from text.
     */
    public PlanTemplate applySpecificGoalNotes(PlanTemplate template, String specificNotes) {
        if (specificNotes == null || specificNotes.trim().isEmpty()) {
            return template;
        }

        String normalizedNotes = specificNotes.toLowerCase(Locale.US);
        List<PlanDay> personalizedDays = new ArrayList<>();
        for (PlanDay day : template.getDays()) {
            List<PlanExercise> exercises = new ArrayList<>(day.getExercises());

            if (mentionsAny(normalizedNotes, "bench", "chest", "push", "upper body")) {
                maybeAdd(exercises, accessoryForPush(template.getGoalType()));
            }
            if (mentionsAny(normalizedNotes, "back", "row", "pull")) {
                maybeAdd(exercises, accessoryForPull(template.getGoalType()));
            }
            if (mentionsAny(normalizedNotes, "leg", "squat", "lower body")) {
                maybeAdd(exercises, accessoryForLegs(template.getGoalType()));
            }
            if (mentionsAny(normalizedNotes, "arm", "bicep", "tricep")) {
                maybeAdd(exercises, accessoryForArms(template.getGoalType()));
            }
            if (mentionsAny(normalizedNotes, "core", "abs", "stomach")) {
                maybeAdd(exercises, accessoryForCore(template.getGoalType()));
            }
            if (mentionsAny(normalizedNotes, "5k", "run", "cardio", "conditioning")) {
                maybeAdd(exercises, accessoryForConditioning(template.getGoalType()));
            }

            personalizedDays.add(new PlanDay(day.getDayNumber(), day.getDayName(), day.getFocus(), exercises));
        }

        String description = template.getDescription() +
                " Specific goal notes were used only to add compatible accessory work without replacing the base split.";
        return new PlanTemplate(
                template.getGoalType(),
                template.getDaysPerWeek(),
                template.getSplitName(),
                description,
                personalizedDays
        );
    }

    private boolean mentionsAny(String notes, String... keywords) {
        for (String keyword : keywords) {
            if (notes.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private void maybeAdd(List<PlanExercise> exercises, PlanExercise extra) {
        for (PlanExercise exercise : exercises) {
            if (exercise.getExerciseName().equalsIgnoreCase(extra.getExerciseName())) {
                return;
            }
        }
        exercises.add(extra);
    }

    private PlanExercise accessoryForPush(GoalType goalType) {
        switch (goalType) {
            case STRENGTH:
                return exercise("Chest Fly", 2, "10-12 reps", "Optional last set near failure", "Accessory only");
            case HYPERTROPHY:
                return exercise("Push-Up", 2, "12-20 reps", "Stop before form breaks", "Accessory only");
            default:
                return exercise("Machine Chest Press", 2, "15-20 reps", "Stop 2-3 reps before failure", "Keep rest short");
        }
    }

    private PlanExercise accessoryForPull(GoalType goalType) {
        switch (goalType) {
            case STRENGTH:
                return exercise("Face Pull", 2, "12-15 reps", "Stop 1-2 reps before failure", null);
            case HYPERTROPHY:
                return exercise("Face Pull", 2, "12-15 reps", "Optional last set near failure", "Accessory only");
            default:
                return exercise("Seated Row", 2, "15-20 reps", "Stop 2-3 reps before failure", "Keep rest short");
        }
    }

    private PlanExercise accessoryForLegs(GoalType goalType) {
        switch (goalType) {
            case STRENGTH:
                return exercise("Walking Lunge", 2, "8 reps per leg", "Stop 1-2 reps before failure", null);
            case HYPERTROPHY:
                return exercise("Leg Extension", 2, "12-15 reps", "Optional last set near failure", "Accessory only");
            default:
                return exercise("Step-Up", 2, "12 reps per leg", "Stop 2-3 reps before failure", "Keep rest short");
        }
    }

    private PlanExercise accessoryForArms(GoalType goalType) {
        switch (goalType) {
            case STRENGTH:
                return exercise("Hammer Curl", 2, "8-10 reps", "Stop 1-2 reps before failure", null);
            case HYPERTROPHY:
                return exercise("Hammer Curl", 2, "10-15 reps", "Optional last set near failure", "Accessory only");
            default:
                return exercise("Cable Curl", 2, "15-20 reps", "Stop 2-3 reps before failure", "Keep rest short");
        }
    }

    private PlanExercise accessoryForCore(GoalType goalType) {
        switch (goalType) {
            case STRENGTH:
                return exercise("Side Plank", 2, "30-45 seconds per side", "Move with control", null);
            case HYPERTROPHY:
                return exercise("Hanging Knee Raise", 2, "10-15 reps", "Move with control", null);
            default:
                return exercise("Plank", 2, "30-45 seconds", "Move with control", null);
        }
    }

    private PlanExercise accessoryForConditioning(GoalType goalType) {
        switch (goalType) {
            case STRENGTH:
                return exercise("Easy Bike or Walk", 1, "10-15 minutes", "Keep effort easy", "Recovery-focused conditioning");
            case HYPERTROPHY:
                return exercise("Moderate Cardio", 1, "10-15 minutes", "Easy to moderate pace", "Recovery-focused conditioning");
            default:
                return exercise("Cardio Intervals", 1, "6-8 rounds of 30s harder / 60s easy", "Stay smooth and controlled", null);
        }
    }

    private PlanExercise exercise(String name, int sets, String reps, String effort, String optionalNotes) {
        return new PlanExercise(name, sets, reps, effort, optionalNotes);
    }
}
