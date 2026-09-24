package com.zanamat.align.repository;

import com.zanamat.align.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUserId(Long userId);

    Optional<Habit> findByIdAndUserId(Long habitId, Long userId);
}