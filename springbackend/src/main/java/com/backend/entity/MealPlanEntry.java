package com.backend.entity;

import com.backend.entity.enums.MealType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "meal_plan_entry", schema = "project")
public class MealPlanEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "meal_date")
    private LocalDate mealDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    /** 0 .. mealsPerDay-1 — care „găură” din zi ocupă această rețetă. */
    @Column(name = "meal_slot_index", nullable = false)
    private Integer mealSlotIndex = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public Long getId() {
        return id;
    }

    public MealPlanEntry setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public MealPlanEntry setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
        return this;
    }

    public MealType getMealType() {
        return mealType;
    }

    public MealPlanEntry setMealType(MealType mealType) {
        this.mealType = mealType;
        return this;
    }

    public Integer getMealSlotIndex() {
        return mealSlotIndex;
    }

    public MealPlanEntry setMealSlotIndex(Integer mealSlotIndex) {
        this.mealSlotIndex = mealSlotIndex;
        return this;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public MealPlanEntry setMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
        return this;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public MealPlanEntry setRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }
}
