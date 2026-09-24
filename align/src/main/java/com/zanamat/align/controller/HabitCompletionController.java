package com.zanamat.align.controller;

import com.zanamat.align.dto.HabitStatsDTO;
import com.zanamat.align.model.HabitCompletion;
import com.zanamat.align.service.HabitCompletionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits/{habitId}")
public class HabitCompletionController {

    private final HabitCompletionService habitCompletionService;

    public HabitCompletionController(
            HabitCompletionService habitCompletionService
    ) {
        this.habitCompletionService = habitCompletionService;
    }

    @GetMapping("/completions")
    public List<HabitCompletion> getCompletions(
            @PathVariable Long habitId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitCompletionService.getCompletions(
                userId,
                habitId
        );
    }

    @PostMapping("/completions")
    public HabitCompletion completeHabit(
            @PathVariable Long habitId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitCompletionService.completeHabit(
                userId,
                habitId
        );
    }

    @GetMapping("/stats")
    public HabitStatsDTO getHabitStats(
            @PathVariable Long habitId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitCompletionService.getHabitStats(
                userId,
                habitId
        );
    }
}