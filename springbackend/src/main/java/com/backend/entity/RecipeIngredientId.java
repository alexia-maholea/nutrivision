package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecipeIngredientId implements Serializable {

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    public Long getRecipeId() {
        return recipeId;
    }

    public RecipeIngredientId setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public RecipeIngredientId setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecipeIngredientId that)) {
            return false;
        }
        return Objects.equals(recipeId, that.recipeId) && Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId);
    }
}

