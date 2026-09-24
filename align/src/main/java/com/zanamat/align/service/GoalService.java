package com.zanamat.align.service;

import com.zanamat.align.dto.GoalProgressDTO;
import com.zanamat.align.exception.ResourceNotFoundException;
import com.zanamat.align.model.Goal;
import com.zanamat.align.model.Task;
import com.zanamat.align.model.User;
import com.zanamat.align.repository.GoalRepository;
import com.zanamat.align.repository.TaskRepository;
import com.zanamat.align.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public GoalService(
            GoalRepository goalRepository,
            UserRepository userRepository,
            TaskRepository taskRepository
    ) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public List<Goal> getGoalsByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return goalRepository.findByUserId(userId);
    }

    public Goal getGoalById(Long userId, Long goalId) {

        return goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found")
                );
    }

    public Goal createGoal(Long userId, Goal goal) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        goal.setUser(user);

        return goalRepository.save(goal);
    }

    public Goal updateGoal(
            Long userId,
            Long goalId,
            Goal updatedGoal
    ) {

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found")
                );

        goal.setTitle(updatedGoal.getTitle());
        goal.setDescription(updatedGoal.getDescription());
        goal.setDeadline(updatedGoal.getDeadline());
        goal.setCompleted(updatedGoal.isCompleted());

        return goalRepository.save(goal);
    }

    public void deleteGoal(Long userId, Long goalId) {

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found")
                );

        goalRepository.delete(goal);
    }

    public GoalProgressDTO getGoalProgress(Long userId, Long goalId) {

        goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found")
                );

        List<Task> tasks = taskRepository.findByGoalId(goalId);

        int totalTasks = tasks.size();

        long completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .count();

        double progress = 0.0;

        if (totalTasks > 0) {
            progress = (completedTasks * 100.0) / totalTasks;
        }

        progress = Math.round(progress * 100.0) / 100.0;

        return new GoalProgressDTO(
                goalId,
                progress,
                completedTasks,
                totalTasks
        );
    }
}