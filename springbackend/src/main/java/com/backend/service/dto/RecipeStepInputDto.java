package com.backend.service.dto;

public class RecipeStepInputDto {

    private Integer stepNo;
    private String description;
    private Integer durationMinutes;

    public Integer getStepNo() {
        return stepNo;
    }

    public RecipeStepInputDto setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecipeStepInputDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public RecipeStepInputDto setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }
}

