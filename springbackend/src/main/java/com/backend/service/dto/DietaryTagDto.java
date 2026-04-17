package com.backend.service.dto;

public class DietaryTagDto {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public DietaryTagDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DietaryTagDto setName(String name) {
        this.name = name;
        return this;
    }
}
