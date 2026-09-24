package com.zanamat.align.dto;

public class DashboardDTO {

    private int totalGoals;
    private long completedGoals;
    private int totalTasks;
    private long completedTasks;
    private int totalHabits;
    private long activeHabits;

    public DashboardDTO(
            int totalGoals,
            long completedGoals,
            int totalTasks,
            long completedTasks,
            int totalHabits,
            long activeHabits
    ) {
        this.totalGoals = totalGoals;
        this.completedGoals = completedGoals;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.totalHabits = totalHabits;
        this.activeHabits = activeHabits;
    }

    public int getTotalGoals() {
        return totalGoals;
    }

    public long getCompletedGoals() {
        return completedGoals;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public int getTotalHabits() {
        return totalHabits;
    }

    public long getActiveHabits() {
        return activeHabits;
    }
}