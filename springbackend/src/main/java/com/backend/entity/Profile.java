package com.backend.entity;

import com.backend.entity.enums.ActivityLevel;
import com.backend.entity.enums.Gender;
import com.backend.entity.enums.Goal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profile", schema = "project")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "age")
    private Integer age;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level")
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal")
    private Goal goal;

    @Column(name = "daily_calories_target")
    private Integer dailyCaloriesTarget;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "profile_dietary_tag",
            schema = "project",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_tag_id")
    )
    private Set<DietaryTag> dietaryRestrictions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Profile setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Profile setUser(User user) {
        this.user = user;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Profile setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Double getHeight() {
        return height;
    }

    public Profile setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public Profile setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Profile setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public Profile setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
        return this;
    }

    public Goal getGoal() {
        return goal;
    }

    public Profile setGoal(Goal goal) {
        this.goal = goal;
        return this;
    }

    public Integer getDailyCaloriesTarget() {
        return dailyCaloriesTarget;
    }

    public Profile setDailyCaloriesTarget(Integer dailyCaloriesTarget) {
        this.dailyCaloriesTarget = dailyCaloriesTarget;
        return this;
    }

    public Set<DietaryTag> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public Profile setDietaryRestrictions(Set<DietaryTag> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
        return this;
    }
}
