package com.zanamat.align.controller;

import com.zanamat.align.model.Task;
import com.zanamat.align.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals/{goalId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks(
            @PathVariable Long goalId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return taskService.getTasksByGoal(
                userId,
                goalId
        );
    }

    @GetMapping("/{taskId}")
    public Task getTaskById(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return taskService.getTaskById(
                userId,
                goalId,
                taskId
        );
    }

    @PostMapping
    public Task createTask(
            @PathVariable Long goalId,
            @RequestBody Task task,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return taskService.createTask(
                userId,
                goalId,
                task
        );
    }

    @PutMapping("/{taskId}")
    public Task updateTask(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            @RequestBody Task task,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        return taskService.updateTask(
                userId,
                goalId,
                taskId,
                task
        );
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        taskService.deleteTask(
                userId,
                goalId,
                taskId
        );
    }
}