package com.backend.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarDayResponseDto {

    private LocalDate date;
    private int mealsPerDay;
    private List<MealSlotStateDto> slots = new ArrayList<>();

    public LocalDate getDate() {
        return date;
    }

    public CalendarDayResponseDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public int getMealsPerDay() {
        return mealsPerDay;
    }

    public CalendarDayResponseDto setMealsPerDay(int mealsPerDay) {
        this.mealsPerDay = mealsPerDay;
        return this;
    }

    public List<MealSlotStateDto> getSlots() {
        return slots;
    }

    public CalendarDayResponseDto setSlots(List<MealSlotStateDto> slots) {
        this.slots = slots;
        return this;
    }
}
