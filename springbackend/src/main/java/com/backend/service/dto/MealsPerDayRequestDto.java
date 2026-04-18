package com.backend.service.dto;

public class MealsPerDayRequestDto {

    private Integer mealsPerDay;

    public Integer getMealsPerDay() {
        return mealsPerDay;
    }

    public MealsPerDayRequestDto setMealsPerDay(Integer mealsPerDay) {
        this.mealsPerDay = mealsPerDay;
        return this;
    }
}
