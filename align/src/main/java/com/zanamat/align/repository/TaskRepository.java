package com.zanamat.align.repository;

import com.zanamat.align.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByGoalId(Long goalId);

    Optional<Task> findByIdAndGoalId(Long taskId, Long goalId);
}