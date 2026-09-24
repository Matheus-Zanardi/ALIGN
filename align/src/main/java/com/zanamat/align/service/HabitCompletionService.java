package com.zanamat.align.service;

import com.zanamat.align.dto.HabitStatsDTO;
import com.zanamat.align.exception.ResourceNotFoundException;
import com.zanamat.align.model.Habit;
import com.zanamat.align.model.HabitCompletion;
import com.zanamat.align.repository.HabitCompletionRepository;
import com.zanamat.align.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitCompletionService {

    private final HabitCompletionRepository habitCompletionRepository;
    private final HabitRepository habitRepository;

    public HabitCompletionService(
            HabitCompletionRepository habitCompletionRepository,
            HabitRepository habitRepository
    ) {
        this.habitCompletionRepository = habitCompletionRepository;
        this.habitRepository = habitRepository;
    }

    public List<HabitCompletion> getCompletions(
            Long userId,
            Long habitId
    ) {

        habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found")
                );

        return habitCompletionRepository
                .findByHabitIdOrderByCompletionDateDesc(habitId);
    }

    public HabitCompletion completeHabit(
            Long userId,
            Long habitId
    ) {

        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found")
                );

        LocalDate today = LocalDate.now();

        boolean alreadyCompleted =
                habitCompletionRepository
                        .existsByHabitIdAndCompletionDate(
                                habitId,
                                today
                        );

        if (alreadyCompleted) {
            throw new IllegalStateException(
                    "Habit already completed today"
            );
        }

        HabitCompletion completion = new HabitCompletion();

        completion.setHabit(habit);
        completion.setCompletionDate(today);

        return habitCompletionRepository.save(completion);
    }

    public HabitStatsDTO getHabitStats(
            Long userId,
            Long habitId
    ) {

        habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found")
                );

        List<HabitCompletion> completions =
                habitCompletionRepository
                        .findByHabitIdOrderByCompletionDateDesc(habitId);

        long totalCompletions = completions.size();

        int currentStreak = 0;

        LocalDate expectedDate = LocalDate.now();

        for (HabitCompletion completion : completions) {

            if (completion.getCompletionDate().equals(expectedDate)) {
                currentStreak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return new HabitStatsDTO(
                habitId,
                currentStreak,
                totalCompletions
        );
    }
}