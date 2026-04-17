package com.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_plan", schema = "project")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanEntry> entries = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public MealPlan setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public MealPlan setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public MealPlan setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public MealPlan setUser(User user) {
        this.user = user;
        return this;
    }

    public List<MealPlanEntry> getEntries() {
        return entries;
    }

    public MealPlan setEntries(List<MealPlanEntry> entries) {
        this.entries = entries;
        return this;
    }
}
