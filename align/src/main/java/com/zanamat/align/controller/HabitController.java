package com.zanamat.align.controller;

import com.zanamat.align.model.Habit;
import com.zanamat.align.service.HabitService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public List<Habit> getHabits(
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitService.getHabitsByUser(userId);
    }

    @GetMapping("/{habitId}")
    public Habit getHabitById(
            @PathVariable Long habitId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitService.getHabitById(userId, habitId);
    }

    @PostMapping
    public Habit createHabit(
            @RequestBody Habit habit,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitService.createHabit(userId, habit);
    }

    @PutMapping("/{habitId}")
    public Habit updateHabit(
            @PathVariable Long habitId,
            @RequestBody Habit habit,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return habitService.updateHabit(
                userId,
                habitId,
                habit
        );
    }

    @DeleteMapping("/{habitId}")
    public void deleteHabit(
            @PathVariable Long habitId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        habitService.deleteHabit(userId, habitId);
    }
}