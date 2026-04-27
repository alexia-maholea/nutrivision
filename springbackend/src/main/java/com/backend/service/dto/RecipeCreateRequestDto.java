package com.backend.service.dto;

import com.backend.entity.enums.RecipeDifficulty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecipeCreateRequestDto {

    private String title;
    private String description;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    @JsonProperty("cooking_time")
    private Integer cookingTime;
    private RecipeDifficulty difficulty;
    private List<Long> dietaryTagIds;
    private List<RecipeIngredientInputDto> ingredients;
    private List<RecipeStepInputDto> steps;

    public String getTitle() {
        return title;
    }

    public RecipeCreateRequestDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecipeCreateRequestDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCalories() {
        return calories;
    }

    public RecipeCreateRequestDto setCalories(Integer calories) {
        this.calories = calories;
        return this;
    }

    public Double getProtein() {
        return protein;
    }

    public RecipeCreateRequestDto setProtein(Double protein) {
        this.protein = protein;
        return this;
    }

    public Double getCarbs() {
        return carbs;
    }

    public RecipeCreateRequestDto setCarbs(Double carbs) {
        this.carbs = carbs;
        return this;
    }

    public Double getFat() {
        return fat;
    }

    public RecipeCreateRequestDto setFat(Double fat) {
        this.fat = fat;
        return this;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public RecipeCreateRequestDto setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public RecipeDifficulty getDifficulty() {
        return difficulty;
    }

    public RecipeCreateRequestDto setDifficulty(RecipeDifficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public List<Long> getDietaryTagIds() {
        return dietaryTagIds;
    }

    public RecipeCreateRequestDto setDietaryTagIds(List<Long> dietaryTagIds) {
        this.dietaryTagIds = dietaryTagIds;
        return this;
    }

    public List<RecipeIngredientInputDto> getIngredients() {
        return ingredients;
    }

    public RecipeCreateRequestDto setIngredients(List<RecipeIngredientInputDto> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public List<RecipeStepInputDto> getSteps() {
        return steps;
    }

    public RecipeCreateRequestDto setSteps(List<RecipeStepInputDto> steps) {
        this.steps = steps;
        return this;
    }
}

