package com.zanamat.align.repository;

import com.zanamat.align.model.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitCompletionRepository
        extends JpaRepository<HabitCompletion, Long> {

    List<HabitCompletion> findByHabitIdOrderByCompletionDateDesc(Long habitId);

    boolean existsByHabitIdAndCompletionDate(
            Long habitId,
            LocalDate completionDate
    );
}