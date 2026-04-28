package com.fitnessproject.core.planner;

public class PlanFormatter {

    public String formatPlan(PlanTemplate template) {
        StringBuilder builder = new StringBuilder();
        for (PlanDay day : template.getDays()) {
            builder.append(day.getDayName())
                    .append(" — ")
                    .append(day.getFocus())
                    .append("\n");

            for (PlanExercise exercise : day.getExercises()) {
                builder.append("• ")
                        .append(exercise.getExerciseName())
                        .append(": ")
                        .append(exercise.getSets())
                        .append(exercise.getSets() == 1 ? " set x " : " sets x ")
                        .append(exercise.getRepRange());

                if (!isBlank(exercise.getEffortNotes())) {
                    builder.append(", ").append(exercise.getEffortNotes());
                }
                if (!isBlank(exercise.getOptionalNotes())) {
                    builder.append(" — ").append(exercise.getOptionalNotes());
                }
                builder.append("\n");
            }

            builder.append("\n");
        }

        builder.append("Safety Notes\n")
                .append("• Stop if you feel sharp, worsening, or persistent pain.\n")
                .append("• Most sets should finish with 1-3 reps in reserve unless the exercise is clearly marked as an accessory that may go near failure.\n")
                .append("• Do not take heavy barbell lifts to true failure.\n");
        return builder.toString().trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
