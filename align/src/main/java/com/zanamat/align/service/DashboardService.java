package com.zanamat.align.service;

import com.zanamat.align.dto.DashboardDTO;
import com.zanamat.align.exception.ResourceNotFoundException;
import com.zanamat.align.model.Goal;
import com.zanamat.align.model.Habit;
import com.zanamat.align.model.Task;
import com.zanamat.align.repository.GoalRepository;
import com.zanamat.align.repository.HabitRepository;
import com.zanamat.align.repository.TaskRepository;
import com.zanamat.align.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final HabitRepository habitRepository;

    public DashboardService(
            UserRepository userRepository,
            GoalRepository goalRepository,
            TaskRepository taskRepository,
            HabitRepository habitRepository
    ) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.taskRepository = taskRepository;
        this.habitRepository = habitRepository;
    }

    public DashboardDTO getDashboard(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Goal> goals = goalRepository.findByUserId(userId);
        List<Habit> habits = habitRepository.findByUserId(userId);

        long completedGoals = goals.stream()
                .filter(Goal::isCompleted)
                .count();

        long activeHabits = habits.stream()
                .filter(Habit::isActive)
                .count();

        int totalTasks = 0;
        long completedTasks = 0;

        for (Goal goal : goals) {

            List<Task> tasks =
                    taskRepository.findByGoalId(goal.getId());

            totalTasks += tasks.size();

            completedTasks += tasks.stream()
                    .filter(Task::isCompleted)
                    .count();
        }

        return new DashboardDTO(
                goals.size(),
                completedGoals,
                totalTasks,
                completedTasks,
                habits.size(),
                activeHabits
        );
    }
}