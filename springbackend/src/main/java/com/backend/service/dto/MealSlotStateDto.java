package com.backend.service.dto;

public class MealSlotStateDto {

    private int slotIndex;
    private RecipeSummaryDto recipe;

    public int getSlotIndex() {
        return slotIndex;
    }

    public MealSlotStateDto setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
        return this;
    }

    public RecipeSummaryDto getRecipe() {
        return recipe;
    }

    public MealSlotStateDto setRecipe(RecipeSummaryDto recipe) {
        this.recipe = recipe;
        return this;
    }
}
