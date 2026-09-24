package com.zanamat.align.dto;

public class GoalProgressDTO {

    private Long goalId;
    private double progress;
    private long completedTasks;
    private int totalTasks;

    public GoalProgressDTO(
            Long goalId,
            double progress,
            long completedTasks,
            int totalTasks
    ) {
        this.goalId = goalId;
        this.progress = progress;
        this.completedTasks = completedTasks;
        this.totalTasks = totalTasks;
    }

    public Long getGoalId() {
        return goalId;
    }

    public double getProgress() {
        return progress;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public int getTotalTasks() {
        return totalTasks;
    }
}