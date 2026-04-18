package com.backend.service.dto;

import com.backend.entity.enums.ActivityLevel;
import com.backend.entity.enums.Gender;
import com.backend.entity.enums.Goal;

import java.util.List;

public class ProfileUpdateRequestDto {

	private Integer age;
	private Double height;
	private Double weight;
	private Gender gender;
	private ActivityLevel activityLevel;
	private Goal goal;
	private Integer dailyCaloriesTarget;
	private List<Long> dietaryRestrictionTagIds;

	public Integer getAge() {
		return age;
	}

	public ProfileUpdateRequestDto setAge(Integer age) {
		this.age = age;
		return this;
	}

	public Double getHeight() {
		return height;
	}

	public ProfileUpdateRequestDto setHeight(Double height) {
		this.height = height;
		return this;
	}

	public Double getWeight() {
		return weight;
	}

	public ProfileUpdateRequestDto setWeight(Double weight) {
		this.weight = weight;
		return this;
	}

	public Gender getGender() {
		return gender;
	}

	public ProfileUpdateRequestDto setGender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public ActivityLevel getActivityLevel() {
		return activityLevel;
	}

	public ProfileUpdateRequestDto setActivityLevel(ActivityLevel activityLevel) {
		this.activityLevel = activityLevel;
		return this;
	}

	public Goal getGoal() {
		return goal;
	}

	public ProfileUpdateRequestDto setGoal(Goal goal) {
		this.goal = goal;
		return this;
	}

	public Integer getDailyCaloriesTarget() {
		return dailyCaloriesTarget;
	}

	public ProfileUpdateRequestDto setDailyCaloriesTarget(Integer dailyCaloriesTarget) {
		this.dailyCaloriesTarget = dailyCaloriesTarget;
		return this;
	}

	public List<Long> getDietaryRestrictionTagIds() {
		return dietaryRestrictionTagIds;
	}

	public ProfileUpdateRequestDto setDietaryRestrictionTagIds(List<Long> dietaryRestrictionTagIds) {
		this.dietaryRestrictionTagIds = dietaryRestrictionTagIds;
		return this;
	}
}


