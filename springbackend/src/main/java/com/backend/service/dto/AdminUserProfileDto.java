package com.backend.service.dto;

import com.backend.entity.enums.ActivityLevel;
import com.backend.entity.enums.Gender;
import com.backend.entity.enums.Goal;
import com.backend.entity.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class AdminUserProfileDto {

    private Long userId;
    private String email;
    private String name;
    private UserRole role;

    private Integer age;
    private Double height;
    private Double weight;
    private Gender gender;
    private ActivityLevel activityLevel;
    private Goal goal;
    private Integer dailyCaloriesTarget;
    private Integer mealsPerDay;

    private List<DietaryTagDto> dietaryRestrictions = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public AdminUserProfileDto setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AdminUserProfileDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public AdminUserProfileDto setName(String name) {
        this.name = name;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public AdminUserProfileDto setRole(UserRole role) {
        this.role = role;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AdminUserProfileDto setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public AdminUserProfileDto setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public AdminUserProfileDto setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public AdminUserProfileDto setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public AdminUserProfileDto setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
        return this;
    }

    public Goal getGoal() {
        return goal;
    }

    public AdminUserProfileDto setGoal(Goal goal) {
        this.goal = goal;
        return this;
    }

    public Integer getDailyCaloriesTarget() {
        return dailyCaloriesTarget;
    }

    public AdminUserProfileDto setDailyCaloriesTarget(Integer dailyCaloriesTarget) {
        this.dailyCaloriesTarget = dailyCaloriesTarget;
        return this;
    }

    public Integer getMealsPerDay() {
        return mealsPerDay;
    }

    public AdminUserProfileDto setMealsPerDay(Integer mealsPerDay) {
        this.mealsPerDay = mealsPerDay;
        return this;
    }

    public List<DietaryTagDto> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public AdminUserProfileDto setDietaryRestrictions(List<DietaryTagDto> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
        return this;
    }
}

