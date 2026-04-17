package com.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredient", schema = "project")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "calories_per_100g")
    private Double caloriesPer100g;

    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.LAZY)
    private Set<Recipe> recipes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Ingredient setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public Double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public Ingredient setCaloriesPer100g(Double caloriesPer100g) {
        this.caloriesPer100g = caloriesPer100g;
        return this;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public Ingredient setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }
}
