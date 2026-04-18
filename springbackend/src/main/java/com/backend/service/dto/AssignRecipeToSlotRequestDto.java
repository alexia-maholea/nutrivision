package com.backend.service.dto;

import java.time.LocalDate;

public class AssignRecipeToSlotRequestDto {

    private LocalDate date;
    private Integer slotIndex;
    private Long recipeId;

    public LocalDate getDate() {
        return date;
    }

    public AssignRecipeToSlotRequestDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public AssignRecipeToSlotRequestDto setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
        return this;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public AssignRecipeToSlotRequestDto setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        return this;
    }
}
