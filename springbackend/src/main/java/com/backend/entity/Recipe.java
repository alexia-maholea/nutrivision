package com.backend.entity;

import com.backend.entity.enums.RecipeDifficulty;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recipe", schema = "project")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "fat")
    private Double fat;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private RecipeDifficulty difficulty;

    @OneToMany(mappedBy = "recipe", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_dietary_tag",
            schema = "project",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_tag_id")
    )
    private Set<DietaryTag> dietaryTags = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteRecipes", fetch = FetchType.LAZY)
    private Set<User> usersWhoFavorited = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    private List<MealPlanEntry> mealPlanEntries = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepNo ASC")
    private List<RecipeStep> steps = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Recipe setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Recipe setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Recipe setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCalories() {
        return calories;
    }

    public Recipe setCalories(Integer calories) {
        this.calories = calories;
        return this;
    }

    public Double getProtein() {
        return protein;
    }

    public Recipe setProtein(Double protein) {
        this.protein = protein;
        return this;
    }

    public Double getCarbs() {
        return carbs;
    }

    public Recipe setCarbs(Double carbs) {
        this.carbs = carbs;
        return this;
    }

    public Double getFat() {
        return fat;
    }

    public Recipe setFat(Double fat) {
        this.fat = fat;
        return this;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public Recipe setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public RecipeDifficulty getDifficulty() {
        return difficulty;
    }

    public Recipe setDifficulty(RecipeDifficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public Recipe setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
        return this;
    }

    public Set<DietaryTag> getDietaryTags() {
        return dietaryTags;
    }

    public Recipe setDietaryTags(Set<DietaryTag> dietaryTags) {
        this.dietaryTags = dietaryTags;
        return this;
    }

    public Set<User> getUsersWhoFavorited() {
        return usersWhoFavorited;
    }

    public Recipe setUsersWhoFavorited(Set<User> usersWhoFavorited) {
        this.usersWhoFavorited = usersWhoFavorited;
        return this;
    }

    public List<MealPlanEntry> getMealPlanEntries() {
        return mealPlanEntries;
    }

    public Recipe setMealPlanEntries(List<MealPlanEntry> mealPlanEntries) {
        this.mealPlanEntries = mealPlanEntries;
        return this;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public Recipe setSteps(List<RecipeStep> steps) {
        this.steps = steps;
        return this;
    }
}
