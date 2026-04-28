package com.fitnessproject.core.planner;

import java.util.Locale;

public class CustomGoalPlanGenerator {
    private static final String[] ENDURANCE_KEYWORDS = {
            "5k", "10k", "half marathon", "marathon", "run", "running", "jog", "jogging",
            "cardio", "conditioning", "stamina", "endurance", "engine", "aerobic",
            "work capacity", "athletic", "fitness", "interval", "intervals", "speed",
            "sprint", "sprints", "bike", "cycling", "row", "rowing", "trail", "race"
    };
    private static final String[] STRENGTH_KEYWORDS = {
            "strength", "stronger", "power", "powerlifting", "heavy", "barbell", "compound",
            "squat", "bench", "deadlift", "press", "overhead press", "ohp", "row",
            "pull-up", "pull up", "chin-up", "chin up", "pr", "personal record", "1rm",
            "top set", "performance", "explosive"
    };
    private static final String[] CHEST_KEYWORDS = {
            "chest", "pec", "pecs", "bench", "push", "press", "upper chest", "incline"
    };
    private static final String[] BACK_KEYWORDS = {
            "back", "lats", "lat", "upper back", "mid back", "row", "rows", "pull",
            "pullup", "pull-up", "pull up", "chinup", "chin-up", "chin up", "rear delt"
    };
    private static final String[] LEGS_KEYWORDS = {
            "leg", "legs", "quad", "quads", "hamstring", "hamstrings", "glute", "glutes",
            "calf", "calves", "lower body", "squat", "lunge", "lunge", "deadlift"
    };
    private static final String[] ARMS_KEYWORDS = {
            "arm", "arms", "bicep", "biceps", "tricep", "triceps", "forearm", "forearms",
            "curl", "curls", "pushdown", "elbow flexor", "elbow extensor"
    };
    private static final String[] CORE_KEYWORDS = {
            "core", "abs", "ab", "abdominal", "abdominals", "stomach", "midsection",
            "waist", "oblique", "obliques", "trunk", "brace", "bracing"
    };

    private final PlanTemplateProvider templateProvider;
    private final PlanPersonalizationEngine personalizationEngine;

    public CustomGoalPlanGenerator(PlanTemplateProvider templateProvider, PlanPersonalizationEngine personalizationEngine) {
        this.templateProvider = templateProvider;
        this.personalizationEngine = personalizationEngine;
    }

    /*
     * The custom mode stays deterministic.
     * We infer the closest safe training style from the user's notes, then
     * personalize a structured template instead of generating an unbounded plan.
     */
    public PlanTemplate buildCustomPlan(int daysPerWeek, String customNotes) {
        GoalType inferredGoal = inferGoalType(customNotes);
        PlanTemplate baseTemplate = templateProvider.getTemplate(inferredGoal, daysPerWeek);
        PlanTemplate personalizedTemplate = personalizationEngine.applySpecificGoalNotes(baseTemplate, customNotes);

        String normalizedNotes = normalize(customNotes);
        String splitName = "Custom Focus — " + personalizedTemplate.getSplitName();
        String description = "Built from your custom notes using a safe " + inferredGoal.getDisplayName().toLowerCase(Locale.US)
                + "-leaning template. The text input adjusts emphasis, but the plan still follows structured beginner/intermediate-friendly rules.";

        if (!normalizedNotes.isEmpty()) {
            description += " Notes matched keywords for: " + summarizeFocus(normalizedNotes) + ".";
        }

        return new PlanTemplate(
                GoalType.CUSTOM,
                personalizedTemplate.getDaysPerWeek(),
                splitName,
                description,
                personalizedTemplate.getDays()
        );
    }

    private GoalType inferGoalType(String customNotes) {
        String normalized = normalize(customNotes);
        if (mentionsAny(normalized, ENDURANCE_KEYWORDS)) {
            return GoalType.ENDURANCE;
        }
        if (mentionsAny(normalized, STRENGTH_KEYWORDS)) {
            return GoalType.STRENGTH;
        }
        return GoalType.HYPERTROPHY;
    }

    private String summarizeFocus(String normalizedNotes) {
        StringBuilder summary = new StringBuilder();
        appendFocus(summary, normalizedNotes, "chest", CHEST_KEYWORDS);
        appendFocus(summary, normalizedNotes, "back", BACK_KEYWORDS);
        appendFocus(summary, normalizedNotes, "legs", LEGS_KEYWORDS);
        appendFocus(summary, normalizedNotes, "arms", ARMS_KEYWORDS);
        appendFocus(summary, normalizedNotes, "core", CORE_KEYWORDS);
        appendFocus(summary, normalizedNotes, "conditioning", ENDURANCE_KEYWORDS);

        if (summary.length() == 0) {
            return "general performance";
        }
        return summary.toString();
    }

    private void appendFocus(StringBuilder summary, String notes, String label, String... keywords) {
        if (!mentionsAny(notes, keywords)) {
            return;
        }
        if (summary.length() > 0) {
            summary.append(", ");
        }
        summary.append(label);
    }

    private boolean mentionsAny(String notes, String... keywords) {
        for (String keyword : keywords) {
            if (notes.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String customNotes) {
        if (customNotes == null) {
            return "";
        }
        return customNotes.trim().toLowerCase(Locale.US);
    }
}
