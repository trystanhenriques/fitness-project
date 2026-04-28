package com.fitnessproject.core.planner;

public class WeeklyPlanService {
    private final PlanTemplateProvider templateProvider;
    private final PlanPersonalizationEngine personalizationEngine;

    public WeeklyPlanService() {
        this(new PlanTemplateProvider(), new PlanPersonalizationEngine());
    }

    public WeeklyPlanService(PlanTemplateProvider templateProvider, PlanPersonalizationEngine personalizationEngine) {
        this.templateProvider = templateProvider;
        this.personalizationEngine = personalizationEngine;
    }

    public PlanTemplate buildPlan(GoalType goalType, int daysPerWeek, String specificNotes) {
        PlanTemplate baseTemplate = templateProvider.getTemplate(goalType, daysPerWeek);
        return personalizationEngine.applySpecificGoalNotes(baseTemplate, specificNotes);
    }
}
