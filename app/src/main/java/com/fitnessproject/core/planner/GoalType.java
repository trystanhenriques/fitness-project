package com.fitnessproject.core.planner;

public enum GoalType {
    STRENGTH("Strength"),
    HYPERTROPHY("Hypertrophy"),
    ENDURANCE("Endurance");

    private final String displayName;

    GoalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GoalType fromDisplayName(String displayName) {
        for (GoalType goalType : values()) {
            if (goalType.displayName.equalsIgnoreCase(displayName)) {
                return goalType;
            }
        }
        throw new IllegalArgumentException("Unsupported goal type: " + displayName);
    }
}
