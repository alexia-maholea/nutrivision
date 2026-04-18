package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipe_ingredient", schema = "project")
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id = new RecipeIngredientId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "unit")
    private String unit;

    public RecipeIngredientId getId() {
        return id;
    }

    public RecipeIngredient setId(RecipeIngredientId id) {
        this.id = id;
        return this;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeIngredient setRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public RecipeIngredient setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public Double getQuantity() {
        return quantity;
    }

    public RecipeIngredient setQuantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public RecipeIngredient setUnit(String unit) {
        this.unit = unit;
        return this;
    }
}

