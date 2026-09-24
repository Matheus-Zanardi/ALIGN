package com.zanamat.align.dto;

public class HabitStatsDTO {

    private Long habitId;
    private int currentStreak;
    private long totalCompletions;

    public HabitStatsDTO(
            Long habitId,
            int currentStreak,
            long totalCompletions
    ) {
        this.habitId = habitId;
        this.currentStreak = currentStreak;
        this.totalCompletions = totalCompletions;
    }

    public Long getHabitId() {
        return habitId;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public long getTotalCompletions() {
        return totalCompletions;
    }
}