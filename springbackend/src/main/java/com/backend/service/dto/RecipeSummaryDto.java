package com.backend.service.dto;

import com.backend.entity.enums.RecipeDifficulty;

public class RecipeSummaryDto {

    private Long id;
    private String title;
    private Integer calories;
    private Integer cookingTime;
    private RecipeDifficulty difficulty;

    public Long getId() {
        return id;
    }

    public RecipeSummaryDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RecipeSummaryDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getCalories() {
        return calories;
    }

    public RecipeSummaryDto setCalories(Integer calories) {
        this.calories = calories;
        return this;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public RecipeSummaryDto setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public RecipeDifficulty getDifficulty() {
        return difficulty;
    }

    public RecipeSummaryDto setDifficulty(RecipeDifficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }
}
