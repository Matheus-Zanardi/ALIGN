package com.zanamat.align.controller;

import com.zanamat.align.model.Goal;
import com.zanamat.align.service.GoalService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public List<Goal> getGoals(
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return goalService.getGoalsByUser(userId);
    }

    @GetMapping("/{goalId}")
    public Goal getGoalById(
            @PathVariable Long goalId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return goalService.getGoalById(userId, goalId);
    }

    @PostMapping
    public Goal createGoal(
            @RequestBody Goal goal,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return goalService.createGoal(userId, goal);
    }

    @PutMapping("/{goalId}")
    public Goal updateGoal(
            @PathVariable Long goalId,
            @RequestBody Goal goal,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return goalService.updateGoal(
                userId,
                goalId,
                goal
        );
    }

    @DeleteMapping("/{goalId}")
    public void deleteGoal(
            @PathVariable Long goalId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        goalService.deleteGoal(userId, goalId);
    }
}