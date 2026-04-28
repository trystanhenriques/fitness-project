package com.fitnessproject.core.planner;

import java.util.Locale;

public class CustomGoalPlanGenerator {
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
        if (mentionsAny(normalized, "5k", "run", "running", "cardio", "conditioning", "stamina", "endurance")) {
            return GoalType.ENDURANCE;
        }
        if (mentionsAny(normalized, "strength", "stronger", "power", "heavy", "squat", "bench", "deadlift", "press")) {
            return GoalType.STRENGTH;
        }
        return GoalType.HYPERTROPHY;
    }

    private String summarizeFocus(String normalizedNotes) {
        StringBuilder summary = new StringBuilder();
        appendFocus(summary, normalizedNotes, "chest", "chest", "bench", "push");
        appendFocus(summary, normalizedNotes, "back", "back", "row", "pull");
        appendFocus(summary, normalizedNotes, "legs", "leg", "legs", "squat", "lower body", "glute");
        appendFocus(summary, normalizedNotes, "arms", "arms", "bicep", "tricep");
        appendFocus(summary, normalizedNotes, "core", "core", "abs", "stomach");
        appendFocus(summary, normalizedNotes, "conditioning", "5k", "run", "cardio", "conditioning");

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
