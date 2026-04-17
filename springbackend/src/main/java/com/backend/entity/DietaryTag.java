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
@Table(name = "dietary_tag", schema = "project")
public class DietaryTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "dietaryTags", fetch = FetchType.LAZY)
    private Set<Recipe> recipes = new HashSet<>();

    @ManyToMany(mappedBy = "dietaryRestrictions", fetch = FetchType.LAZY)
    private Set<Profile> profiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public DietaryTag setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DietaryTag setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public DietaryTag setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public DietaryTag setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
        return this;
    }
}
