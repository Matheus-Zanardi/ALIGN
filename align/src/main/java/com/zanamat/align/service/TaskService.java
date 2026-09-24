package com.zanamat.align.service;

import com.zanamat.align.exception.ResourceNotFoundException;
import com.zanamat.align.model.Goal;
import com.zanamat.align.model.Task;
import com.zanamat.align.repository.GoalRepository;
import com.zanamat.align.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;

    public TaskService(
            TaskRepository taskRepository,
            GoalRepository goalRepository
    ) {
        this.taskRepository = taskRepository;
        this.goalRepository = goalRepository;
    }

    public List<Task> getTasksByGoal(Long userId, Long goalId) {

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        return taskRepository.findByGoalId(goal.getId());
    }

    public Task getTaskById(Long userId, Long goalId, Long taskId) {

        goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        return taskRepository.findByIdAndGoalId(taskId, goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public Task createTask(Long userId, Long goalId, Task task) {

        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        task.setGoal(goal);

        return taskRepository.save(task);
    }

    public Task updateTask(
            Long userId,
            Long goalId,
            Long taskId,
            Task updatedTask
    ) {

        goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        Task task = taskRepository.findByIdAndGoalId(taskId, goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(task);
    }

    public void deleteTask(Long userId, Long goalId, Long taskId) {

        goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        Task task = taskRepository.findByIdAndGoalId(taskId, goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }
}