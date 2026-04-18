package com.backend.service.dto;

import com.backend.entity.enums.RecipeDifficulty;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailDto {

    private Long id;
    private String title;
    private String description;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Integer cookingTime;
    private RecipeDifficulty difficulty;
    private List<DietaryTagDto> dietaryTags = new ArrayList<>();
    private List<RecipeIngredientDto> ingredients = new ArrayList<>();
    private List<RecipeStepDto> steps = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public RecipeDetailDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RecipeDetailDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecipeDetailDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCalories() {
        return calories;
    }

    public RecipeDetailDto setCalories(Integer calories) {
        this.calories = calories;
        return this;
    }

    public Double getProtein() {
        return protein;
    }

    public RecipeDetailDto setProtein(Double protein) {
        this.protein = protein;
        return this;
    }

    public Double getCarbs() {
        return carbs;
    }

    public RecipeDetailDto setCarbs(Double carbs) {
        this.carbs = carbs;
        return this;
    }

    public Double getFat() {
        return fat;
    }

    public RecipeDetailDto setFat(Double fat) {
        this.fat = fat;
        return this;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public RecipeDetailDto setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public RecipeDifficulty getDifficulty() {
        return difficulty;
    }

    public RecipeDetailDto setDifficulty(RecipeDifficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public List<DietaryTagDto> getDietaryTags() {
        return dietaryTags;
    }

    public RecipeDetailDto setDietaryTags(List<DietaryTagDto> dietaryTags) {
        this.dietaryTags = dietaryTags;
        return this;
    }

    public List<RecipeIngredientDto> getIngredients() {
        return ingredients;
    }

    public RecipeDetailDto setIngredients(List<RecipeIngredientDto> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public List<RecipeStepDto> getSteps() {
        return steps;
    }

    public RecipeDetailDto setSteps(List<RecipeStepDto> steps) {
        this.steps = steps;
        return this;
    }
}

