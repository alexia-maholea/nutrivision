package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_step", schema = "project")
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "step_no", nullable = false)
    private Integer stepNo;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    public Long getId() {
        return id;
    }

    public RecipeStep setId(Long id) {
        this.id = id;
        return this;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeStep setRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public Integer getStepNo() {
        return stepNo;
    }

    public RecipeStep setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecipeStep setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public RecipeStep setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }
}

