package com.backend.service.dto;

import com.backend.entity.enums.ActivityLevel;
import com.backend.entity.enums.Gender;
import com.backend.entity.enums.Goal;

import java.util.ArrayList;
import java.util.List;

/**
 * Nutrition profile for the authenticated user: anthropometrics, goal, calorie target, dietary restrictions.
 */
public class ProfileResponseDto {

    private String email;
    private String name;

    private Integer age;
    private Double height;
    private Double weight;
    private Gender gender;
    private ActivityLevel activityLevel;
    private Goal goal;
    private Integer dailyCaloriesTarget;

    private List<DietaryTagDto> dietaryRestrictions = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public ProfileResponseDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProfileResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public ProfileResponseDto setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public ProfileResponseDto setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public ProfileResponseDto setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public ProfileResponseDto setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public ProfileResponseDto setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
        return this;
    }

    public Goal getGoal() {
        return goal;
    }

    public ProfileResponseDto setGoal(Goal goal) {
        this.goal = goal;
        return this;
    }

    public Integer getDailyCaloriesTarget() {
        return dailyCaloriesTarget;
    }

    public ProfileResponseDto setDailyCaloriesTarget(Integer dailyCaloriesTarget) {
        this.dailyCaloriesTarget = dailyCaloriesTarget;
        return this;
    }

    public List<DietaryTagDto> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public ProfileResponseDto setDietaryRestrictions(List<DietaryTagDto> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
        return this;
    }
}
