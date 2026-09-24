package com.zanamat.align.service;

import com.zanamat.align.exception.ResourceNotFoundException;
import com.zanamat.align.model.Habit;
import com.zanamat.align.model.User;
import com.zanamat.align.repository.HabitRepository;
import com.zanamat.align.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitService(
            HabitRepository habitRepository,
            UserRepository userRepository
    ) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    public List<Habit> getHabitsByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return habitRepository.findByUserId(userId);
    }

    public Habit getHabitById(Long userId, Long habitId) {

        return habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found")
                );
    }

    public Habit createHabit(Long userId, Habit habit) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        habit.setUser(user);

        return habitRepository.save(habit);
    }

    public Habit updateHabit(
            Long userId,
            Long habitId,
            Habit updatedHabit
    ) {

        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found")
                );

        habit.setTitle(updatedHabit.getTitle());
        habit.setDescription(updatedHabit.getDescription());
        habit.setFrequency(updatedHabit.getFrequency());
        habit.setActive(updatedHabit.isActive());

        return habitRepository.save(habit);
    }

    public void deleteHabit(Long userId, Long habitId) {

        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Habit not found")
                );

        habitRepository.delete(habit);
    }
}