package com.zanamat.align.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "habit_completions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"habit_id", "completion_date"})
        }
)
public class HabitCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    public HabitCompletion() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}