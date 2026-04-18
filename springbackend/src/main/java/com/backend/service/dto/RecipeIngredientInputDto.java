package com.backend.service.dto;

public class RecipeIngredientInputDto {

    private Long ingredientId;
    private String name;
    private Double caloriesPer100g;
    private Double quantity;
    private String unit;

    public Long getIngredientId() {
        return ingredientId;
    }

    public RecipeIngredientInputDto setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecipeIngredientInputDto setName(String name) {
        this.name = name;
        return this;
    }

    public Double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public RecipeIngredientInputDto setCaloriesPer100g(Double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public RecipeIngredientInputDto setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public RecipeIngredientInputDto setUnit(String unit) {
        this.unit = unit;
        return this;
    }
}

