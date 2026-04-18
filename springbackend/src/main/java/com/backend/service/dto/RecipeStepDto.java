package com.backend.service.dto;

public class RecipeStepDto {

    private Integer stepNo;
    private String description;
    private Integer durationMinutes;

    public Integer getStepNo() {
        return stepNo;
    }

    public RecipeStepDto setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecipeStepDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public RecipeStepDto setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }
}

