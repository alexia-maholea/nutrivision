package com.backend.service.dto;

public class RecipeIngredientDto {

    private Long ingredientId;
    private String name;
    private Double caloriesPer100g;
    private Double quantity;
    private String unit;

    public Long getIngredientId() {
        return ingredientId;
    }

    public RecipeIngredientDto setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecipeIngredientDto setName(String name) {
        this.name = name;
        return this;
    }

    public Double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public RecipeIngredientDto setCaloriesPer100g(Double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public RecipeIngredientDto setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public RecipeIngredientDto setUnit(String unit) {
        this.unit = unit;
        return this;
    }
}

